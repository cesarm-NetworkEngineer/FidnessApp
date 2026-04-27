/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Usuario;
import persistencia.GestorDatos;

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
 * 
 * 🔧 CORREGIDO (Abril 2026):
 * - Ajustado para trabajar correctamente con GestorDatos moderno
 * - Los usuarios demo y admin se crean automáticamente si no existen
 * - La persistencia ahora es permanente gracias a auto-commit=true
 * - Corregido el método diagnosticar() para usar el método correcto de GestorDatos
 * - Agregados mensajes de depuración más claros
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Abril 2026
 */
public class ControladorUsuarios {

    private List<Usuario> usuarios;
    private int siguienteId;

    /**
     * Constructor del controlador
     * 
     * Inicializa la lista de usuarios y carga los datos desde la BD.
     * Si la base de datos no existe o está vacía, crea usuarios de demostración.
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
     * Si no hay usuarios, crea los de demostración automáticamente.
     * Si hay error de conexión, activa el modo de emergencia en memoria.
     */
    private void cargarDatos() {
        try {
            usuarios = GestorDatos.cargarUsuarios();
            
            // Si no hay usuarios en la base de datos, crear los de demostración
            if (usuarios.isEmpty()) {
                System.out.println("📝 Base de datos sin usuarios. Creando usuarios de demostración...");
                crearUsuariosDeDemostracion();
                usuarios = GestorDatos.cargarUsuarios(); // Recargar después de crear
            }
            
            // Calcular el siguiente ID disponible
            siguienteId = usuarios.stream()
                    .mapToInt(Usuario::getId)
                    .max()
                    .orElse(0) + 1;

            System.out.println("✅ Usuarios cargados desde base de datos: " + usuarios.size());
            System.out.println("📌 Próximo ID disponible: " + siguienteId);
            
            // Mostrar usuarios disponibles para depuración
            for (Usuario u : usuarios) {
                System.out.println("   - " + u.getEmail() + " (Admin: " + u.isEsAdmin() + ")");
            }

        } catch (Exception e) {  // 🔴 CORREGIDO: SQLException → Exception
            System.err.println("❌ Error crítico al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: modo local en memoria (solo para emergencia)
            System.out.println("⚠️ Activando modo de emergencia (solo memoria)");
            System.out.println("   Los datos NO se guardarán al cerrar la aplicación");
            usuarios = new ArrayList<>();
            usuarios.add(new Usuario(1, "Administrador", "admin@fidness.com", "admin123", true));
            usuarios.add(new Usuario(2, "Usuario Demo", "demo@fidness.com", "demo123", false));
            siguienteId = 3;
        }
    }
    
    /**
     * ============================================================
     * USUARIOS DE DEMOSTRACIÓN
     * ============================================================
     * 
     * Crea usuarios de demostración en la base de datos
     * Estos usuarios permiten probar la aplicación sin tener que registrarse
     */
    private void crearUsuariosDeDemostracion() {
        // Usuario Administrador (tiene acceso a todas las funciones)
        Usuario admin = new Usuario(0, "Administrador", "admin@fidness.com", "admin123", true);
        if (GestorDatos.guardarUsuario(admin)) {
            System.out.println("   ✅ Admin creado: admin@fidness.com / admin123");
        } else {
            System.err.println("   ❌ Error al crear usuario administrador");
        }
        
        // Usuario Demo (usuario normal sin privilegios de administrador)
        Usuario demo = new Usuario(0, "Usuario Demo", "demo@fidness.com", "demo123", false);
        if (GestorDatos.guardarUsuario(demo)) {
            System.out.println("   ✅ Demo creado: demo@fidness.com / demo123");
        } else {
            System.err.println("   ❌ Error al crear usuario demo");
        }
    }

    /**
     * ============================================================
     * INICIO DE SESIÓN
     * ============================================================
     * 
     * Verifica las credenciales del usuario.
     * 
     * @param email correo electrónico del usuario
     * @param password contraseña del usuario
     * @return Usuario autenticado o null si las credenciales son incorrectas
     */
    public Usuario iniciarSesion(String email, String password) {
        // Validar que los parámetros no sean nulos
        if (email == null || email.trim().isEmpty()) {
            System.out.println("❌ Email vacío");
            return null;
        }
        if (password == null || password.isEmpty()) {
            System.out.println("❌ Contraseña vacía");
            return null;
        }
        
        System.out.println("🔐 Intentando login para: " + email);
        
        // Primero intentamos con GestorDatos directamente (más seguro y actualizado)
        Usuario usuario = GestorDatos.verificarLogin(email, password);
        
        if (usuario != null) {
            System.out.println("✅ Login exitoso: " + usuario.getNombre());
            return usuario;
        }
        
        // Fallback: buscar en la lista local (por si acaso hay desincronización)
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email.trim())
                    && u.getPasswordHash().equals(password)) {
                System.out.println("✅ Login exitoso (modo memoria): " + u.getNombre());
                return u;
            }
        }

        System.out.println("❌ Credenciales incorrectas para: " + email);
        return null;
    }

    /**
     * ============================================================
     * REGISTRO DE USUARIO
     * ============================================================
     * 
     * Crea un nuevo usuario en el sistema.
     * Los datos se guardan PERMANENTEMENTE en la base de datos.
     * 
     * @param nombre Nombre completo del usuario
     * @param email Correo electrónico (debe ser único)
     * @param password Contraseña (mínimo 4 caracteres)
     * @param esAdmin True si el usuario será administrador
     * @return Usuario creado con su ID asignado, o null si hubo error
     * @throws IllegalArgumentException Si algún campo es inválido
     */
    public Usuario registrarUsuario(String nombre, String email, String password, boolean esAdmin) {
        // ===== VALIDACIONES =====
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido (debe contener @)");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres");
        }

        // Validar que no exista un usuario con el mismo email
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("El correo '" + email + "' ya está registrado");
            }
        }

        // ===== CREAR USUARIO =====
        // ID 0 indica que es nuevo, la base de datos asignará el ID automáticamente
        Usuario nuevo = new Usuario(0, nombre.trim(), email.trim(), password, esAdmin);
        
        // ===== GUARDAR EN BASE DE DATOS =====
        boolean guardado = GestorDatos.guardarUsuario(nuevo);
        
        if (guardado) {
            // Actualizar la lista local
            usuarios.add(nuevo);
            siguienteId = Math.max(siguienteId, nuevo.getId() + 1);
            System.out.println("✅ Usuario registrado y guardado permanentemente: " + nombre);
            System.out.println("   ID asignado: " + nuevo.getId());
            System.out.println("   Email: " + nuevo.getEmail());
            System.out.println("   Admin: " + (esAdmin ? "Sí" : "No"));
            return nuevo;
        } else {
            System.err.println("❌ Error al guardar el usuario en la base de datos");
            return null;
        }
    }

    /**
     * ============================================================
     * BÚSQUEDAS
     * ============================================================
     */
    
    /**
     * Busca un usuario por su email
     * @param email Email del usuario a buscar
     * @return Usuario encontrado, o null si no existe
     */
    public Usuario buscarPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario a buscar
     * @return Usuario encontrado, o null si no existe
     */
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
     * 
     * @return Copia de la lista de usuarios (no se puede modificar la original)
     */
    public List<Usuario> getTodosLosUsuarios() {
        return new ArrayList<>(usuarios);
    }

    /**
     * ============================================================
     * ACTUALIZACIÓN DE USUARIO
     * ============================================================
     * 
     * Actualiza los datos de un usuario existente
     * 
     * @param actualizado Usuario con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarUsuario(Usuario actualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == actualizado.getId()) {
                // Actualizar en memoria
                usuarios.set(i, actualizado);
                
                // Persistir el cambio en la base de datos
                boolean actualizadoBD = GestorDatos.actualizarUsuario(actualizado);
                
                if (actualizadoBD) {
                    System.out.println("✅ Usuario actualizado permanentemente: " + actualizado.getNombre());
                } else {
                    System.err.println("⚠️ Usuario actualizado en memoria, pero hubo problema en la BD");
                }
                
                return true;
            }
        }
        System.err.println("❌ No se encontró el usuario con ID: " + actualizado.getId());
        return false;
    }

    /**
     * ============================================================
     * ELIMINACIÓN DE USUARIO
     * ============================================================
     * 
     * Elimina un usuario del sistema
     * No permite eliminar el último administrador para dejar siempre un admin
     * 
     * @param id ID del usuario a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalStateException Si se intenta eliminar el último administrador
     */
    public boolean eliminarUsuario(int id) {
        Usuario u = buscarPorId(id);
        if (u == null) {
            System.err.println("❌ Usuario con ID " + id + " no encontrado");
            return false;
        }

        // Validación de seguridad: no eliminar el último administrador
        if (u.isEsAdmin()) {
            long admins = usuarios.stream()
                    .filter(Usuario::isEsAdmin)
                    .count();
            if (admins <= 1) {
                throw new IllegalStateException("No se puede eliminar el último administrador del sistema");
            }
        }

        // Eliminar de la base de datos
        boolean eliminadoBD = GestorDatos.eliminarUsuario(id);
        
        if (eliminadoBD) {
            usuarios.remove(u);
            System.out.println("✅ Usuario eliminado permanentemente: " + u.getNombre());
            return true;
        } else {
            System.err.println("❌ No se pudo eliminar el usuario de la base de datos");
            return false;
        }
    }
    
    /**
     * ============================================================
     * DIAGNÓSTICO
     * ============================================================
     * 
     * Método de diagnóstico para verificar el estado de la conexión
     * y los usuarios cargados en memoria
     * 
     * Útil para depurar problemas de conexión o carga de datos
     */
    public void diagnosticar() {
        System.out.println("\n🔍 ===== DIAGNÓSTICO DEL CONTROLADOR DE USUARIOS =====");
        System.out.println("   👥 Usuarios en memoria: " + usuarios.size());
        System.out.println("   📌 Próximo ID disponible: " + siguienteId);
        
        if (!usuarios.isEmpty()) {
            System.out.println("   📋 Usuarios cargados:");
            for (Usuario u : usuarios) {
                System.out.println("      - ID: " + u.getId() + " | Email: " + u.getEmail() + " | Admin: " + u.isEsAdmin());
            }
        } else {
            System.out.println("   ⚠️ No hay usuarios cargados en memoria");
        }
        
        // Llamar al diagnóstico de la base de datos
        GestorDatos.diagnosticar();
        
        System.out.println("==================================================\n");
    }
}