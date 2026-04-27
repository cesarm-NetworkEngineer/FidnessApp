package persistencia;

import modelo.Ejercicio;
import modelo.Rutina;
import modelo.DetalleRutina;
import modelo.TipoEjercicio;
import modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GESTOR DE DATOS - FIDNESS APP
 * VERSIÓN CON ARCHIVOS .DAT (Serialización)
 * 
 * Esta versión guarda todos los datos en archivos binarios dentro de la carpeta:
 * C:\Users\TuUsuario\FitnessAppData\
 * 
 * Los archivos que se crean son:
 * - usuarios.dat
 * - ejercicios.dat
 * - rutinas.dat
 * - detalles_rutina.dat
 * 
 * @author César Alonso Morera Alpízar
 */
public class GestorDatos {
    
    private static final String CARPETA_DATOS = System.getProperty("user.home") + 
                                                File.separator + "FitnessAppData";
    
    // Archivos de datos
    private static final String ARCHIVO_USUARIOS = CARPETA_DATOS + File.separator + "usuarios.dat";
    private static final String ARCHIVO_EJERCICIOS = CARPETA_DATOS + File.separator + "ejercicios.dat";
    private static final String ARCHIVO_RUTINAS = CARPETA_DATOS + File.separator + "rutinas.dat";
    private static final String ARCHIVO_DETALLES = CARPETA_DATOS + File.separator + "detalles_rutina.dat";
    
