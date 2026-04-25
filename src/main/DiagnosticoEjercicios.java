package main;

import persistencia.GestorDatos;
import modelo.Ejercicio;
import controlador.ControladorEjercicios;
import java.util.List;

public class DiagnosticoEjercicios {
    public static void main(String[] args) {
        System.out.println("==== DIAGNOSTICO DE EJERCICIOS ====\n");
        
        // PRUEBA 1: Cargar directamente desde GestorDatos
        System.out.println("1. Cargando desde GestorDatos...");
        try {
            List<Ejercicio> ejercicios = GestorDatos.cargarEjercicios();
            System.out.println("   Ejercicios encontrados: " + ejercicios.size());
            for (Ejercicio e : ejercicios) {
                System.out.println("   - " + e.getId() + ": " + e.getNombre());
            }
        } catch (Exception e) {
            System.out.println("   ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        // PRUEBA 2: Cargar desde ControladorEjercicios
        System.out.println("\n2. Cargando desde ControladorEjercicios...");
        ControladorEjercicios controlador = new ControladorEjercicios();
        List<Ejercicio> lista = controlador.getTodosLosEjercicios();
        System.out.println("   Ejercicios en controlador: " + lista.size());
        for (Ejercicio e : lista) {
            System.out.println("   - " + e.getId() + ": " + e.getNombre());
        }
        
        System.out.println("\n==== FIN DIAGNOSTICO ====");
    }
}