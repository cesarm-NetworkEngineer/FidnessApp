/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Rutina;
import modelo.Usuario;
import modelo.Ejercicio;
import modelo.DetalleRutina;
import controlador.ControladorEjercicios;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diálogo para crear o editar una rutina (HU3)
 * 
 * Usé un JSplitPane para dividir la pantalla en dos:
 * - Izquierda: ejercicios disponibles (para elegir)
 * - Derecha: ejercicios seleccionados (para ordenar)
 * 
 * Esto permite al usuario armar su rutina visualmente,
 * como arrastrando ejercicios (aunque acá es con botones).
 * 
 * @author César Alonso Morera Alpízar
 */
public class DialogoRutina extends JDialog {
    
    private Usuario usuario;
    private Rutina rutinaExistente;
    private ControladorEjercicios controlador;
    
    private JTextField txtNombreRutina;
    private JTable tablaEjerciciosDisponibles;
    private JTable tablaEjerciciosSeleccionados;
    private DefaultTableModel modeloDisponibles;
    private DefaultTableModel modeloSeleccionados;
    private List<DetalleRutina> detallesTemporales;
    
    private JSpinner spnSeries;
    private JSpinner spnRepeticiones;
    private JButton btnAgregar;
    private JButton btnQuitar;
    private JButton btnGuardar;
    private JButton btnCancelar;
    
    public DialogoRutina(JFrame parent, Rutina rutina, Usuario usuario) {
        super(parent, true); // true = modal (bloquea la ventana padre)
        this.usuario = usuario;
        this.rutinaExistente = rutina;
        this.controlador = new ControladorEjercicios();
        this.detallesTemporales = new ArrayList<>();
        
        initComponents();
        configurarVentana();
        cargarDatos();
    }
    
