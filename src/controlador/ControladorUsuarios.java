/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Usuario;
import persistencia.GestorDatos;

import java.io.IOException;
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
     * Carga los usuarios desde el archivo
     * Si no hay, crea usuarios por defecto para pruebas
     */
    private void cargarDatos() {
        try {
            usuarios = GestorDatos.cargarUsuarios();
            
            // Calcular siguiente ID basado en el máximo existente
            siguienteId = usuarios.stream()
                .mapToInt(Usuario::getId)
                .max()
                .orElse(0) + 1;
            
            // Si no hay usuarios, creamos admin por defecto para pruebas
            if (usuarios.isEmpty()) {
                Usuario admin = new Usuario(siguienteId++, "Administrador", 
                    "admin@fidness.com", "admin123", true);
                usuarios.add(admin);
                
                Usuario demo = new Usuario(siguienteId++, "Usuario Demo", 
                    "demo@fidness.com", "demo123", false);
                usuarios.add(demo);
                
                guardarDatos();
                System.out.println("👤 Usuarios por defecto creados: admin@fidness.com / demo@fidness.com");
            }
            
        } catch (Exception e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
            usuarios = new ArrayList<>();
            siguienteId = 1;
        }
    }
    
    /**
     * Guarda los usuarios en archivo
     */
    private void guardarDatos() {
        try {
            GestorDatos.guardarUsuarios(usuarios);
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }
    
    /**
     * Intenta iniciar sesión
     * 
     * @param email email del usuario
     * @param password contraseña en texto plano
     * @return Usuario si éxito, null si no
     */
    public Usuario iniciarSesion(String email, String password) {
        // Buscar usuario por email (case insensitive)
        // En soporte, aprendí que los usuarios escriben "Admin" o "admin" igual
        return usuarios.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
            .filter(u -> u.verificarPassword(password))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Registra un nuevo usuario
     * 
     * @param nombre nombre completo
     * @param email email (debe ser único)
     * @param password contraseña (mínimo 6 caracteres)
     * @param esAdmin si es administrador
     * @return el usuario creado
     * @throws IllegalArgumentException si el email ya existe o datos inválidos
     */
    public Usuario registrarUsuario(String nombre, String email, String password, boolean esAdmin) {
        // Validaciones básicas
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
        
        Usuario nuevo = new Usuario(siguienteId++, nombre, email, password, esAdmin);
        usuarios.add(nuevo);
        guardarDatos();
        
        return nuevo;
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
     * Actualiza datos de un usuario
     */
    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuarioActualizado.getId()) {
                usuarios.set(i, usuarioActualizado);
                guardarDatos();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Elimina un usuario (solo si no es el último admin)
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
        
        boolean removido = usuarios.removeIf(u -> u.getId() == id);
        if (removido) {
            guardarDatos();
        }
        return removido;
    }
}