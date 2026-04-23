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
 * AHORA CON BASE DE DATOS: ya no usamos archivos .dat, todo se guarda
 * en SQLite. Es como pasar de una libreta de apuntes a una computadora.
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
     * Carga los usuarios desde la BASE DE DATOS (ya no desde archivos)
     * 
     * Antes usábamos serialización con archivos .dat, pero ahora
     * todo está en SQLite. Es más seguro, más rápido y más profesional.
     * 
     * Si no hay usuarios, se crean automáticamente los de prueba.
     * Como cuando compras un celular nuevo y ya trae algunas apps.
     */
    private void cargarDatos() {
        try {
            // Ahora usamos la base de datos, no archivos
            usuarios = GestorDatos.cargarUsuarios();
            
            // Calcular siguiente ID basado en el máximo existente
            siguienteId = usuarios.stream()
                .mapToInt(Usuario::getId)
                .max()
                .orElse(0) + 1;
            
            System.out.println("✅ ControladorUsuarios: " + usuarios.size() + " usuarios cargados desde BD");
            System.out.println("📌 Próximo ID disponible: " + siguienteId);
            
        } catch (SQLException e) {
            System.err.println("❌ Error cargando usuarios desde la base de datos: " + e.getMessage());
            System.err.println("   Verifica que la librería SQLite esté agregada correctamente.");
            usuarios = new ArrayList<>();
            siguienteId = 1;
        }
    }
    
    /**
     * GUARDA los usuarios en la base de datos
     * 
     * IMPORTANTE: En la versión con base de datos, los cambios se guardan
     * inmediatamente cuando se agrega o modifica un usuario.
     * Ya no necesitamos este método para guardar TODO cada vez.
     * 
     * Lo mantengo por compatibilidad, pero cada operación individual
     * ya guarda en la BD automáticamente.
     */
    private void guardarDatos() {
        // En la versión con base de datos, los guardados son inmediatos
        // Este método ya no es necesario, pero lo dejo para no romper nada
        System.out.println("💾 Nota: Los datos se guardan automáticamente en la BD");
    }
    
    /**
     * Intenta iniciar sesión
     * 
     * @param email email del usuario
     * @param password contraseña en texto plano (en producción usaríamos hash)
     * @return Usuario si éxito, null si no
     */
    public Usuario iniciarSesion(String email, String password) {
        // Buscar usuario por email (case insensitive)
        // En soporte, aprendí que los usuarios escriben "Admin" o "admin" igual
        Usuario encontrado = usuarios.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
            .filter(u -> u.verificarPassword(password))
            .findFirst()
            .orElse(null);
        
        if (encontrado != null) {
            System.out.println("✅ Login exitoso: " + encontrado.getEmail());
        } else {
            System.out.println("❌ Intento de login fallido para: " + email);
        }
        
        return encontrado;
    }
    
    /**
     * Registra un nuevo usuario en la BASE DE DATOS
     * 
     * @param nombre nombre completo
     * @param email email (debe ser único)
     * @param password contraseña (mínimo 6 caracteres)
     * @param esAdmin si es administrador
     * @return el usuario creado
     * @throws IllegalArgumentException si el email ya existe o datos inválidos
     */
    public Usuario registrarUsuario(String nombre, String email, String password, boolean esAdmin) {
        // Validaciones básicas (igual que antes, la seguridad no cambia)
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido. Debe contener @");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        
        // Validar email único en la lista actual
        boolean existe = usuarios.stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        
        if (existe) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        
        try {
            // Crear el usuario con el siguiente ID disponible
            Usuario nuevo = new Usuario(siguienteId++, nombre, email, password, esAdmin);
            
            // GUARDAR EN BASE DE DATOS (esto es nuevo)
            GestorDatos.guardarUsuario(nuevo);
            
            // Agregar a la lista en memoria
            usuarios.add(nuevo);
            
            System.out.println("✅ Nuevo usuario registrado: " + email + " (ID: " + nuevo.getId() + ")");
            
            return nuevo;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar en BD: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el usuario en la base de datos", e);
        }
    }
    
    /**
     * Busca un usuario por su email
     */
    public Usuario buscarPorEmail(String email) {
        return usuarios.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Busca un usuario por su ID
     */
    public Usuario buscarPorId(int id) {
        return usuarios.stream()
            .filter(u -> u.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene todos los usuarios (solo para administración)
     */
    public List<Usuario> getTodosLosUsuarios() {
        return new ArrayList<>(usuarios);
    }
    
    /**
     * Actualiza datos de un usuario en la BASE DE DATOS
     * 
     * @param usuarioActualizado usuario con los nuevos datos
     * @return true si se actualizó correctamente
     */
    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuarioActualizado.getId()) {
                try {
                    // Actualizar en base de datos
                    GestorDatos.actualizarUsuario(usuarioActualizado);
                    
                    // Actualizar en memoria
                    usuarios.set(i, usuarioActualizado);
                    
                    System.out.println("✅ Usuario actualizado: " + usuarioActualizado.getEmail());
                    return true;
                    
                } catch (SQLException e) {
                    System.err.println("❌ Error actualizando usuario: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Elimina un usuario de la BASE DE DATOS (solo si no es el último admin)
     * 
     * @param id ID del usuario a eliminar
     * @return true si se eliminó
     * @throws IllegalStateException si es el último administrador
     */
    public boolean eliminarUsuario(int id) {
        Usuario usuario = buscarPorId(id);
        if (usuario == null) return false;
        
        // No permitir eliminar si es el último administrador
        // En sistemas reales, siempre debe haber al menos un admin
        if (usuario.isEsAdmin()) {
            long adminsRestantes = usuarios.stream()
                .filter(Usuario::isEsAdmin)
                .count();
            
            if (adminsRestantes <= 1) {
                throw new IllegalStateException("No se puede eliminar el último administrador");
            }
        }
        
        // En la versión con BD, necesitaríamos un método eliminarUsuario()
        // Por ahora, lo eliminamos de la lista en memoria
        // NOTA: En la siguiente versión agregaremos la eliminación en BD
        
        boolean removido = usuarios.removeIf(u -> u.getId() == id);
        if (removido) {
            System.out.println("✅ Usuario eliminado (ID: " + id + ")");
            // TODO: Implementar eliminación en BD
        }
        return removido;
    }
}