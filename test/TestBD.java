/*
 * Clase de prueba para verificar que la Base de Datos funciona
 * 
 * Ejecuta esta clase sola (no con la app completa)
 * para diagnosticar problemas de conexión a SQLite
 * 
 * @author César Alonso Morera Alpízar
 */
package prueba;

import persistencia.GestorDatos;
import modelo.Usuario;
import modelo.Ejercicio;
import java.sql.SQLException;
import java.util.List;

public class TestBD {
    
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   🧪 PRUEBA DE BASE DE DATOS");
        System.out.println("=====================================");
        
        // PRUEBA 1: Cargar usuarios
        System.out.println("\n📋 PRUEBA 1: Cargar usuarios");
        try {
            List<Usuario> usuarios = GestorDatos.cargarUsuarios();
            System.out.println("✅ ÉXITO: " + usuarios.size() + " usuarios cargados");
            for (Usuario u : usuarios) {
                System.out.println("   - " + u.getEmail() + " (Admin: " + u.isEsAdmin() + ")");
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        // PRUEBA 2: Cargar ejercicios
        System.out.println("\n📋 PRUEBA 2: Cargar ejercicios");
        try {
            List<Ejercicio> ejercicios = GestorDatos.cargarEjercicios();
            System.out.println("✅ ÉXITO: " + ejercicios.size() + " ejercicios cargados");
            for (Ejercicio e : ejercicios) {
                System.out.println("   - " + e.getNombre() + " (" + e.getTipo() + ")");
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        // PRUEBA 3: Insertar un ejercicio de prueba
        System.out.println("\n📋 PRUEBA 3: Insertar ejercicio de prueba");
        try {
            Ejercicio nuevo = new Ejercicio(0, "Ejercicio Test", "Descripción de prueba", 
                modelo.TipoEjercicio.FULLBODY, "", "");
            GestorDatos.guardarEjercicio(nuevo);
            System.out.println("✅ ÉXITO: Ejercicio guardado con ID: " + nuevo.getId());
        } catch (SQLException e) {
            System.out.println("❌ ERROR al guardar: " + e.getMessage());
        }
        
        // PRUEBA 4: Verificar que se guardó
        System.out.println("\n📋 PRUEBA 4: Verificar persistencia");
        try {
            List<Ejercicio> ejercicios = GestorDatos.cargarEjercicios();
            System.out.println("✅ ÉXITO: Ahora hay " + ejercicios.size() + " ejercicios");
        } catch (SQLException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
        
        System.out.println("\n=====================================");
        System.out.println("   🏁 PRUEBA COMPLETADA");
        System.out.println("=====================================");
    }
}