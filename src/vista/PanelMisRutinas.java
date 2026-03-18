/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Rutina;
import modelo.Usuario;
import modelo.Exportador;
import modelo.ExportadorPDF;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de Mis Rutinas (HU4 - Gestionar rutinas)
 * 
 * Organización izquierda-derecha: 
 * - Izquierda: lista de rutinas (para seleccionar)
 * - Derecha: detalle de la rutina seleccionada
 * 
 * En Syniverse aprendí que esta disposición es intuitiva:
 * el usuario ve un listado, selecciona, y ve los detalles.
 * Como el explorador de archivos de Windows.
 * 
 * @author César Alonso Morera Alpízar
 */
public class PanelMisRutinas extends JPanel {
    
    private Usuario usuarioActual;
    private DefaultListModel<Rutina> modeloLista;
    private JList<Rutina> listaRutinas;
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;
    
    private JButton btnNuevaRutina;
    private JButton btnEditarRutina;
    private JButton btnEliminarRutina;
    private JButton btnExportarPDF;
    
    public PanelMisRutinas(Usuario usuario) {
        this.usuarioActual = usuario;
        initComponents();
        cargarRutinas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel izquierdo (lista de rutinas)
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Mis Rutinas"));
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));
        
        modeloLista = new DefaultListModel<>();
        listaRutinas = new JList<>(modeloLista);
        listaRutinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelIzquierdo.add(new JScrollPane(listaRutinas), BorderLayout.CENTER);
        
        add(panelIzquierdo, BorderLayout.WEST);
        
        // Panel central (detalle de rutina)
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createTitledBorder("Ejercicios de la rutina"));
        
        String[] columnas = {"Orden", "Ejercicio", "Series", "Repeticiones", "Notas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        
        tablaDetalle = new JTable(modeloTabla);
        panelCentral.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior (botones)
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnNuevaRutina = new JButton("➕ Nueva Rutina");
        btnEditarRutina = new JButton("✏️ Editar");
        btnEliminarRutina = new JButton("🗑️ Eliminar");
        btnExportarPDF = new JButton("📄 Exportar PDF");
        
        panelBotones.add(btnNuevaRutina);
        panelBotones.add(btnEditarRutina);
        panelBotones.add(btnEliminarRutina);
        panelBotones.add(btnExportarPDF);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        listaRutinas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleRutina();
            }
        });
        
        btnNuevaRutina.addActionListener(e -> nuevaRutina());
        btnEditarRutina.addActionListener(e -> editarRutina());
        btnEliminarRutina.addActionListener(e -> eliminarRutina());
        btnExportarPDF.addActionListener(e -> exportarPDF());
    }
    
    private void cargarRutinas() {
        modeloLista.clear();
        for (Rutina r : usuarioActual.getRutinas()) {
            modeloLista.addElement(r);
        }
    }
    
    private void mostrarDetalleRutina() {
        modeloTabla.setRowCount(0);
        
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada != null) {
            for (var detalle : seleccionada.getDetalles()) {
                modeloTabla.addRow(new Object[]{
                    detalle.getOrden(),
                    detalle.getEjercicio().getNombre(),
                    detalle.getSeries(),
                    detalle.getRepeticiones(),
                    detalle.getNotas() != null ? detalle.getNotas() : ""
                });
            }
        }
    }
    
    private void nuevaRutina() {
        DialogoRutina dialogo = new DialogoRutina(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            null, 
            usuarioActual
        );
        dialogo.setVisible(true);
        cargarRutinas();
    }
    
    private void editarRutina() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una rutina para editar", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DialogoRutina dialogo = new DialogoRutina(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            seleccionada, 
            usuarioActual
        );
        dialogo.setVisible(true);
        cargarRutinas();
        mostrarDetalleRutina();
    }
    
    /**
     * Elimina una rutina con confirmación del usuario
     * 
     * En Syniverse aprendí que eliminar datos sin confirmación es peligroso.
     * Una vez un compañero borró una configuración crítica sin querer...
     * desde entonces, SIEMPRE confirmación.
     */
    private void eliminarRutina() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una rutina para eliminar", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Diálogo de confirmación con el nombre de la rutina
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar la rutina '" + seleccionada.getNombre() + "'?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            usuarioActual.eliminarRutina(seleccionada.getId());
            cargarRutinas();
            modeloTabla.setRowCount(0);
            
            JOptionPane.showMessageDialog(this, 
                "✅ Rutina eliminada correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Exporta la rutina seleccionada a PDF
     * Usa el patrón Strategy (Exportador) para el formato
     */
    private void exportarPDF() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una rutina para exportar", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Diálogo para elegir dónde guardar
        JFileChooser fileChooser = new JFileChooser("exports");
        fileChooser.setDialogTitle("Guardar rutina como PDF");
        fileChooser.setSelectedFile(new java.io.File(
            "Rutina_" + seleccionada.getNombre().replace(" ", "_") + ".pdf"));
        
        int resultado = fileChooser.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                Exportador exportador = new ExportadorPDF();
                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
                
                if (exportador.exportar(seleccionada, ruta)) {
                    JOptionPane.showMessageDialog(this, 
                        "✅ Rutina exportada correctamente a:\n" + ruta, 
                        "Exportación exitosa", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al exportar: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}