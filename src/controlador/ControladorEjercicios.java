/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Ejercicio;
import modelo.TipoEjercicio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para operaciones relacionadas con ejercicios.
 * 
 * Separa la lógica de negocio de los ejercicios del resto.
 * Así si mañana cambiamos la persistencia, solo tocamos esta clase.
 * 
 * En DXC Technology, aprendí que los controladores deben ser
 * como los switches de red: reciben peticiones y las dirigen
 * al lugar correcto, sin importarles el contenido.
 * 
 * 🔧 VERSIÓN DE EMERGENCIA - Sin dependencia de base de datos
 * Debido a la incompatibilidad entre JDK 25 y SQLite, esta versión
 * funciona completamente en memoria. Como cuando se va la luz
 * en un restaurante y sacan las velas: no es lo ideal, pero
 * la gente sigue comiendo. Todas las funcionalidades están presentes.
 * 
 * @author César Alonso Morera Alpízar
 */
public class ControladorEjercicios {
    
    private List<Ejercicio> ejercicios;
    private int siguienteId;
    
    public ControladorEjercicios() {
        this.ejercicios = new ArrayList<>();
        cargarDatos();
    }
    
    /**
     * Carga ejercicios de demostración (modo 100% funcional sin BD)
     * 
     * En mis clases presenciales, estos son los ejercicios que más
     * piden mis estudiantes. Por eso los puse como datos de prueba.
     * 
     * Cuando un alumno me pregunta "profe, ¿qué ejercicios puedo hacer?",
     * esta es mi respuesta: los básicos que nunca fallan.
     */
    private void cargarDatos() {
        System.out.println("\n🏋️ === CARGANDO CATÁLOGO DE EJERCICIOS (MODO DEMOSTRACIÓN) ===");
        System.out.println("   Como cuando llegas a un gimnasio nuevo y te dan");
        System.out.println("   una rutina básica para empezar. ¡Aquí están los clásicos!\n");
        
        ejercicios = new ArrayList<>();
        
        // ===== EJERCICIO 1: PRESS DE BANCA =====
        Ejercicio e1 = new Ejercicio(1, "Press de Banca", 
            "Acostado en banca plana, baja la barra hasta el pecho y empuja hacia arriba. Mantén los codos a 45 grados.", 
            TipoEjercicio.PECHO, "", "");
        ejercicios.add(e1);
        System.out.println("   ✅ Press de Banca (Pecho) - El clásico para lucir pecho");
        
        // ===== EJERCICIO 2: SENTADILLA =====
        Ejercicio e2 = new Ejercicio(2, "Sentadilla", 
            "Con barra sobre el trapecio, baja como si fueras a sentarte en una silla. Mantén la espalda recta.", 
            TipoEjercicio.PIERNA, "", "");
        ejercicios.add(e2);
        System.out.println("   ✅ Sentadilla (Pierna) - El rey de los ejercicios de pierna");
        
        // ===== EJERCICIO 3: CURL DE BÍCEPS =====
        Ejercicio e3 = new Ejercicio(3, "Curl de Bíceps", 
            "De pie, con codos pegados al cuerpo, sube las mancuernas hacia los hombros. Baja controlado.", 
            TipoEjercicio.BRAZO, "", "");
        ejercicios.add(e3);
        System.out.println("   ✅ Curl de Bíceps (Brazo) - Para esos brazos que lucen en verano");
        
        // ===== EJERCICIO 4: DOMINADAS =====
        Ejercicio e4 = new Ejercicio(4, "Dominadas", 
            "Agarrar la barra con palmas al frente (agarre prono). Sube hasta que la barbilla supere la barra.", 
            TipoEjercicio.ESPALDA, "", "");
        ejercicios.add(e4);
        System.out.println("   ✅ Dominadas (Espalda) - El ejercicio que todos aman odiar");
        
        // ===== EJERCICIO 5: PLANCHA ABDOMINAL =====
        Ejercicio e5 = new Ejercicio(5, "Plancha Abdominal", 
            "Apoyado en antebrazos y puntas de pies. Mantén el cuerpo recto como una tabla.", 
            TipoEjercicio.ABDOMEN, "", "");
        ejercicios.add(e5);
        System.out.println("   ✅ Plancha Abdominal (Abdomen) - Simple, pero no fácil");
        
        // ===== EJERCICIO 6: PESO MUERTO =====
        Ejercicio e6 = new Ejercicio(6, "Peso Muerto", 
            "Con barra en el suelo, agarre a lo ancho de hombros. Empuja con las piernas manteniendo la espalda recta.", 
            TipoEjercicio.FULLBODY, "", "");
        ejercicios.add(e6);
        System.out.println("   ✅ Peso Muerto (Fullbody) - El ejercicio más completo que existe");
        
        siguienteId = 7;
        
        System.out.println("\n📊 RESUMEN: " + ejercicios.size() + " ejercicios cargados");
        System.out.println("   💡 Puedes agregar más desde el panel de administrador");
        System.out.println("   ⚠️ Los cambios no se guardan al cerrar la app (modo demostración)\n");
    }
    
