/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.sql.*;

/**
 * Clase encargada de la conexión con la base de datos SQLite.
 * 
 * En mis años de experiencia, he aprendido que la conexión a la base de datos
 * es como la electricidad de una casa: si falla, nada más funciona.
 * Por eso esta clase es tan importante y la trato con mucho cuidado.
 * 
 * SQLite es una base de datos ligera que no necesita instalación.
 * Es como tener una libreta digital: guarda los datos en un solo archivo
 * y no requiere un servidor aparte.
 * 
 * @author César Alonso Morera Alpízar
 */
public class ConexionBD {
    
    private static Connection connection = null;
    
    /**
     * Configuración especial para JDK 25 (Java 25)
     * 
     * Cuando cambié a Java 25, empecé a tener problemas con las librerías
     * nativas de SQLite. Después de investigar, encontré que deshabilitando
     * la carga de nativos se soluciona. Es como cuando un carro no arranca
     * y le cambias una pieza: a veces hay que hacer ajustes finos.
     */
    static {
        // Deshabilitar carga de librerías nativas para evitar errores en JDK 25
        System.setProperty("org.sqlite.lib.path", "");
        System.setProperty("org.sqlite.lib.name", "dummy");
        System.setProperty("org.sqlite.useJNILoader", "false");
        System.setProperty("org.sqlite.purejava", "true");
    }
    
    /**
     * Obtiene la conexión a la base de datos SQLite
     * 
     * Uso el patrón Singleton: una sola conexión para toda la app.
     * Como tener una sola llave para entrar a tu casa, no necesitas mil.
     * 
     * @return Connection objeto de conexión a SQLite
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Cargar el driver de SQLite (como poner la llave en la cerradura)
                Class.forName("org.sqlite.JDBC");
                
                // La URL dice: "conéctate al archivo fidness.db"
                // Si no existe, SQLite lo crea automáticamente
                // El parámetro enable_load_extension=false evita problemas con JDK 25
                String url = "jdbc:sqlite:fidness.db?enable_load_extension=false";
                connection = DriverManager.getConnection(url);
                System.out.println("✅ Base de datos conectada: fidness.db");
                System.out.println("   (Modo compatible con JDK 25)");
                
                // Crear las tablas si es la primera vez que corremos
                crearTablas();
                
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver SQLite no encontrado.");
                System.err.println("   ¿Agregaste la librería sqlite-jdbc al proyecto?");
                System.err.println("   Detalle técnico: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Error de conexión con la base de datos:");
                System.err.println("   " + e.getMessage());
                System.err.println("   Verifica que el archivo fidness.db no esté corrupto");
            }
        }
        return connection;
    }
    
    /**
     * Crea las tablas necesarias si no existen
     * 
     * Esto es como preparar el terreno antes de construir una casa.
     * Si ya hay tablas, no hacemos nada. Si no, las creamos.
     * 
     * Las tablas son:
     * - usuarios: guarda la información de cada persona que usa la app
     * - ejercicios: el catálogo de todos los ejercicios disponibles
     * - rutinas: las rutinas que crea cada usuario
     * - detalle_rutina: la relación entre rutinas y ejercicios (con series y repeticiones)
     * 
     * En mis clases, siempre digo: "una buena base de datos es como un buen armario:
     * cada cosa en su lugar y fácil de encontrar".
     */
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
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlUsuarios);
            System.out.println("   ✅ Tabla 'usuarios' verificada");
            stmt.execute(sqlEjercicios);
            System.out.println("   ✅ Tabla 'ejercicios' verificada");
            stmt.execute(sqlRutinas);
            System.out.println("   ✅ Tabla 'rutinas' verificada");
            stmt.execute(sqlDetalle);
            System.out.println("   ✅ Tabla 'detalle_rutina' verificada");
            
            System.out.println("📁 Estructura de base de datos lista para trabajar");
            
        } catch (SQLException e) {
            System.err.println("⚠️ Error verificando/creando tablas: " + e.getMessage());
            System.err.println("   Esto puede ocurrir si la base de datos está corrupta.");
            System.err.println("   Solución: elimina el archivo fidness.db y vuelve a ejecutar.");
        }
    }
    
    /**
     * Cierra la conexión a la base de datos
     * 
     * Buena práctica: siempre cerrar lo que abrimos.
     * Como cuando terminas de usar una herramienta, la guardas en su lugar.
     * 
     * @return true si se cerró correctamente, false si hubo error
     */
    public static boolean cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔌 Conexión a base de datos cerrada");
                return true;
            } catch (SQLException e) {
                System.err.println("⚠️ Error al cerrar la conexión: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}