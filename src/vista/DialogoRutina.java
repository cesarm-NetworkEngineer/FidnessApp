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
 * Funcionalidades:
 * - Asignar un nombre a la rutina
 * - Agregar ejercicios del catálogo
 * - Especificar series y repeticiones por ejercicio
 * - Agregar notas opcionales
 * - Guardar la rutina en la base de datos
 * 
 * Reflexión:
 * En mis años como instructor, siempre digo:
 * "Una buena interfaz es como una buena conversación:
 *  clara, directa y sin confusiones".
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Abril 2026
 */
public class DialogoRutina extends JDialog {
    
    // ============================================================
    // ATRIBUTOS PRINCIPALES
    // ============================================================
    
    private Rutina rutinaEditando;        // Rutina que se está editando (null si es nueva)
    private Usuario usuarioActual;        // Usuario que está creando la rutina
    private ControladorEjercicios controladorEjercicios;  // Controlador compartido
    
    private DefaultTableModel modeloTabla;  // Modelo de la tabla de ejercicios
    private JTable tablaEjercicios;         // Tabla que muestra los ejercicios agregados
    private List<DetalleRutina> detallesTemp;  // Lista temporal de ejercicios en la rutina
    
    // Componentes de la interfaz
    private JTextField txtNombre;                // Campo para el nombre de la rutina
    private JComboBox<Ejercicio> cmbEjercicios;  // Combo con todos los ejercicios del catálogo
    private JSpinner spnSeries;                  // Selector de series
    private JSpinner spnRepeticiones;            // Selector de repeticiones
    private JTextArea txtNotas;                  // Área de notas adicionales
    
    private boolean guardado = false;  // Indica si la rutina se guardó correctamente
    
    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    
    /**
     * Constructor del diálogo
     * 
     * @param parent        Ventana padre (JFrame)
     * @param rutina        Rutina a editar (null si es nueva)
     * @param usuario       Usuario actual (dueño de la rutina)
     * @param controlador   Controlador de ejercicios (compartido)
     */
    public DialogoRutina(JFrame parent, Rutina rutina, Usuario usuario, ControladorEjercicios controlador) {
        super(parent, rutina == null ? "➕ Crear Nueva Rutina" : "✏️ Editar Rutina", true);
        
        // Inicializar atributos
        this.rutinaEditando = rutina;
        this.usuarioActual = usuario;
        this.controladorEjercicios = controlador;
        this.detallesTemp = new ArrayList<>();
        
        // Si estamos editando una rutina existente, cargar sus ejercicios
        if (rutina != null) {
            try {
                this.detallesTemp = GestorDatos.cargarDetallesRutina(rutina.getId());
                System.out.println("📋 Cargados " + detallesTemp.size() + " ejercicios para edición");
            } catch (Exception e) {
                System.err.println("⚠️ Error cargando detalles de la rutina: " + e.getMessage());
            }
        }
        
        // Construir la interfaz gráfica
        initComponents();
        
        // Cargar el catálogo de ejercicios en el combo
        cargarEjerciciosEnCombo();
        
        // Si estamos editando, cargar los datos de la rutina
        if (rutina != null) {
            txtNombre.setText(rutina.getNombre());
            cargarTabla();
        }
        
        System.out.println("✅ Diálogo de rutina inicializado");
    }
    
    // ============================================================
    // CONSTRUCCIÓN DE LA INTERFAZ GRÁFICA
    // ============================================================
    
