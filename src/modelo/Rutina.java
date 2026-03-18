/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Una rutina es como un script de Python bien estructurado:
 * tiene nombre, autor, y se ejecuta paso a paso.
 * 
 * En Syniverse aprendí que todo cambio debe tener timestamp por auditoría.
 * Cuando trabajaba con redes empresariales, cada cambio en la configuración
 * se logueaba con fecha y hora. Por eso la fechaCreacion es vital acá.
 * 
 * @author César Alonso Morera Alpízar
 */
public class Rutina implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private int idUsuario;           // Dueño de la rutina (clave foránea)
    private LocalDateTime fechaCreacion;
    private List<DetalleRutina> detalles;  // Los ejercicios en orden
    
    /**
     * Constructor vacío
     * Inicializa la fecha actual y la lista de detalles
     */
    public Rutina() {
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }
    
    /**
     * Constructor básico
     * 
     * @param id identificador único
     * @param nombre nombre de la rutina (obligatorio)
     * @param idUsuario ID del usuario que crea la rutina
     */
    public Rutina(int id, String nombre, int idUsuario) {
        this.id = id;
        setNombre(nombre);  // Usamos setter para validar
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
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
     * Setter con validación de nombre obligatorio
     * Una rutina sin nombre es como un estudiante sin nombre: no existe
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la rutina es obligatorio");
        }
        this.nombre = nombre;
    }
    
    public int getIdUsuario() { 
        return idUsuario; 
    }
    
    public void setIdUsuario(int idUsuario) { 
        this.idUsuario = idUsuario; 
    }
    
    public LocalDateTime getFechaCreacion() { 
        return fechaCreacion; 
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }
    
    /**
     * Fecha formateada para mostrar en interfaz
     * Los usuarios no quieren ver "2026-03-17T15:30:00", quieren "17/03/2026 15:30"
     */
    public String getFechaCreacionFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaCreacion.format(formatter);
    }
    
    public List<DetalleRutina> getDetalles() { 
        // Devolvemos una copia para mantener encapsulamiento
        return new ArrayList<>(detalles); 
    }
    
    public void setDetalles(List<DetalleRutina> detalles) { 
        this.detalles = detalles; 
    }
    
    /**
     * Agrega un ejercicio a la rutina
     * 
     * @param ejercicio Ejercicio a agregar
     * @param series Número de series
     * @param repeticiones Número de repeticiones
     * @return true si se agregó, false si ya existía (evitamos duplicados)
     */
    public boolean agregarEjercicio(Ejercicio ejercicio, int series, int repeticiones) {
        // Validar que no exista duplicado (según criterio HU3)
        for (DetalleRutina detalle : detalles) {
            if (detalle.getEjercicio().equals(ejercicio)) {
                return false; // Ya existe
            }
        }
        
        int nuevoOrden = detalles.size() + 1;
        DetalleRutina detalle = new DetalleRutina(nuevoOrden, ejercicio, series, repeticiones);
        return detalles.add(detalle);
    }
    
    /**
     * Quita un ejercicio de la rutina
     * 
     * @param ejercicio Ejercicio a quitar
     * @return true si se quitó, false si no estaba
     */
    public boolean quitarEjercicio(Ejercicio ejercicio) {
        boolean removido = detalles.removeIf(d -> d.getEjercicio().equals(ejercicio));
        
        if (removido) {
            // Reordenar los que quedan (mantener secuencia 1,2,3...)
            for (int i = 0; i < detalles.size(); i++) {
                detalles.get(i).setOrden(i + 1);
            }
        }
        
        return removido;
    }
    
    /**
     * Actualiza series y repeticiones de un ejercicio
     */
    public boolean actualizarDetalle(Ejercicio ejercicio, int series, int repeticiones) {
        for (DetalleRutina detalle : detalles) {
            if (detalle.getEjercicio().equals(ejercicio)) {
                detalle.setSeries(series);
                detalle.setRepeticiones(repeticiones);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Reordena los ejercicios (intercambia posiciones)
     * Útil cuando el usuario quiere cambiar el orden de la rutina
     * 
     * @param orden1 primera posición
     * @param orden2 segunda posición
     * @return true si se pudo intercambiar
     */
    public boolean intercambiarOrden(int orden1, int orden2) {
        if (orden1 < 1 || orden1 > detalles.size() || 
            orden2 < 1 || orden2 > detalles.size()) {
            return false;
        }
        
        DetalleRutina detalle1 = detalles.get(orden1 - 1);
        DetalleRutina detalle2 = detalles.get(orden2 - 1);
        
        detalle1.setOrden(orden2);
        detalle2.setOrden(orden1);
        
        // Reordenar la lista según el nuevo orden
        detalles.sort((d1, d2) -> Integer.compare(d1.getOrden(), d2.getOrden()));
        
        return true;
    }
    
    /**
     * Obtiene el número total de ejercicios
     */
    public int getTotalEjercicios() {
        return detalles.size();
    }
    
    @Override
    public String toString() {
        return nombre + " (" + getTotalEjercicios() + " ejercicios) - " + getFechaCreacionFormateada();
    }
}