/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Ejercicio;
import modelo.TipoEjercicio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para operaciones relacionadas con ejercicios.
 * 
 * VERSION DE DEMOSTRACION - Sin dependencia de base de datos
 * Todos los ejercicios están precargados para la presentación.
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
     * Carga los ejercicios de demostración
     */
    private void cargarDatos() {
        System.out.println("Cargando catálogo de ejercicios de demostración...");
        
        ejercicios = new ArrayList<>();
        
        // Ejercicio 1
        ejercicios.add(new Ejercicio(1, "Press de Banca", 
            "Acostado en banca plana, baja la barra hasta el pecho y empuja hacia arriba. Mantén los codos a 45 grados.", 
            TipoEjercicio.PECHO, "", ""));
        
        // Ejercicio 2
        ejercicios.add(new Ejercicio(2, "Sentadilla", 
            "Con barra sobre el trapecio, baja como si fueras a sentarte en una silla. Mantén la espalda recta.", 
            TipoEjercicio.PIERNA, "", ""));
        
        // Ejercicio 3
        ejercicios.add(new Ejercicio(3, "Curl de Bíceps", 
            "De pie, con codos pegados al cuerpo, sube las mancuernas hacia los hombros. Baja controlado.", 
            TipoEjercicio.BRAZO, "", ""));
        
        // Ejercicio 4
        ejercicios.add(new Ejercicio(4, "Dominadas", 
            "Agarrar la barra con palmas al frente. Sube hasta que la barbilla supere la barra.", 
            TipoEjercicio.ESPALDA, "", ""));
        
        // Ejercicio 5
        ejercicios.add(new Ejercicio(5, "Plancha Abdominal", 
            "Apoyado en antebrazos y puntas de pies. Mantén el cuerpo recto como una tabla.", 
            TipoEjercicio.ABDOMEN, "", ""));
        
        // Ejercicio 6
        ejercicios.add(new Ejercicio(6, "Peso Muerto", 
            "Con barra en el suelo, agarre a lo ancho de hombros. Empuja con las piernas manteniendo la espalda recta.", 
            TipoEjercicio.FULLBODY, "", ""));
        
        siguienteId = 7;
        
        System.out.println("Cargados " + ejercicios.size() + " ejercicios de demostración");
        System.out.println("Ejercicios disponibles: Press de Banca, Sentadilla, Curl de Bíceps, Dominadas, Plancha, Peso Muerto");
    }
    
    public List<Ejercicio> getTodosLosEjercicios() {
        return new ArrayList<>(ejercicios);
    }
    
    public List<Ejercicio> buscarPorTipo(TipoEjercicio tipo) {
        return ejercicios.stream()
            .filter(e -> e.getTipo() == tipo)
            .collect(Collectors.toList());
    }
    
    public List<Ejercicio> buscarPorNombre(String nombre) {
        String busqueda = nombre.toLowerCase().trim();
        return ejercicios.stream()
            .filter(e -> e.getNombre().toLowerCase().contains(busqueda))
            .collect(Collectors.toList());
    }
    
    public Ejercicio buscarPorId(int id) {
        return ejercicios.stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    public Ejercicio agregarEjercicio(String nombre, String descripcion, 
                                      TipoEjercicio tipo, String videoURL, String imagenURL) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        
        boolean existe = ejercicios.stream()
            .anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre));
        
        if (existe) {
            throw new IllegalArgumentException("Ya existe un ejercicio con ese nombre");
        }
        
        Ejercicio nuevo = new Ejercicio(siguienteId++, nombre, descripcion, tipo, videoURL, imagenURL);
        ejercicios.add(nuevo);
        
        System.out.println("Nuevo ejercicio agregado: " + nombre);
        return nuevo;
    }
    
    public boolean actualizarEjercicio(int id, String nombre, String descripcion, 
                                       TipoEjercicio tipo, String videoURL, String imagenURL) {
        Ejercicio ejercicio = buscarPorId(id);
        if (ejercicio == null) return false;
        
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
        
        if (videoURL != null) ejercicio.setVideoURL(videoURL);
        if (imagenURL != null) ejercicio.setImagenURL(imagenURL);
        
        System.out.println("Ejercicio actualizado: " + ejercicio.getNombre());
        return true;
    }
    
    public boolean eliminarEjercicio(int id) {
        Ejercicio ejercicio = buscarPorId(id);
        if (ejercicio == null) return false;
        
        boolean removido = ejercicios.removeIf(e -> e.getId() == id);
        if (removido) {
            System.out.println("Ejercicio eliminado: " + ejercicio.getNombre());
        }
        return removido;
    }
}