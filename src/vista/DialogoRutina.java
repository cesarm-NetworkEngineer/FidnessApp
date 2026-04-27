/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControladorEjercicios;
import modelo.DetalleRutina;
import modelo.Ejercicio;
import modelo.Rutina;
import modelo.Usuario;
import persistencia.GestorDatos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================
 * DIÁLOGO PARA CREAR O EDITAR RUTINAS - FIDNESS APP
 * ===============================================================
 * 
 * Autor: César Alonso Morera Alpízar
 * 
 * Descripción:
 * Esta clase representa la ventana que permite a los usuarios
 * crear una nueva rutina o editar una existente.
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.1 - Abril 2026
 */
public class DialogoRutina extends JDialog {
    
    // ============================================================
    // ATRIBUTOS
    // ============================================================
    
    private Rutina rutinaEditando;
    private Usuario usuarioActual;
    private ControladorEjercicios controladorEjercicios;
    
    private DefaultTableModel modeloTabla;
    private JTable tablaEjercicios;
    private List<DetalleRutina> detallesTemp;
    
    private JTextField txtNombre;
    private JComboBox<Ejercicio> cmbEjercicios;
    private JSpinner spnSeries;
    private JSpinner spnRepeticiones;
    private JTextArea txtNotas;
    
    private boolean guardado = false;
    
    // 🎨 Colores profesionales
    private static final Color COLOR_FONDO = new Color(245, 245, 250);
    private static final Color COLOR_TEXTO_AZUL = new Color(25, 25, 112);
    private static final Color COLOR_BOTON_FONDO = new Color(240, 240, 245);
    private static final Color COLOR_BOTON_BORDE = new Color(25, 25, 112);
    
    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    
    public DialogoRutina(JFrame parent, Rutina rutina, Usuario usuario, ControladorEjercicios controlador) {
        super(parent, rutina == null ? "➕ Crear Nueva Rutina" : "✏️ Editar Rutina", true);
        
        this.rutinaEditando = rutina;
        this.usuarioActual = usuario;
        this.controladorEjercicios = controlador;
        this.detallesTemp = new ArrayList<>();
        
        if (rutina != null) {
            try {
                this.detallesTemp = GestorDatos.cargarDetallesRutina(rutina.getId());
                System.out.println("📋 Cargados " + detallesTemp.size() + " ejercicios para edición");
            } catch (Exception e) {
                System.err.println("⚠️ Error cargando detalles: " + e.getMessage());
            }
        }
        
        initComponents();
        cargarEjerciciosEnCombo();
        
        if (rutina != null) {
            txtNombre.setText(rutina.getNombre());
            cargarTabla();
        }
    }
    
