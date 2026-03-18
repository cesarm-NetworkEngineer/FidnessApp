/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import vista.VentanaLogin;
import javax.swing.*;

/**
 * Clase principal de la aplicación Fidness.
 * 
 * Punto de entrada del sistema. Como el main en C: el inicio de todo.
 * En mis años como instructor, siempre digo a mis estudiantes:
 * "El main es como la puerta de entrada a tu casa - tiene que ser acogedora".
 * 
 * @author César Alonso Morera Alpízar
 * @version 1.0
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
     * @param args argumentos de línea de comandos (no se usan en esta app)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Intento usar el look and feel del sistema operativo
                    // Así la app se ve "nativa" en Windows, Mac o Linux
                    // En mis cursos de Rocky Linux, siempre enseño que la
                    // experiencia de usuario debe sentirse familiar
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Si falla, usamos el default (Metal) y ya
                    // No es lo ideal, pero la app sigue funcionando
                    System.err.println("No se pudo cargar el Look and Feel: " + e.getMessage());
                }
                
                // Mensaje de bienvenida en consola (para los que les gusta ver el backend)
                System.out.println("=====================================");
                System.out.println("   🏋️‍♂️ FIDNESS APP - Iniciando...");
                System.out.println("=====================================");
                System.out.println("📌 Usuario demo: demo@fidness.com / demo123");
                System.out.println("📌 Admin: admin@fidness.com / admin123");
                System.out.println("📌 Datos guardados en: /datos");
                System.out.println("=====================================");
                
                // Crear y mostrar la ventana de login
                // La primera impresión del usuario - tiene que ser perfecta
                new VentanaLogin().setVisible(true);
            }
        });
    }
}