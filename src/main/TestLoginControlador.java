package main;

import controlador.ControladorUsuarios;
import modelo.Usuario;

public class TestLoginControlador {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA LOGIN CON CONTROLADOR ===\n");
        
        ControladorUsuarios controlador = new ControladorUsuarios();
        
        // Prueba 1: Admin
        String email1 = "admin@fidness.com";
        String pass1 = "admin123";
        System.out.println("Probando: " + email1 + " / " + pass1);
        Usuario u1 = controlador.iniciarSesion(email1, pass1);
        System.out.println("Resultado: " + (u1 != null ? "✅ EXITOSO: " + u1.getNombre() : "❌ FALLIDO"));
        
        // Prueba 2: Demo
        String email2 = "demo@fidness.com";
        String pass2 = "demo123";
        System.out.println("\nProbando: " + email2 + " / " + pass2);
        Usuario u2 = controlador.iniciarSesion(email2, pass2);
        System.out.println("Resultado: " + (u2 != null ? "✅ EXITOSO: " + u2.getNombre() : "❌ FALLIDO"));
        
        // Prueba 3: Admin con mayúsculas
        String email3 = "ADMIN@fidness.com";
        String pass3 = "admin123";
        System.out.println("\nProbando: " + email3 + " / " + pass3);
        Usuario u3 = controlador.iniciarSesion(email3, pass3);
        System.out.println("Resultado: " + (u3 != null ? "✅ EXITOSO: " + u3.getNombre() : "❌ FALLIDO"));
    }
}