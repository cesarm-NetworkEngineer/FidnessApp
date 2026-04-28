/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import controlador.ControladorEjercicios;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel de Administración del Catálogo de Ejercicios
 * Permite gestionar (Crear, Leer, Actualizar, Eliminar) los ejercicios del sistema
 * 
 * @author cesarmorera
 * @version 1.0
 * @since 2026
 */
public class PanelAdministrador extends JPanel {
    
    private ControladorEjercicios controlador;
    private JTable tablaEjercicios;
    private DefaultTableModel modeloTabla;
    private PanelEjercicios panelEjercicios;
    
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    
    // 🎨 Colores profesionales con ALTO CONTRASTE
    private static final Color COLOR_FONDO = new Color(245, 245, 250);
    private static final Color COLOR_TEXTO = new Color(0, 0, 0);
    private static final Color COLOR_TABLA_FONDO = Color.WHITE;
    private static final Color COLOR_ENCABEZADO = new Color(220, 220, 230);
    
    // 🔧 CORRECCIÓN DE BOTONES: Colores oscuros para que el texto BLANCO sea visible
    private static final Color COLOR_BOTON_PRIMARY = new Color(25, 118, 210);    // Azul oscuro
    private static final Color COLOR_BOTON_EDIT = new Color(56, 56, 96);         // Gris oscuro
    private static final Color COLOR_BOTON_DELETE = new Color(211, 47, 47);      // Rojo oscuro
    private static final Color COLOR_BOTON_REFRESH = new Color(46, 125, 50);     // Verde oscuro
    
    /**
     * Constructor del panel administrador
     * @param controlador Controlador que gestiona los ejercicios
     * @param panelEjercicios Panel de ejercicios para actualizar vista
     * @author cesarmorera
     */
    public PanelAdministrador(ControladorEjercicios controlador, PanelEjercicios panelEjercicios) {
        this.controlador = controlador;
        this.panelEjercicios = panelEjercicios;
        initComponents();
        cargarEjercicios();
    }
    
    /**
     * Inicializa todos los componentes visuales del panel
     * @author cesarmorera
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(COLOR_FONDO);
        
        JLabel lblTitulo = new JLabel("Administración del Catálogo de Ejercicios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_TEXTO);
        add(lblTitulo, BorderLayout.NORTH);
        
        String[] columnas = {"ID", "Nombre", "Tipo", "Descripción", "Video URL", "Imagen URL"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEjercicios = new JTable(modeloTabla);
        tablaEjercicios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEjercicios.setForeground(Color.BLACK);
        tablaEjercicios.setBackground(Color.WHITE);
        tablaEjercicios.setSelectionForeground(Color.WHITE);
        tablaEjercicios.setSelectionBackground(new Color(25, 118, 210));
        tablaEjercicios.setRowHeight(28);
        
        JTableHeader header = tablaEjercicios.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setForeground(Color.BLACK);
        header.setBackground(COLOR_ENCABEZADO);
        
        tablaEjercicios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaEjercicios.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaEjercicios.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaEjercicios.getColumnModel().getColumn(3).setPreferredWidth(350);
        
        add(new JScrollPane(tablaEjercicios), BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(COLOR_FONDO);
        
        btnAgregar = crearBoton("➕ Agregar", COLOR_BOTON_PRIMARY, Color.WHITE);
        btnEditar = crearBoton("✏️ Editar", COLOR_BOTON_EDIT, Color.WHITE);
        btnEliminar = crearBoton("🗑️ Eliminar", COLOR_BOTON_DELETE, Color.WHITE);
        btnActualizar = crearBoton("🔄 Actualizar", COLOR_BOTON_REFRESH, Color.WHITE);
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        btnAgregar.addActionListener(e -> agregarEjercicio());
        btnEditar.addActionListener(e -> editarEjercicio());
        btnEliminar.addActionListener(e -> eliminarEjercicio());
        btnActualizar.addActionListener(e -> cargarEjercicios());
    }
    
    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(textoColor);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        return boton;
    }
    
    private void cargarEjercicios() {
        modeloTabla.setRowCount(0);
        for (Ejercicio e : controlador.getTodosLosEjercicios()) {
            modeloTabla.addRow(new Object[]{
                e.getId(), e.getNombre(), e.getTipo().getNombreLegible(),
                e.getDescripcion(),
                e.getVideoUrl() != null ? e.getVideoUrl() : "",
                e.getImagenUrl() != null ? e.getImagenUrl() : ""
            });
        }
    }
    
    private void agregarEjercicio() {
        JTextField txtNombre = new JTextField(20);
        JTextArea txtDescripcion = new JTextArea(3, 20);
        JComboBox<String> cmbTipo = new JComboBox<>(TipoEjercicio.getNombres());
        JTextField txtVideo = new JTextField(20);
        JTextField txtImagen = new JTextField(20);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; panel.add(txtNombre, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(txtDescripcion), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; panel.add(cmbTipo, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Video URL:"), gbc);
        gbc.gridx = 1; panel.add(txtVideo, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Imagen URL:"), gbc);
        gbc.gridx = 1; panel.add(txtImagen, gbc);
        
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Agregar Ejercicio", 
                JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                controlador.agregarEjercicio(txtNombre.getText(), txtDescripcion.getText(),
                    TipoEjercicio.fromString((String) cmbTipo.getSelectedItem()),
                    txtVideo.getText(), txtImagen.getText());
                cargarEjercicios();
                
                // ✅ Refrescar el panel de ejercicios para que los usuarios vean el nuevo ejercicio
                if (panelEjercicios != null) {
                    panelEjercicios.refrescar();
                }
                
                JOptionPane.showMessageDialog(this, "Ejercicio agregado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarEjercicio() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Ejercicio e = controlador.buscarPorId(id);
        if (e == null) return;
        
        JTextField txtNombre = new JTextField(e.getNombre(), 20);
        JTextArea txtDescripcion = new JTextArea(e.getDescripcion(), 3, 20);
        JComboBox<String> cmbTipo = new JComboBox<>(TipoEjercicio.getNombres());
        cmbTipo.setSelectedItem(e.getTipo().getNombreLegible());
        JTextField txtVideo = new JTextField(e.getVideoUrl() != null ? e.getVideoUrl() : "", 20);
        JTextField txtImagen = new JTextField(e.getImagenUrl() != null ? e.getImagenUrl() : "", 20);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; panel.add(txtNombre, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(txtDescripcion), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; panel.add(cmbTipo, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Video URL:"), gbc);
        gbc.gridx = 1; panel.add(txtVideo, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Imagen URL:"), gbc);
        gbc.gridx = 1; panel.add(txtImagen, gbc);
        
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Editar Ejercicio", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                controlador.actualizarEjercicio(id, txtNombre.getText(), txtDescripcion.getText(),
                    TipoEjercicio.fromString((String) cmbTipo.getSelectedItem()),
                    txtVideo.getText(), txtImagen.getText());
                cargarEjercicios();
                
                // ✅ Refrescar el panel de ejercicios después de editar
                if (panelEjercicios != null) {
                    panelEjercicios.refrescar();
                }
                
                JOptionPane.showMessageDialog(this, "Ejercicio actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarEjercicio() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombre + "'?", "Confirmar", 
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controlador.eliminarEjercicio(id);
            cargarEjercicios();
            
            // ✅ Refrescar el panel de ejercicios después de eliminar
            if (panelEjercicios != null) {
                panelEjercicios.refrescar();
            }
            
            JOptionPane.showMessageDialog(this, "Ejercicio eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void refrescar() {
        cargarEjercicios();
    }
}