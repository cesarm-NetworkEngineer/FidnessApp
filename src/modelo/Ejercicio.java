/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 * Clase que representa un ejercicio del catálogo de FidnessApp.
 * 
 * Cada ejercicio tiene un tipo (PIERNA, BRAZO, etc.), una descripción
 * y opcionalmente URLs de video o imagen para demostración.
 * 
 * En mi experiencia dando soporte en Infinite Computer Solutions,
 * aprendí que los datos deben tener una estructura clara y consistente.
 * Un ejercicio mal definido es como una IP mal configurada: todo falla.
 * 
 * @author César Alonso Morera Alpízar
 */
public class Ejercicio implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private String descripcion;
    private TipoEjercicio tipo;
    private String videoUrl;
    private String imagenUrl;
    
    /**
     * Constructor completo (para cargar desde base de datos)
     */
    public Ejercicio(int id, String nombre, String descripcion, TipoEjercicio tipo, String videoUrl, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.videoUrl = videoUrl;
        this.imagenUrl = imagenUrl;
    }
    
    /**
     * Constructor para crear nuevos ejercicios (sin ID aún)
     */
    public Ejercicio(String nombre, String descripcion, TipoEjercicio tipo) {
        this(0, nombre, descripcion, tipo, null, null);
    }
    
    /**
     * Constructor simplificado para pruebas rápidas
     */
    public Ejercicio(String nombre, TipoEjercicio tipo) {
        this(0, nombre, "Ejercicio de " + tipo.name().toLowerCase(), tipo, null, null);
    }
    
    // ============================================================
    // GETTERS Y SETTERS
    // ============================================================
    
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del ejercicio es obligatorio");
        }
        this.nombre = nombre;
    }
    
    public String getDescripcion() { 
        return descripcion; 
    }
    
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
    
    public TipoEjercicio getTipo() { 
        return tipo; 
    }
    
    public void setTipo(TipoEjercicio tipo) { 
        this.tipo = tipo; 
    }
    
    public String getVideoUrl() { 
        return videoUrl; 
    }
    
    public void setVideoUrl(String videoUrl) { 
        this.videoUrl = videoUrl; 
    }
    
    public String getImagenUrl() { 
        return imagenUrl; 
    }
    
    public void setImagenUrl(String imagenUrl) { 
        this.imagenUrl = imagenUrl; 
    }
    
    /**
     * Obtiene el nombre del tipo en formato legible
     * Ejemplo: PIERNA -> "Pierna"
     */
    public String getTipoNombre() {
        String nombre = tipo.name();
        return nombre.charAt(0) + nombre.substring(1).toLowerCase();
    }
    
    @Override
    public String toString() {
        return nombre + " (" + getTipoNombre() + ")";
    }
    
    /**
     * Dos ejercicios son iguales si tienen el mismo ID
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ejercicio ejercicio = (Ejercicio) obj;
        return id == ejercicio.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}