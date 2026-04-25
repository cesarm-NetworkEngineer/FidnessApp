package prueba;

import java.sql.*;
import persistencia.ConexionBD;

/**
 * Clase de prueba para inspeccionar el contenido completo de la base de datos.
 * 
 * Autor: César Alonso Morera Alpízar
 * 
 * Objetivo:
 * Permitir validar rápidamente si los datos se están guardando correctamente
 * en todas las tablas del sistema.
 */
public class ImprimirBD {

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("   CONTENIDO DE LA BASE DE DATOS");
        System.out.println("==========================================\n");

        try (Connection conn = ConexionBD.getConnection()) {

            if (conn == null) {
                System.err.println("No se pudo establecer conexión con la base de datos");
                return;
            }

            System.out.println("Conexión establecida correctamente\n");

            imprimirUsuarios(conn);
            imprimirEjercicios(conn);
            imprimirRutinas(conn);
            imprimirDetalleRutina(conn);
            imprimirResumen(conn);

        } catch (SQLException e) {
            System.err.println("Error al consultar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nFIN DEL REPORTE");
    }

    private static void imprimirUsuarios(Connection conn) throws SQLException {

        System.out.println("TABLA: usuarios");
        System.out.println("------------------------------------------");

        String sql = "SELECT * FROM usuarios";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int contador = 0;

            while (rs.next()) {
                contador++;

                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Admin: " + (rs.getInt("es_admin") == 1));
                System.out.println("----------------------------------");
            }

            if (contador == 0) {
                System.out.println("No hay usuarios registrados");
            }
        }
    }

    private static void imprimirEjercicios(Connection conn) throws SQLException {

        System.out.println("\nTABLA: ejercicios");
        System.out.println("------------------------------------------");

        String sql = "SELECT * FROM ejercicios";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int contador = 0;

            while (rs.next()) {
                contador++;

                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Tipo: " + rs.getString("tipo"));
                System.out.println("----------------------------------");
            }

            if (contador == 0) {
                System.out.println("No hay ejercicios registrados");
            }
        }
    }

    private static void imprimirRutinas(Connection conn) throws SQLException {

        System.out.println("\nTABLA: rutinas");
        System.out.println("------------------------------------------");

        String sql = "SELECT * FROM rutinas";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int contador = 0;

            while (rs.next()) {
                contador++;

                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Usuario ID: " + rs.getInt("id_usuario"));
                System.out.println("----------------------------------");
            }

            if (contador == 0) {
                System.out.println("No hay rutinas registradas");
            }
        }
    }

    private static void imprimirDetalleRutina(Connection conn) throws SQLException {

        System.out.println("\nTABLA: detalle_rutina");
        System.out.println("------------------------------------------");

        String sql = "SELECT * FROM detalle_rutina";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int contador = 0;

            while (rs.next()) {
                contador++;

                System.out.println("Rutina ID: " + rs.getInt("id_rutina"));
                System.out.println("Ejercicio ID: " + rs.getInt("id_ejercicio"));
                System.out.println("Series: " + rs.getInt("series"));
                System.out.println("Repeticiones: " + rs.getInt("repeticiones"));
                System.out.println("----------------------------------");
            }

            if (contador == 0) {
                System.out.println("No hay ejercicios asociados a rutinas");
            }
        }
    }

    private static void imprimirResumen(Connection conn) throws SQLException {

        System.out.println("\nRESUMEN GENERAL");
        System.out.println("------------------------------------------");

        String sql = """
            SELECT 'usuarios' AS tabla, COUNT(*) FROM usuarios
            UNION
            SELECT 'ejercicios', COUNT(*) FROM ejercicios
            UNION
            SELECT 'rutinas', COUNT(*) FROM rutinas
            UNION
            SELECT 'detalle_rutina', COUNT(*) FROM detalle_rutina
        """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(rs.getString(1) + ": " + rs.getInt(2));
            }
        }
    }
}