    /**
     * Obtiene todos los ejercicios del catálogo
     * 
     * @return lista completa de ejercicios
     */
    public List<Ejercicio> getTodosLosEjercicios() {
        return new ArrayList<>(ejercicios);
    }
    
    /**
     * Busca ejercicios por tipo (PIERNA, BRAZO, PECHO, etc.)
     * 
     * @param tipo el tipo de ejercicio a filtrar
     * @return lista de ejercicios de ese tipo
     */
    public List<Ejercicio> buscarPorTipo(TipoEjercicio tipo) {
        return ejercicios.stream()
            .filter(e -> e.getTipo() == tipo)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca ejercicios por nombre (búsqueda parcial, sin importar mayúsculas)
     * 
     * En mis cursos de Rocky Linux, enseñaba que el grep es case sensitive
     * y los estudiantes siempre se quejaban. Acá lo hacemos amigable.
     * Si el usuario escribe "press", encuentra "Press de Banca".
     * 
     * @param nombre texto a buscar (puede ser parte del nombre)
     * @return lista de ejercicios que coinciden
     */
    public List<Ejercicio> buscarPorNombre(String nombre) {
        String busqueda = nombre.toLowerCase().trim();
        return ejercicios.stream()
            .filter(e -> e.getNombre().toLowerCase().contains(busqueda))
            .collect(Collectors.toList());
    }
    
    /**
     * Busca un ejercicio por su ID único
     * 
     * @param id identificador del ejercicio
     * @return el ejercicio encontrado, o null si no existe
     */
    public Ejercicio buscarPorId(int id) {
        return ejercicios.stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Agrega un nuevo ejercicio al catálogo
     * 
     * @param nombre nombre del ejercicio (debe ser único)
     * @param descripcion explicación de cómo hacerlo
     * @param tipo categoría del ejercicio
     * @param videoURL enlace a video (opcional)
     * @param imagenURL enlace a imagen (opcional)
     * @return el ejercicio creado
     * @throws IllegalArgumentException si hay datos inválidos o nombre duplicado
     */
    public Ejercicio agregarEjercicio(String nombre, String descripcion, 
                                      TipoEjercicio tipo, String videoURL, String imagenURL) {
        // Validaciones básicas - la seguridad no cambia, estemos en modo normal o emergencia
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ El nombre es obligatorio. No dejes el campo vacío.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ La descripción es obligatoria. Explica cómo se hace el ejercicio.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("❌ El tipo es obligatorio. Selecciona una categoría.");
        }
        
        // Validar que no exista otro ejercicio con el mismo nombre
        boolean existe = ejercicios.stream()
            .anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre));
        
        if (existe) {
            throw new IllegalArgumentException("❌ Ya existe un ejercicio con el nombre '" + nombre + "'. Usa otro nombre.");
        }
        
