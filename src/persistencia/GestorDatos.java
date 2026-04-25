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
    
    // Las constantes ya no son archivos .dat, ahora es una base de datos
    // SQLite crea un solo archivo que contiene TODAS las tablas
    private static final String ARCHIVO_BD = "fidness.db";
    
    // Bloque estático para configurar SQLite antes de cualquier conexión
    // Esto es CRÍTICO para que funcione con JDK 25
    static {
        // Deshabilitar la carga de librerías nativas (causa del error)
        System.setProperty("org.sqlite.lib.path", "");
        System.setProperty("org.sqlite.lib.name", "");
        System.setProperty("org.sqlite.useJNILoader", "false");
        System.setProperty("org.sqlite.purejava", "true");
    }
    
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
            // Cargar el driver de SQLite (como poner la llave en la cerradura)
            Class.forName("org.sqlite.JDBC");
            
            // jdbc:sqlite:fidness.db -> el archivo se crea automáticamente
            // enable_load_extension=false es clave para JDK 25
            String url = "jdbc:sqlite:" + ARCHIVO_BD + "?enable_load_extension=false";
            Connection conn = DriverManager.getConnection(url);
            
            System.out.println("✅ Conectado a SQLite (modo compatible con JDK 25)");
            
            // Si es la primera vez que corremos, creamos las tablas
            crearTablasSiNoExisten(conn);
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver de SQLite no encontrado. ¿Agregaste la librería?");
            System.err.println("   Descarga sqlite-jdbc-3.47.2.0.jar desde:");
            System.err.println("   https://github.com/xerial/sqlite-jdbc/releases");
            throw new SQLException("Driver no disponible", e);
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión SQLite: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Crea las tablas si no existen en la base de datos
     * 
     * Esto es como preparar el terreno antes de construir una casa.
     * Si ya hay tablas, no hacemos nada. Si no, las creamos.
     * 
     * En mis años de experiencia, he aprendido que las migraciones
     * automáticas son clave. Así el usuario no tiene que hacer nada.
     * 
     * @param conn conexión activa a la base de datos
     */
    private static void crearTablasSiNoExisten(Connection conn) {
        // SQL para crear la tabla de usuarios
        String sqlUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                es_admin INTEGER DEFAULT 0
            )
        """;
        
        // SQL para crear la tabla de ejercicios
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
        
        // SQL para crear la tabla de rutinas
        String sqlRutinas = """
            CREATE TABLE IF NOT EXISTS rutinas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                id_usuario INTEGER NOT NULL,
                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
            )
        """;
        
        // SQL para crear la tabla de detalle de rutinas (relación muchos a muchos)
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
            System.out.println("✅ Tablas verificadas/creadas en la base de datos");
        } catch (SQLException e) {
            System.err.println("⚠️ Error creando tablas: " + e.getMessage());
        }
    }
    
    // ==================== MÉTODOS PARA USUARIOS ====================
    
    /**
     * Carga todos los usuarios desde la base de datos
     * 
     * Ahora en lugar de leer un archivo binario, hacemos una consulta SQL.
     * Es como preguntarle a la base de datos: "Oye, ¿quiénes están registrados?"
     * 
     * @return lista de usuarios (vacía si no hay ninguno)
     * @throws SQLException si hay problemas con la base de datos
     */
    public static List<Usuario> cargarUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";
        
        System.out.println("📖 Cargando usuarios desde la base de datos...");
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getInt("es_admin") == 1  // SQLite usa 1 para true, 0 para false
                );
                usuarios.add(u);
            }
        }
        
        System.out.println("📖 Se cargaron " + usuarios.size() + " usuarios");
        
        // Si no hay usuarios, creamos los de prueba para que la app no esté vacía
        // Como cuando llegas a un gimnasio nuevo y te dan una rutina de prueba
        if (usuarios.isEmpty()) {
            System.out.println("👤 No hay usuarios, creando cuentas de demostración...");
            crearUsuariosDePrueba();
            return cargarUsuarios(); // Recargar después de crear
        }
        
        return usuarios;
    }
    
    /**
     * Crea usuarios de prueba para que la app funcione desde el inicio
     * 
     * En mis cursos, siempre dejo datos de muestra. Así el estudiante
     * puede probar la app sin tener que registrarse primero.
     * 
     * @throws SQLException si hay error al guardar
     */
    private static void crearUsuariosDePrueba() throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password_hash, es_admin) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Usuario administrador (tiene poderes especiales como en Linux el root)
            pstmt.setString(1, "Administrador");
            pstmt.setString(2, "admin@fidness.com");
            pstmt.setString(3, "admin123");  // En producción usaríamos hash con BCrypt
            pstmt.setInt(4, 1);  // es_admin = true
            pstmt.executeUpdate();
            
            // Usuario de demostración (para que cualquiera pueda probar)
            pstmt.setString(1, "Usuario Demo");
            pstmt.setString(2, "demo@fidness.com");
            pstmt.setString(3, "demo123");
            pstmt.setInt(4, 0);  // es_admin = false
            pstmt.executeUpdate();
            
            System.out.println("✅ Usuarios de prueba creados:");
            System.out.println("   📧 admin@fidness.com / admin123 (👑 Administrador)");
            System.out.println("   📧 demo@fidness.com / demo123 (👤 Usuario normal)");
        }
    }
    
    /**
     * Guarda un nuevo usuario en la base de datos
     * 
     * La base de datos asigna automáticamente el ID (AUTOINCREMENT),
     * como cuando llegas a un gimnasio y te dan un número de socio.
     * 
     * @param usuario el usuario a guardar
     * @throws SQLException si hay error (email duplicado, etc.)
     */
    public static void guardarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password_hash, es_admin) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPasswordHash());
            pstmt.setInt(4, usuario.isEsAdmin() ? 1 : 0);
            pstmt.executeUpdate();
            
            // Obtener el ID que la base de datos generó automáticamente
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
                System.out.println("✅ Usuario guardado con ID: " + usuario.getId());
            }
        }
    }
    
    /**
     * Actualiza los datos de un usuario existente
     * 
     * @param usuario el usuario con los datos actualizados
     * @throws SQLException si hay error
     */
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
    
    /**
     * Carga todos los ejercicios desde la base de datos
     * 
     * @return lista de ejercicios
     * @throws SQLException si hay error
     */
    public static List<Ejercicio> cargarEjercicios() throws SQLException {
        List<Ejercicio> ejercicios = new ArrayList<>();
        String sql = "SELECT * FROM ejercicios ORDER BY id";
        
        System.out.println("📖 Cargando ejercicios desde la base de datos...");
        
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
        
        // Si no hay ejercicios, creamos los de prueba
        if (ejercicios.isEmpty()) {
            System.out.println("🏋️ No hay ejercicios, creando catálogo de demostración...");
            crearEjerciciosDePrueba();
            return cargarEjercicios();
        }
        
        return ejercicios;
    }
    
    /**
     * Crea ejercicios de prueba para que el catálogo no esté vacío
     * 
     * Estos son los ejercicios clásicos que todo gimnasio tiene.
     * Como cuando compras un teléfono y ya trae apps básicas.
     * 
     * @throws SQLException si hay error
     */
    private static void crearEjerciciosDePrueba() throws SQLException {
        String sql = "INSERT INTO ejercicios (nombre, descripcion, tipo, video_url, imagen_url) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Array con los ejercicios de prueba
            // En mis clases presenciales, estos son los que más piden mis estudiantes
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
    
    /**
     * Guarda un nuevo ejercicio en la base de datos
     * 
     * @param ejercicio el ejercicio a guardar
     * @throws SQLException si hay error (nombre duplicado, etc.)
     */
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
    
    /**
     * Actualiza un ejercicio existente
     * 
     * @param ejercicio el ejercicio con los datos actualizados
     * @throws SQLException si hay error
     */
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
    
    /**
     * Elimina un ejercicio de la base de datos
     * 
     * @param id ID del ejercicio a eliminar
     * @throws SQLException si hay error
     */
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
}