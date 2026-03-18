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
 * Usé JTabbedPane porque en mis años de experiencia, las pestañas
 * son la forma más intuitiva de organizar funcionalidades.
 * Como un cuaderno con separadores: cada cosa en su lugar.
 * 
 * Las pestañas son:
 * - 🏋️ Ejercicios: explorar el catálogo
 * - 📋 Mis Rutinas: gestionar mis rutinas
 * - 👤 Perfil: ver y editar mi información
 * - ⚙️ Administrar: solo para admins (gestionar catálogo)
 * 
 * @author César Alonso Morera Alpízar
 */
public class VentanaPrincipal extends JFrame {
    
    private Usuario usuarioActual;
    private ControladorEjercicios controladorEjercicios;
    
    private JTabbedPane panelPestanas;
    
    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        this.controladorEjercicios = new ControladorEjercicios();
        initComponents();
        configurarVentana();
    }
    
    private void configurarVentana() {
        setTitle("Fidness - " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Centrar en pantalla
    }
    
    /**
     * Inicializa las pestañas según el tipo de usuario
     */
    private void initComponents() {
        panelPestanas = new JTabbedPane();
        
        // Panel de ejercicios - para todos los usuarios
        panelPestanas.addTab("🏋️ Ejercicios", new PanelEjercicios(usuarioActual, controladorEjercicios));
        
        // Panel de mis rutinas - para todos los usuarios
        panelPestanas.addTab("📋 Mis Rutinas", new PanelMisRutinas(usuarioActual));
        
        // Panel de perfil - para todos los usuarios
        panelPestanas.addTab("👤 Perfil", new PanelPerfil(usuarioActual));
        
        // Panel de administración - SOLO para admins
        if (usuarioActual.isEsAdmin()) {
            panelPestanas.addTab("⚙️ Administrar", new PanelAdministrador(controladorEjercicios));
        }
        
        add(panelPestanas);
    }
}