        // Crear y agregar el ejercicio
        Ejercicio nuevo = new Ejercicio(siguienteId++, nombre, descripcion, tipo, videoURL, imagenURL);
        ejercicios.add(nuevo);
        
        System.out.println("✅ NUEVO EJERCICIO AGREGADO: " + nombre);
        System.out.println("   📝 Descripción: " + (descripcion.length() > 50 ? descripcion.substring(0, 50) + "..." : descripcion));
        System.out.println("   🏷️  Tipo: " + tipo);
        if (videoURL != null && !videoURL.isEmpty()) {
            System.out.println("   🎬 Video: " + videoURL);
        }
        
        return nuevo;
    }
    
    /**
     * Actualiza los datos de un ejercicio existente
     * 
     * @param id ID del ejercicio a actualizar
     * @param nombre nuevo nombre (opcional)
     * @param descripcion nueva descripción (opcional)
     * @param tipo nuevo tipo (opcional)
     * @param videoURL nueva URL de video (opcional)
     * @param imagenURL nueva URL de imagen (opcional)
     * @return true si se actualizó correctamente
     */
    public boolean actualizarEjercicio(int id, String nombre, String descripcion, 
                                       TipoEjercicio tipo, String videoURL, String imagenURL) {
        Ejercicio ejercicio = buscarPorId(id);
        if (ejercicio == null) {
            System.err.println("❌ No se encontró el ejercicio con ID: " + id);
            return false;
        }
        
        System.out.println("✏️ ACTUALIZANDO EJERCICIO: " + ejercicio.getNombre());
        
        // Validar que el nuevo nombre no esté en uso por otro ejercicio
        if (nombre != null && !nombre.trim().isEmpty()) {
            boolean nombreEnUso = ejercicios.stream()
                .anyMatch(e -> e.getId() != id && e.getNombre().equalsIgnoreCase(nombre));
            
            if (nombreEnUso) {
                throw new IllegalArgumentException("❌ Ya existe otro ejercicio con el nombre '" + nombre + "'");
            }
            System.out.println("   📝 Nombre anterior: " + ejercicio.getNombre());
            ejercicio.setNombre(nombre);
            System.out.println("   📝 Nuevo nombre: " + nombre);
        }
        
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            ejercicio.setDescripcion(descripcion);
            System.out.println("   📝 Descripción actualizada");
        }
        
        if (tipo != null) {
            System.out.println("   🏷️  Tipo anterior: " + ejercicio.getTipo());
            ejercicio.setTipo(tipo);
            System.out.println("   🏷️  Nuevo tipo: " + tipo);
        }
        
        if (videoURL != null) {
            ejercicio.setVideoURL(videoURL);
            System.out.println("   🎬 Video URL actualizada");
        }
        
        if (imagenURL != null) {
            ejercicio.setImagenURL(imagenURL);
            System.out.println("   🖼️ Imagen URL actualizada");
        }
        
        System.out.println("✅ EJERCICIO ACTUALIZADO CORRECTAMENTE: " + ejercicio.getNombre());
        return true;
    }
    
    /**
     * Elimina un ejercicio del catálogo
     * 
     * NOTA: En un sistema completo, habría que verificar que el ejercicio
     * no esté siendo usado en ninguna rutina antes de eliminarlo.
     * Por ahora, eliminamos directamente.
     * 
     * @param id ID del ejercicio a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarEjercicio(int id) {
        Ejercicio ejercicio = buscarPorId(id);
        if (ejercicio == null) {
            System.err.println("❌ No se encontró el ejercicio con ID: " + id);
            return false;
        }
        
        System.out.println("🗑️ ELIMINANDO EJERCICIO: " + ejercicio.getNombre());
        
        boolean removido = ejercicios.removeIf(e -> e.getId() == id);
        
        if (removido) {
            System.out.println("✅ Ejercicio eliminado: " + ejercicio.getNombre() + " (ID: " + id + ")");
        } else {
            System.out.println("❌ No se pudo eliminar el ejercicio");
        }
        
        return removido;
    }
}