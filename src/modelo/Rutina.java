package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * CLASE: Rutina
 * ============================================================
 *
 * Autor: Cesar Alonso Morera Alpizar
 *
 * 📌 ¿Qué representa esta clase?
 * ------------------------------------------------------------
 * Una rutina es un conjunto organizado de ejercicios.
 *
 * 📌 Analogía (para exposición):
 * ------------------------------------------------------------
 * Una rutina es como un "script de programación":
 *
 * - Tiene un nombre (identificación)
 * - Tiene un autor (usuario)
 * - Tiene pasos ordenados (ejercicios)
 *
 * Cada paso está representado por la clase DetalleRutina.
 *
 * 📌 Relación importante:
 * ------------------------------------------------------------
 * Rutina 1 --- * DetalleRutina
 *
 * Es decir:
 * Una rutina tiene MUCHOS ejercicios.
 *
 * ============================================================
 */
public class Rutina implements Serializable {

    private static final long serialVersionUID = 1L;

    // ================== ATRIBUTOS ==================
    private int id;
    private String nombre;
    private int idUsuario; // 🔥 IMPORTANTE: es INT, no String
    private LocalDateTime fechaCreacion;
    private List<DetalleRutina> detalles;

    // ================== CONSTRUCTORES ==================

    /**
     * Constructor vacío (usado por serialización o BD)
     */
    public Rutina() {
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }

    /**
     * Constructor principal
     */
    public Rutina(int id, String nombre, int idUsuario) {
        this.id = id;
        setNombre(nombre);
        this.idUsuario = idUsuario;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }

    // ================== GETTERS Y SETTERS ==================

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
     * Validación básica: nombre obligatorio
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la rutina es obligatorio");
        }
        this.nombre = nombre;
    }

    /**
     * 🔥 MÉTODO CORRECTO (ANTES ESTABA MAL)
     */
    public int getUsuarioId() {
        return idUsuario;
    }

    public void setUsuarioId(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Formato bonito para UI o reportes
     */
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

    // ================== LÓGICA DE NEGOCIO ==================

    /**
     * Agrega un ejercicio a la rutina
     *
     * ✔ Evita duplicados
     * ✔ Valida datos
     * ✔ Mantiene el orden automático
     */
    public boolean agregarEjercicio(Ejercicio ejercicio, int series, int repeticiones) {

        if (ejercicio == null) {
            throw new IllegalArgumentException("El ejercicio no puede ser null");
        }

        if (series < 1 || repeticiones < 1) {
            throw new IllegalArgumentException("Series y repeticiones deben ser >= 1");
        }

        // 🔐 Evitar duplicados
        for (DetalleRutina d : detalles) {
            if (d.getEjercicio().equals(ejercicio)) {
                return false;
            }
        }

        int orden = detalles.size() + 1;

        DetalleRutina nuevo = new DetalleRutina(orden, ejercicio, series, repeticiones);

        return detalles.add(nuevo);
    }

    /**
     * Elimina un ejercicio y reordena la lista
     */
    public boolean quitarEjercicio(Ejercicio ejercicio) {

        if (ejercicio == null) return false;

        boolean eliminado = detalles.removeIf(d -> d.getEjercicio().equals(ejercicio));

        if (eliminado) {
            // 🔄 Reordenar
            for (int i = 0; i < detalles.size(); i++) {
                detalles.get(i).setOrden(i + 1);
            }
        }

        return eliminado;
    }

    /**
     * Actualiza series y repeticiones de un ejercicio
     */
    public boolean actualizarDetalle(Ejercicio ejercicio, int series, int repeticiones) {

        for (DetalleRutina d : detalles) {
            if (d.getEjercicio().equals(ejercicio)) {
                d.setSeries(series);
                d.setRepeticiones(repeticiones);
                return true;
            }
        }

        return false;
    }

    /**
     * Intercambia el orden de dos ejercicios
     */
    public boolean intercambiarOrden(int o1, int o2) {

        int size = detalles.size();

        if (o1 < 1 || o1 > size || o2 < 1 || o2 > size) {
            return false;
        }

        DetalleRutina d1 = detalles.get(o1 - 1);
        DetalleRutina d2 = detalles.get(o2 - 1);

        d1.setOrden(o2);
        d2.setOrden(o1);

        detalles.sort((a, b) -> Integer.compare(a.getOrden(), b.getOrden()));

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