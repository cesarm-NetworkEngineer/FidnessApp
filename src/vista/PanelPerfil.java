/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Panel de perfil de usuario
 * 
 * Muestra la información del usuario y permite cambiar contraseña.
 * Usé GridBagLayout para alinear los datos de forma prolija.
 * 
 * @author César Alonso Morera Alpízar
 */
public class PanelPerfil extends JPanel {
    
    private Usuario usuario;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblTipo;
    private JLabel lblTotalRutinas;
    private JButton btnCambiarPassword;
    
    public PanelPerfil(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Foto de perfil (placeholder con emoji)
        JLabel lblFoto = new JLabel("👤");
        lblFoto.setFont(new Font("Arial", Font.PLAIN, 64));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.insets = new Insets(5, 5, 5, 20);
        add(lblFoto, gbc);
        
        // Etiquetas de los campos
        gbc.gridheight = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        
        gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);
        gbc.gridy = 2;
        add(new JLabel("Tipo:"), gbc);
        gbc.gridy = 3;
        add(new JLabel("Rutinas:"), gbc);
        
        // Valores de los campos
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        
        gbc.gridy = 0;
        lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblNombre, gbc);
        
        gbc.gridy = 1;
        lblEmail = new JLabel(usuario.getEmail());
        add(lblEmail, gbc);
        
        gbc.gridy = 2;
        lblTipo = new JLabel(usuario.isEsAdmin() ? "Administrador 👑" : "Usuario regular");
        add(lblTipo, gbc);
        
        gbc.gridy = 3;
        lblTotalRutinas = new JLabel(String.valueOf(usuario.getRutinas().size()));
        add(lblTotalRutinas, gbc);
        
        // Botón cambiar contraseña
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        btnCambiarPassword = new JButton("🔐 Cambiar contraseña");
        btnCambiarPassword.addActionListener(e -> cambiarPassword());
        add(btnCambiarPassword, gbc);
    }
    
    /**
     * Diálogo para cambiar la contraseña
     * Pide contraseña actual, nueva y confirmación
     */
    private void cambiarPassword() {
        JPasswordField txtActual = new JPasswordField();
        JPasswordField txtNueva = new JPasswordField();
        JPasswordField txtConfirmar = new JPasswordField();
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Contraseña actual:"));
        panel.add(txtActual);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(txtNueva);
        panel.add(new JLabel("Confirmar:"));
        panel.add(txtConfirmar);
        
        int opcion = JOptionPane.showConfirmDialog(this, panel,
            "Cambiar contraseña", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String actual = new String(txtActual.getPassword());
            String nueva = new String(txtNueva.getPassword());
            String confirmar = new String(txtConfirmar.getPassword());
            
            // Validaciones
            if (!usuario.verificarPassword(actual)) {
                JOptionPane.showMessageDialog(this,
                    "La contraseña actual es incorrecta",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (nueva.length() < 6) {
                JOptionPane.showMessageDialog(this,
                    "La nueva contraseña debe tener al menos 6 caracteres",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!nueva.equals(confirmar)) {
                JOptionPane.showMessageDialog(this,
                    "Las contraseñas no coinciden",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            usuario.setPasswordHash(nueva);
            JOptionPane.showMessageDialog(this,
                "✅ Contraseña actualizada correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}