package controlador;

import modelo.Usuario;
import persistencia.GestorDatos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * CONTROLADOR DE USUARIOS - FIDNESS APP
 * ===============================================================
 * 
 * Autor: César Alonso Morera Alpízar
 * 
 * Descripción:
 * Esta clase representa la capa de lógica de negocio para la gestión
 * de usuarios dentro del sistema.
 * 
 * Su responsabilidad principal es actuar como intermediario entre:
 * 
 * - La capa de persistencia (GestorDatos)
 * - La capa de presentación (vista)
 * 
 * Filosofía aplicada:
 * "Separar responsabilidades permite sistemas más claros, mantenibles
 * y fáciles de escalar". Esto es algo que se refuerza tanto en la
 * universidad como en entornos profesionales.
 * 
 * En términos simples:
 * - El modelo almacena los datos
 * - La vista los muestra
 * - El controlador decide qué hacer con ellos
 */
public class ControladorUsuarios {

    private List<Usuario> usuarios;
    private int siguienteId;

    /**
     * Constructor del controlador
     * 
     * Inicializa la lista de usuarios y carga los datos desde la BD.
     */
    public ControladorUsuarios() {
        usuarios = new ArrayList<>();
        cargarDatos();
    }

    /**
     * ============================================================
     * CARGA DE DATOS
     * ============================================================
     * 
     * Intenta obtener los usuarios desde la base de datos.
     * 
     * Si ocurre un error (por ejemplo, problemas con SQLite o JDK),
     * se activa un modo de respaldo en memoria.
     * 
     * Esto asegura que la aplicación pueda seguir funcionando.
     */
    private void cargarDatos() {

        try {
            usuarios = GestorDatos.cargarUsuarios();

            siguienteId = usuarios.stream()
                    .mapToInt(Usuario::getId)
                    .max()
                    .orElse(0) + 1;

            System.out.println("✅ Usuarios cargados desde base de datos: " + usuarios.size());
            System.out.println("📌 Próximo ID disponible: " + siguienteId);

        } catch (SQLException e) {

            System.out.println("⚠️ No se pudo conectar a la base de datos.");
            System.out.println("💡 Activando modo local (sin persistencia)");

            usuarios = new ArrayList<>();
            usuarios.add(new Usuario(1, "Administrador", "admin@fidness.com", "admin123", true));
            usuarios.add(new Usuario(2, "Usuario Demo", "demo@fidness.com", "demo123", false));

            siguienteId = 3;
        }
    }

    /**
     * ============================================================
     * INICIO DE SESIÓN
     * ============================================================
     * 
     * Verifica las credenciales del usuario.
     * 
     * @param email correo electrónico
     * @param password contraseña
     * @return Usuario autenticado o null
     */
    public Usuario iniciarSesion(String email, String password) {

        for (Usuario u : usuarios) {

            if (u.getEmail().equalsIgnoreCase(email.trim())
                    && u.getPasswordHash().equals(password)) {

                System.out.println("✅ Login exitoso: " + u.getNombre());
                return u;
            }
        }

        System.out.println("❌ Credenciales incorrectas");
        return null;
    }

    /**
     * ============================================================
     * REGISTRO DE USUARIO
     * ============================================================
     * 
     * Crea un nuevo usuario en el sistema.
     */
    public Usuario registrarUsuario(String nombre, String email, String password, boolean esAdmin) {

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Contraseña muy corta");
        }

        // Validar duplicados
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("El correo ya está registrado");
            }
        }

        Usuario nuevo = new Usuario(siguienteId++, nombre, email, password, esAdmin);
        usuarios.add(nuevo);

        System.out.println("✅ Usuario registrado: " + nombre);

        // Intentar persistir en BD
        GestorDatos.guardarUsuario(nuevo);
        System.out.println("💾 Guardado en base de datos");

        return nuevo;
    }

    /**
     * ============================================================
     * BÚSQUEDAS
     * ============================================================
     */

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    /**
     * ============================================================
     * LISTADO DE USUARIOS
     * ============================================================
     */
    public List<Usuario> getTodosLosUsuarios() {
        return new ArrayList<>(usuarios);
    }

    /**
     * ============================================================
     * ACTUALIZACIÓN DE USUARIO
     * ============================================================
     */
    public boolean actualizarUsuario(Usuario actualizado) {

        for (int i = 0; i < usuarios.size(); i++) {

            if (usuarios.get(i).getId() == actualizado.getId()) {

                usuarios.set(i, actualizado);
                System.out.println("✅ Usuario actualizado");

                GestorDatos.actualizarUsuario(actualizado);

                return true;
            }
        }

        return false;
    }

    /**
     * ============================================================
     * ELIMINACIÓN DE USUARIO
     * ============================================================
     */
    public boolean eliminarUsuario(int id) {

        Usuario u = buscarPorId(id);

        if (u == null) return false;

        // Validación: no eliminar último admin
        if (u.isEsAdmin()) {

            long admins = usuarios.stream()
                    .filter(Usuario::isEsAdmin)
                    .count();

            if (admins <= 1) {
                throw new IllegalStateException("No se puede eliminar el último administrador");
            }
        }

        usuarios.remove(u);
        System.out.println("✅ Usuario eliminado");

        return true;
    }
}