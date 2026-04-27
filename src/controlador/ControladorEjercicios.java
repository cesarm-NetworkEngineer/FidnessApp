/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import persistencia.GestorDatos;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones relacionadas con ejercicios.
 * 
 * Este controlador maneja toda la lógica de negocio relacionada con los
 * ejercicios del catálogo: agregar, editar, eliminar y buscar ejercicios.
 * 
 * Comunica la vista (interfaz gráfica) con la capa de persistencia (base de datos).
 * 
 * @author César Alonso Morera Alpízar
 */
public class ControladorEjercicios {
    
    private List<Ejercicio> ejercicios;
    
    /**
     * Constructor del controlador de ejercicios
     * Inicializa la lista cargando los datos desde la base de datos
     */
    public ControladorEjercicios() {
        this.ejercicios = new ArrayList<>();
        cargarDatos();
    }
    
    /**
     * Carga los ejercicios desde la base de datos
     * Si falla la conexión, usa ejercicios por defecto en memoria
     */
    private void cargarDatos() {
        try {
            ejercicios = GestorDatos.cargarEjercicios();
            System.out.println("✅ Cargados " + ejercicios.size() + " ejercicios desde BD");
        } catch (Exception e) {  // 🔴 CORREGIDO: SQLException → Exception
            System.err.println("❌ Error cargando ejercicios: " + e.getMessage());
            cargarEjerciciosPorDefecto();
        }
    }
    
    /**
     * Carga ejercicios de demostración en memoria (fallback)
     * Se usa cuando no hay conexión a la base de datos
     */
    private void cargarEjerciciosPorDefecto() {
        ejercicios = new ArrayList<>();
        ejercicios.add(new Ejercicio(1, "Press de Banca", "Ejercicio para pecho", TipoEjercicio.PECHO, "", ""));
        ejercicios.add(new Ejercicio(2, "Sentadilla", "Ejercicio para piernas", TipoEjercicio.PIERNA, "", ""));
        ejercicios.add(new Ejercicio(3, "Curl de Biceps", "Ejercicio para brazos", TipoEjercicio.BRAZO, "", ""));
        ejercicios.add(new Ejercicio(4, "Dominadas", "Ejercicio para espalda", TipoEjercicio.ESPALDA, "", ""));
        ejercicios.add(new Ejercicio(5, "Plancha", "Ejercicio para abdomen", TipoEjercicio.ABDOMEN, "", ""));
        System.out.println("⚠️ Usando " + ejercicios.size() + " ejercicios por defecto (en memoria)");
    }
    
    /**
     * Obtiene todos los ejercicios del catálogo
     * @return Lista de ejercicios (copia para no modificar la original)
     */
    public List<Ejercicio> getTodosLosEjercicios() {
        return new ArrayList<>(ejercicios);
    }
    
    /**
     * Busca un ejercicio por su ID
     * @param id Identificador del ejercicio
     * @return El ejercicio encontrado, o null si no existe
     */
    public Ejercicio buscarPorId(int id) {
        for (Ejercicio e : ejercicios) {
            if (e.getId() == id) return e;
        }
        return null;
    }
    
    /**
     * Busca ejercicios por nombre (búsqueda parcial, no sensible a mayúsculas)
     * @param texto Texto a buscar en el nombre
     * @return Lista de ejercicios que coinciden
     */
    public List<Ejercicio> buscarPorNombre(String texto) {
        List<Ejercicio> resultado = new ArrayList<>();
        String busqueda = texto.toLowerCase();
        for (Ejercicio e : ejercicios) {
            if (e.getNombre().toLowerCase().contains(busqueda)) {
                resultado.add(e);
            }
        }
        return resultado;
    }
    
    /**
     * Busca ejercicios por tipo (Pecho, Pierna, Brazo, etc.)
     * @param tipo Tipo de ejercicio a filtrar
     * @return Lista de ejercicios del tipo especificado
     */
    public List<Ejercicio> buscarPorTipo(TipoEjercicio tipo) {
        List<Ejercicio> resultado = new ArrayList<>();
        for (Ejercicio e : ejercicios) {
            if (e.getTipo() == tipo) {
                resultado.add(e);
            }
        }
        return resultado;
    }
    
    /**
     * Agrega un nuevo ejercicio al catálogo
     * @param nombre Nombre del ejercicio
     * @param descripcion Descripción del ejercicio
     * @param tipo Tipo de ejercicio (Pecho, Pierna, etc.)
     * @param videoUrl URL del video demostrativo (opcional)
     * @param imagenUrl URL de la imagen ilustrativa (opcional)
     * @return El ejercicio creado con su ID asignado, o null si hubo error
     */
    public Ejercicio agregarEjercicio(String nombre, String descripcion, TipoEjercicio tipo, String videoUrl, String imagenUrl) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        int nuevoId = ejercicios.size() + 1;
        Ejercicio nuevo = new Ejercicio(nuevoId, nombre, descripcion, tipo, videoUrl, imagenUrl);
        ejercicios.add(nuevo);
        
        // Intentar guardar en base de datos
        boolean guardado = GestorDatos.guardarEjercicio(nuevo);
        if (guardado) {
            System.out.println("✅ Ejercicio guardado en BD: " + nombre);
        } else {
            System.out.println("⚠️ Ejercicio guardado solo en memoria: " + nombre);
        }
        
        return nuevo;
    }
    
    /**
     * Actualiza un ejercicio existente
     * @param id ID del ejercicio a actualizar
     * @param nombre Nuevo nombre
     * @param descripcion Nueva descripción
     * @param tipo Nuevo tipo
     * @param videoUrl Nueva URL de video
     * @param imagenUrl Nueva URL de imagen
     * @return true si se actualizó correctamente
     */
    public boolean actualizarEjercicio(int id, String nombre, String descripcion, TipoEjercicio tipo, String videoUrl, String imagenUrl) {
        Ejercicio e = buscarPorId(id);
        if (e == null) return false;
        
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setTipo(tipo);
        e.setVideoUrl(videoUrl);
        e.setImagenUrl(imagenUrl);
        
        // Intentar actualizar en base de datos
        boolean actualizado = GestorDatos.actualizarEjercicio(e);
        if (actualizado) {
            System.out.println("✅ Ejercicio actualizado en BD: " + nombre);
        } else {
            System.out.println("⚠️ Ejercicio actualizado solo en memoria: " + nombre);
        }
        return true;
    }
    
    /**
     * Elimina un ejercicio del catálogo
     * @param id ID del ejercicio a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarEjercicio(int id) {
        Ejercicio e = buscarPorId(id);
        if (e == null) return false;
        
        ejercicios.remove(e);
        boolean eliminado = GestorDatos.eliminarEjercicio(id);
        if (eliminado) {
            System.out.println("✅ Ejercicio eliminado de BD: " + e.getNombre());
        } else {
            System.out.println("⚠️ Ejercicio eliminado solo de memoria: " + e.getNombre());
        }
        return true;
    }
    
    /**
     * Refresca la lista de ejercicios desde la base de datos
     */
    public void refrescar() {
        cargarDatos();
    }
}