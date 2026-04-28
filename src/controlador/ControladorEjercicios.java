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
 * Comunica la vista (interfaz gráfica) con la capa de persistencia (archivos .dat).
 * 
 * @author César Alonso Morera Alpízar
 */
public class ControladorEjercicios {
    
    // Lista en memoria que actúa como caché de los ejercicios
    private List<Ejercicio> ejercicios;
    
    /**
     * Constructor: al crear el controlador, carga los datos desde el archivo.
     */
    public ControladorEjercicios() {
        this.ejercicios = new ArrayList<>();
        cargarDatos();
    }
    
    /**
     * Carga los ejercicios desde el gestor de datos (archivo .dat).
     * Si falla la conexión, usa una lista de ejercicios por defecto en memoria.
     * Así la aplicación nunca se queda sin datos.
     */
    private void cargarDatos() {
        try {
            ejercicios = GestorDatos.cargarEjercicios();
            System.out.println("✅ Cargados " + ejercicios.size() + " ejercicios desde archivo");
        } catch (Exception e) {
            System.err.println("❌ Error cargando ejercicios: " + e.getMessage());
            cargarEjerciciosPorDefecto();
        }
    }
    
    /**
     * Carga ejercicios de demostración en memoria (fallback).
     * Esto ocurre solo si no se pudo leer el archivo (ejemplo: primera ejecución).
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
     * Devuelve una copia de la lista de ejercicios.
     * Se usa sobre todo para mostrar en tablas o listas de la interfaz.
     * @return Lista de todos los ejercicios (copia para no modificar la original).
     */
    public List<Ejercicio> getTodosLosEjercicios() {
        return new ArrayList<>(ejercicios);
    }
    
    /**
     * Busca un ejercicio por su ID.
     * @param id Identificador único del ejercicio
     * @return El ejercicio encontrado, o null si no existe
     */
    public Ejercicio buscarPorId(int id) {
        for (Ejercicio e : ejercicios) {
            if (e.getId() == id) return e;
        }
        return null;
    }
    
    /**
     * Busca ejercicios por nombre (búsqueda parcial, no sensible a mayúsculas).
     * @param texto Texto a buscar en el nombre
     * @return Lista de ejercicios que coinciden (puede estar vacía)
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
     * Agrega un nuevo ejercicio al catálogo.
     * Primero guarda en el archivo (GestorDatos) y luego recarga la lista local
     * para que quede sincronizada. Así no hace falta reiniciar la app.
     * 
     * @param nombre      Nombre del ejercicio
     * @param descripcion Descripción del ejercicio
     * @param tipo        Tipo de ejercicio (Pecho, Pierna, etc.)
     * @param videoUrl    URL del video demostrativo (opcional)
     * @param imagenUrl   URL de la imagen ilustrativa (opcional)
     * @return El ejercicio creado con su ID real asignado (desde el archivo)
     */
    public Ejercicio agregarEjercicio(String nombre, String descripcion, TipoEjercicio tipo, 
                                      String videoUrl, String imagenUrl) {
        // 1. Creamos un objeto con ID = 0 (el gestor le asignará el real)
        Ejercicio nuevo = new Ejercicio(0, nombre, descripcion, tipo, videoUrl, imagenUrl);
        
        // 2. Guardamos en el archivo (el gestor asigna el ID real)
        boolean guardado = GestorDatos.guardarEjercicio(nuevo);
        
        if (guardado) {
            // 3. ¡Clave! Recargamos toda la lista desde el archivo.
            //    Así la lista local (ejercicios) queda idéntica a lo que hay en disco.
            cargarDatos();
            System.out.println("✅ Ejercicio guardado en archivo y memoria actualizada. ID: " + nuevo.getId());
        } else {
            // Fallo al guardar en disco: al menos lo dejamos en memoria para esta sesión.
            System.err.println("❌ No se pudo guardar en archivo. Agregando solo en memoria.");
            int nuevoId = ejercicios.stream().mapToInt(Ejercicio::getId).max().orElse(0) + 1;
            nuevo.setId(nuevoId);
            ejercicios.add(nuevo);
        }
        
        // 4. Devolvemos el ejercicio con su ID final (real o temporal)
        return buscarPorId(nuevo.getId());
    }
    
    /**
     * Actualiza un ejercicio existente.
     * @param id         ID del ejercicio a actualizar
     * @param nombre     Nuevo nombre
     * @param descripcion Nueva descripción
     * @param tipo       Nuevo tipo
     * @param videoUrl   Nueva URL de video
     * @param imagenUrl  Nueva URL de imagen
     * @return true si se actualizó correctamente
     */
    public boolean actualizarEjercicio(int id, String nombre, String descripcion, 
                                       TipoEjercicio tipo, String videoUrl, String imagenUrl) {
        Ejercicio e = buscarPorId(id);
        if (e == null) return false;
        
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setTipo(tipo);
        e.setVideoUrl(videoUrl);
        e.setImagenUrl(imagenUrl);
        
        boolean actualizado = GestorDatos.actualizarEjercicio(e);
        if (actualizado) {
            System.out.println("✅ Ejercicio actualizado en archivo: " + nombre);
            // Recargar para mantener sincronía
            cargarDatos();
        } else {
            System.out.println("⚠️ Ejercicio actualizado solo en memoria: " + nombre);
        }
        return true;
    }
    
    /**
     * Elimina un ejercicio del catálogo.
     * @param id ID del ejercicio a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarEjercicio(int id) {
        Ejercicio e = buscarPorId(id);
        if (e == null) return false;
        
        ejercicios.remove(e);
        boolean eliminado = GestorDatos.eliminarEjercicio(id);
        if (eliminado) {
            System.out.println("✅ Ejercicio eliminado del archivo: " + e.getNombre());
            // Recargar para actualizar la lista local (pueden cambiar los IDs)
            cargarDatos();
        } else {
            System.out.println("⚠️ Ejercicio eliminado solo de memoria: " + e.getNombre());
        }
        return true;
    }
    
    /**
     * Refresca manualmente la lista de ejercicios desde el archivo.
     * Útil si otras partes de la aplicación modificaron los datos.
     */
    public void refrescar() {
        cargarDatos();
        System.out.println("🔄 Lista de ejercicios recargada desde archivo");
    }
}