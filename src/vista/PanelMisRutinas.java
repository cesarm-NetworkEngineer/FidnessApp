package vista;

import controlador.ControladorEjercicios;
import modelo.Rutina;
import modelo.Usuario;
import modelo.Exportador;
import modelo.ExportadorPDF;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Autor: César Alonso Morera Alpízar
 * 
 * Panel de Mis Rutinas para gestionar rutinas del usuario.
 * 
 * 🔥 IMPORTANTE (arquitectura):
 * 
 * Este panel recibe una instancia de ControladorEjercicios desde la VentanaPrincipal.
 * 
 * Esto evita un problema muy común:
 * ❌ Crear múltiples controladores con datos distintos en memoria
 * 
 * En sistemas reales, esto se conoce como:
 * 👉 "Múltiples fuentes de verdad" (grave error)
 * 
 * Aquí aplico el principio:
 * ✅ "Una sola fuente de verdad"
 * 
 * Todos los paneles y diálogos usan el mismo controlador,
 * garantizando consistencia en los datos.
 */
public class PanelMisRutinas extends JPanel {
    
    private Usuario usuarioActual;

    // 🔥 NUEVO: Guardamos el controlador compartido
    private ControladorEjercicios controladorEjercicios;

    private DefaultListModel<Rutina> modeloLista;
    private JList<Rutina> listaRutinas;
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;
    
    private JButton btnNuevaRutina;
    private JButton btnEditarRutina;
    private JButton btnEliminarRutina;
    private JButton btnExportarPDF;
    private JButton btnRefrescar;
    
    /**
     * Constructor del panel
     * 
     * @param usuario usuario actual
     * @param controladorEjercicios instancia compartida del controlador
     */
    public PanelMisRutinas(Usuario usuario, ControladorEjercicios controladorEjercicios) {
        this.usuarioActual = usuario;

        // 🔥 CLAVE: Guardar el controlador
        this.controladorEjercicios = controladorEjercicios;

        initComponents();
        cargarRutinas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Mis Rutinas"));
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));
        
        modeloLista = new DefaultListModel<>();
        listaRutinas = new JList<>(modeloLista);
        listaRutinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelIzquierdo.add(new JScrollPane(listaRutinas), BorderLayout.CENTER);
        
        add(panelIzquierdo, BorderLayout.WEST);
        
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createTitledBorder("Ejercicios de la rutina"));
        
        String[] columnas = {"Orden", "Ejercicio", "Series", "Repeticiones", "Notas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalle = new JTable(modeloTabla);
        panelCentral.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnNuevaRutina = new JButton("Nueva Rutina");
        btnEditarRutina = new JButton("Editar");
        btnEliminarRutina = new JButton("Eliminar");
        btnExportarPDF = new JButton("Exportar PDF");
        btnRefrescar = new JButton("Refrescar Lista");
        
        panelBotones.add(btnNuevaRutina);
        panelBotones.add(btnEditarRutina);
        panelBotones.add(btnEliminarRutina);
        panelBotones.add(btnExportarPDF);
        panelBotones.add(btnRefrescar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        listaRutinas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleRutina();
            }
        });
        
        btnNuevaRutina.addActionListener(e -> nuevaRutina());
        btnEditarRutina.addActionListener(e -> editarRutina());
        btnEliminarRutina.addActionListener(e -> eliminarRutina());
        btnExportarPDF.addActionListener(e -> exportarPDF());
        btnRefrescar.addActionListener(e -> refrescar());
    }
    
    private void cargarRutinas() {
        modeloLista.clear();
        for (Rutina r : usuarioActual.getRutinas()) {
            modeloLista.addElement(r);
        }
    }
    
    public void refrescar() {
        cargarRutinas();
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
    
    /**
     * 🔥 MÉTODO CORREGIDO
     */
    private void nuevaRutina() {

        DialogoRutina dialogo = new DialogoRutina(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            null, 
            usuarioActual,
            controladorEjercicios // 🔥 AQUÍ ESTÁ LA SOLUCIÓN
        );

        dialogo.setVisible(true);
        refrescar();
    }
    
    private void editarRutina() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) return;
        
        DialogoRutina dialogo = new DialogoRutina(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            seleccionada, 
            usuarioActual,
            controladorEjercicios // 🔥 TAMBIÉN AQUÍ
        );

        dialogo.setVisible(true);
        refrescar();
        mostrarDetalleRutina();
    }
    
    private void eliminarRutina() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) return;
        
        usuarioActual.eliminarRutina(seleccionada.getId());
        refrescar();
        modeloTabla.setRowCount(0);
    }
    
    private void exportarPDF() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) return;
        
        JFileChooser fileChooser = new JFileChooser("exports");
        
        int resultado = fileChooser.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                Exportador exportador = new ExportadorPDF();
                exportador.exportar(seleccionada, fileChooser.getSelectedFile().getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}