/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 * DetalleRutina - La relación entre una rutina y un ejercicio.
 * 
 * En bases de datos, esto es lo que llamamos una "tabla puente" o
 * "tabla de relación". Permite que una rutina tenga muchos ejercicios
 * y que un ejercicio aparezca en muchas rutinas.
 * 
 * Además del vínculo, guarda información específica de la aparición:
 * - En qué orden va el ejercicio dentro de la rutina
 * - Cuántas series hay que hacer
 * - Cuántas repeticiones por serie
 * - Notas adicionales (ej: "usar mancuernas de 10kg")
 * 
 * En mi carrera como ingeniero de redes, esto es como una VLAN trunk:
 * conecta dos mundos y lleva información adicional (tag) para distinguir.
 * 
 * @author César Alonso Morera Alpízar
 */
public class DetalleRutina implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int idRutina;
    private int idEjercicio;
    private int orden;
    private int series;
    private int repeticiones;
    private String notas;
    private Ejercicio ejercicio;  // Referencia al objeto (no se guarda en BD)
    
    /**
     * Constructor completo (para cargar desde base de datos)
     */
    public DetalleRutina(int id, int idRutina, int idEjercicio, int orden, int series, int repeticiones, String notas) {
        this.id = id;
        this.idRutina = idRutina;
        this.idEjercicio = idEjercicio;
        this.orden = orden;
        this.series = series;
        this.repeticiones = repeticiones;
        this.notas = notas != null ? notas : "";
        this.ejercicio = null;
    }
    
    /**
     * Constructor para crear nuevos detalles (sin ID, sin nota)
     */
    public DetalleRutina(int orden, Ejercicio ejercicio, int series, int repeticiones) {
        this(0, 0, ejercicio.getId(), orden, series, repeticiones, "");
        this.ejercicio = ejercicio;
    }
    
    /**
     * Constructor para crear nuevos detalles (con nota)
     */
    public DetalleRutina(int orden, Ejercicio ejercicio, int series, int repeticiones, String notas) {
        this(0, 0, ejercicio.getId(), orden, series, repeticiones, notas);
        this.ejercicio = ejercicio;
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
    
    public int getIdRutina() { 
        return idRutina; 
    }
    
    public void setIdRutina(int idRutina) { 
        this.idRutina = idRutina; 
    }
    
    public int getIdEjercicio() { 
        return idEjercicio; 
    }
    
    public void setIdEjercicio(int idEjercicio) { 
        this.idEjercicio = idEjercicio; 
    }
    
    public int getOrden() { 
        return orden; 
    }
    
    public void setOrden(int orden) { 
        this.orden = orden; 
    }
    
    public int getSeries() { 
        return series; 
    }
    
    public void setSeries(int series) {
        if (series < 1) {
            throw new IllegalArgumentException("Las series deben ser al menos 1");
        }
        this.series = series; 
    }
    
    public int getRepeticiones() { 
        return repeticiones; 
    }
    
    public void setRepeticiones(int repeticiones) {
        if (repeticiones < 1) {
            throw new IllegalArgumentException("Las repeticiones deben ser al menos 1");
        }
        this.repeticiones = repeticiones; 
    }
    
    public String getNotas() { 
        return notas; 
    }
    
    public void setNotas(String notas) { 
        this.notas = notas != null ? notas : ""; 
    }
    
    public Ejercicio getEjercicio() { 
        return ejercicio; 
    }
    
    public void setEjercicio(Ejercicio ejercicio) { 
        this.ejercicio = ejercicio;
        if (ejercicio != null) {
            this.idEjercicio = ejercicio.getId();
        }
    }
    
    /**
     * Texto resumen para mostrar en las listas de la UI
     */
    public String getResumen() {
        String nombreEjercicio = ejercicio != null ? ejercicio.getNombre() : "Ejercicio #" + idEjercicio;
        return nombreEjercicio + " - " + series + "x" + repeticiones;
    }
    
    @Override
    public String toString() {
        return getResumen();
    }
}