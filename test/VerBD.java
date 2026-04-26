/*
 * Clase simple para ver el contenido de la base de datos
 * 
 * @author Cesar Alonso Morera Alpizar
 */

import persistencia.GestorDatos;
import modelo.Usuario;
import modelo.Ejercicio;
import java.sql.SQLException;
import java.util.List;

public class VerBD {
    
    public static void main(String[] args) {
        
        System.out.println("======================================");
        System.out.println("   BASE DE DATOS - FIDNESSAPP");
        System.out.println("======================================\n");
        
        // ===== MOSTRAR USUARIOS =====
        System.out.println("👥 USUARIOS:");
        System.out.println("--------------------------------------");
        try {
            List<Usuario> usuarios = GestorDatos.cargarUsuarios();
            if (usuarios.isEmpty()) {
                System.out.println("   No hay usuarios registrados");
            } else {
                for (Usuario u : usuarios) {
                    System.out.println("   ID: " + u.getId());
                    System.out.println("   Nombre: " + u.getNombre());
                    System.out.println("   Email: " + u.getEmail());
                    System.out.println("   Admin: " + (u.isEsAdmin() ? "Sí" : "No"));
                    System.out.println("   ---");
                }
            }
        } catch (SQLException e) {
            System.out.println("   ERROR: " + e.getMessage());
        }
        
        // ===== MOSTRAR EJERCICIOS =====
        System.out.println("\n🏋️ EJERCICIOS:");
        System.out.println("--------------------------------------");
        try {
            List<Ejercicio> ejercicios = GestorDatos.cargarEjercicios();
            if (ejercicios.isEmpty()) {
                System.out.println("   No hay ejercicios cargados");
            } else {
                for (Ejercicio e : ejercicios) {
                    System.out.println("   ID: " + e.getId());
                    System.out.println("   Nombre: " + e.getNombre());
                    System.out.println("   Tipo: " + e.getTipo());
                    System.out.println("   ---");
                }
            }
        } catch (SQLException e) {
            System.out.println("   ERROR: " + e.getMessage());
        }
        
        // ===== RESUMEN =====
        System.out.println("\n======================================");
        System.out.println("   ✅ BASE DE DATOS FUNCIONANDO");
        System.out.println("======================================");
    }
}