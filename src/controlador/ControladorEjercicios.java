/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import persistencia.GestorDatos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para operaciones relacionadas con ejercicios.
 * 
 * Separa la lógica de negocio de los ejercicios del resto.
 * Así si mañana cambiamos la persistencia, solo tocamos esta clase.
 * 
 * En DXC Technology, aprendí que los controladores deben ser
 * como los switches de red: reciben peticiones y las dirigen
 * al lugar correcto, sin importarles el contenido.
 * 
 * @author César Alonso Morera Alpízar
 */
public class ControladorEjercicios {
    
    private List<Ejercicio> ejercicios;
    private int siguienteId;
    
    public ControladorEjercicios() {
        this.ejercicios = new ArrayList<>();
        cargarDatos();
    }
    
    /**
     * Carga los ejercicios desde el archivo
     */
    private void cargarDatos() {
        try {
            ejercicios = GestorDatos.cargarEjercicios();
            
            // Calcular siguiente ID
            siguienteId = ejercicios.stream()
                .mapToInt(Ejercicio::getId)
                .max()
                .orElse(0) + 1;
            
        } catch (Exception e) {
            System.err.println("Error cargando ejercicios: " + e.getMessage());
            ejercicios = new ArrayList<>();
            siguienteId = 1;
        }
    }
    
    /**
     * Guarda los ejercicios en archivo
     */
    private void guardarDatos() {
        try {
            GestorDatos.guardarEjercicios(ejercicios);
        } catch (IOException e) {
            System.err.println("Error guardando ejercicios: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los ejercicios
     */
    public List<Ejercicio> getTodosLosEjercicios() {
        return new ArrayList<>(ejercicios);
    }
    
    /**
     * Busca ejercicios por tipo
     * 
     * @param tipo el tipo de ejercicio (PIERNA, BRAZO, etc.)
     * @return lista de ejercicios de ese tipo
     */
    public List<Ejercicio> buscarPorTipo(TipoEjercicio tipo) {
        return ejercicios.stream()
            .filter(e -> e.getTipo() == tipo)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca ejercicios por nombre (búsqueda parcial, case insensitive)
     * 
     * En mis cursos de Rocky Linux, enseñaba que el grep es case sensitive
     * y los estudiantes siempre se quejaban. Acá lo hacemos amigable.
     * 
     * @param nombre texto a buscar (puede ser parte del nombre)
     * @return lista de ejercicios que coinciden
     */
    public List<Ejercicio> buscarPorNombre(String nombre) {
        String busqueda = nombre.toLowerCase().trim();
        return ejercicios.stream()
            .filter(e -> e.getNombre().toLowerCase().contains(busqueda))
            .collect(Collectors.toList());
    }
    
    /**
     * Busca un ejercicio por ID
     */
    public Ejercicio buscarPorId(int id) {
        return ejercicios.stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Agrega un nuevo ejercicio al catálogo
     * 
     * @param nombre nombre del ejercicio (único)
     * @param descripcion cómo se ejecuta
     * @param tipo categoría
     * @param videoURL enlace a video (opcional)
     * @param imagenURL enlace a imagen (opcional)
     * @return el ejercicio creado
     * @throws IllegalArgumentException si hay datos inválidos o nombre duplicado
     */
    public Ejercicio agregarEjercicio(String nombre, String descripcion, 
                                      TipoEjercicio tipo, String videoURL, String imagenURL) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        
        // Validar nombre único (buena práctica)
        boolean existe = ejercicios.stream()
            .anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre));
        
        if (existe) {
            throw new IllegalArgumentException("Ya existe un ejercicio con ese nombre");
        }
        
        Ejercicio nuevo = new Ejercicio(siguienteId++, nombre, descripcion, tipo, videoURL, imagenURL);
        ejercicios.add(nuevo);
        guardarDatos();
        
        return nuevo;
    }
    
    /**
     * Actualiza un ejercicio existente
     * 
     * @param id ID del ejercicio a actualizar
     * @param nombre nuevo nombre (si se quiere cambiar)
     * @param descripcion nueva descripción
     * @param tipo nuevo tipo
     * @param videoURL nueva URL de video
     * @param imagenURL nueva URL de imagen
     * @return true si se actualizó
     */
    public boolean actualizarEjercicio(int id, String nombre, String descripcion, 
                                       TipoEjercicio tipo, String videoURL, String imagenURL) {
        Ejercicio ejercicio = buscarPorId(id);
        if (ejercicio == null) {
            return false;
        }
        
        // Validar que el nuevo nombre no esté en uso por otro ejercicio
        if (nombre != null && !nombre.trim().isEmpty()) {
            boolean nombreEnUso = ejercicios.stream()
                .anyMatch(e -> e.getId() != id && e.getNombre().equalsIgnoreCase(nombre));
            
            if (nombreEnUso) {
                throw new IllegalArgumentException("Ya existe otro ejercicio con ese nombre");
            }
            ejercicio.setNombre(nombre);
        }
        
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            ejercicio.setDescripcion(descripcion);
        }
        
        if (tipo != null) {
            ejercicio.setTipo(tipo);
        }
        
        // Las URLs pueden ser null
        try {
            ejercicio.setVideoURL(videoURL);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("URL de video inválida: " + e.getMessage());
        }
        
        try {
            ejercicio.setImagenURL(imagenURL);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("URL de imagen inválida: " + e.getMessage());
        }
        
        guardarDatos();
        return true;
    }
    
    /**
     * Elimina un ejercicio del catálogo
     * 
     * NOTA: En un sistema completo, habría que verificar que el ejercicio
     * no esté siendo usado en ninguna rutina antes de eliminarlo.
     * Por ahora, eliminamos directamente.
     * 
     * @param id ID del ejercicio a eliminar
     * @return true si se eliminó
     */
    public boolean eliminarEjercicio(int id) {
        boolean removido = ejercicios.removeIf(e -> e.getId() == id);
        if (removido) {
            guardarDatos();
        }
        return removido;
    }
}