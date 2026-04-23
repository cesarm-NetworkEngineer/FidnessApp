/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import persistencia.GestorDatos;

import java.sql.SQLException;
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
 * AHORA CON BASE DE DATOS: Los ejercicios se guardan en SQLite,
 * no más archivos .dat. Es más seguro y fácil de mantener.
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
     * Carga los ejercicios desde la BASE DE DATOS (ya no desde archivos)
     * 
     * Antes usábamos serialización con archivos .dat, pero ahora
     * todo está en SQLite. Es más rápido y profesional.
     */
    private void cargarDatos() {
        try {
            // Ahora usamos la base de datos, no archivos
            ejercicios = GestorDatos.cargarEjercicios();
            
            // Calcular siguiente ID basado en el máximo existente
            siguienteId = ejercicios.stream()
                .mapToInt(Ejercicio::getId)
                .max()
                .orElse(0) + 1;
            
            System.out.println("✅ ControladorEjercicios: " + ejercicios.size() + " ejercicios cargados desde BD");
            
        } catch (SQLException e) {
            System.err.println("❌ Error cargando ejercicios desde la base de datos: " + e.getMessage());
            System.err.println("   Verifica que la librería SQLite esté agregada correctamente.");
            ejercicios = new ArrayList<>();
            siguienteId = 1;
        }
    }
    
    /**
     * GUARDA los cambios en la base de datos
     * 
     * En la versión con base de datos, las operaciones individuales
     * (agregar, actualizar, eliminar) ya guardan automáticamente.
     * Este método ya no es necesario, pero lo mantengo por si acaso.
     */
    private void guardarDatos() {
        // En la versión con base de datos, los guardados son inmediatos
        // Cada operación (agregar, actualizar, eliminar) ya guarda en la BD
        System.out.println("💾 Nota: Los ejercicios se guardan automáticamente en la BD");
    }
    
    /**
     * Obtiene todos los ejercicios
     * 
     * @return lista de todos los ejercicios del catálogo
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
     * 
     * @param id identificador único del ejercicio
     * @return el ejercicio encontrado, o null si no existe
     */
    public Ejercicio buscarPorId(int id) {
        return ejercicios.stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Agrega un nuevo ejercicio al catálogo (GUARDA EN BASE DE DATOS)
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
        // Validaciones básicas (igual que antes)
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        
        // Validar nombre único en la lista actual
        boolean existe = ejercicios.stream()
            .anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre));
        
        if (existe) {
            throw new IllegalArgumentException("Ya existe un ejercicio con ese nombre");
        }
        
        try {
            // Crear el ejercicio con el siguiente ID disponible
            Ejercicio nuevo = new Ejercicio(siguienteId++, nombre, descripcion, tipo, videoURL, imagenURL);
            
            // GUARDAR EN BASE DE DATOS (esto es nuevo)
            GestorDatos.guardarEjercicio(nuevo);
            
            // Agregar a la lista en memoria
            ejercicios.add(nuevo);
            
            System.out.println("✅ Nuevo ejercicio agregado: " + nombre + " (ID: " + nuevo.getId() + ")");
            
            return nuevo;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar ejercicio en BD: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el ejercicio en la base de datos", e);
        }
    }
    
    /**
     * Actualiza un ejercicio existente (GUARDA EN BASE DE DATOS)
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
            System.err.println("❌ No se encontró el ejercicio con ID: " + id);
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
        
        try {
            // ACTUALIZAR EN BASE DE DATOS
            GestorDatos.actualizarEjercicio(ejercicio);
            System.out.println("✅ Ejercicio actualizado: " + ejercicio.getNombre() + " (ID: " + id + ")");
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar ejercicio en BD: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un ejercicio del catálogo (ELIMINA DE BASE DE DATOS)
     * 
     * NOTA: En un sistema completo, habría que verificar que el ejercicio
     * no esté siendo usado en ninguna rutina antes de eliminarlo.
     * Por ahora, eliminamos directamente.
     * 
     * @param id ID del ejercicio a eliminar
     * @return true si se eliminó
     */
    public boolean eliminarEjercicio(int id) {
        // Verificar que el ejercicio existe
        Ejercicio ejercicio = buscarPorId(id);
        if (ejercicio == null) {
            System.err.println("❌ No se encontró el ejercicio con ID: " + id);
            return false;
        }
        
        try {
            // ELIMINAR DE BASE DE DATOS
            GestorDatos.eliminarEjercicio(id);
            
            // Eliminar de la lista en memoria
            boolean removido = ejercicios.removeIf(e -> e.getId() == id);
            
            if (removido) {
                System.out.println("✅ Ejercicio eliminado: " + ejercicio.getNombre() + " (ID: " + id + ")");
            }
            
            return removido;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar ejercicio de BD: " + e.getMessage());
            return false;
        }
    }
}