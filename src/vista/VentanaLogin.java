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
 * Ventana de inicio de sesion / registro.
 * 
 * Esta es la primera impresion del usuario, por lo que se busca simplicidad y rapidez.
 * Como un buen entrenador personal: clara, firme, amable y siempre dispuesta a guiar.
 * 
 * En mis clases de Hotmart, siempre digo: "la primera impresion es la que queda".
 * Por eso use colores profesionales: fondo gris suave y texto azul oscuro.
 * Los botones tambien siguen la paleta corporativa con texto azul oscuro.
 * 
 * @author Cesar Alonso Morera Alpizar
 */
public class VentanaLogin extends JFrame {
    
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    
    private ControladorUsuarios controlador;
    
    // ===== PALETA DE COLORES CORPORATIVOS =====
    private static final Color COLOR_FONDO = new Color(240, 240, 245);
    private static final Color COLOR_TEXTO = new Color(25, 25, 112);
    private static final Color COLOR_TITULO = new Color(0, 51, 102);
    private static final Color COLOR_BOTON_LOGIN = new Color(220, 220, 230);
    private static final Color COLOR_BOTON_REGISTRO = new Color(200, 200, 210);
    
    public VentanaLogin() {
        System.out.println("VentanaLogin: Constructor iniciado");
        this.controlador = new ControladorUsuarios();
        System.out.println("VentanaLogin: Controlador creado");
        initComponents();
        configurarVentana();
        System.out.println("VentanaLogin: Inicializacion completa");
    }
    
    private void configurarVentana() {
        setTitle("Fidness - Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        System.out.println("VentanaLogin: Configuracion de ventana completada");
    }
    
    private void initComponents() {
        System.out.println("VentanaLogin: Iniciando componentes graficos...");
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(COLOR_FONDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        JLabel lblTitulo = new JLabel("FIDNESS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setBackground(COLOR_FONDO);
        lblTitulo.setOpaque(true);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        JLabel lblSubtitulo = new JLabel("Tu companero de entrenamiento", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSubtitulo.setForeground(COLOR_TEXTO);
        lblSubtitulo.setBackground(COLOR_FONDO);
        lblSubtitulo.setOpaque(true);
        
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);
        
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(15), gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(COLOR_TEXTO);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblEmail, gbc);
        
        txtEmail = new JTextField(20);
        txtEmail.setBackground(Color.WHITE);
        txtEmail.setForeground(COLOR_TEXTO);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setCaretColor(COLOR_TEXTO);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        
        JLabel lblPassword = new JLabel("Contrasena:");
        lblPassword.setForeground(COLOR_TEXTO);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblPassword, gbc);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(COLOR_TEXTO);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setCaretColor(COLOR_TEXTO);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        JLabel lblAyuda = new JLabel("Demo: demo@fidness.com / demo123", SwingConstants.CENTER);
        lblAyuda.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAyuda.setForeground(COLOR_TEXTO);
        lblAyuda.setBackground(COLOR_FONDO);
        lblAyuda.setOpaque(true);
        panel.add(lblAyuda, gbc);
        
        btnLogin = new JButton("INICIAR SESION");
        btnLogin.setBackground(COLOR_BOTON_LOGIN);
        btnLogin.setForeground(COLOR_TITULO);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 8, 5, 8);
        panel.add(btnLogin, gbc);
        
        btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setBackground(COLOR_BOTON_REGISTRO);
        btnRegistrar.setForeground(COLOR_TITULO);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 8, 10, 8);
        panel.add(btnRegistrar, gbc);
        
        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistrar.addActionListener(e -> mostrarRegistro());
        txtPassword.addActionListener(e -> iniciarSesion());
        
        add(panel);
        System.out.println("VentanaLogin: Componentes graficos creados");
    }
    
    private void iniciarSesion() {
        System.out.println("Boton de login presionado");
        
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        System.out.println("Email ingresado: '" + email + "'");
        System.out.println("Password ingresada: '" + password + "'");
        
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Campos vacios");
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Campos vacios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            System.out.println("Llamando a controlador.iniciarSesion()...");
            Usuario usuario = controlador.iniciarSesion(email, password);
            System.out.println("Resultado del controlador: " + (usuario != null ? usuario.getEmail() : "null"));
            
            if (usuario != null) {
                System.out.println("LOGIN EXITOSO para: " + usuario.getEmail());
                System.out.println("Nombre: " + usuario.getNombre());
                System.out.println("Admin: " + usuario.isEsAdmin());
                
                JOptionPane.showMessageDialog(this, 
                    "Bienvenido " + usuario.getNombre() + "!", 
                    "Login exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                System.out.println("Creando VentanaPrincipal...");
                VentanaPrincipal ventana = new VentanaPrincipal(usuario);
                System.out.println("VentanaPrincipal creada");
                
                System.out.println("Haciendo visible...");
                ventana.setVisible(true);
                System.out.println("VentanaPrincipal visible");
                
                System.out.println("Cerrando ventana de login...");
                dispose();
                System.out.println("VentanaLogin cerrada");
                
            } else {
                System.out.println("LOGIN FALLIDO: Credenciales incorrectas");
                JOptionPane.showMessageDialog(this, 
                    "Email o contrasena incorrectos", 
                    "Error de autenticacion", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (Exception ex) {
            System.out.println("EXCEPCION: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al conectar con el sistema: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarRegistro() {
        System.out.println("Boton de registro presionado");
        
        JTextField txtNombre = new JTextField();
        txtNombre.setBackground(Color.WHITE);
        txtNombre.setForeground(COLOR_TEXTO);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JTextField txtEmailReg = new JTextField();
        txtEmailReg.setBackground(Color.WHITE);
        txtEmailReg.setForeground(COLOR_TEXTO);
        txtEmailReg.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPasswordField txtPasswordReg = new JPasswordField();
        txtPasswordReg.setBackground(Color.WHITE);
        txtPasswordReg.setForeground(COLOR_TEXTO);
        txtPasswordReg.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPasswordField txtConfirmar = new JPasswordField();
        txtConfirmar.setBackground(Color.WHITE);
        txtConfirmar.setForeground(COLOR_TEXTO);
        txtConfirmar.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(COLOR_TEXTO);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblNombre);
        panel.add(txtNombre);
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(COLOR_TEXTO);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblEmail);
        panel.add(txtEmailReg);
        
        JLabel lblPassword = new JLabel("Contrasena:");
        lblPassword.setForeground(COLOR_TEXTO);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblPassword);
        panel.add(txtPasswordReg);
        
        JLabel lblConfirmar = new JLabel("Confirmar:");
        lblConfirmar.setForeground(COLOR_TEXTO);
        lblConfirmar.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblConfirmar);
        panel.add(txtConfirmar);
        
        int opcion = JOptionPane.showConfirmDialog(this, panel, 
            "Registro de nuevo usuario", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String email = txtEmailReg.getText().trim();
            String password = new String(txtPasswordReg.getPassword());
            String confirmar = new String(txtConfirmar.getPassword());
            
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Todos los campos son obligatorios", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmar)) {
                JOptionPane.showMessageDialog(this, 
                    "Las contrasenas no coinciden", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, 
                    "La contrasena debe tener al menos 6 caracteres", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Usuario nuevo = controlador.registrarUsuario(nombre, email, password, false);
                
                JOptionPane.showMessageDialog(this, 
                    "Usuario registrado exitosamente.\nYa puede iniciar sesion.", 
                    "Registro completado", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                txtEmail.setText(email);
                txtPassword.setText("");
                
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, 
                    e.getMessage(), 
                    "Error de registro", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}