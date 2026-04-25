package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ============================================================
 * CLASE: ConexionBD
 * ============================================================
 *
 * Autor: Cesar Alonso Morera Alpizar
 *
 * PROPÓSITO:
 * Esta clase se encarga de gestionar la conexión con la base de datos SQLite.
 * Funciona como el punto central de acceso a los datos dentro del sistema.
 *
 * IMPORTANCIA:
 * En cualquier sistema, la conexión a la base de datos es un componente crítico.
 * Si esta capa falla, el resto de la aplicación no puede operar correctamente.
 *
 * ANALOGÍA:
 * Se puede comparar con la electricidad de una casa:
 * si no hay energía, ningún dispositivo funciona.
 *
 * TECNOLOGÍA UTILIZADA:
 * SQLite es una base de datos ligera que no requiere servidor.
 * Toda la información se almacena en un solo archivo físico (fidness.db).
 *
 * PATRÓN APLICADO:
 * Se utiliza el patrón Singleton, asegurando que exista una única conexión
 * activa durante la ejecución de la aplicación.
 */
public class ConexionBD {

    // Instancia única de la conexión (Singleton)
    private static Connection connection = null;

    /**
     * ============================================================
     * MÉTODO: getConnection()
     * ============================================================
     *
     * PROPÓSITO:
     * Obtener una conexión activa a la base de datos.
     *
     * FUNCIONAMIENTO:
     * - Si no existe conexión, se crea
     * - Si ya existe, se reutiliza
     *
     * BENEFICIO:
     * Evita múltiples conexiones innecesarias y mejora el rendimiento.
     *
     * @return Connection objeto de conexión a SQLite
     */
    public static Connection getConnection() {

        try {
            // Verifica si la conexión no existe o fue cerrada
            if (connection == null || connection.isClosed()) {

                // Carga del driver JDBC de SQLite
                Class.forName("org.sqlite.JDBC");

                // URL de conexión (archivo local)
                String url = "jdbc:sqlite:fidness.db";

                // Establecer conexión
                connection = DriverManager.getConnection(url);

                System.out.println("Conexion establecida con la base de datos SQLite");

                // Crear tablas si no existen
                crearTablas();
            }

        } catch (Exception e) {

            // Manejo de error detallado para diagnóstico
            System.err.println("Error al establecer conexión con la base de datos");
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * ============================================================
     * MÉTODO: crearTablas()
     * ============================================================
     *
     * PROPÓSITO:
     * Crear la estructura de la base de datos si no existe.
     *
     * TABLAS:
     * - usuarios
     * - ejercicios
     * - rutinas
     * - detalle_rutina
     *
     * IMPORTANCIA:
     * Garantiza que el sistema siempre tenga la estructura necesaria
     * para operar correctamente, incluso en la primera ejecución.
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
            stmt.execute(sqlEjercicios);
            stmt.execute(sqlRutinas);
            stmt.execute(sqlDetalle);

            System.out.println("Estructura de base de datos verificada correctamente");

        } catch (SQLException e) {

            System.err.println("Error al crear/verificar tablas");
            e.printStackTrace();
        }
    }

    /**
     * ============================================================
     * MÉTODO: cerrarConexion()
     * ============================================================
     *
     * PROPÓSITO:
     * Cerrar la conexión a la base de datos.
     *
     * IMPORTANCIA:
     * Liberar recursos es una buena práctica en cualquier sistema.
     *
     * @return true si se cerró correctamente
     */
    public static boolean cerrarConexion() {

        if (connection != null) {
            try {
                connection.close();
                connection = null;

                System.out.println("Conexion cerrada correctamente");
                return true;

            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión");
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}