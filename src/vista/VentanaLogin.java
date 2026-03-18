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
 * Por eso usé colores suaves (gris clarito) y el título grande en azul.
 * El usuario tiene que sentir que entra a un lugar amigable, no a una interfaz fría.
 * 
 * @author César Alonso Morera Alpízar
 */
public class VentanaLogin extends JFrame {
    
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    
    private ControladorUsuarios controlador;
    
    public VentanaLogin() {
        this.controlador = new ControladorUsuarios();
        initComponents();
        configurarVentana();
    }
    
    private void configurarVentana() {
        setTitle("Fidness - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false); // No queremos que el usuario desacomode el diseño
    }
    
    /**
     * Inicializa todos los componentes gráficos
     * Uso GridBagLayout porque me da control preciso de dónde va cada cosa
     * Como cuando organizo los ejercicios en una rutina: todo en su lugar
     */
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(245, 245, 245)); // Gris clarito, no blanco quirúrgico
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8); // Espaciado entre componentes
        
        // Título grande - que se vea desde lejos
        JLabel lblTitulo = new JLabel("🏋️‍♂️ FIDNESS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(70, 130, 180)); // Azul profesional
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        // Subtítulo - más pequeño pero importante
        JLabel lblSubtitulo = new JLabel("Tu compañero de entrenamiento", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSubtitulo.setForeground(Color.GRAY);
        
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);
        
        // Espaciador
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(10), gbc);
        
        // Campo de email
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("📧 Email:"), gbc);
        
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        
        // Campo de contraseña
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("🔒 Contraseña:"), gbc);
        
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        
        // Mensaje de ayuda con credenciales de prueba
        // En mis cursos, siempre dejo datos de prueba para facilitar
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel lblAyuda = new JLabel("💡 Demo: demo@fidness.com / demo123", SwingConstants.CENTER);
        lblAyuda.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAyuda.setForeground(new Color(100, 100, 100));
        panel.add(lblAyuda, gbc);
        
        // Botón de inicio de sesión - verde para indicar "acción positiva"
        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setBackground(new Color(60, 179, 113)); // Verde
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false); // Quita el borde feo de foco
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Manito al pasar
        
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 5, 8);
        panel.add(btnLogin, gbc);
        
        // Botón de registro - azul para diferenciar
        btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setBackground(new Color(70, 130, 180)); // Azul
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 8, 10, 8);
        panel.add(btnRegistrar, gbc);
        
        // Eventos
        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistrar.addActionListener(e -> mostrarRegistro());
        
        // Permitir login con Enter (detalle de usabilidad)
        txtPassword.addActionListener(e -> iniciarSesion());
        
        add(panel);
    }
    
    /**
     * Intenta iniciar sesión con las credenciales ingresadas
     */
    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        // Validaciones básicas - el usuario a veces deja campos vacíos
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
                // Login exitoso - mensaje de bienvenida personalizado
                JOptionPane.showMessageDialog(this, 
                    "¡Bienvenido " + usuario.getNombre() + "!", 
                    "Login exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Abrir ventana principal y cerrar login
                new VentanaPrincipal(usuario).setVisible(true);
                dispose(); // Cierra esta ventana
                
            } else {
                // Login fallido - no especificamos si es email o password por seguridad
                JOptionPane.showMessageDialog(this, 
                    "Email o contraseña incorrectos", 
                    "Error de autenticación", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText(""); // Limpiar password por seguridad
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
     * Muestra diálogo de registro
     * Uso un JPanel personalizado en lugar de mostrar campos sueltos
     * Así se ve más profesional
     */
    private void mostrarRegistro() {
        // Crear campos personalizados
        JTextField txtNombre = new JTextField();
        JTextField txtEmailReg = new JTextField();
        JPasswordField txtPasswordReg = new JPasswordField();
        JPasswordField txtConfirmar = new JPasswordField();
        
        // Panel con layout para el diálogo
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmailReg);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPasswordReg);
        panel.add(new JLabel("Confirmar:"));
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
            
            // Validaciones
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
                
                // Auto-completar el email en el login (detalle que gusta)
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