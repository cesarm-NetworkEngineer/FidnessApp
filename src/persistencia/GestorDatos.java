package persistencia;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ===============================================================
 * GESTOR DE DATOS - FIDNESS APP
 * ===============================================================
 * 
 * Autor: César Alonso Morera Alpízar
 * 
 * Esta clase centraliza toda la interacción con la base de datos SQLite.
 * 
 * Aquí se manejan:
 * - Conexión a la base de datos
 * - Creación de tablas
 * - Operaciones CRUD (usuarios y ejercicios)
 * 
 * Reflexión:
 * Pasar de archivos .dat a SQLite permitió que el sistema sea más
 * ordenado, estable y cercano a un entorno real de desarrollo.
 */
public class GestorDatos {

    // ============================================================
    // CONFIGURACIÓN INICIAL DE SQLITE
    // ============================================================
    static {
        Logger.getLogger("org.sqlite").setLevel(Level.SEVERE);

        System.setProperty("org.sqlite.useJNILoader", "false");
        System.setProperty("org.sqlite.purejava", "true");
    }

    private static final String ARCHIVO_BD = "fidness.db";

    // ============================================================
    // MÉTODO PRIVADO DE CONEXIÓN (USO INTERNO)
    // ============================================================
    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:" + ARCHIVO_BD;
            Connection conn = DriverManager.getConnection(url);

            System.out.println("✅ Conectado a SQLite");

            crearTablasSiNoExisten(conn);

            return conn;

        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ Driver SQLite no encontrado", e);
        }
    }

    // ============================================================
    // MÉTODO PÚBLICO PARA PRUEBAS (IMPORTANTE)
    // ============================================================
    public static Connection getConexion() {
        try {
            return getConnection();
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener conexión:");
            e.printStackTrace();
            return null;
        }
    }

    // ============================================================
    // CREACIÓN DE TABLAS
    // ============================================================
    private static void crearTablasSiNoExisten(Connection conn) {

        String sqlUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                es_admin INTEGER DEFAULT 0
            )
        """;

        String sqlEjercicios = """
            CREATE TABLE IF NOT EXISTS ejercicios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT UNIQUE NOT NULL,
                descripcion TEXT NOT NULL,
                tipo TEXT NOT NULL,
                video_url TEXT,
                imagen_url TEXT
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlEjercicios);
            System.out.println("✅ Tablas verificadas/creadas");
        } catch (SQLException e) {
            System.out.println("⚠️ Error creando tablas");
        }
    }

    // ============================================================
    // USUARIOS
    // ============================================================
    public static List<Usuario> cargarUsuarios() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getInt("es_admin") == 1
                );
                usuarios.add(u);
            }
        }

        if (usuarios.isEmpty()) {
            crearUsuariosDePrueba();
            return cargarUsuarios();
        }

        return usuarios;
    }

    private static void crearUsuariosDePrueba() throws SQLException {

        String sql = "INSERT INTO usuarios (nombre, email, password_hash, es_admin) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "Cesar Morera");
            pstmt.setString(2, "cesar@fidness.com");
            pstmt.setString(3, "1234");
            pstmt.setInt(4, 1);
            pstmt.executeUpdate();

            System.out.println("✅ Usuario de prueba creado: Cesar Morera");
        }
    }

    // ============================================================
    // EJERCICIOS
    // ============================================================
    public static List<Ejercicio> cargarEjercicios() throws SQLException {

        List<Ejercicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ejercicios";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ejercicio e = new Ejercicio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        TipoEjercicio.fromString(rs.getString("tipo")),
                        rs.getString("video_url"),
                        rs.getString("imagen_url")
                );
                lista.add(e);
            }
        }

        return lista;
    }

    public static void guardarUsuario(Usuario nuevo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void actualizarUsuario(Usuario actualizado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}