    private void configurarVentana() {
        setTitle(rutinaExistente == null ? "➕ Nueva Rutina" : "✏️ Editar Rutina");
        setSize(800, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior: nombre de rutina
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNombre.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panelNombre.add(new JLabel("Nombre de la rutina:"));
        txtNombreRutina = new JTextField(30);
        panelNombre.add(txtNombreRutina);
        add(panelNombre, BorderLayout.NORTH);
        
        // Panel central dividido en dos
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        
        // Panel izquierdo: ejercicios disponibles
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Ejercicios Disponibles"));
        
        String[] colDisponibles = {"ID", "Nombre", "Tipo"};
        modeloDisponibles = new DefaultTableModel(colDisponibles, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaEjerciciosDisponibles = new JTable(modeloDisponibles);
        tablaEjerciciosDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelIzquierdo.add(new JScrollPane(tablaEjerciciosDisponibles), BorderLayout.CENTER);
        
        splitPane.setLeftComponent(panelIzquierdo);
        
        // Panel derecho: ejercicios seleccionados
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Ejercicios en la Rutina"));
        
        String[] colSeleccionados = {"Orden", "Ejercicio", "Series", "Repeticiones"};
        modeloSeleccionados = new DefaultTableModel(colSeleccionados, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return column >= 2; // Solo series y repeticiones editables
            }
        };
        tablaEjerciciosSeleccionados = new JTable(modeloSeleccionados);
        tablaEjerciciosSeleccionados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelDerecho.add(new JScrollPane(tablaEjerciciosSeleccionados), BorderLayout.CENTER);
        
        splitPane.setRightComponent(panelDerecho);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Panel inferior con controles
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        // Panel de configuración de series/reps
        JPanel panelConfig = new JPanel(new FlowLayout());
        panelConfig.setBorder(BorderFactory.createTitledBorder("Agregar ejercicio"));
        
        panelConfig.add(new JLabel("Series:"));
        spnSeries = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
        panelConfig.add(spnSeries);
        
        panelConfig.add(new JLabel("Repeticiones:"));
        spnRepeticiones = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        panelConfig.add(spnRepeticiones);
        
        btnAgregar = new JButton("➕ Agregar >>");
        btnQuitar = new JButton("<< Quitar");
        panelConfig.add(btnAgregar);
        panelConfig.add(btnQuitar);
        
        panelInferior.add(panelConfig, BorderLayout.NORTH);
        
        // Panel de botones guardar/cancelar
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("💾 Guardar Rutina");
        btnCancelar = new JButton("✖ Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelInferior, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregar.addActionListener(e -> agregarEjercicio());
        btnQuitar.addActionListener(e -> quitarEjercicio());
        btnGuardar.addActionListener(e -> guardarRutina());
        btnCancelar.addActionListener(e -> dispose());
    }
    
    private void cargarDatos() {
        // Cargar ejercicios disponibles
        for (Ejercicio e : controlador.getTodosLosEjercicios()) {
            modeloDisponibles.addRow(new Object[]{
                e.getId(), e.getNombre(), e.getTipo()
            });
        }
        
        // Si es edición, cargar detalles existentes
        if (rutinaExistente != null) {
            txtNombreRutina.setText(rutinaExistente.getNombre());
            detallesTemporales.addAll(rutinaExistente.getDetalles());
            actualizarTablaSeleccionados();
        }
    }
    
    private void agregarEjercicio() {
        int fila = tablaEjerciciosDisponibles.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un ejercicio de la lista",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modeloDisponibles.getValueAt(fila, 0);
        Ejercicio ejercicio = controlador.buscarPorId(id);
        
        // Verificar duplicado (no permitir el mismo ejercicio dos veces)
        boolean existe = detallesTemporales.stream()
            .anyMatch(d -> d.getEjercicio().getId() == id);
        
        if (existe) {
            JOptionPane.showMessageDialog(this,
                "Este ejercicio ya está en la rutina",
                "Ejercicio duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int series = (int) spnSeries.getValue();
        int repeticiones = (int) spnRepeticiones.getValue();
        
        DetalleRutina detalle = new DetalleRutina(
            detallesTemporales.size() + 1,
            ejercicio,
            series,
            repeticiones
        );
        
        detallesTemporales.add(detalle);
        actualizarTablaSeleccionados();
    }
    
    private void quitarEjercicio() {
        int fila = tablaEjerciciosSeleccionados.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un ejercicio de la rutina para quitar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        detallesTemporales.remove(fila);
        
        // Reordenar los que quedan
        for (int i = 0; i < detallesTemporales.size(); i++) {
            detallesTemporales.get(i).setOrden(i + 1);
        }
        
        actualizarTablaSeleccionados();
    }
    
    private void actualizarTablaSeleccionados() {
        modeloSeleccionados.setRowCount(0);
        for (DetalleRutina d : detallesTemporales) {
            modeloSeleccionados.addRow(new Object[]{
                d.getOrden(),
                d.getEjercicio().getNombre(),
                d.getSeries(),
                d.getRepeticiones()
            });
        }
    }
    
    private void guardarRutina() {
        String nombre = txtNombreRutina.getText().trim();
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre de la rutina es obligatorio",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (detallesTemporales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La rutina debe tener al menos un ejercicio",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (rutinaExistente == null) {
                // Crear nueva rutina
                int nuevoId = (int) (System.currentTimeMillis() % 10000); // ID temporal
                Rutina nueva = new Rutina(nuevoId, nombre, usuario.getId());
                
                for (DetalleRutina d : detallesTemporales) {
                    nueva.agregarEjercicio(
                        d.getEjercicio(),
                        d.getSeries(),
                        d.getRepeticiones()
                    );
                }
                
                usuario.agregarRutina(nueva);
            } else {
                // Actualizar existente
                rutinaExistente.setNombre(nombre);
                // Limpiar y volver a agregar
                rutinaExistente.getDetalles().clear();
                for (DetalleRutina d : detallesTemporales) {
                    rutinaExistente.agregarEjercicio(
                        d.getEjercicio(),
                        d.getSeries(),
                        d.getRepeticiones()
                    );
                }
            }
            
            JOptionPane.showMessageDialog(this,
                "✅ Rutina guardada correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}