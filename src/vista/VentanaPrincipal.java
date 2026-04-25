/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Usuario;
import controlador.ControladorEjercicios;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal despues del login.
 * 
 * Use JTabbedPane porque en mis anos de experiencia, las pestanas
 * son la forma mas intuitiva de organizar funcionalidades.
 * Como un cuaderno con separadores: cada cosa en su lugar.
 * 
 * Las pestanas son:
 * - Ejercicios: explorar el catalogo
 * - Mis Rutinas: gestionar mis rutinas
 * - Perfil: ver y editar mi informacion
 * - Administrar: solo para admins (gestionar catalogo)
 * 
 * En DXC Technology aprendi que las interfaces con pestanas reducen
 * la curva de aprendizaje del usuario. No hay botones escondidos,
 * todo esta a la vista. Como cuando organizas herramientas en un taller:
 * cada una en su lugar, facil de encontrar.
 * 
 * @author Cesar Alonso Morera Alpizar
 */
public class VentanaPrincipal extends JFrame {
    
    private Usuario usuarioActual;
    private ControladorEjercicios controladorEjercicios;
    private JTabbedPane panelPestanas;
    
    /**
     * Constructor de la ventana principal
     * 
     * @param usuario usuario que ha iniciado sesion (puede ser admin o normal)
     */
    public VentanaPrincipal(Usuario usuario) {
        System.out.println("VentanaPrincipal: Constructor iniciado");
        System.out.println("Usuario recibido: " + (usuario != null ? usuario.getEmail() : "null"));
        
        this.usuarioActual = usuario;
        this.controladorEjercicios = new ControladorEjercicios();
        
        System.out.println("VentanaPrincipal: ControladorEjercicios creado");
        
        initComponents();
        configurarVentana();
        
        System.out.println("VentanaPrincipal: Inicializacion completa");
    }
    
    /**
     * Configura las propiedades basicas de la ventana
     * Tamaño, titulo, posicion centrada, etc.
     */
    private void configurarVentana() {
        System.out.println("VentanaPrincipal: Configurando ventana...");
        setTitle("Fidness - " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Centrar en pantalla
        System.out.println("VentanaPrincipal: Configuracion completada (900x600)");
    }
    
    /**
     * Inicializa las pestanas segun el tipo de usuario
     * 
     * Si el usuario es administrador, se agrega una cuarta pestana
     * llamada "Administrar". Esto permite gestionar el catalogo de ejercicios.
     * 
     * En los sistemas que desarrolle en Infinite Computer Solutions,
     * aprendi que la interfaz debe adaptarse al rol del usuario.
     * Un usuario normal no necesita ver opciones de administracion.
     */
    private void initComponents() {
        System.out.println("VentanaPrincipal: Creando pestanas...");
        panelPestanas = new JTabbedPane();
        
        // ===== PANEL DE EJERCICIOS =====
        // Este panel muestra el catalogo completo de ejercicios
        // Es visible para todos los usuarios
        System.out.println("Creando panel de ejercicios...");
        PanelEjercicios panelEjercicios = new PanelEjercicios(usuarioActual, controladorEjercicios);
        
        // ===== PANEL DE MIS RUTINAS =====
        // Este panel muestra las rutinas creadas por el usuario
        // Es visible para todos los usuarios
        System.out.println("Creando panel de mis rutinas...");
        PanelMisRutinas panelMisRutinas = new PanelMisRutinas(usuarioActual, controladorEjercicios);
        
        // ===== AGREGAR PESTANAS BASICAS =====
        System.out.println("Agregando pestana: Ejercicios");
        panelPestanas.addTab("Ejercicios", panelEjercicios);
        
        System.out.println("Agregando pestana: Mis Rutinas");
        panelPestanas.addTab("Mis Rutinas", panelMisRutinas);
        
        System.out.println("Agregando pestana: Perfil");
        panelPestanas.addTab("Perfil", new PanelPerfil(usuarioActual));
        
        // ===== PESTANA DE ADMINISTRACION (SOLO PARA ADMINS) =====
        // En mis cursos de Linux, siempre hay un usuario root con privilegios especiales.
        // Exactamente lo mismo aplico aca: solo los administradores pueden gestionar el catalogo.
        if (usuarioActual.isEsAdmin()) {
            System.out.println("Agregando pestana: Administrar (solo admin)");
            PanelAdministrador panelAdmin = new PanelAdministrador(controladorEjercicios, panelEjercicios);
            panelPestanas.addTab("Administrar", panelAdmin);
        } else {
            System.out.println("Usuario normal - sin pestana de administracion");
        }
        
        add(panelPestanas);
        System.out.println("VentanaPrincipal: " + panelPestanas.getTabCount() + " pestanas creadas");
    }
}