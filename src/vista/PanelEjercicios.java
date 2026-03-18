/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import modelo.Usuario;
import controlador.ControladorEjercicios;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de ejercicios (HU2 - Explorar biblioteca)
 * 
 * Este panel implementa la funcionalidad de explorar el catálogo de ejercicios.
 * Cuando creaba contenido para AVA, entendí que los usuarios necesitan
 * encontrar rápido lo que buscan. Por eso puse:
 * - Filtro por tipo (como las VLANs que mencioné antes)
 * - Búsqueda por nombre (para el que ya sabe lo que quiere)
 * - Tabla con los resultados (información clara y ordenada)
 * 
 * @author César Alonso Morera Alpízar
 */
public class PanelEjercicios extends JPanel {
    
    private Usuario usuarioActual;
    private ControladorEjercicios controlador;
    private List<Ejercicio> ejerciciosActuales;
    
    private JComboBox<String> cmbTipoEjercicio;
    private JTextField txtBuscar;
    private JTable tablaEjercicios;
    private DefaultTableModel modeloTabla;
    private JTextArea txtDescripcion;
    private JButton btnVerVideo;
    private JButton btnAgregarARutina;
    
    public PanelEjercicios(Usuario usuario, ControladorEjercicios controlador) {
        this.usuarioActual = usuario;
        this.controlador = controlador;
        this.ejerciciosActuales = controlador.getTodosLosEjercicios();
        initComponents();
        configurarEventos();
        cargarEjercicios();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior con filtros - lo pongo arriba para acceso rápido
        // Como la barra de búsqueda de Google: lo primero que ves
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBackground(new Color(240, 240, 240)); // Un tono diferente para destacar
        
        panelFiltros.add(new JLabel("Tipo:"));
        cmbTipoEjercicio = new JComboBox<>(TipoEjercicio.getNombres());
        panelFiltros.add(cmbTipoEjercicio);
        
        panelFiltros.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        panelFiltros.add(txtBuscar);
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        panelFiltros.add(btnBuscar);
        
        JButton btnLimpiar = new JButton("🗑️ Limpiar");
        panelFiltros.add(btnLimpiar);
        
        add(panelFiltros, BorderLayout.NORTH);
        
        // Panel central (tabla de ejercicios)
        String[] columnas = {"ID", "Nombre", "Tipo", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No queremos que editen directamente en la tabla
            }
        };
        
        tablaEjercicios = new JTable(modeloTabla);
        tablaEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEjercicios.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaEjercicios.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaEjercicios.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaEjercicios.getColumnModel().getColumn(3).setPreferredWidth(300);
        
        add(new JScrollPane(tablaEjercicios), BorderLayout.CENTER);
        
        // Panel inferior (detalle y acciones)
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setBorder(BorderFactory.createTitledBorder("Detalle del ejercicio"));
        
        txtDescripcion = new JTextArea(4, 40);
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInferior.add(new JScrollPane(txtDescripcion), BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnVerVideo = new JButton("🎬 Ver Video");
        btnAgregarARutina = new JButton("➕ Agregar a Rutina");
        panelBotones.add(btnVerVideo);
        panelBotones.add(btnAgregarARutina);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelInferior, BorderLayout.SOUTH);
        
        // Eventos de botones
        btnBuscar.addActionListener(e -> buscarEjercicios());
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        btnVerVideo.addActionListener(e -> verVideo());
        btnAgregarARutina.addActionListener(e -> agregarARutina());
    }
    
    private void configurarEventos() {
        // Cuando seleccionan una fila, mostramos el detalle
        tablaEjercicios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleEjercicio();
            }
        });
    }
    
    private void cargarEjercicios() {
        modeloTabla.setRowCount(0);
        for (Ejercicio e : ejerciciosActuales) {
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getNombre(),
                e.getTipo(),
                e.getDescripcion()
            });
        }
    }
    
    /**
     * Busca ejercicios según filtros seleccionados
     * Lógica: si hay texto en búsqueda, priorizo eso sobre el tipo
     * Aprendí en soporte: el usuario siempre quiere lo que escribió
     */
    private void buscarEjercicios() {
        String tipo = (String) cmbTipoEjercicio.getSelectedItem();
        String busqueda = txtBuscar.getText().trim();
        
        if (!busqueda.isEmpty()) {
            ejerciciosActuales = controlador.buscarPorNombre(busqueda);
        } else {
            ejerciciosActuales = controlador.buscarPorTipo(TipoEjercicio.fromString(tipo));
        }
        
        cargarEjercicios();
        
        // Mensaje amigable si no hay resultados
        if (ejerciciosActuales.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay ejercicios para el criterio seleccionado", 
                "Sin resultados", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limpiarFiltros() {
        cmbTipoEjercicio.setSelectedIndex(0);
        txtBuscar.setText("");
        ejerciciosActuales = controlador.getTodosLosEjercicios();
        cargarEjercicios();
    }
    
    private void mostrarDetalleEjercicio() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            Ejercicio e = controlador.buscarPorId(id);
            if (e != null) {
                txtDescripcion.setText(e.getDescripcion());
            }
        }
    }
    
    private void verVideo() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un ejercicio primero", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Ejercicio e = controlador.buscarPorId(id);
        
        if (e != null && e.getVideoURL() != null && !e.getVideoURL().isEmpty()) {
            try {
                // Abrir en el navegador por defecto
                Desktop.getDesktop().browse(new java.net.URI(e.getVideoURL()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo abrir el video: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Este ejercicio no tiene video asociado", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Agrega el ejercicio seleccionado a la rutina actual
     * Por ahora es un placeholder - se integrará con PanelMisRutinas
     */
    private void agregarARutina() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un ejercicio primero", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Aquí se integraría con el panel de rutinas
        // Por ahora, mensaje informativo
        JOptionPane.showMessageDialog(this, 
            "Funcionalidad: Agregar a rutina\n(Se integrará con PanelMisRutinas)", 
            "En desarrollo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}