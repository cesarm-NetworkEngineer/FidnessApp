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
 * @author César Alonso Morera Alpízar
 */
public class Rutina implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private int idUsuario;
    private LocalDateTime fechaCreacion;
    private List<DetalleRutina> detalles;
    
    // 🔴 CONSTRUCTOR CORREGIDO - ahora sí asigna los valores
    public Rutina(int id, String nombre, int idUsuario, String fechaCreacionStr) {
        this.id = id;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        
        // Si se proporciona una fecha, la usamos
        if (fechaCreacionStr != null && !fechaCreacionStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                this.fechaCreacion = LocalDateTime.parse(fechaCreacionStr, formatter);
            } catch (Exception e) {
                this.fechaCreacion = LocalDateTime.now();
            }
        }
    }
    
    // Constructor principal para crear NUEVAS rutinas
    public Rutina(String nombre, int idUsuario) {
        this.id = 0; // 0 significa que aún no tiene ID asignado por la BD
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }
    
    // Constructor simplificado para cargar desde BD
    public Rutina(int id, String nombre, int idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }
    
    // Getters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la rutina es obligatorio");
        }
        this.nombre = nombre;
    }
    
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getFechaCreacionFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaCreacion.format(formatter);
    }
    
    public List<DetalleRutina> getDetalles() { 
        return new ArrayList<>(detalles); 
    }
    
    public void setDetalles(List<DetalleRutina> detalles) { 
        this.detalles = detalles; 
    }
    
    /**
     * Agrega un ejercicio a la rutina
     * Validacion: evita duplicados y maneja el orden correctamente
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
        
        for (DetalleRutina detalle : detalles) {
            if (detalle.getEjercicio().getId() == ejercicio.getId()) {
                return false;
            }
        }
        
        int nuevoOrden = detalles.size() + 1;
        DetalleRutina detalle = new DetalleRutina(0, this.id, ejercicio.getId(), nuevoOrden, series, repeticiones, "");
        return detalles.add(detalle);
    }
    
    public boolean quitarEjercicio(Ejercicio ejercicio) {
        if (ejercicio == null) {
            return false;
        }
        
        boolean removido = detalles.removeIf(d -> d.getIdEjercicio() == ejercicio.getId());
        
        if (removido) {
            for (int i = 0; i < detalles.size(); i++) {
                detalles.get(i).setOrden(i + 1);
            }
        }
        
        return removido;
    }
    
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
    
    public int getTotalEjercicios() {
        return detalles.size();
    }
    
    @Override
    public String toString() {
        return nombre + " (" + getTotalEjercicios() + " ejercicios) - " + getFechaCreacionFormateada();
    }
}