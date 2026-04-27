/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import controlador.ControladorEjercicios;
import controlador.ControladorUsuarios;
import modelo.Ejercicio;
import modelo.Usuario;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * Manejador de cada cliente que se conecta al servidor.
 * 
 * Cada cliente tiene SU PROPIO manejador ejecutándose en un hilo separado.
 * Esto es lo que llamamos CONCURRENCIA: múltiples clientes atendidos
 * al mismo tiempo, como una central de call center con varios operadores.
 * 
 * En mis cursos de Rocky Linux, enseñaba que los servidores deben poder
 * atender muchas peticiones a la vez. Este manejador hace exactamente eso.
 * 
 * @author César Alonso Morera Alpízar
 */
public class ManejadorCliente extends Thread {
    
    private Socket socket;
    private ControladorUsuarios controladorUsuarios;
    private ControladorEjercicios controladorEjercicios;
    private boolean conectado = true;
    
    /**
     * Constructor del manejador
     * 
     * @param socket el socket del cliente conectado
     */
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
        this.controladorUsuarios = new ControladorUsuarios();
        this.controladorEjercicios = new ControladorEjercicios();
        System.out.println("🆕 Nuevo manejador creado para cliente: " + socket.getInetAddress());
    }
    
    /**
     * Método principal que atiende al cliente
     * 
     * Protocolo de comunicación:
     * 1. El cliente envía un comando (String)
     * 2. El servidor procesa el comando
     * 3. El servidor envía la respuesta
     * 
     * Es como un diálogo: el cliente habla, el servidor responde.
     */
    @Override
    public void run() {
        System.out.println("🔄 Atendiendo cliente en hilo: " + Thread.currentThread().getName());
        
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            
            out.flush(); // Asegurar que el stream esté listo
            
            while (conectado) {
                try {
                    // Esperar comando del cliente
                    String comando = (String) in.readObject();
                    System.out.println("📨 Comando recibido: " + comando);
                    
                    // Procesar según el comando
                    switch (comando) {
                        case "LOGIN":
                            procesarLogin(in, out);
                            break;
                            
                        case "LISTAR_EJERCICIOS":
                            procesarListarEjercicios(out);
                            break;
                            
                        case "BUSCAR_EJERCICIO_POR_ID":
                            procesarBuscarEjercicioPorId(in, out);
                            break;
                            
                        case "BUSCAR_EJERCICIO_POR_NOMBRE":
                            procesarBuscarEjercicioPorNombre(in, out);
                            break;
                            
                        case "BUSCAR_EJERCICIO_POR_TIPO":
                            procesarBuscarEjercicioPorTipo(in, out);
                            break;
                            
                        case "AGREGAR_EJERCICIO":
                            procesarAgregarEjercicio(in, out);
                            break;
                            
                        case "REGISTRAR_USUARIO":
                            procesarRegistro(in, out);
                            break;
                            
                        case "LISTAR_USUARIOS":
                            procesarListarUsuarios(out);
                            break;
                            
                        case "PING":
                            out.writeObject("PONG");
                            out.flush();
                            break;
                            
                        case "CERRAR":
                            System.out.println("👋 Cliente solicitó desconexión");
                            conectado = false;
                            break;
                            
                        default:
                            System.out.println("⚠️ Comando desconocido: " + comando);
                            out.writeObject("ERROR: Comando desconocido");
                            out.flush();
                    }
                } catch (SocketException e) {
                    System.out.println("🔌 Conexión interrumpida con el cliente");
                    conectado = false;
                } catch (ClassNotFoundException e) {
                    System.err.println("❌ Error de deserialización: " + e.getMessage());
                    out.writeObject("ERROR: Formato de datos incorrecto");
                    out.flush();
                }
            }
            
        } catch (EOFException e) {
            System.out.println("🔌 Cliente desconectado abruptamente");
        } catch (IOException e) {
            System.err.println("❌ Error en comunicación con cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Procesa la petición de login
     */
    private void procesarLogin(ObjectInputStream in, ObjectOutputStream out) 
            throws IOException, ClassNotFoundException {
        
        String email = (String) in.readObject();
        String password = (String) in.readObject();
        
        System.out.println("🔐 Intento de login - Email: " + email);
        
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            System.out.println("❌ Credenciales inválidas (vacías)");
            out.writeObject(null);
            out.flush();
            return;
        }
        
        Usuario usuario = controladorUsuarios.iniciarSesion(email, password);
        
        if (usuario != null) {
            System.out.println("✅ Login exitoso para: " + email);
            // No enviamos la contraseña por seguridad
            usuario.setPasswordHash(null);
        } else {
            System.out.println("❌ Login fallido para: " + email);
        }
        
        out.writeObject(usuario);
        out.flush();
    }
    
    /**
     * Procesa la petición de listar ejercicios
     */
    private void procesarListarEjercicios(ObjectOutputStream out) throws IOException {
        List<Ejercicio> ejercicios = controladorEjercicios.getTodosLosEjercicios();
        System.out.println("📋 Enviando " + ejercicios.size() + " ejercicios al cliente");
        out.writeObject(ejercicios);
        out.flush();
    }
    
    /**
     * Procesa la petición de buscar ejercicio por ID
     */
    private void procesarBuscarEjercicioPorId(ObjectInputStream in, ObjectOutputStream out) 
            throws IOException, ClassNotFoundException {
        
        int id = (int) in.readObject();
        Ejercicio ejercicio = controladorEjercicios.buscarPorId(id);
        
        if (ejercicio != null) {
            System.out.println("🔍 Enviando ejercicio ID " + id + ": " + ejercicio.getNombre());
        } else {
            System.out.println("⚠️ Ejercicio ID " + id + " no encontrado");
        }
        
        out.writeObject(ejercicio);
        out.flush();
    }
    
    /**
     * Procesa la petición de buscar ejercicio por nombre (nuevo)
     */
    private void procesarBuscarEjercicioPorNombre(ObjectInputStream in, ObjectOutputStream out) 
            throws IOException, ClassNotFoundException {
        
        String nombre = (String) in.readObject();
        List<Ejercicio> ejercicios = controladorEjercicios.buscarPorNombre(nombre);
        System.out.println("🔍 Buscando ejercicios con nombre: '" + nombre + "' - Encontrados: " + ejercicios.size());
        out.writeObject(ejercicios);
        out.flush();
    }
    
    /**
     * Procesa la petición de buscar ejercicio por tipo (nuevo)
     */
    private void procesarBuscarEjercicioPorTipo(ObjectInputStream in, ObjectOutputStream out) 
            throws IOException, ClassNotFoundException {
        
        String tipoStr = (String) in.readObject();
        modelo.TipoEjercicio tipo = modelo.TipoEjercicio.fromString(tipoStr);
        List<Ejercicio> ejercicios = controladorEjercicios.buscarPorTipo(tipo);
        System.out.println("🔍 Buscando ejercicios de tipo: " + tipoStr + " - Encontrados: " + ejercicios.size());
        out.writeObject(ejercicios);
        out.flush();
    }
    
    /**
     * Procesa la petición de agregar ejercicio (nuevo)
     */
    private void procesarAgregarEjercicio(ObjectInputStream in, ObjectOutputStream out) 
            throws IOException, ClassNotFoundException {
        
        String nombre = (String) in.readObject();
        String descripcion = (String) in.readObject();
        String tipoStr = (String) in.readObject();
        String videoUrl = (String) in.readObject();
        String imagenUrl = (String) in.readObject();
        
        modelo.TipoEjercicio tipo = modelo.TipoEjercicio.fromString(tipoStr);
        
        System.out.println("➕ Agregando nuevo ejercicio: " + nombre);
        
        try {
            Ejercicio nuevo = controladorEjercicios.agregarEjercicio(nombre, descripcion, tipo, videoUrl, imagenUrl);
            out.writeObject(nuevo);
            System.out.println("✅ Ejercicio agregado: " + nombre);
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Error al agregar ejercicio: " + e.getMessage());
            out.writeObject(null);
        }
        out.flush();
    }
    
    /**
     * Procesa la petición de registrar usuario
     */
    private void procesarRegistro(ObjectInputStream in, ObjectOutputStream out) 
            throws IOException, ClassNotFoundException {
        
        String nombre = (String) in.readObject();
        String email = (String) in.readObject();
        String password = (String) in.readObject();
        boolean esAdmin = (boolean) in.readObject();
        
        System.out.println("📝 Registro de nuevo usuario - Email: " + email);
        
        try {
            Usuario nuevo = controladorUsuarios.registrarUsuario(nombre, email, password, esAdmin);
            if (nuevo != null) {
                nuevo.setPasswordHash(null); // No enviamos contraseña por seguridad
                System.out.println("✅ Usuario registrado exitosamente: " + email);
            } else {
                System.out.println("❌ Error en registro de: " + email);
            }
            out.writeObject(nuevo);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error en registro: " + e.getMessage());
            out.writeObject(null);
        }
        out.flush();
    }
    
    /**
     * Procesa la petición de listar usuarios (solo para admin)
     */
    private void procesarListarUsuarios(ObjectOutputStream out) throws IOException {
        List<Usuario> usuarios = controladorUsuarios.getTodosLosUsuarios();
        // Limpiar contraseñas por seguridad
        for (Usuario u : usuarios) {
            u.setPasswordHash(null);
        }
        System.out.println("📋 Enviando " + usuarios.size() + " usuarios al cliente");
        out.writeObject(usuarios);
        out.flush();
    }
    
    /**
     * Cierra la conexión con el cliente
     */
    private void cerrarConexion() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("🔌 Conexión cerrada con cliente");
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error cerrando conexión: " + e.getMessage());
        }
    }
}