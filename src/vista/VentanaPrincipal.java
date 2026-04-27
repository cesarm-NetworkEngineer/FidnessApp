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
 * Ventana principal después del login.
 * 
 * Use JTabbedPane porque en mis años de experiencia, las pestañas
 * son la forma más intuitiva de organizar funcionalidades.
 * Como un cuaderno con separadores: cada cosa en su lugar.
 * 
 * Las pestañas son:
 * - Ejercicios: explorar el catálogo
 * - Mis Rutinas: gestionar mis rutinas
 * - Perfil: ver y editar mi información
 * - Administrar: solo para admins (gestionar catálogo)
 * 
 * En DXC Technology aprendí que las interfaces con pestañas reducen
 * la curva de aprendizaje del usuario. No hay botones escondidos,
 * todo está a la vista. Como cuando organizas herramientas en un taller:
 * cada una en su lugar, fácil de encontrar.
 * 
 * @author César Alonso Morera Alpízar
 */
public class VentanaPrincipal extends JFrame {
    
    private Usuario usuarioActual;
    private ControladorEjercicios controladorEjercicios;
    private JTabbedPane panelPestanas;
    
    // Colores del tema
    private static final Color COLOR_FONDO = new Color(240, 240, 245);
    private static final Color COLOR_TEXTO = new Color(25, 25, 112);
    
    /**
     * Constructor de la ventana principal
     * 
     * @param usuario usuario que ha iniciado sesión (puede ser admin o normal)
     */
    public VentanaPrincipal(Usuario usuario) {
        System.out.println("VentanaPrincipal: Constructor iniciado");
        System.out.println("Usuario recibido: " + (usuario != null ? usuario.getEmail() : "null"));
        
        this.usuarioActual = usuario;
        this.controladorEjercicios = new ControladorEjercicios();
        
        System.out.println("VentanaPrincipal: ControladorEjercicios creado");
        
        initComponents();
        configurarVentana();
        
        System.out.println("VentanaPrincipal: Inicialización completa");
    }
    
    /**
     * Configura las propiedades básicas de la ventana
     * Tamaño, título, posición centrada, etc.
     */
    private void configurarVentana() {
        System.out.println("VentanaPrincipal: Configurando ventana...");
        setTitle("Fidness App - " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        
        // Configurar icono si existe
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("No se encontró el icono de la aplicación");
        }
        
        System.out.println("VentanaPrincipal: Configuración completada (1000x700)");
    }
    
    /**
     * Inicializa las pestañas según el tipo de usuario
     * 
     * Si el usuario es administrador, se agrega una cuarta pestaña
     * llamada "Administrar". Esto permite gestionar el catálogo de ejercicios.
     * 
     * En los sistemas que desarrollé en Infinite Computer Solutions,
     * aprendí que la interfaz debe adaptarse al rol del usuario.
     * Un usuario normal no necesita ver opciones de administración.
     */
    private void initComponents() {
        System.out.println("VentanaPrincipal: Creando pestañas...");
        
        // Panel principal con estilo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_FONDO);
        
        panelPestanas = new JTabbedPane();
        panelPestanas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // ===== PANEL DE EJERCICIOS =====
        System.out.println("Creando panel de ejercicios...");
        PanelEjercicios panelEjercicios = new PanelEjercicios(usuarioActual, controladorEjercicios);
        
        // ===== PANEL DE MIS RUTINAS =====
        System.out.println("Creando panel de mis rutinas...");
        PanelMisRutinas panelMisRutinas = new PanelMisRutinas(usuarioActual, controladorEjercicios);
        
        // ===== AGREGAR PESTAÑAS BÁSICAS =====
        System.out.println("Agregando pestaña: Ejercicios");
        panelPestanas.addTab("🏋️ Ejercicios", panelEjercicios);
        
        System.out.println("Agregando pestaña: Mis Rutinas");
        panelPestanas.addTab("📋 Mis Rutinas", panelMisRutinas);
        
        System.out.println("Agregando pestaña: Perfil");
        panelPestanas.addTab("👤 Perfil", new PanelPerfil(usuarioActual));
        
        // ===== PESTAÑA DE ADMINISTRACIÓN (SOLO PARA ADMINS) =====
        if (usuarioActual.isEsAdmin()) {
            System.out.println("Agregando pestaña: Administrar (solo admin)");
            PanelAdministrador panelAdmin = new PanelAdministrador(controladorEjercicios, panelEjercicios);
            panelPestanas.addTab("⚙️ Administrar", panelAdmin);
        } else {
            System.out.println("Usuario normal - sin pestaña de administración");
        }
        
        mainPanel.add(panelPestanas, BorderLayout.CENTER);
        
        // Barra de estado en la parte inferior
        JLabel lblEstado = new JLabel(" Conectado como: " + usuarioActual.getNombre() + 
                                      " (" + (usuarioActual.isEsAdmin() ? "Administrador" : "Usuario") + ")");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(100, 100, 100));
        lblEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblEstado.setBackground(new Color(230, 230, 240));
        lblEstado.setOpaque(true);
        mainPanel.add(lblEstado, BorderLayout.SOUTH);
        
        add(mainPanel);
        System.out.println("VentanaPrincipal: " + panelPestanas.getTabCount() + " pestañas creadas");
    }
}