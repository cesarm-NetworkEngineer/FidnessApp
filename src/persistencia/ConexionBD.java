package persistencia;

import java.sql.*;

public class ConexionBD {
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // La base de datos se crea automáticamente en la carpeta del proyecto
                String url = "jdbc:sqlite:fidness.db";
                connection = DriverManager.getConnection(url);
                System.out.println("✅ Base de datos conectada: fidness.db");
                
                // Crear tablas si no existen
                crearTablas();
                
            } catch (SQLException e) {
                System.err.println("❌ Error de conexión: " + e.getMessage());
            }
        }
        return connection;
    }
    
    private static void crearTablas() {
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
        
        String sqlRutinas = """
            CREATE TABLE IF NOT EXISTS rutinas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                id_usuario INTEGER NOT NULL,
                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
            )
        """;
        
        String sqlDetalle = """
            CREATE TABLE IF NOT EXISTS detalle_rutina (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_rutina INTEGER NOT NULL,
                id_ejercicio INTEGER NOT NULL,
                orden INTEGER NOT NULL,
                series INTEGER NOT NULL,
                repeticiones INTEGER NOT NULL,
                notas TEXT,
                FOREIGN KEY (id_rutina) REFERENCES rutinas(id),
                FOREIGN KEY (id_ejercicio) REFERENCES ejercicios(id)
            )
        """;
        
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlEjercicios);
            stmt.execute(sqlRutinas);
            stmt.execute(sqlDetalle);
            System.out.println("✅ Tablas creadas/verificadas");
        } catch (SQLException e) {
            System.err.println("Error creando tablas: " + e.getMessage());
        }
    }
}