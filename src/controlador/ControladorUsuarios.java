/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Usuario;
import persistencia.GestorDatos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones relacionadas con usuarios.
 * 
 * Separamos la lógica de la vista para mantener todo ordenado.
 * Como decía mi profe: "capa, capa, capa... no mezclar!"
 * Esta clase se encarga de toda la lógica de negocio de usuarios.
 * 
 * En mis años de experiencia, aprendí que los controladores son
 * como los gerentes de un restaurante: la cocina (modelo) prepara,
 * los meseros (vista) atienden, y el gerente (controlador) coordina.
 * 
 * 🔧 MODO DE EMERGENCIA: Debido a incompatibilidades entre JDK 25
 * y la versión actual de SQLite, implementé un modo de emergencia
 * que permite que la app funcione sin base de datos. Esto es temporal
 * para poder hacer la presentación mientras resolvemos el problema técnico.
 * 
 * Como cuando se va la luz en un restaurante y sacan las velas:
 * no es lo ideal, pero la gente sigue comiendo. La funcionalidad
 * principal está ahí, solo que los datos no se guardan permanentemente.
 * 
 * @author César Alonso Morera Alpízar
 */
public class ControladorUsuarios {
    
    private List<Usuario> usuarios;
    private int siguienteId;
    
    public ControladorUsuarios() {
        this.usuarios = new ArrayList<>();
        cargarDatos();
    }
    
