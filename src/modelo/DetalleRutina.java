/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 * Clase asociativa que resuelve la relación muchos-a-muchos
 * entre Rutina y Ejercicio.
 * 
 * Una rutina es como un script de Python bien estructurado: tiene un nombre 
 * que lo identifica, pertenece a un autor, y se ejecuta paso a paso.
 * Esta clase es cada línea de ese script: el ejercicio específico,
 * en qué orden va, cuántas series y repeticiones.
 * 
 * En mis clases, siempre digo: "un detalle es donde viven los datos específicos"
 * Como cuando llenás una planilla de Excel: cada fila es un detalle.
 * 
 * @author César Alonso Morera Alpízar
 */
public class DetalleRutina implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int orden;           // Posición en la rutina (1, 2, 3...)
    private Ejercicio ejercicio;  // Referencia al ejercicio (no guardamos ID, guardamos el objeto)
    private int series;           // Número de series (>= 1)
    private int repeticiones;     // Número de repeticiones (>= 1)
    private String notas;         // Notas adicionales (opcional, ej: "hacer lento" o "peso moderado")
    
    /**
     * Constructor vacío para serialización
     */
    public DetalleRutina() {
    }
    
    /**
     * Constructor con campos obligatorios
     * 
     * @param orden posición en la rutina
     * @param ejercicio el ejercicio a realizar
     * @param series número de series (debe ser >= 1)
     * @param repeticiones número de repeticiones (debe ser >= 1)
     */
    public DetalleRutina(int orden, Ejercicio ejercicio, int series, int repeticiones) {
        this.orden = orden;
        this.ejercicio = ejercicio;
        setSeries(series);           // Usamos setter para validar
        setRepeticiones(repeticiones); // Usamos setter para validar
    }
    
    // Getters y Setters con validación
    
    public int getOrden() { 
        return orden; 
    }
    
    /**
     * Valida que el orden sea positivo
     */
    public void setOrden(int orden) { 
        if (orden < 1) {
            throw new IllegalArgumentException("El orden debe ser 1 o superior");
        }
        this.orden = orden; 
    }
    
    public Ejercicio getEjercicio() { 
        return ejercicio; 
    }
    
    public void setEjercicio(Ejercicio ejercicio) { 
        if (ejercicio == null) {
            throw new IllegalArgumentException("El ejercicio no puede ser null");
        }
        this.ejercicio = ejercicio; 
    }
    
    public int getSeries() { 
        return series; 
    }
    
    /**
     * Valida que las series sean >= 1
     * Un ejercicio con 0 series no tiene sentido
     */
    public void setSeries(int series) {
        if (series < 1) {
            throw new IllegalArgumentException("Las series deben ser al menos 1");
        }
        this.series = series;
    }
    
    public int getRepeticiones() { 
        return repeticiones; 
    }
    
    /**
     * Valida que las repeticiones sean >= 1
     */
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
        this.notas = notas; 
    }
    
    @Override
    public String toString() {
        String base = orden + ". " + ejercicio.getNombre() + " - " + series + "x" + repeticiones;
        if (notas != null && !notas.isEmpty()) {
            base += " (" + notas + ")";
        }
        return base;
    }
}