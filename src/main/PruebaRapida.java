package main;

import controlador.ControladorUsuarios;
import modelo.Usuario;

public class PruebaRapida {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA RÁPIDA DEL SISTEMA ===\n");
        
        // 1. Probar diagnóstico de base de datos
        System.out.println("1. DIAGNÓSTICO DE BD:");
        persistencia.GestorDatos.diagnosticar();
        
        // 2. Crear controlador
        System.out.println("\n2. CREANDO CONTROLADOR...");
        ControladorUsuarios controlador = new ControladorUsuarios();
        
        // 3. Probar login
        System.out.println("\n3. PROBANDO LOGIN CON CREDENCIALES:");
        
        String[] emails = {"admin@fidness.com", "demo@fidness.com"};
        String[] passwords = {"admin123", "demo123"};
        
        for (int i = 0; i < emails.length; i++) {
            System.out.println("   Intentando: " + emails[i]);
            Usuario u = controlador.iniciarSesion(emails[i], passwords[i]);
            if (u != null) {
                System.out.println("   ✅ ÉXITO! Usuario: " + u.getNombre());
                System.out.println("      ID: " + u.getId());
                System.out.println("      Admin: " + u.isEsAdmin());
            } else {
                System.out.println("   ❌ FALLÓ para: " + emails[i]);
            }
        }
        
        // 4. Mostrar usuarios cargados
        System.out.println("\n4. USUARIOS EN EL SISTEMA:");
        for (Usuario u : controlador.getTodosLosUsuarios()) {
            System.out.println("   - " + u.getEmail() + " (Admin: " + u.isEsAdmin() + ")");
        }
        
        System.out.println("\n=== FIN PRUEBA ===");
    }
}