package pruebas;

import persistencia.GestorDatos;
import modelo.Usuario;
import modelo.Ejercicio;

import java.util.List;

/**
 * ===============================================================
 * CLASE DE PRUEBA - VISUALIZACIÓN DE BASE DE DATOS
 * ===============================================================
 * 
 * Autor: César Morera
 * 
 * Objetivo:
 * Mostrar en consola todos los registros almacenados en la base de datos.
 * 
 * Esta clase permite verificar:
 * - Que la base de datos funciona
 * - Que los datos se están guardando correctamente
 * - Que las consultas retornan información real
 */
public class TestVerBaseDatos {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   VISUALIZACIÓN DE BASE DE DATOS");
        System.out.println("=======================================\n");

        try {

            // =====================================================
            // 1. CARGAR USUARIOS
            // =====================================================
            List<Usuario> usuarios = GestorDatos.cargarUsuarios();

            System.out.println("👤 USUARIOS REGISTRADOS:");
            System.out.println("-----------------------------------");

            for (Usuario u : usuarios) {
                System.out.println("ID: " + u.getId());
                System.out.println("Nombre: " + u.getNombre());
                System.out.println("Email: " + u.getEmail());
                System.out.println("Admin: " + (u.isEsAdmin() ? "Sí" : "No"));
                System.out.println("-----------------------------------");
            }

            // =====================================================
            // 2. CARGAR EJERCICIOS
            // =====================================================
            List<Ejercicio> ejercicios = GestorDatos.cargarEjercicios();

            System.out.println("\n🏋️ EJERCICIOS REGISTRADOS:");
            System.out.println("-----------------------------------");

            for (Ejercicio e : ejercicios) {
                System.out.println("ID: " + e.getId());
                System.out.println("Nombre: " + e.getNombre());
                System.out.println("Tipo: " + e.getTipo());
                System.out.println("Descripción: " + e.getDescripcion());
                System.out.println("-----------------------------------");
            }

            System.out.println("\n✅ Datos mostrados correctamente.");

        } catch (Exception e) {
            System.out.println("❌ Error al leer la base de datos:");
            e.printStackTrace();
        }
    }
}