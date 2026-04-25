package modelo;

import java.io.Serializable;

/**
 * ============================================================
 * DETALLE DE RUTINA
 * ============================================================
 * 
 * Autor: Cesar Alonso Morera Alpizar
 * 
 * Esta clase representa UN ejercicio dentro de una rutina.
 * 
 * Es la "línea de ejecución" de la rutina:
 * - Qué ejercicio se hace
 * - En qué orden
 * - Cuántas series
 * - Cuántas repeticiones
 * 
 * Analogía real:
 * Una rutina es como una receta de cocina.
 * Cada DetalleRutina es un paso:
 * "1. Mezclar", "2. Hornear", etc.
 * 
 * Conceptos aplicados:
 * ✔ Programación orientada a objetos
 * ✔ Validación de datos
 * ✔ Serialización
 */
public class DetalleRutina implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== ATRIBUTOS =====
    private int orden;
    private Ejercicio ejercicio;
    private int series;
    private int repeticiones;
    private String notas;

    /**
     * Constructor vacío (requerido para serialización)
     */
    public DetalleRutina() {}

    /**
     * Constructor principal
     */
    public DetalleRutina(int orden, Ejercicio ejercicio, int series, int repeticiones) {
        setOrden(orden);
        setEjercicio(ejercicio);
        setSeries(series);
        setRepeticiones(repeticiones);
    }

    // ===== GETTERS Y SETTERS =====

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        if (orden < 1) {
            throw new IllegalArgumentException("El orden debe ser mayor o igual a 1");
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
        this.notas = notas;
    }

    /**
     * Representación legible (útil para debugging)
     */
    @Override
    public String toString() {
        String texto = orden + ". " + ejercicio.getNombre() + " - " + series + "x" + repeticiones;

        if (notas != null && !notas.isEmpty()) {
            texto += " (" + notas + ")";
        }

        return texto;
    }
}