    static {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
            System.out.println("📁 Carpeta de datos creada: " + CARPETA_DATOS);
        }
    }
    
    // ============================================================
    // USUARIOS - CRUD COMPLETO
    // ============================================================
    
    @SuppressWarnings("unchecked")
    public static List<Usuario> cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) {
            // Crear usuarios demo
            List<Usuario> demo = new ArrayList<>();
            demo.add(new Usuario(1, "Administrador", "admin@fidness.com", "admin123", true));
            demo.add(new Usuario(2, "Usuario Demo", "demo@fidness.com", "demo123", false));
            guardarUsuarios(demo);
            return demo;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public static void guardarUsuarios(List<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
            System.out.println("✅ Usuarios guardados: " + usuarios.size());
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }
    
    public static boolean guardarUsuario(Usuario nuevo) {
        List<Usuario> usuarios = cargarUsuarios();
        int nuevoId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        nuevo.setId(nuevoId);
        usuarios.add(nuevo);
        guardarUsuarios(usuarios);
        System.out.println("✅ Usuario guardado: " + nuevo.getNombre() + " (ID: " + nuevoId + ")");
        return true;
    }
    
    public static boolean actualizarUsuario(Usuario actualizado) {
        List<Usuario> usuarios = cargarUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == actualizado.getId()) {
                usuarios.set(i, actualizado);
                guardarUsuarios(usuarios);
                System.out.println("✅ Usuario actualizado: " + actualizado.getNombre());
                return true;
            }
        }
        System.err.println("❌ Usuario no encontrado para actualizar");
        return false;
    }
    
    public static boolean eliminarUsuario(int id) {
        List<Usuario> usuarios = cargarUsuarios();
        boolean removido = usuarios.removeIf(u -> u.getId() == id);
        if (removido) {
            guardarUsuarios(usuarios);
            System.out.println("✅ Usuario eliminado (ID: " + id + ")");
        }
        return removido;
    }
    
    public static Usuario verificarLogin(String email, String password) {
        for (Usuario u : cargarUsuarios()) {
            if (u.getEmail().equalsIgnoreCase(email) && u.verificarPassword(password)) {
                System.out.println("✅ Login exitoso: " + u.getNombre());
                return u;
            }
        }
        System.out.println("❌ Login fallido para: " + email);
        return null;
    }
    
    // ============================================================
    // EJERCICIOS - CRUD COMPLETO
    // ============================================================
    
    @SuppressWarnings("unchecked")
    public static List<Ejercicio> cargarEjercicios() {
        File archivo = new File(ARCHIVO_EJERCICIOS);
        if (!archivo.exists()) {
            // Crear ejercicios demo
            List<Ejercicio> demo = new ArrayList<>();
            demo.add(new Ejercicio(1, "Press de Banca", "Ejercicio para pecho", TipoEjercicio.PECHO, "", ""));
            demo.add(new Ejercicio(2, "Sentadilla", "Ejercicio para piernas", TipoEjercicio.PIERNA, "", ""));
            demo.add(new Ejercicio(3, "Curl de Bíceps", "Ejercicio para brazos", TipoEjercicio.BRAZO, "", ""));
            demo.add(new Ejercicio(4, "Dominadas", "Ejercicio para espalda", TipoEjercicio.ESPALDA, "", ""));
            demo.add(new Ejercicio(5, "Plancha", "Ejercicio para abdomen", TipoEjercicio.ABDOMEN, "", ""));
            guardarEjercicios(demo);
            return demo;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Ejercicio>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando ejercicios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public static void guardarEjercicios(List<Ejercicio> ejercicios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EJERCICIOS))) {
            oos.writeObject(ejercicios);
            System.out.println("✅ Ejercicios guardados: " + ejercicios.size());
        } catch (IOException e) {
            System.err.println("Error guardando ejercicios: " + e.getMessage());
        }
    }
    
    public static boolean guardarEjercicio(Ejercicio nuevo) {
        List<Ejercicio> ejercicios = cargarEjercicios();
        int nuevoId = ejercicios.stream().mapToInt(Ejercicio::getId).max().orElse(0) + 1;
        nuevo.setId(nuevoId);
        ejercicios.add(nuevo);
        guardarEjercicios(ejercicios);
        System.out.println("✅ Ejercicio guardado: " + nuevo.getNombre() + " (ID: " + nuevoId + ")");
        return true;
    }
    
    public static boolean actualizarEjercicio(Ejercicio ejercicio) {
        List<Ejercicio> ejercicios = cargarEjercicios();
        for (int i = 0; i < ejercicios.size(); i++) {
            if (ejercicios.get(i).getId() == ejercicio.getId()) {
                ejercicios.set(i, ejercicio);
                guardarEjercicios(ejercicios);
                System.out.println("✅ Ejercicio actualizado: " + ejercicio.getNombre());
                return true;
            }
        }
        return false;
    }
    
    public static boolean eliminarEjercicio(int id) {
        List<Ejercicio> ejercicios = cargarEjercicios();
        boolean removido = ejercicios.removeIf(e -> e.getId() == id);
        if (removido) {
            guardarEjercicios(ejercicios);
            System.out.println("✅ Ejercicio eliminado (ID: " + id + ")");
        }
        return removido;
    }
    
    // ============================================================
    // RUTINAS - CRUD COMPLETO
    // ============================================================
    
    @SuppressWarnings("unchecked")
    public static List<Rutina> cargarRutinasPorUsuario(int idUsuario) {
        List<Rutina> todas = cargarTodasRutinas();
        return todas.stream()
                .filter(r -> r.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
    }
    
    @SuppressWarnings("unchecked")
    private static List<Rutina> cargarTodasRutinas() {
        File archivo = new File(ARCHIVO_RUTINAS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Rutina>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando rutinas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private static void guardarTodasRutinas(List<Rutina> rutinas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RUTINAS))) {
            oos.writeObject(rutinas);
            System.out.println("✅ Rutinas guardadas: " + rutinas.size());
        } catch (IOException e) {
            System.err.println("Error guardando rutinas: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<DetalleRutina> cargarDetallesRutina(int idRutina) {
        File archivo = new File(ARCHIVO_DETALLES);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            List<DetalleRutina> todos = (List<DetalleRutina>) ois.readObject();
            return todos.stream()
                    .filter(d -> d.getIdRutina() == idRutina)
                    .collect(Collectors.toList());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando detalles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private static void guardarTodosDetalles(List<DetalleRutina> detalles) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DETALLES))) {
            oos.writeObject(detalles);
            System.out.println("✅ Detalles guardados: " + detalles.size());
        } catch (IOException e) {
            System.err.println("Error guardando detalles: " + e.getMessage());
        }
    }
    
    public static boolean guardarRutina(Rutina rutina, List<DetalleRutina> detalles) {
        // Cargar rutinas existentes
        List<Rutina> todasRutinas = cargarTodasRutinas();
        
        // Generar nuevo ID
        int nuevoId = todasRutinas.stream().mapToInt(Rutina::getId).max().orElse(0) + 1;
        rutina.setId(nuevoId);
        
        // Agregar rutina
        todasRutinas.add(rutina);
        guardarTodasRutinas(todasRutinas);
        
        // Guardar detalles
        if (detalles != null && !detalles.isEmpty()) {
            List<DetalleRutina> todosDetalles = cargarTodosDetalles();
            
            for (DetalleRutina detalle : detalles) {
                int nuevoIdDetalle = todosDetalles.stream().mapToInt(DetalleRutina::getId).max().orElse(0) + 1;
                detalle.setId(nuevoIdDetalle);
                detalle.setIdRutina(nuevoId);
                todosDetalles.add(detalle);
            }
            guardarTodosDetalles(todosDetalles);
        }
        
        System.out.println("✅ Rutina guardada: " + rutina.getNombre() + " (ID: " + nuevoId + ")");
        System.out.println("   Con " + (detalles != null ? detalles.size() : 0) + " ejercicios");
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private static List<DetalleRutina> cargarTodosDetalles() {
        File archivo = new File(ARCHIVO_DETALLES);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<DetalleRutina>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando todos los detalles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public static boolean eliminarRutina(int idRutina) {
        // Eliminar rutina
        List<Rutina> todasRutinas = cargarTodasRutinas();
        boolean removido = todasRutinas.removeIf(r -> r.getId() == idRutina);
        
        if (removido) {
            guardarTodasRutinas(todasRutinas);
            
            // Eliminar detalles asociados
            List<DetalleRutina> todosDetalles = cargarTodosDetalles();
            boolean removidoDetalles = todosDetalles.removeIf(d -> d.getIdRutina() == idRutina);
            if (removidoDetalles) {
                guardarTodosDetalles(todosDetalles);
            }
            
            System.out.println("✅ Rutina eliminada (ID: " + idRutina + ")");
        }
        
        return removido;
    }
    
    // ============================================================
    // DIAGNÓSTICO
    // ============================================================
    
    public static void diagnosticar() {
        System.out.println("\n🔍 ===== DIAGNÓSTICO =====");
        System.out.println("📁 Carpeta de datos: " + CARPETA_DATOS);
        
        File carpeta = new File(CARPETA_DATOS);
        if (carpeta.exists()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File f : archivos) {
                    System.out.println("   📄 " + f.getName() + " - " + f.length() + " bytes");
                }
            }
        }
        
        System.out.println("👥 Usuarios: " + cargarUsuarios().size());
        System.out.println("💪 Ejercicios: " + cargarEjercicios().size());
        System.out.println("📋 Rutinas: " + cargarTodasRutinas().size());
        System.out.println("=====================================\n");
    }
}