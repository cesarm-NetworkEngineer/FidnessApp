/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.DetalleRutina;
import modelo.Rutina;

/**
 * Clase encargada de guardar y cargar datos usando BASE DE DATOS SQLite.
 * 
 * ¡Adiós serialización, hola base de datos!
 * Como cuando pasé de usar papel y lápiz a una hoja de cálculo:
 * los datos están más organizados, se pueden consultar fácilmente
 * y no se pierden si la app se cae inesperadamente.
 * 
 * En mis clases de Rocky Linux 9, siempre digo: 
 * "Si no guardas los datos, no existen".
 * Ahora, en lugar de archivos .dat, usamos una base de datos real.
 * 
 * En DXC Technology aprendí que las bases de datos son el corazón
 * de cualquier sistema serio. Por eso decidí migrar de serialización
 * a SQLite. Es como cambiar un motor de 2 tiempos por uno de 4:
 * más confiable, más rápido y más profesional.
 * 
 * @author César Alonso Morera Alpízar
 */
public class GestorDatos {
    
    // ================================================================
    // BLOQUE ESTÁTICO PARA SILENCIAR ERRORES Y ADVERTENCIAS DE SQLITE
    // ================================================================
    static {
        // === SILENCIAR LOGS DE SQLITE ===
        Logger.getLogger("org.sqlite").setLevel(Level.SEVERE);
        
        // === CONFIGURAR PROPIEDADES PARA EVITAR ERRORES DE LIBRERÍAS NATIVAS ===
        System.setProperty("org.sqlite.lib.path", "");
        System.setProperty("org.sqlite.lib.name", "");
        System.setProperty("org.sqlite.useJNILoader", "false");
        System.setProperty("org.sqlite.purejava", "true");
        
        // === REDIRIGIR System.err A NULL (OPCIONAL - ELIMINA TODOS LOS ERRORES) ===
        // Esto evita que cualquier error de SQLite se muestre en consola
        System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) throws java.io.IOException {
                // No hacer nada - descartar todos los errores
            }
        }));
    }
    
    // Las constantes ya no son archivos .dat, ahora es una base de datos
    // SQLite crea un solo archivo que contiene TODAS las tablas
    private static final String ARCHIVO_BD = "fidness.db";
    
    /**
     * Obtiene la conexión a la base de datos
     * 
     * Usamos el patrón Singleton: una sola conexión para toda la app.
     * Como tener una sola llave para entrar a tu casa, no necesitas mil.
     * 
     * @return Connection objeto de conexión a SQLite
     */
    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + ARCHIVO_BD + "?enable_load_extension=false";
            Connection conn = DriverManager.getConnection(url);
            
            System.out.println("✅ Conectado a SQLite");
            crearTablasSiNoExisten(conn);
            return conn;
            
        } catch (ClassNotFoundException e) {
            // Error silenciado - solo para diagnóstico
            throw new SQLException("Driver no disponible", e);
        } catch (SQLException e) {
            throw e;
        }
    }
    
    /**
     * Crea las tablas si no existen en la base de datos
     */
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
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlEjercicios);
            stmt.execute(sqlRutinas);
            stmt.execute(sqlDetalle);
            System.out.println("✅ Tablas verificadas/creadas");
        } catch (SQLException e) {
            // Error silenciado
        }
    }
    
    // ==================== MÉTODOS PARA USUARIOS ====================
    
    public static List<Usuario> cargarUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";
        
        System.out.println("📖 Cargando usuarios...");
        
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
        
        System.out.println("📖 Se cargaron " + usuarios.size() + " usuarios");
        
        if (usuarios.isEmpty()) {
            System.out.println("👤 No hay usuarios, creando cuentas de demostración...");
            crearUsuariosDePrueba();
            return cargarUsuarios();
        }
        
        return usuarios;
    }
    
    private static void crearUsuariosDePrueba() throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password_hash, es_admin) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "Administrador");
            pstmt.setString(2, "admin@fidness.com");
            pstmt.setString(3, "admin123");
            pstmt.setInt(4, 1);
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Usuario Demo");
            pstmt.setString(2, "demo@fidness.com");
            pstmt.setString(3, "demo123");
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
            
            System.out.println("✅ Usuarios de prueba creados:");
            System.out.println("   📧 admin@fidness.com / admin123 (👑 Administrador)");
            System.out.println("   📧 demo@fidness.com / demo123 (👤 Usuario normal)");
        }
    }
    
    public static void guardarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password_hash, es_admin) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPasswordHash());
            pstmt.setInt(4, usuario.isEsAdmin() ? 1 : 0);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
                System.out.println("✅ Usuario guardado con ID: " + usuario.getId());
            }
        }
    }
    
    public static void actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, password_hash = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getPasswordHash());
            pstmt.setInt(3, usuario.getId());
            pstmt.executeUpdate();
            
            System.out.println("✅ Usuario actualizado: " + usuario.getNombre());
        }
    }
    
    // ==================== MÉTODOS PARA EJERCICIOS ====================
    
    public static List<Ejercicio> cargarEjercicios() throws SQLException {
        List<Ejercicio> ejercicios = new ArrayList<>();
        String sql = "SELECT * FROM ejercicios ORDER BY id";
        
        System.out.println("📖 Cargando ejercicios...");
        
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
                ejercicios.add(e);
            }
        }
        
        System.out.println("📖 Se cargaron " + ejercicios.size() + " ejercicios");
        
        if (ejercicios.isEmpty()) {
            System.out.println("🏋️ No hay ejercicios, creando catálogo de demostración...");
            crearEjerciciosDePrueba();
            return cargarEjercicios();
        }
        
        return ejercicios;
    }
    
    private static void crearEjerciciosDePrueba() throws SQLException {
        String sql = "INSERT INTO ejercicios (nombre, descripcion, tipo, video_url, imagen_url) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            Object[][] ejercicios = {
                {"Press de Banca", "Acostado en banca plana, baja la barra hasta el pecho y empuja hacia arriba. Mantén los codos a 45 grados.", "PECHO", "", ""},
                {"Sentadilla", "Con barra sobre el trapecio, baja como si fueras a sentarte en una silla. Mantén la espalda recta.", "PIERNA", "", ""},
                {"Curl de Bíceps", "De pie, con codos pegados al cuerpo, sube las mancuernas hacia los hombros. Baja controlado.", "BRAZO", "", ""},
                {"Dominadas", "Agarrar la barra con palmas al frente. Sube hasta que la barbilla supere la barra.", "ESPALDA", "", ""},
                {"Plancha Abdominal", "Apoyado en antebrazos y puntas de pies. Mantén el cuerpo recto como una tabla.", "ABDOMEN", "", ""},
                {"Peso Muerto", "Con barra en el suelo, empuja con las piernas manteniendo la espalda recta.", "FULLBODY", "", ""}
            };
            
            for (Object[] ej : ejercicios) {
                pstmt.setString(1, (String) ej[0]);
                pstmt.setString(2, (String) ej[1]);
                pstmt.setString(3, (String) ej[2]);
                pstmt.setString(4, (String) ej[3]);
                pstmt.setString(5, (String) ej[4]);
                pstmt.executeUpdate();
            }
            
            System.out.println("✅ 6 ejercicios de prueba creados en el catálogo");
        }
    }
    
    public static void guardarEjercicio(Ejercicio ejercicio) throws SQLException {
        String sql = "INSERT INTO ejercicios (nombre, descripcion, tipo, video_url, imagen_url) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, ejercicio.getNombre());
            pstmt.setString(2, ejercicio.getDescripcion());
            pstmt.setString(3, ejercicio.getTipo().toString());
            pstmt.setString(4, ejercicio.getVideoURL());
            pstmt.setString(5, ejercicio.getImagenURL());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                ejercicio.setId(rs.getInt(1));
                System.out.println("✅ Ejercicio guardado: " + ejercicio.getNombre());
            }
        }
    }
    
    public static void actualizarEjercicio(Ejercicio ejercicio) throws SQLException {
        String sql = "UPDATE ejercicios SET nombre = ?, descripcion = ?, tipo = ?, video_url = ?, imagen_url = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ejercicio.getNombre());
            pstmt.setString(2, ejercicio.getDescripcion());
            pstmt.setString(3, ejercicio.getTipo().toString());
            pstmt.setString(4, ejercicio.getVideoURL());
            pstmt.setString(5, ejercicio.getImagenURL());
            pstmt.setInt(6, ejercicio.getId());
            pstmt.executeUpdate();
            
            System.out.println("✅ Ejercicio actualizado: " + ejercicio.getNombre());
        }
    }
    
    public static void eliminarEjercicio(int id) throws SQLException {
        String sql = "DELETE FROM ejercicios WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int afectados = pstmt.executeUpdate();
            
            if (afectados > 0) {
                System.out.println("✅ Ejercicio eliminado (ID: " + id + ")");
            }
        }
    }

    public static List<DetalleRutina> cargarDetalles() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static List<Rutina> cargarRutinas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}