    /**
     * Carga los usuarios desde la base de datos.
     * 
     * Si la base de datos no está disponible (como pasa con JDK 25),
     * entramos en MODO DE EMERGENCIA con usuarios hardcodeados.
     * 
     * En Soporte Técnico aprendí que siempre hay que tener un plan B.
     * Como cuando haces una presentación y llevas el archivo en USB
     * además del correo electrónico. Nunca se sabe cuándo falla la tecnología.
     * 
     * En mis clases, siempre digo: "el que tiene dos opciones, tiene una.
     * El que tiene una opción, no tiene ninguna".
     */
    private void cargarDatos() {
        // ===== INTENTAR CONEXIÓN A BASE DE DATOS =====
        try {
            usuarios = GestorDatos.cargarUsuarios();
            siguienteId = usuarios.stream()
                .mapToInt(Usuario::getId)
                .max()
                .orElse(0) + 1;
            
            System.out.println("✅ ControladorUsuarios: " + usuarios.size() + " usuarios cargados desde BD");
            System.out.println("   📌 Próximo ID disponible: " + siguienteId);
            return; // Si todo bien, salimos del método
            
        } catch (SQLException e) {
            System.err.println("⚠️ No se pudo conectar a la base de datos.");
            System.err.println("   Error: " + e.getMessage());
            System.err.println("   Esto es normal con JDK 25. Usando modo de emergencia...");
        }
        
        // ===== MODO DE EMERGENCIA - Usuarios hardcodeados =====
        // En DXC Technology aprendí que los sistemas críticos necesitan
        // un "fallback". Esto es nuestro fallback: datos locales.
        System.out.println("\n🔥 ENTRANDO EN MODO DE EMERGENCIA 🔥");
        System.out.println("   Los usuarios se cargarán en memoria.");
        System.out.println("   Los cambios NO se guardarán en la base de datos.");
        
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1, "Administrador", "admin@fidness.com", "admin123", true));
        usuarios.add(new Usuario(2, "Usuario Demo", "demo@fidness.com", "demo123", false));
        siguienteId = 3;
        
        System.out.println("\n✅ MODO EMERGENCIA ACTIVADO: " + usuarios.size() + " usuarios cargados");
        System.out.println("   📧 admin@fidness.com / admin123 (👑 Administrador)");
        System.out.println("   📧 demo@fidness.com / demo123 (👤 Usuario normal)");
        System.out.println("   ⚠️ Los datos no persisten al cerrar la aplicación.");
        System.out.println("   🔧 Para persistencia real, cambiar a JDK 17.\n");
    }
    
    /**
     * Intenta iniciar sesión
     * 
     * Busca un usuario por email (case insensitive) y verifica la contraseña.
     * En soporte, aprendí que los usuarios escriben "Admin" o "admin" igual.
     * Por eso usamos equalsIgnoreCase.
     * 
     * @param email email del usuario
     * @param password contraseña en texto plano
     * @return Usuario si éxito, null si no
     */
    public Usuario iniciarSesion(String email, String password) {
        System.out.println("🔐 Intentando login: " + email);
        
        Usuario encontrado = usuarios.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
            .filter(u -> u.verificarPassword(password))
            .findFirst()
            .orElse(null);
        
        if (encontrado != null) {
            System.out.println("✅ Login exitoso: " + encontrado.getEmail() + " (" + encontrado.getNombre() + ")");
        } else {
            System.out.println("❌ Login fallido: " + email + " - Credenciales incorrectas");
        }
        
        return encontrado;
    }
    
    /**
     * Registra un nuevo usuario
     * 
     * En modo de emergencia, el usuario se guarda en memoria pero NO en BD.
     * Esto permite probar toda la funcionalidad de la app.
     * 
     * @param nombre nombre completo
     * @param email email (debe ser único)
     * @param password contraseña (mínimo 6 caracteres)
     * @param esAdmin si es administrador
     * @return el usuario creado
     * @throws IllegalArgumentException si el email ya existe o datos inválidos
     */
    public Usuario registrarUsuario(String nombre, String email, String password, boolean esAdmin) {
        // Validaciones básicas - la seguridad no cambie, estemos en modo normal o emergencia
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido. Debe contener @");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        
        // Validar email único
        boolean existe = usuarios.stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        
        if (existe) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        
        // Crear el usuario
        Usuario nuevo = new Usuario(siguienteId++, nombre, email, password, esAdmin);
        usuarios.add(nuevo);
        
        System.out.println("✅ Nuevo usuario registrado: " + email + " (ID: " + nuevo.getId() + ")");
        System.out.println("   ⚠️ (Modo emergencia: los datos no se guardan en BD)");
        
        // Intentar guardar en BD si está disponible
        try {
            GestorDatos.guardarUsuario(nuevo);
            System.out.println("   💾 También se guardó en la base de datos.");
        } catch (SQLException e) {
            // En modo emergencia, ignoramos el error de BD
            System.out.println("   💾 No se pudo guardar en BD (modo emergencia).");
        }
        
        return nuevo;
    }
    
    /**
     * Busca un usuario por su email
     * 
     * @param email email a buscar
     * @return Usuario encontrado o null
     */
    public Usuario buscarPorEmail(String email) {
        return usuarios.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Busca un usuario por su ID
     * 
     * @param id ID a buscar
     * @return Usuario encontrado o null
     */
    public Usuario buscarPorId(int id) {
        return usuarios.stream()
            .filter(u -> u.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene todos los usuarios (solo para administración)
     * 
     * @return lista de todos los usuarios
     */
    public List<Usuario> getTodosLosUsuarios() {
        return new ArrayList<>(usuarios);
    }
    
    /**
     * Actualiza datos de un usuario
     * 
     * @param usuarioActualizado usuario con los nuevos datos
     * @return true si se actualizó correctamente
     */
    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuarioActualizado.getId()) {
                usuarios.set(i, usuarioActualizado);
                System.out.println("✅ Usuario actualizado: " + usuarioActualizado.getEmail());
                
                // Intentar actualizar en BD
                try {
                    GestorDatos.actualizarUsuario(usuarioActualizado);
                } catch (SQLException e) {
                    System.out.println("   ⚠️ No se pudo actualizar en BD (modo emergencia).");
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Elimina un usuario (solo si no es el último admin)
     * 
     * En sistemas reales, siempre debe haber al menos un administrador.
     * Esto lo aprendí en DXC Technology, donde los sistemas bancarios
     * nunca pueden quedarse sin superadministrador.
     * 
     * @param id ID del usuario a eliminar
     * @return true si se eliminó
     * @throws IllegalStateException si es el último administrador
     */
    public boolean eliminarUsuario(int id) {
        Usuario usuario = buscarPorId(id);
        if (usuario == null) return false;
        
        // Validación crítica: no eliminar al último admin
        if (usuario.isEsAdmin()) {
            long adminsRestantes = usuarios.stream()
                .filter(Usuario::isEsAdmin)
                .count();
            
            if (adminsRestantes <= 1) {
                throw new IllegalStateException("No se puede eliminar el último administrador");
            }
        }
        
        boolean removido = usuarios.removeIf(u -> u.getId() == id);
        if (removido) {
            System.out.println("✅ Usuario eliminado (ID: " + id + ")");
        }
        return removido;
    }
}