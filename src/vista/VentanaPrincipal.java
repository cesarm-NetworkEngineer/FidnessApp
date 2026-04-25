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
        System.out.println("🔧 VentanaPrincipal: Constructor iniciado");
        System.out.println("   Usuario recibido: " + (usuario != null ? usuario.getEmail() : "null"));
        
        this.usuarioActual = usuario;
        this.controladorEjercicios = new ControladorEjercicios();
        
        System.out.println("🔧 VentanaPrincipal: ControladorEjercicios creado");
        
        initComponents();
        configurarVentana();
        
        System.out.println("✅ VentanaPrincipal: Inicialización completa");
    }
    
    private void configurarVentana() {
        System.out.println("🔧 VentanaPrincipal: Configurando ventana...");
        setTitle("Fidness - " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        System.out.println("✅ VentanaPrincipal: Configuración completada (900x600)");
    }
    
    private void initComponents() {
        System.out.println("🔧 VentanaPrincipal: Creando pestañas...");
        panelPestanas = new JTabbedPane();
        
        System.out.println("   ➕ Agregando pestaña: Ejercicios");
        panelPestanas.addTab("🏋️ Ejercicios", new PanelEjercicios(usuarioActual, controladorEjercicios));
        
        System.out.println("   ➕ Agregando pestaña: Mis Rutinas");
        panelPestanas.addTab("📋 Mis Rutinas", new PanelMisRutinas(usuarioActual));
        
        System.out.println("   ➕ Agregando pestaña: Perfil");
        panelPestanas.addTab("👤 Perfil", new PanelPerfil(usuarioActual));
        
        if (usuarioActual.isEsAdmin()) {
            System.out.println("   ➕ Agregando pestaña: Administrar (solo admin)");
            panelPestanas.addTab("⚙️ Administrar", new PanelAdministrador(controladorEjercicios));
        } else {
            System.out.println("   ℹ️ Usuario normal - sin pestaña de administración");
        }
        
        add(panelPestanas);
        System.out.println("✅ VentanaPrincipal: " + panelPestanas.getTabCount() + " pestañas creadas");
    }
}