    /**
     * Construye todos los componentes visuales del diálogo
     * 
     * La interfaz está organizada en tres secciones:
     * 1. Panel superior: datos generales y formulario para agregar ejercicios
     * 2. Panel central: tabla con los ejercicios agregados
     * 3. Panel inferior: botones de acción (guardar, cancelar, eliminar)
     */
    private void initComponents() {
        // Configuración básica del diálogo
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));
        
        // ========================================================
        // PANEL SUPERIOR - Formulario de la rutina
        // ========================================================
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(245, 245, 250));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ----- Campo: Nombre de la rutina (obligatorio) -----
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNombre = new JLabel("📝 Nombre de la rutina:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(new Color(50, 50, 70));
        panelFormulario.add(lblNombre, gbc);
        
        gbc.gridx = 1;
        txtNombre = new JTextField(25);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        panelFormulario.add(txtNombre, gbc);
        
        // ----- Separador visual -----
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(200, 200, 210));
        panelFormulario.add(separador, gbc);
        gbc.gridwidth = 1;
        
        // ----- Subtítulo: Agregar ejercicios -----
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel lblSub = new JLabel("➕ Agregar ejercicios a la rutina");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSub.setForeground(new Color(70, 130, 180));
        panelFormulario.add(lblSub, gbc);
        gbc.gridwidth = 1;
        
        // ----- Campo: Selección de ejercicio -----
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblEjercicio = new JLabel("Ejercicio:");
        lblEjercicio.setForeground(new Color(50, 50, 70));
        panelFormulario.add(lblEjercicio, gbc);
        
        gbc.gridx = 1;
        cmbEjercicios = new JComboBox<>();
        cmbEjercicios.setPreferredSize(new Dimension(220, 28));
        cmbEjercicios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelFormulario.add(cmbEjercicios, gbc);
        
        // ----- Campo: Series -----
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblSeries = new JLabel("Series:");
        lblSeries.setForeground(new Color(50, 50, 70));
        panelFormulario.add(lblSeries, gbc);
        
        gbc.gridx = 1;
        spnSeries = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        spnSeries.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelFormulario.add(spnSeries, gbc);
        
        // ----- Campo: Repeticiones -----
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblRepeticiones = new JLabel("Repeticiones:");
        lblRepeticiones.setForeground(new Color(50, 50, 70));
        panelFormulario.add(lblRepeticiones, gbc);
        
        gbc.gridx = 1;
        spnRepeticiones = new JSpinner(new SpinnerNumberModel(12, 1, 50, 1));
        spnRepeticiones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelFormulario.add(spnRepeticiones, gbc);
        
        // ----- Campo: Notas (opcional) -----
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblNotas = new JLabel("Notas (opcional):");
        lblNotas.setForeground(new Color(50, 50, 70));
        panelFormulario.add(lblNotas, gbc);
        
        gbc.gridx = 1;
        txtNotas = new JTextArea(3, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        txtNotas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNotas.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 1));
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        panelFormulario.add(scrollNotas, gbc);
        
        // ----- Botón para agregar ejercicio a la lista -----
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        
        JButton btnAgregar = new JButton("➕ Agregar este ejercicio a la rutina");
        btnAgregar.setBackground(new Color(70, 130, 180));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarEjercicioALista());
        panelFormulario.add(btnAgregar, gbc);
        
        add(panelFormulario, BorderLayout.NORTH);
        
        // ========================================================
        // PANEL CENTRAL - Tabla de ejercicios agregados
        // ========================================================
        String[] columnas = {"#", "Ejercicio", "Series", "Repeticiones", "Notas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // La tabla no es editable directamente
            }
        };
        
        tablaEjercicios = new JTable(modeloTabla);
        tablaEjercicios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEjercicios.setRowHeight(28);
        tablaEjercicios.setSelectionBackground(new Color(200, 210, 230));
        
        JScrollPane scrollTabla = new JScrollPane(tablaEjercicios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            "📋 Ejercicios en esta rutina"
        ));
        add(scrollTabla, BorderLayout.CENTER);
        
        // ========================================================
        // PANEL INFERIOR - Botones de acción
        // ========================================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 250));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        
        // Botón Guardar
        JButton btnGuardar = new JButton("💾 Guardar Rutina");
        btnGuardar.setBackground(new Color(60, 120, 90));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarRutina());
        
        // Botón Eliminar seleccionado
        JButton btnEliminarSeleccion = new JButton("🗑️ Eliminar Seleccionado");
        btnEliminarSeleccion.setBackground(new Color(180, 100, 70));
        btnEliminarSeleccion.setForeground(Color.WHITE);
        btnEliminarSeleccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnEliminarSeleccion.setFocusPainted(false);
        btnEliminarSeleccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminarSeleccion.addActionListener(e -> eliminarEjercicioSeleccionado());
        
        // Botón Cancelar
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(150, 70, 70));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminarSeleccion);
        panelBotones.add(btnCancelar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // Configuración final del diálogo
        setSize(750, 650);
        setLocationRelativeTo(getParent());
    }
    
    // ============================================================
    // MÉTODOS DE LÓGICA
    // ============================================================
    
    /**
     * Carga todos los ejercicios del catálogo en el combo de selección
     * Así el usuario puede elegir qué ejercicio agregar a su rutina
     */
    private void cargarEjerciciosEnCombo() {
        cmbEjercicios.removeAllItems();
        List<Ejercicio> ejercicios = controladorEjercicios.getTodosLosEjercicios();
        for (Ejercicio e : ejercicios) {
            cmbEjercicios.addItem(e);
        }
        System.out.println("📚 Catálogo cargado: " + ejercicios.size() + " ejercicios disponibles");
    }
    
    /**
     * Agrega el ejercicio seleccionado a la lista temporal de la rutina
     * 
     * Este método:
     * 1. Toma el ejercicio seleccionado del combo
     * 2. Toma los valores de series, repeticiones y notas
     * 3. Crea un DetalleRutina con esos datos
     * 4. Lo agrega a la lista temporal
     * 5. Actualiza la tabla visual
     */
    private void agregarEjercicioALista() {
        // Obtener el ejercicio seleccionado
        Ejercicio ejercicio = (Ejercicio) cmbEjercicios.getSelectedItem();
        if (ejercicio == null) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un ejercicio", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtener valores
        int series = (int) spnSeries.getValue();
        int repeticiones = (int) spnRepeticiones.getValue();
        String notas = txtNotas.getText().trim();
        
        // Calcular el orden (último + 1)
        int nuevoOrden = detallesTemp.size() + 1;
        
        // Crear el detalle de rutina con el ejercicio completo
        // 🔴 IMPORTANTE: Pasamos el objeto Ejercicio, no solo su ID
        DetalleRutina detalle = new DetalleRutina(nuevoOrden, ejercicio, series, repeticiones);
        detalle.setNotas(notas);
        
        // Agregar a la lista temporal
        detallesTemp.add(detalle);
        
        // Limpiar campos para el siguiente ejercicio
        txtNotas.setText("");
        spnSeries.setValue(3);
        spnRepeticiones.setValue(12);
        
        // Actualizar la tabla visual
        cargarTabla();
        
        System.out.println("➕ Ejercicio agregado: " + ejercicio.getNombre());
    }
    
    /**
     * Elimina el ejercicio seleccionado de la tabla
     * 
     * Después de eliminar, reordena los números de orden
     * para que queden consecutivos (1, 2, 3...)
     */
    private void eliminarEjercicioSeleccionado() {
        int fila = tablaEjercicios.getSelectedRow();
        if (fila >= 0 && fila < detallesTemp.size()) {
            // Remover el detalle seleccionado
            String nombreEj = detallesTemp.get(fila).getEjercicio() != null ? 
                              detallesTemp.get(fila).getEjercicio().getNombre() : "Ejercicio";
            detallesTemp.remove(fila);
            
            // Reordenar los detalles restantes
            for (int i = 0; i < detallesTemp.size(); i++) {
                detallesTemp.get(i).setOrden(i + 1);
            }
            
            // Actualizar la tabla
            cargarTabla();
            
            System.out.println("🗑️ Ejercicio eliminado: " + nombreEj);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio para eliminar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Actualiza la tabla visual con los ejercicios agregados
     * 
     * Recorre la lista temporal de detalles y los muestra en la tabla.
     * Si algún detalle no tiene el objeto Ejercicio cargado, lo busca
     * en el controlador por su ID.
     */
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        
        for (DetalleRutina d : detallesTemp) {
            String nombreEjercicio = "Ejercicio #" + d.getIdEjercicio();
            
            // Intentar obtener el nombre del ejercicio
            if (d.getEjercicio() != null) {
                nombreEjercicio = d.getEjercicio().getNombre();
            } else if (controladorEjercicios != null) {
                Ejercicio e = controladorEjercicios.buscarPorId(d.getIdEjercicio());
                if (e != null) {
                    nombreEjercicio = e.getNombre();
                    d.setEjercicio(e);  // Guardar referencia para futuros usos
                }
            }
            
            modeloTabla.addRow(new Object[]{
                d.getOrden(),
                nombreEjercicio,
                d.getSeries(),
                d.getRepeticiones(),
                d.getNotas() != null && !d.getNotas().isEmpty() ? d.getNotas() : "—"
            });
        }
    }
    
    /**
     * Guarda la rutina en la base de datos
     * 
     * Este método:
     * 1. Valida que la rutina tenga nombre y al menos un ejercicio
     * 2. Asegura que todos los detalles tengan el objeto Ejercicio
     * 3. Llama a GestorDatos para guardar en archivo .dat
     * 4. Muestra mensaje de éxito o error
     */
    private void guardarRutina() {
        // Validación: nombre no vacío
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese un nombre para la rutina", 
                "Nombre requerido", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        
        // Validación: al menos un ejercicio
        if (detallesTemp.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Agregue al menos un ejercicio a la rutina", 
                "Rutina vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Asegurar que todos los detalles tengan el objeto Ejercicio
        // Esto es importante porque al guardar necesitamos el ID del ejercicio
        for (DetalleRutina d : detallesTemp) {
            if (d.getEjercicio() == null && controladorEjercicios != null) {
                Ejercicio e = controladorEjercicios.buscarPorId(d.getIdEjercicio());
                if (e != null) {
                    d.setEjercicio(e);
                }
            }
        }
        
        // Crear o editar la rutina
        Rutina rutina;
        boolean exito = false;
        
        if (rutinaEditando == null) {
            // ===== ES UNA RUTINA NUEVA =====
            rutina = new Rutina(nombre, usuarioActual.getId());
            exito = GestorDatos.guardarRutina(rutina, detallesTemp);
            
            if (exito) {
                usuarioActual.agregarRutina(rutina);
                guardado = true;
                JOptionPane.showMessageDialog(this, 
                    "✅ ¡Rutina guardada exitosamente!\n" +
                    "   Nombre: " + nombre + "\n" +
                    "   Ejercicios: " + detallesTemp.size(), 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error al guardar la rutina.\n" +
                    "   Verifique que la base de datos esté disponible.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // ===== ES UNA RUTINA EXISTENTE (EDITAR) =====
            rutinaEditando.setNombre(nombre);
            // Para editar, eliminamos la rutina antigua y guardamos la nueva
            GestorDatos.eliminarRutina(rutinaEditando.getId());
            exito = GestorDatos.guardarRutina(rutinaEditando, detallesTemp);
            
            if (exito) {
                guardado = true;
                JOptionPane.showMessageDialog(this, 
                    "✅ ¡Rutina actualizada exitosamente!\n" +
                    "   Nombre: " + nombre, 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error al actualizar la rutina.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ============================================================
    // MÉTODO PÚBLICO
    // ============================================================
    
    /**
     * Indica si la rutina se guardó correctamente
     * 
     * @return true si se guardó, false si se canceló o hubo error
     */
    public boolean isGuardado() {
        return guardado;
    }
}