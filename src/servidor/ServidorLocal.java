/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.*;
import java.net.*;

/**
 * Servidor local de FidnessApp para manejar conexiones de red.
 * 
 * REQUISITO DE REDES: Este servidor permite que la aplicación se comunique
 * a través de sockets, cumpliendo con lo solicitado en el proyecto final.
 * 
 * En mis años como ingeniero de redes en Syniverse, aprendí que los servidores
 * son como los conmutadores de una empresa: reciben llamadas (conexiones)
 * y las dirigen al departamento correcto (manejador).
 * 
 * Este servidor corre en segundo plano mientras la app funciona.
 * Es como tener un recepcionista que atiende a los clientes mientras tú
 * haces otras cosas.
 * 
 * @author César Alonso Morera Alpízar
 */
public class ServidorLocal extends Thread {
    
    private static ServidorLocal instancia;  // Patrón Singleton
    private ServerSocket serverSocket;
    private boolean ejecutando = true;
    private static final int PUERTO = 9090;
    
    /**
     * Obtiene la única instancia del servidor (Patrón Singleton)
     * 
     * En DXC Technology usábamos este patrón para servicios que debían
     * tener una sola instancia en todo el sistema. Como el router principal
     * de una red: solo debe haber uno.
     * 
     * @return la instancia única del servidor
     */
    public static ServidorLocal getInstance() {
        if (instancia == null) {
            instancia = new ServidorLocal();
        }
        return instancia;
    }
    
    /**
     * Constructor privado (patrón Singleton)
     * 
     * Inicializa el servidor en el puerto 9090.
     * Elegí el 9090 porque es un puerto poco común, así no conflictúa
     * con otros servicios como el 8080 (web) o el 3306 (MySQL).
     */
    private ServidorLocal() {
        try {
            serverSocket = new ServerSocket(PUERTO);
            System.out.println("🌐 ======================================");
            System.out.println("🌐 SERVIDOR FIDNESS INICIADO");
            System.out.println("🌐 Puerto: " + PUERTO);
            System.out.println("🌐 Estado: Escuchando conexiones...");
            System.out.println("🌐 ======================================");
        } catch (IOException e) {
            System.err.println("❌ ERROR: No se pudo iniciar el servidor en el puerto " + PUERTO);
            System.err.println("   ¿Quizás ya hay otro servidor corriendo?");
            System.err.println("   Detalle: " + e.getMessage());
        }
    }
    
    /**
     * Método principal del hilo del servidor
     * 
     * Este método corre en un hilo SEPARADO (concurrencia).
     * Así el servidor no bloquea la interfaz gráfica.
     * 
     * Es como tener un empleado dedicado solo a contestar el teléfono,
     * mientras los demás hacen su trabajo normal.
     */
    @Override
    public void run() {
        while (ejecutando) {
            try {
                // Esperar a que un cliente se conecte (bloqueante)
                Socket cliente = serverSocket.accept();
                
                String ipCliente = cliente.getInetAddress().getHostAddress();
                System.out.println("📱 NUEVO CLIENTE CONECTADO");
                System.out.println("   IP: " + ipCliente);
                System.out.println("   Puerto local: " + cliente.getLocalPort());
                
                // Crear un hilo para atender a este cliente
                // Cada cliente tiene su propio hilo (CONCURRENCIA)
                ManejadorCliente manejador = new ManejadorCliente(cliente);
                manejador.start();
                
                System.out.println("   ✅ Hilo creado para atender a este cliente");
                
            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println("❌ Error aceptando conexión: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Detiene el servidor gracefulmente
     * 
     * Cierra el socket principal para que deje de aceptar conexiones.
     * Es como cerrar la recepción de una empresa: ya no entran más visitas.
     */
    public void detener() {
        ejecutando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("🛑 Servidor detenido correctamente");
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error al detener servidor: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el servidor está corriendo
     * 
     * @return true si el servidor está activo
     */
    public boolean estaCorriendo() {
        return serverSocket != null && !serverSocket.isClosed() && ejecutando;
    }
}