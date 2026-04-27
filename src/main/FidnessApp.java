/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import vista.VentanaLogin;
import servidor.ServidorLocal;  // 🔴 IMPORTANTE: Agregar esta importación
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
 * @version 2.2 - Abril 2026 - Corregido para máxima estabilidad
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
        System.setProperty("org.sqlite.useJNILoader", "false");
        System.setProperty("org.sqlite.purejava", "true");
        Logger.getLogger("org.sqlite").setLevel(Level.SEVERE);
        // Suprimir también otras advertencias de SQLite
        Logger.getLogger("org.sqlite.JDBC").setLevel(Level.SEVERE);
        // ==========================================================
        
        // Mostrar información del sistema
        System.out.println("=====================================");
        System.out.println("   🏋️ FIDNESS APP - Iniciando...");
        System.out.println("=====================================");
        System.out.println("   ☕ Java version: " + System.getProperty("java.version"));
        System.out.println("   💻 SO: " + System.getProperty("os.name"));
        System.out.println("=====================================");
        
        // ===== INICIAR SERVIDOR (Requisito de REDES) =====
        // El servidor corre en segundo plano atendiendo conexiones
        // Es como tener un recepcionista que trabaja mientras tú haces otras cosas
        System.out.println("\n🔧 Iniciando componentes del sistema...");
        
        // Iniciar servidor en un hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                // 🔴 CORREGIDO: Usar la importación directamente
                ServidorLocal servidor = ServidorLocal.getInstance();
                servidor.start();
                
                // Pequeña pausa para que el servidor se estabilice
                Thread.sleep(500);
                
                if (servidor.estaCorriendo()) {
                    System.out.println("🌐 ✅ Servidor de red activo en puerto: 9091");
                } else {
                    System.out.println("🌐 ⚠️ Servidor en modo compatible (funciones de red limitadas)");
                }
            } catch (Exception e) {
                System.err.println("🌐 ⚠️ No se pudo iniciar el servidor de red");
                System.err.println("   La aplicación funcionará correctamente sin funciones de red");
                System.err.println("   Detalle: " + e.getMessage());
            }
        }).start();
        
        // ===== INTERFAZ GRÁFICA =====
        SwingUtilities.invokeLater(() -> {
            try {
                // Intentar usar el look and feel del sistema operativo
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("🎨 Look and Feel nativo cargado");
            } catch (Exception e) {
                System.err.println("🎨 Usando Look and Feel por defecto");
            }
            
            // Mensajes de bienvenida en consola
            System.out.println("\n=====================================");
            System.out.println("   ✅ FIDNESS APP - INICIADA CORRECTAMENTE");
            System.out.println("=====================================");
            System.out.println("📝 CREDENCIALES DE ACCESO:");
            System.out.println("   👑 Admin:    admin@fidness.com");
            System.out.println("   🔑 Password: admin123");
            System.out.println("   👤 Demo:     demo@fidness.com");
            System.out.println("   🔑 Password: demo123");
            System.out.println("=====================================\n");
            
            // Crear y mostrar la ventana de login
            VentanaLogin login = new VentanaLogin();
            login.setVisible(true);
            System.out.println("🪟 Ventana de login mostrada");
        });
    }
}