    // ============================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    // ============================================================
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);
        
        // ===== PANEL SUPERIOR =====
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_FONDO);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nombre de la rutina
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNombre = new JLabel("📝 Nombre de la rutina:");
        lblNombre.setForeground(COLOR_TEXTO_AZUL);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelFormulario.add(lblNombre, gbc);
        
        gbc.gridx = 1;
        txtNombre = new JTextField(25);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelFormulario.add(txtNombre, gbc);
        
        // Separador
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        
        // Subtítulo
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel lblSub = new JLabel("➕ Agregar ejercicios a la rutina");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSub.setForeground(COLOR_TEXTO_AZUL);
        panelFormulario.add(lblSub, gbc);
        gbc.gridwidth = 1;
        
        // Ejercicio
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblEjercicio = new JLabel("Ejercicio:");
        lblEjercicio.setForeground(COLOR_TEXTO_AZUL);
        panelFormulario.add(lblEjercicio, gbc);
        
        gbc.gridx = 1;
        cmbEjercicios = new JComboBox<>();
        cmbEjercicios.setPreferredSize(new Dimension(220, 28));
        panelFormulario.add(cmbEjercicios, gbc);
        
        // Series
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblSeries = new JLabel("Series:");
        lblSeries.setForeground(COLOR_TEXTO_AZUL);
        panelFormulario.add(lblSeries, gbc);
        
        gbc.gridx = 1;
        spnSeries = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        panelFormulario.add(spnSeries, gbc);
        
        // Repeticiones
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblRepeticiones = new JLabel("Repeticiones:");
        lblRepeticiones.setForeground(COLOR_TEXTO_AZUL);
        panelFormulario.add(lblRepeticiones, gbc);
        
        gbc.gridx = 1;
        spnRepeticiones = new JSpinner(new SpinnerNumberModel(12, 1, 50, 1));
        panelFormulario.add(spnRepeticiones, gbc);
        
        // Notas
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblNotas = new JLabel("Notas (opcional):");
        lblNotas.setForeground(COLOR_TEXTO_AZUL);
        panelFormulario.add(lblNotas, gbc);
        
        gbc.gridx = 1;
        txtNotas = new JTextArea(3, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        panelFormulario.add(scrollNotas, gbc);
        
        // Botón Agregar
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        
        JButton btnAgregar = crearBotonConEstilo("➕ Agregar ejercicio", new Color(70, 130, 180));
        btnAgregar.addActionListener(e -> agregarEjercicioALista());
        panelFormulario.add(btnAgregar, gbc);
        
        add(panelFormulario, BorderLayout.NORTH);
        
        // ===== PANEL CENTRAL - TABLA =====
        String[] columnas = {"#", "Ejercicio", "Series", "Reps", "Notas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEjercicios = new JTable(modeloTabla);
        tablaEjercicios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEjercicios.setRowHeight(28);
        tablaEjercicios.setForeground(new Color(50, 50, 70));
        
        JScrollPane scrollTabla = new JScrollPane(tablaEjercicios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("📋 Ejercicios en esta rutina"));
        add(scrollTabla, BorderLayout.CENTER);
        
        // ===== PANEL INFERIOR - BOTONES =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        
        // 🔴 BOTONES CON ESTILO CORREGIDO (texto azul oscuro, fondo gris claro)
        JButton btnGuardar = crearBotonConEstilo("💾 Guardar Rutina", new Color(60, 120, 90));
        JButton btnEliminarSeleccion = crearBotonConEstilo("🗑️ Eliminar", new Color(180, 100, 70));
        JButton btnCancelar = crearBotonConEstilo("❌ Cancelar", new Color(150, 70, 70));
        
        btnGuardar.addActionListener(e -> guardarRutina());
        btnEliminarSeleccion.addActionListener(e -> eliminarEjercicioSeleccionado());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminarSeleccion);
        panelBotones.add(btnCancelar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        setSize(750, 650);
        setLocationRelativeTo(getParent());
    }
    
    /**
     * 🔴 NUEVO MÉTODO: Crea botones con estilo profesional
     * - Texto en AZUL OSCURO
     * - Fondo gris claro
     * - Borde del color del botón
     * - Efecto hover
     */
    private JButton crearBotonConEstilo(String texto, Color colorBorde) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boton.setForeground(COLOR_TEXTO_AZUL);           // 🔵 Texto AZUL OSCURO
        boton.setBackground(COLOR_BOTON_FONDO);          // Fondo gris claro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorde, 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover: fondo gris más oscuro
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(220, 220, 230));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON_FONDO);
            }
        });
        
        return boton;
    }
    
    // ============================================================
    // MÉTODOS DE LÓGICA
    // ============================================================
    
    private void cargarEjerciciosEnCombo() {
        cmbEjercicios.removeAllItems();
        for (Ejercicio e : controladorEjercicios.getTodosLosEjercicios()) {
            cmbEjercicios.addItem(e);
        }
    }
    
    private void agregarEjercicioALista() {
        Ejercicio ejercicio = (Ejercicio) cmbEjercicios.getSelectedItem();
        if (ejercicio == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int series = (int) spnSeries.getValue();
        int repeticiones = (int) spnRepeticiones.getValue();
        String notas = txtNotas.getText().trim();
        
        int nuevoOrden = detallesTemp.size() + 1;
        DetalleRutina detalle = new DetalleRutina(nuevoOrden, ejercicio, series, repeticiones);
        detalle.setNotas(notas);
        detallesTemp.add(detalle);
        
        txtNotas.setText("");
        spnSeries.setValue(3);
        spnRepeticiones.setValue(12);
        cargarTabla();
    }
    
    private void eliminarEjercicioSeleccionado() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila >= 0 && fila < detallesTemp.size()) {
            detallesTemp.remove(fila);
            for (int i = 0; i < detallesTemp.size(); i++) {
                detallesTemp.get(i).setOrden(i + 1);
            }
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (DetalleRutina d : detallesTemp) {
            String nombreEjercicio = "Ejercicio #" + d.getIdEjercicio();
            if (d.getEjercicio() != null) {
                nombreEjercicio = d.getEjercicio().getNombre();
            }
            modeloTabla.addRow(new Object[]{
                d.getOrden(), nombreEjercicio, d.getSeries(), 
                d.getRepeticiones(), d.getNotas() != null ? d.getNotas() : "—"
            });
        }
    }
    
    private void guardarRutina() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para la rutina", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (detallesTemp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un ejercicio", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (DetalleRutina d : detallesTemp) {
            if (d.getEjercicio() == null && controladorEjercicios != null) {
                Ejercicio e = controladorEjercicios.buscarPorId(d.getIdEjercicio());
                if (e != null) d.setEjercicio(e);
            }
        }
        
        boolean exito = false;
        
        if (rutinaEditando == null) {
            Rutina nuevaRutina = new Rutina(nombre, usuarioActual.getId());
            exito = GestorDatos.guardarRutina(nuevaRutina, detallesTemp);
            if (exito) {
                nuevaRutina.setDetalles(detallesTemp);
                usuarioActual.agregarRutina(nuevaRutina);
                guardado = true;
                JOptionPane.showMessageDialog(this, "✅ Rutina guardada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            rutinaEditando.setNombre(nombre);
            GestorDatos.eliminarRutina(rutinaEditando.getId());
            exito = GestorDatos.guardarRutina(rutinaEditando, detallesTemp);
            if (exito) {
                rutinaEditando.setDetalles(detallesTemp);
                guardado = true;
                JOptionPane.showMessageDialog(this, "✅ Rutina actualizada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public boolean isGuardado() {
        return guardado;
    }
}