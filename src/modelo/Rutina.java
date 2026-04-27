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
 * ===============================================================
 * RUTINA DE ENTRENAMIENTO - FIDNESS APP
 * ===============================================================
 * 
 * Autor: César Alonso Morera Alpízar
 * 
 * Descripción:
 * Una rutina es el corazón de la aplicación. Representa un plan de
 * entrenamiento personalizado que contiene uno o más ejercicios.
 * 
 * Cada rutina pertenece a un usuario y tiene:
 * - Un nombre descriptivo
 * - Una fecha de creación
 * - Una lista de ejercicios con series y repeticiones
 * 
 * Reflexión:
 * En mis años como instructor, siempre digo:
 * "Una rutina bien estructurada es como un buen algoritmo:
 *  clara, eficiente y fácil de seguir".
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Abril 2026
 */
public class Rutina implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // ============================================================
    // ATRIBUTOS PRINCIPALES
    // ============================================================
    
    private int id;                     // Identificador único (lo asigna la BD)
    private String nombre;              // Nombre descriptivo de la rutina
    private int idUsuario;              // ID del usuario dueño de la rutina
    private LocalDateTime fechaCreacion; // Cuándo se creó esta rutina
    private List<DetalleRutina> detalles; // Lista de ejercicios con series y repeticiones
    
    // ============================================================
    // CONSTRUCTORES
    // ============================================================
    
    /**
     * Constructor para cargar rutinas desde la base de datos
     * 
     * @param id ID de la rutina
     * @param nombre Nombre de la rutina
     * @param idUsuario ID del usuario dueño
     * @param fechaCreacionStr Fecha en formato String (opcional)
     */
    public Rutina(int id, String nombre, int idUsuario, String fechaCreacionStr) {
        this.id = id;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.detalles = new ArrayList<>();
        
        // Procesar la fecha si viene proporcionada
        if (fechaCreacionStr != null && !fechaCreacionStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                this.fechaCreacion = LocalDateTime.parse(fechaCreacionStr, formatter);
            } catch (Exception e) {
                this.fechaCreacion = LocalDateTime.now();
            }
        } else {
            this.fechaCreacion = LocalDateTime.now();
        }
    }
    
    /**
     * Constructor para crear NUEVAS rutinas (sin ID aún)
     * 
     * @param nombre Nombre de la rutina
     * @param idUsuario ID del usuario dueño
     */
    public Rutina(String nombre, int idUsuario) {
        this.id = 0; // 0 indica que aún no tiene ID asignado por la BD
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }
    
    /**
     * Constructor simplificado para cargar desde BD (sin fecha)
     * 
     * @param id ID de la rutina
     * @param nombre Nombre de la rutina
     * @param idUsuario ID del usuario dueño
     */
    public Rutina(int id, String nombre, int idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
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
     * Obtiene la fecha de creación formateada para mostrar en la UI
     * 
     * @return Fecha en formato dd/MM/yyyy HH:mm
     */
    public String getFechaCreacionFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaCreacion.format(formatter);
    }
    
    /**
     * 🔴 CORREGIDO: Retorna la lista original (no una copia)
     * Esto es necesario para que ExportadorPDF pueda leer los detalles
     * después de guardar la rutina.
     * 
     * @return Lista de detalles (la original, no una copia)
     */
    public List<DetalleRutina> getDetalles() { 
        return detalles;  // 🔴 Ya no retorna una copia nueva
    }
    
    /**
     * Establece la lista de detalles de la rutina
     * 
     * @param detalles Lista de ejercicios con series y repeticiones
     */
    public void setDetalles(List<DetalleRutina> detalles) { 
        this.detalles = detalles; 
    }
    
    // ============================================================
    // MÉTODOS DE MANIPULACIÓN DE EJERCICIOS
    // ============================================================
    
    /**
     * Agrega un ejercicio a la rutina
     * 
     * @param ejercicio El ejercicio a agregar
     * @param series Número de series (mínimo 1)
     * @param repeticiones Número de repeticiones (mínimo 1)
     * @return true si se agregó correctamente
     */
    public boolean agregarEjercicio(Ejercicio ejercicio, int series, int repeticiones) {
        if (ejercicio == null) {
            throw new IllegalArgumentException("El ejercicio no puede ser null");
        }
        if (series < 1) {
            throw new IllegalArgumentException("Las series deben ser al menos 1");
        }
        if (repeticiones < 1) {
            throw new IllegalArgumentException("Las repeticiones deben ser al menos 1");
        }
        
        // Verificar que no exista duplicado
        for (DetalleRutina detalle : detalles) {
            if (detalle.getEjercicio() != null && detalle.getEjercicio().getId() == ejercicio.getId()) {
                return false; // Ya existe, no lo agregamos
            }
        }
        
        int nuevoOrden = detalles.size() + 1;
        DetalleRutina detalle = new DetalleRutina(0, this.id, ejercicio.getId(), nuevoOrden, series, repeticiones, "");
        detalle.setEjercicio(ejercicio); // 🔴 Asegurar que el ejercicio esté asociado
        return detalles.add(detalle);
    }
    
    /**
     * Quita un ejercicio de la rutina y reordena los restantes
     * 
     * @param ejercicio El ejercicio a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean quitarEjercicio(Ejercicio ejercicio) {
        if (ejercicio == null) {
            return false;
        }
        
        boolean removido = detalles.removeIf(d -> d.getIdEjercicio() == ejercicio.getId());
        
        if (removido) {
            // Reordenar los que quedan (mantener secuencia 1, 2, 3...)
            for (int i = 0; i < detalles.size(); i++) {
                detalles.get(i).setOrden(i + 1);
            }
        }
        
        return removido;
    }
    
    /**
     * Actualiza las series y repeticiones de un ejercicio en la rutina
     * 
     * @param ejercicio El ejercicio a actualizar
     * @param series Nuevo número de series
     * @param repeticiones Nuevo número de repeticiones
     * @return true si se actualizó correctamente
     */
    public boolean actualizarDetalle(Ejercicio ejercicio, int series, int repeticiones) {
        for (DetalleRutina detalle : detalles) {
            if (detalle.getIdEjercicio() == ejercicio.getId()) {
                detalle.setSeries(series);
                detalle.setRepeticiones(repeticiones);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Intercambia el orden de dos ejercicios en la rutina
     * 
     * @param orden1 Posición del primer ejercicio
     * @param orden2 Posición del segundo ejercicio
     * @return true si se intercambiaron correctamente
     */
    public boolean intercambiarOrden(int orden1, int orden2) {
        int size = detalles.size();
        
        if (orden1 < 1 || orden1 > size || orden2 < 1 || orden2 > size) {
            System.err.println("Error: Indices fuera de rango. Tamaño de la lista: " + size);
            return false;
        }
        
        DetalleRutina detalle1 = detalles.get(orden1 - 1);
        DetalleRutina detalle2 = detalles.get(orden2 - 1);
        
        detalle1.setOrden(orden2);
        detalle2.setOrden(orden1);
        
        detalles.sort((d1, d2) -> Integer.compare(d1.getOrden(), d2.getOrden()));
        return true;
    }
    
    /**
     * Obtiene el número total de ejercicios en esta rutina
     * 
     * @return Cantidad de ejercicios
     */
    public int getTotalEjercicios() {
        return detalles.size();
    }
    
    // ============================================================
    // MÉTODO TO STRING
    // ============================================================
    
    /**
     * Representación textual de la rutina para mostrar en la UI
     * Formato: "Nombre (X ejercicios) - dd/MM/yyyy HH:mm"
     * 
     * @return Texto descriptivo de la rutina
     */
    @Override
    public String toString() {
        int total = (detalles != null) ? detalles.size() : 0;
        return nombre + " (" + total + " ejercicios) - " + getFechaCreacionFormateada();
    }
}