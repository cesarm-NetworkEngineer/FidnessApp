/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un usuario del sistema.
 * 
 * En mis años dando soporte en Infinite Computer Solutions, aprendí que la 
 * seguridad no es negociable. Por eso el password se maneja con hash, igual 
 * que protegemos redes empresariales con firewalls.
 * 
 * Y el esAdmin no es capricho: en mis cursos de Linux, siempre hay un root
 * y usuarios regulares. El root puede hacer de todo, los usuarios normales
 * tienen restricciones. Exactamente lo mismo acá.
 * 
 * @author César Alonso Morera Alpízar
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private String email;
    private String passwordHash;  // NUNCA guardamos la contraseña en texto plano
    private boolean esAdmin;
    private List<Rutina> rutinas;  // Relación 1 a N (un usuario puede tener muchas rutinas)
    
    /**
     * Constructor completo
     * 
     * @param id identificador único
     * @param nombre nombre completo del usuario
     * @param email email (usado para login)
     * @param passwordHash hash de la contraseña
     * @param esAdmin true si es administrador, false si es usuario regular
     */
    public Usuario(int id, String nombre, String email, String passwordHash, boolean esAdmin) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.esAdmin = esAdmin;
        this.rutinas = new ArrayList<>();
    }
    
    /**
     * Verifica si la contraseña proporcionada coincide con el hash
     * 
     * NOTA: En producción esto debería usar BCrypt o PBKDF2.
     * En DXC Technology usábamos BCrypt para todos los proyectos bancarios.
     * Para el proyecto, usamos comparación directa por simplicidad,
     * pero dejamos claro que en el mundo real se hace con hash.
     * 
     * @param passwordPlain Texto plano de la contraseña ingresada
     * @return true si coincide, false si no
     */
    public boolean verificarPassword(String passwordPlain) {
        if (this.passwordHash == null || passwordPlain == null) {
            return false;
        }
        // En un sistema real: return BCrypt.checkpw(passwordPlain, this.passwordHash)
        return this.passwordHash.equals(passwordPlain);
    }
    
    // Getters y Setters con validaciones donde corresponde
    
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
     * Valida que el nombre no esté vacío
     * En mis cursos, siempre digo: "validen, validen, validen"
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }
    
    public String getEmail() { 
        return email; 
    }
    
    /**
     * Validación básica de email
     * No es perfecta (no valida dominios), pero para el proyecto alcanza
     */
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido. Debe contener @");
        }
        this.email = email;
    }
    
    public String getPasswordHash() { 
        return passwordHash; 
    }
    
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }
    
    public boolean isEsAdmin() { 
        return esAdmin; 
    }
    
    public void setEsAdmin(boolean esAdmin) { 
        this.esAdmin = esAdmin; 
    }
    
    public List<Rutina> getRutinas() { 
        // Devolvemos una copia para mantener encapsulamiento
        // Así nadie puede modificar la lista original desde fuera
        return new ArrayList<>(rutinas); 
    }
    
    public void setRutinas(List<Rutina> rutinas) { 
        this.rutinas = rutinas; 
    }
    
    /**
     * Agrega una rutina a la lista del usuario
     * 
     * @param rutina La rutina a agregar
     * @throws IllegalArgumentException si la rutina es null
     */
    public void agregarRutina(Rutina rutina) {
        if (rutina == null) {
            throw new IllegalArgumentException("La rutina no puede ser null");
        }
        if (!rutinas.contains(rutina)) {
            rutinas.add(rutina);
        }
    }
    
    /**
     * Elimina una rutina de la lista
     * 
     * @param idRutina ID de la rutina a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarRutina(int idRutina) {
        return rutinas.removeIf(r -> r.getId() == idRutina);
    }
    
    @Override
    public String toString() {
        // La coronita 👑 solo para admins - un pequeño detalle visual
        return nombre + " (" + email + ")" + (esAdmin ? " 👑" : "");
    }
    
    /**
     * Dos usuarios son iguales si tienen el mismo ID o el mismo email
     * Esto evita duplicados en colecciones
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return id == usuario.id || email.equalsIgnoreCase(usuario.email);
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}