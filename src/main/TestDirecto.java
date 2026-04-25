package main;

import persistencia.GestorDatos;
import modelo.Usuario;
import java.util.List;

public class TestDirecto {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DIRECTA ===\n");
        
        try {
            // Probar carga de usuarios
            List<Usuario> usuarios = GestorDatos.cargarUsuarios();
            System.out.println("Usuarios cargados: " + usuarios.size());
            
            for (Usuario u : usuarios) {
                System.out.println("Email: " + u.getEmail());
                System.out.println("Password hash: " + u.getPasswordHash());
                
                // Probar verificación
                boolean ok = u.verificarPassword("admin123");
                System.out.println("Verificar 'admin123': " + ok);
                System.out.println("---");
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}