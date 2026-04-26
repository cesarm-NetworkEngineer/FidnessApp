package main;

import controlador.ControladorEjercicios;
import modelo.Ejercicio;
import java.util.List;

public class TestEjercicios {
    public static void main(String[] args) {
        System.out.println("=== TEST EJERCICIOS ===\n");
        
        ControladorEjercicios controlador = new ControladorEjercicios();
        List<Ejercicio> ejercicios = controlador.getTodosLosEjercicios();
        
        System.out.println("Ejercicios cargados: " + ejercicios.size());
        
        for (Ejercicio e : ejercicios) {
            System.out.println("   - " + e.getNombre() + " (" + e.getTipo() + ")");
        }
        
        System.out.println("\n=== FIN TEST ===");
    }
}