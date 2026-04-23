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
 * Ventana de inicio de sesión / registro.
 * 
 * Esta es la primera impresión del usuario, por lo que se busca simplicidad y rapidez.
 * Como un buen entrenador personal: clara, firme, amable y siempre dispuesta a guiar.
 * 
 * En mis clases de Hotmart, siempre digo: "la primera impresión es la que queda".
 * Por eso usé colores profesionales: fondo gris suave y texto azul oscuro.
 * Los botones también siguen la paleta corporativa con texto azul oscuro.
 * 
 * @author César Alonso Morera Alpízar
 */
public class VentanaLogin extends JFrame {
    
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    
    private ControladorUsuarios controlador;
    
    // ===== PALETA DE COLORES CORPORATIVOS =====
    private static final Color COLOR_FONDO = new Color(240, 240, 245);      // Gris muy suave (fondo)
    private static final Color COLOR_TEXTO = new Color(25, 25, 112);         // Azul medianoche (texto normal)
    private static final Color COLOR_TITULO = new Color(0, 51, 102);         // Azul marino (títulos y botones)
    private static final Color COLOR_BOTON_LOGIN = new Color(220, 220, 230);   // Gris claro (fondo botón login)
    private static final Color COLOR_BOTON_REGISTRO = new Color(200, 200, 210); // Gris medio (fondo botón registro)
    
    public VentanaLogin() {
        this.controlador = new ControladorUsuarios();
        initComponents();
        configurarVentana();
    }
    
    private void configurarVentana() {
        setTitle("Fidness - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Inicializa todos los componentes gráficos
     * Uso GridBagLayout porque me da control preciso de dónde va cada cosa
     */
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(COLOR_FONDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // ===== TÍTULO PRINCIPAL (Azul oscuro) =====
        JLabel lblTitulo = new JLabel("🏋️‍♂️ FIDNESS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setBackground(COLOR_FONDO);
        lblTitulo.setOpaque(true);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        // ===== SUBTÍTULO (Azul medianoche) =====
        JLabel lblSubtitulo = new JLabel("Tu compañero de entrenamiento", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSubtitulo.setForeground(COLOR_TEXTO);
        lblSubtitulo.setBackground(COLOR_FONDO);
        lblSubtitulo.setOpaque(true);
        
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);
        
        // Espaciador
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(15), gbc);
        
        // ===== CAMPO EMAIL =====
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        
        JLabel lblEmail = new JLabel("📧 Email:");
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
        
        // ===== CAMPO CONTRASEÑA =====
        gbc.gridy = 4;
        gbc.gridx = 0;
        
        JLabel lblPassword = new JLabel("🔒 Contraseña:");
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
        
        // ===== MENSAJE DE AYUDA =====
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        JLabel lblAyuda = new JLabel("💡 Demo: demo@fidness.com / demo123", SwingConstants.CENTER);
        lblAyuda.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAyuda.setForeground(COLOR_TEXTO);
        lblAyuda.setBackground(COLOR_FONDO);
        lblAyuda.setOpaque(true);
        panel.add(lblAyuda, gbc);
        
        // ===== BOTÓN INICIAR SESIÓN (Fondo gris claro, texto azul oscuro) =====
        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setBackground(COLOR_BOTON_LOGIN);      // Fondo gris claro
        btnLogin.setForeground(COLOR_TITULO);          // Texto azul oscuro
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 8, 5, 8);
        panel.add(btnLogin, gbc);
        
        // ===== BOTÓN REGISTRARSE (Fondo gris medio, texto azul oscuro) =====
        btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setBackground(COLOR_BOTON_REGISTRO); // Fondo gris medio
        btnRegistrar.setForeground(COLOR_TITULO);         // Texto azul oscuro
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 8, 10, 8);
        panel.add(btnRegistrar, gbc);
        
        // ===== EVENTOS =====
        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistrar.addActionListener(e -> mostrarRegistro());
        txtPassword.addActionListener(e -> iniciarSesion());
        
        add(panel);
    }
    
    /**
     * Intenta iniciar sesión con las credenciales ingresadas
     */
    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Campos vacíos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Usuario usuario = controlador.iniciarSesion(email, password);
            
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, 
                    "¡Bienvenido " + usuario.getNombre() + "!", 
                    "Login exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                new VentanaPrincipal(usuario).setVisible(true);
                dispose();
                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Email o contraseña incorrectos", 
                    "Error de autenticación", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al conectar con el sistema: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra diálogo de registro con los mismos colores corporativos
     */
    private void mostrarRegistro() {
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
        
        JLabel lblPassword = new JLabel("Contraseña:");
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
            "📝 Registro de nuevo usuario", 
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
                    "Las contraseñas no coinciden", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, 
                    "La contraseña debe tener al menos 6 caracteres", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Usuario nuevo = controlador.registrarUsuario(nombre, email, password, false);
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Usuario registrado exitosamente.\nYa puede iniciar sesión.", 
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