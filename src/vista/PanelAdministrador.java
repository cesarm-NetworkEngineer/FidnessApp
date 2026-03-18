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
import java.awt.*;

/**
 * Panel de administración (HU6 - Gestionar catálogo)
 * 
 * Solo visible para usuarios con esAdmin = true.
 * Permite agregar, editar y eliminar ejercicios del catálogo.
 * 
 * En mis años de experiencia, aprendí que los paneles de admin
 * deben ser potentes pero claros. Por eso uso una tabla con
 * todas las columnas y botones específicos.
 * 
 * @author César Alonso Morera Alpízar
 */
public class PanelAdministrador extends JPanel {
    
    private ControladorEjercicios controlador;
    private JTable tablaEjercicios;
    private DefaultTableModel modeloTabla;
    
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    
    public PanelAdministrador(ControladorEjercicios controlador) {
        this.controlador = controlador;
        initComponents();
        cargarEjercicios();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título
        JLabel lblTitulo = new JLabel("⚙️ Administración del Catálogo de Ejercicios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);
        
        // Tabla de ejercicios - mostramos toda la información
        String[] columnas = {"ID", "Nombre", "Tipo", "Descripción", "Video URL", "Imagen URL"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        
        tablaEjercicios = new JTable(modeloTabla);
        tablaEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaEjercicios), BorderLayout.CENTER);
        
        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnAgregar = new JButton("➕ Agregar Ejercicio");
        btnEditar = new JButton("✏️ Editar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnActualizar = new JButton("🔄 Actualizar Lista");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregar.addActionListener(e -> agregarEjercicio());
        btnEditar.addActionListener(e -> editarEjercicio());
        btnEliminar.addActionListener(e -> eliminarEjercicio());
        btnActualizar.addActionListener(e -> cargarEjercicios());
    }
    
    private void cargarEjercicios() {
        modeloTabla.setRowCount(0);
        for (Ejercicio e : controlador.getTodosLosEjercicios()) {
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getNombre(),
                e.getTipo(),
                e.getDescripcion(),
                e.getVideoURL() != null ? e.getVideoURL() : "",
                e.getImagenURL() != null ? e.getImagenURL() : ""
            });
        }
    }
    
    /**
     * Diálogo para agregar un nuevo ejercicio
     */
    private void agregarEjercicio() {
        JTextField txtNombre = new JTextField();
        JTextArea txtDescripcion = new JTextArea(3, 20);
        JComboBox<String> cmbTipo = new JComboBox<>(TipoEjercicio.getNombres());
        JTextField txtVideo = new JTextField();
        JTextField txtImagen = new JTextField();
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(txtDescripcion), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Video URL:"), gbc);
        gbc.gridx = 1;
        panel.add(txtVideo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Imagen URL:"), gbc);
        gbc.gridx = 1;
        panel.add(txtImagen, gbc);
        
        int opcion = JOptionPane.showConfirmDialog(this, panel,
            "➕ Agregar Nuevo Ejercicio", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                TipoEjercicio tipo = TipoEjercicio.fromString((String) cmbTipo.getSelectedItem());
                controlador.agregarEjercicio(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    tipo,
                    txtVideo.getText(),
                    txtImagen.getText()
                );
                cargarEjercicios();
                JOptionPane.showMessageDialog(this, 
                    "✅ Ejercicio agregado correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Diálogo para editar un ejercicio existente
     */
    private void editarEjercicio() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un ejercicio para editar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Ejercicio e = controlador.buscarPorId(id);
        
        if (e == null) return;
        
        JTextField txtNombre = new JTextField(e.getNombre());
        JTextArea txtDescripcion = new JTextArea(e.getDescripcion(), 3, 20);
        JComboBox<String> cmbTipo = new JComboBox<>(TipoEjercicio.getNombres());
        cmbTipo.setSelectedItem(e.getTipo().toString().charAt(0) + 
            e.getTipo().toString().substring(1).toLowerCase());
        
        JTextField txtVideo = new JTextField(e.getVideoURL());
        JTextField txtImagen = new JTextField(e.getImagenURL());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(txtDescripcion), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Video URL:"), gbc);
        gbc.gridx = 1;
        panel.add(txtVideo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Imagen URL:"), gbc);
        gbc.gridx = 1;
        panel.add(txtImagen, gbc);
        
        int opcion = JOptionPane.showConfirmDialog(this, panel,
            "✏️ Editar Ejercicio", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                TipoEjercicio tipo = TipoEjercicio.fromString((String) cmbTipo.getSelectedItem());
                controlador.actualizarEjercicio(
                    id,
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    tipo,
                    txtVideo.getText(),
                    txtImagen.getText()
                );
                cargarEjercicios();
                JOptionPane.showMessageDialog(this, 
                    "✅ Ejercicio actualizado correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Elimina un ejercicio con confirmación
     * NOTA: En un sistema real, habría que verificar que no esté en uso
     */
    private void eliminarEjercicio() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un ejercicio para eliminar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el ejercicio '" + nombre + "'?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controlador.eliminarEjercicio(id);
                cargarEjercicios();
                JOptionPane.showMessageDialog(this, 
                    "✅ Ejercicio eliminado correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}