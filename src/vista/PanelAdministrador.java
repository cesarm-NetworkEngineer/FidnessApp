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

public class PanelAdministrador extends JPanel {
    
    private ControladorEjercicios controlador;
    private JTable tablaEjercicios;
    private DefaultTableModel modeloTabla;
    private PanelEjercicios panelEjercicios;
    
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    
    public PanelAdministrador(ControladorEjercicios controlador, PanelEjercicios panelEjercicios) {
        this.controlador = controlador;
        this.panelEjercicios = panelEjercicios;
        initComponents();
        cargarEjercicios();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Administracion del Catalogo de Ejercicios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);
        
        String[] columnas = {"ID", "Nombre", "Tipo", "Descripcion", "Video URL", "Imagen URL"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEjercicios = new JTable(modeloTabla);
        tablaEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaEjercicios), BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnAgregar = new JButton("Agregar Ejercicio");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar Lista");
        
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
        panel.add(new JLabel("Descripcion:"), gbc);
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
        
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Agregar Nuevo Ejercicio", JOptionPane.OK_CANCEL_OPTION);
        
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
                if (panelEjercicios != null) panelEjercicios.refrescar();
                JOptionPane.showMessageDialog(this, "Ejercicio agregado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarEjercicio() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Ejercicio e = controlador.buscarPorId(id);
        if (e == null) return;
        
        JTextField txtNombre = new JTextField(e.getNombre());
        JTextArea txtDescripcion = new JTextArea(e.getDescripcion(), 3, 20);
        JComboBox<String> cmbTipo = new JComboBox<>(TipoEjercicio.getNombres());
        String tipoStr = e.getTipo().toString();
        cmbTipo.setSelectedItem(tipoStr.charAt(0) + tipoStr.substring(1).toLowerCase());
        
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
        panel.add(new JLabel("Descripcion:"), gbc);
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
        
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Editar Ejercicio", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                TipoEjercicio tipo = TipoEjercicio.fromString((String) cmbTipo.getSelectedItem());
                controlador.actualizarEjercicio(id, txtNombre.getText(), txtDescripcion.getText(), tipo, txtVideo.getText(), txtImagen.getText());
                cargarEjercicios();
                if (panelEjercicios != null) panelEjercicios.refrescar();
                JOptionPane.showMessageDialog(this, "Ejercicio actualizado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarEjercicio() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirmacion = JOptionPane.showConfirmDialog(this, "Eliminar el ejercicio '" + nombre + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controlador.eliminarEjercicio(id);
                cargarEjercicios();
                if (panelEjercicios != null) panelEjercicios.refrescar();
                JOptionPane.showMessageDialog(this, "Ejercicio eliminado", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}