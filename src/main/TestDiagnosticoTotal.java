package main;

import controlador.ControladorEjercicios;
import modelo.Ejercicio;
import modelo.Usuario;
import modelo.Rutina;
import java.util.List;

public class TestDiagnosticoTotal {
    public static void main(String[] args) {
        System.out.println("=== DIAGNOSTICO TOTAL ===\n");
        
        // 1. Verificar ControladorEjercicios
        System.out.println("1. ControladorEjercicios:");
        ControladorEjercicios c = new ControladorEjercicios();
        List<Ejercicio> ejercicios = c.getTodosLosEjercicios();
        System.out.println("   Ejercicios: " + ejercicios.size());
        for (Ejercicio e : ejercicios) {
            System.out.println("      - " + e.getNombre());
        }
        
        // 2. Verificar Usuario
        System.out.println("\n2. Usuario:");
        Usuario u = new Usuario(1, "Admin", "admin@fidness.com", "admin123", true);
        System.out.println("   Rutinas iniciales: " + u.getRutinas().size());
        
        // 3. Verificar si se pueden agregar rutinas
        System.out.println("\n3. Agregando rutina de prueba:");
        Rutina r = new Rutina(1, "Rutina Test", u.getId());
        if (ejercicios.size() > 0) {
            r.agregarEjercicio(ejercicios.get(0), 3, 10);
            u.agregarRutina(r);
            System.out.println("   Rutina agregada. Total: " + u.getRutinas().size());
        } else {
            System.out.println("   ERROR: No hay ejercicios para agregar a la rutina");
        }
        
        System.out.println("\n=== FIN DIAGNOSTICO ===");
    }
}