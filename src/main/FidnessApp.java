/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import vista.VentanaLogin;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal de la aplicación Fidness.
 * 
 * Punto de entrada del sistema. Como el main en C: el inicio de todo.
 * En mis años como instructor, siempre digo a mis estudiantes:
 * "El main es como la puerta de entrada a tu casa - tiene que ser acogedora".
 * 
 * AHORA CON REDES: Este main también inicia el servidor local que permite
 * la comunicación por sockets. Es como tener un portero electrónico
 * además de la puerta principal.
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Con base de datos y redes
 */
public class FidnessApp {

    /**
     * Método principal que inicia la aplicación
     * 
     * Uso SwingUtilities.invokeLater por una razón muy importante:
     * cuando daba soporte en Infinite Computer Solutions, aprendí que
     * la GUI debe ejecutarse en su propio hilo para no congelarse.
     * Es como tener un cocinero principal (hilo de eventos) y ayudantes
     * (otros hilos) que no estorban.
     * 
     * NUEVO: Ahora también iniciamos el servidor en un hilo separado
     * para cumplir con el requisito de REDES del proyecto final.
     * 
     * @param args argumentos de línea de comandos (no se usan en esta app)
     */
    public static void main(String[] args) {
        
        // ==========================================================
        // CONFIGURACION PARA ELIMINAR ADVERTENCIAS DE SQLITE
        // ==========================================================
        // Estas líneas reducen/eliminan las advertencias de acceso nativo
        // que aparecen en Java 16+ con SQLite
        System.setProperty("org.sqlite.lib.path", "");
        System.setProperty("org.sqlite.lib.name", "");
        Logger.getLogger("org.sqlite").setLevel(Level.SEVERE);
        // Suprimir también otras advertencias de SQLite
        Logger.getLogger("org.sqlite.JDBC").setLevel(Level.SEVERE);
        // ==========================================================
        
        // ===== NUEVO: INICIAR SERVIDOR (Requisito de REDES) =====
        // El servidor corre en segundo plano atendiendo conexiones
        // Es como tener un recepcionista que trabaja mientras tú haces otras cosas
        System.out.println("Iniciando componentes del sistema...");
        
        try {
            // Iniciar el servidor local en un hilo separado
            // Esto NO bloquea la interfaz gráfica
            servidor.ServidorLocal.getInstance().start();
            System.out.println("Servidor de red iniciado correctamente");
        } catch (Exception e) {
            System.err.println("ADVERTENCIA: No se pudo iniciar el servidor de red");
            System.err.println("   La aplicacion funcionara igual, pero sin funcionalidad de red");
            System.err.println("   Error: " + e.getMessage());
        }
        
        // ===== INTERFAZ GRAFICA (como antes) =====
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Intento usar el look and feel del sistema operativo
                    // Asi la app se ve "nativa" en Windows, Mac o Linux
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Si falla, usamos el default (Metal) y ya
                    System.err.println("No se pudo cargar el Look and Feel: " + e.getMessage());
                }
                
                // Mensaje de bienvenida en consola
                System.out.println("=====================================");
                System.out.println("   FIDNESS APP - Iniciando...");
                System.out.println("=====================================");
                System.out.println("Usuario demo: demo@fidness.com / demo123");
                System.out.println("Admin: admin@fidness.com / admin123");
                System.out.println("Base de datos: SQLite (fidness.db)");
                System.out.println("Servidor de red: Puerto 9090");
                System.out.println("=====================================");
                
                // Crear y mostrar la ventana de login
                new VentanaLogin().setVisible(true);
            }
        });
    }
}