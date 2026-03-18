/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 * Clase que representa un ejercicio del catálogo.
 * 
 * Cuando creaba contenido para AVA® sobre Rocky Linux 9, entendí que los 
 * ejemplos concretos son los que pegan. Por eso puse 'Press de Banca' en lugar 
 * de 'Ejercicio1'. La gente necesita visualizar.
 * 
 * Y el videoURL no es adorno: en mis clases virtuales, el video es el 50% del 
 * aprendizaje. Un texto bien escrito está bien, pero un video mostrando
 * la técnica correcta vale oro.
 * 
 * @author César Alonso Morera Alpízar
 */
public class Ejercicio implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private String descripcion;
    private TipoEjercicio tipo;
    private String videoURL;    // Enlace a YouTube o similar
    private String imagenURL;   // Imagen ilustrativa
    
    /**
     * Constructor vacío necesario para serialización
     */
    public Ejercicio() {
    }
    
    /**
     * Constructor con campos obligatorios
     */
    public Ejercicio(int id, String nombre, String descripcion, TipoEjercicio tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }
    
    /**
     * Constructor completo
     */
    public Ejercicio(int id, String nombre, String descripcion, TipoEjercicio tipo, 
                    String videoURL, String imagenURL) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.videoURL = videoURL;
        this.imagenURL = imagenURL;
    }
    
    // Getters y Setters
    
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    /**
     * Valida que el nombre no esté vacío
     * Un ejercicio sin nombre es como un estudiante sin matrícula
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del ejercicio es obligatorio");
        }
        this.nombre = nombre;
    }
    
    public String getDescripcion() { 
        return descripcion; 
    }
    
    /**
     * Valida que la descripción no esté vacía
     * La descripción es donde el usuario aprende la técnica
     */
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        this.descripcion = descripcion;
    }
    
    public TipoEjercicio getTipo() { 
        return tipo; 
    }
    
    public void setTipo(TipoEjercicio tipo) { 
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de ejercicio es obligatorio");
        }
        this.tipo = tipo; 
    }
    
    public String getVideoURL() { 
        return videoURL; 
    }
    
    /**
     * Setter con validación básica de URL
     * 
     * @param videoURL La URL a guardar
     * @throws IllegalArgumentException si la URL no es válida
     */
    public void setVideoURL(String videoURL) {
        if (videoURL != null && !videoURL.isEmpty()) {
            // Validación simple: debe empezar con http:// o https://
            if (videoURL.startsWith("http://") || videoURL.startsWith("https://")) {
                this.videoURL = videoURL;
            } else {
                throw new IllegalArgumentException("La URL del video debe comenzar con http:// o https://");
            }
        } else {
            this.videoURL = null;
        }
    }
    
    public String getImagenURL() { 
        return imagenURL; 
    }
    
    /**
     * Setter con validación básica de URL
     * 
     * @param imagenURL La URL a guardar
     * @throws IllegalArgumentException si la URL no es válida
     */
    public void setImagenURL(String imagenURL) {
        if (imagenURL != null && !imagenURL.isEmpty()) {
            if (imagenURL.startsWith("http://") || imagenURL.startsWith("https://")) {
                this.imagenURL = imagenURL;
            } else {
                throw new IllegalArgumentException("La URL de la imagen debe comenzar con http:// o https://");
            }
        } else {
            this.imagenURL = null;
        }
    }
    
    @Override
    public String toString() {
        return nombre;
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