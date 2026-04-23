/*
 * Clase para imprimir todo el contenido de la base de datos
 * 
 * Muestra usuarios, ejercicios, rutinas y detalles
 * 
 * @author César Alonso Morera Alpízar
 */
package prueba;

import java.sql.*;
import persistencia.ConexionBD;

public class ImprimirBD {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   📊 CONTENIDO COMPLETO DE LA BASE DE DATOS");
        System.out.println("==========================================\n");
        
        try (Connection conn = ConexionBD.getConnection()) {
            
            // ===== TABLA USUARIOS =====
            System.out.println("👥 TABLA: usuarios");
            System.out.println("------------------------------------------");
            String sqlUsuarios = "SELECT * FROM usuarios";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlUsuarios)) {
                
                while (rs.next()) {
                    System.out.println("   ID: " + rs.getInt("id"));
                    System.out.println("   Nombre: " + rs.getString("nombre"));
                    System.out.println("   Email: " + rs.getString("email"));
                    System.out.println("   Contraseña: " + rs.getString("password_hash"));
                    System.out.println("   Es Admin: " + (rs.getInt("es_admin") == 1 ? "Sí" : "No"));
                    System.out.println("   ---");
                }
            }
            
            // ===== TABLA EJERCICIOS =====
            System.out.println("\n🏋️ TABLA: ejercicios");
            System.out.println("------------------------------------------");
            String sqlEjercicios = "SELECT * FROM ejercicios";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlEjercicios)) {
                
                while (rs.next()) {
                    System.out.println("   ID: " + rs.getInt("id"));
                    System.out.println("   Nombre: " + rs.getString("nombre"));
                    System.out.println("   Descripción: " + rs.getString("descripcion"));
                    System.out.println("   Tipo: " + rs.getString("tipo"));
                    System.out.println("   Video URL: " + (rs.getString("video_url") != null ? rs.getString("video_url") : "(ninguno)"));
                    System.out.println("   Imagen URL: " + (rs.getString("imagen_url") != null ? rs.getString("imagen_url") : "(ninguno)"));
                    System.out.println("   ---");
                }
            }
            
            // ===== TABLA RUTINAS =====
            System.out.println("\n📋 TABLA: rutinas");
            System.out.println("------------------------------------------");
            String sqlRutinas = "SELECT * FROM rutinas";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlRutinas)) {
                
                boolean hayRutinas = false;
                while (rs.next()) {
                    hayRutinas = true;
                    System.out.println("   ID: " + rs.getInt("id"));
                    System.out.println("   Nombre: " + rs.getString("nombre"));
                    System.out.println("   ID Usuario: " + rs.getInt("id_usuario"));
                    System.out.println("   Fecha: " + rs.getString("fecha_creacion"));
                    System.out.println("   ---");
                }
                if (!hayRutinas) {
                    System.out.println("   (No hay rutinas guardadas aún)");
                }
            }
            
            // ===== TABLA DETALLE_RUTINA =====
            System.out.println("\n📝 TABLA: detalle_rutina");
            System.out.println("------------------------------------------");
            String sqlDetalle = "SELECT * FROM detalle_rutina";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlDetalle)) {
                
                boolean hayDetalles = false;
                while (rs.next()) {
                    hayDetalles = true;
                    System.out.println("   ID: " + rs.getInt("id"));
                    System.out.println("   ID Rutina: " + rs.getInt("id_rutina"));
                    System.out.println("   ID Ejercicio: " + rs.getInt("id_ejercicio"));
                    System.out.println("   Orden: " + rs.getInt("orden"));
                    System.out.println("   Series: " + rs.getInt("series"));
                    System.out.println("   Repeticiones: " + rs.getInt("repeticiones"));
                    System.out.println("   Notas: " + (rs.getString("notas") != null ? rs.getString("notas") : "(ninguna)"));
                    System.out.println("   ---");
                }
                if (!hayDetalles) {
                    System.out.println("   (No hay detalles de rutinas aún)");
                }
            }
            
            // ===== RESUMEN FINAL =====
            System.out.println("\n==========================================");
            System.out.println("   📊 RESUMEN FINAL");
            System.out.println("==========================================");
            
            // Contar registros
            String sqlCount = "SELECT 'usuarios' as tabla, COUNT(*) as total FROM usuarios UNION " +
                              "SELECT 'ejercicios', COUNT(*) FROM ejercicios UNION " +
                              "SELECT 'rutinas', COUNT(*) FROM rutinas UNION " +
                              "SELECT 'detalle_rutina', COUNT(*) FROM detalle_rutina";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlCount)) {
                while (rs.next()) {
                    System.out.println("   📌 " + rs.getString("tabla") + ": " + rs.getInt("total") + " registros");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al leer la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n✅ FIN DEL REPORTE");
    }
}