/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControladorUsuarios;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana de inicio de sesión de FidnessApp
 * 
 * Diseño profesional con colores sobrios y elegantes.
 * - Texto del botón: Azul oscuro (#191970)
 * - Fondo del botón: Gris claro (#F0F0F0)
 * - Borde del botón: Azul oscuro sutil
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Abril 2026
 */
public class VentanaLogin extends JFrame {
    
    private ControladorUsuarios controlador;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    
    // 🎨 Colores profesionales y sobrios
    private static final Color COLOR_FONDO = new Color(245, 245, 250);     // Gris muy claro
    private static final Color COLOR_TEXTO = new Color(50, 50, 70);        // Gris oscuro
    private static final Color COLOR_TITULO = new Color(25, 25, 112);      // Azul oscuro para título
    private static final Color COLOR_BOTON_TEXTO = new Color(25, 25, 112); // 🔵 Azul oscuro para texto del botón
    private static final Color COLOR_BOTON_FONDO = new Color(240, 240, 245); // Gris claro para fondo del botón
    private static final Color COLOR_BOTON_BORDE = new Color(25, 25, 112);   // Borde azul oscuro
    private static final Color COLOR_BOTON_HOVER_FONDO = new Color(230, 230, 240); // Gris más oscuro al pasar mouse
    private static final Color COLOR_ENLACE = new Color(25, 25, 112);       // Azul oscuro para enlaces
    
    public VentanaLogin() {
        System.out.println("VentanaLogin: Constructor iniciado");
        controlador = new ControladorUsuarios();
        System.out.println("VentanaLogin: Controlador creado");
        initComponents();
        System.out.println("VentanaLogin: Inicializacion completa");
    }
    
    private void initComponents() {
        setTitle("FidnessApp - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal con borde
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BOTON_BORDE, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Logo / Título - Azul oscuro
        JLabel lblTitulo = new JLabel("FIDNESS APP", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TITULO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 10, 8);
        mainPanel.add(lblTitulo, gbc);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Tu compañero de entrenamiento", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(COLOR_TEXTO);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 8, 25, 8);
        mainPanel.add(lblSubtitulo, gbc);
        
        // Resetear gridwidth
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Email
        gbc.gridy = 2;
        JLabel lblEmail = new JLabel("Correo electrónico");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEmail.setForeground(COLOR_TEXTO);
        mainPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        txtEmail = new JTextField(18);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        mainPanel.add(txtEmail, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPassword.setForeground(COLOR_TEXTO);
        mainPanel.add(lblPassword, gbc);
        
        gbc.gridx = 1;
        txtPassword = new JPasswordField(18);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        mainPanel.add(txtPassword, gbc);
        
        // Botón Login - Texto azul oscuro, fondo gris, borde azul oscuro
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 5, 5);
        
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(COLOR_BOTON_TEXTO);      // 🔵 Texto AZUL OSCURO
        btnLogin.setBackground(COLOR_BOTON_FONDO);      // Fondo GRIS CLARO
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BOTON_BORDE, 2),  // Borde azul oscuro
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover (cambia fondo a gris un poco más oscuro)
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(COLOR_BOTON_HOVER_FONDO);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(COLOR_BOTON_FONDO);
            }
        });
        
        mainPanel.add(btnLogin, gbc);
        
        // Panel de registro
        gbc.gridy = 5;
        gbc.insets = new Insets(15, 5, 5, 5);
        
        JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelRegistro.setBackground(COLOR_FONDO);
        panelRegistro.setOpaque(true);
        
        JLabel lblNoCuenta = new JLabel("¿No tienes cuenta?");
        lblNoCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNoCuenta.setForeground(COLOR_TEXTO);
        
        btnRegistrar = new JButton("Regístrate aquí");
        btnRegistrar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnRegistrar.setForeground(COLOR_ENLACE);  // Azul oscuro
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setContentAreaFilled(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelRegistro.add(lblNoCuenta);
        panelRegistro.add(btnRegistrar);
        
        mainPanel.add(panelRegistro, gbc);
        
        add(mainPanel);
        
        // Eventos
        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistrar.addActionListener(e -> abrirRegistro());
        txtPassword.addActionListener(e -> iniciarSesion());
    }
    
    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese email y contraseña", 
                "Campos vacíos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        System.out.println("Intentando login para: " + email);
        Usuario usuario = controlador.iniciarSesion(email, password);
        
        if (usuario != null) {
            System.out.println("Login exitoso: " + usuario.getNombre());
            
            // Abrir ventana principal
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(usuario);
            ventanaPrincipal.setVisible(true);
            
            // Cerrar ventana de login
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Email o contraseña incorrectos", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
    
    private void abrirRegistro() {
        JOptionPane.showMessageDialog(this, 
            "Funcionalidad de registro en desarrollo", 
            "Próximamente", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}