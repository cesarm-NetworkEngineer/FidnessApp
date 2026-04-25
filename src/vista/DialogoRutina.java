/*
 * FidnessApp - Dialogo para crear y editar rutinas de ejercicios
 * 
 * Autor: Cesar Alonso Morera Alpizar
 * 
 * Este dialogo permite:
 * - Seleccionar ejercicios disponibles
 * - Agregarlos a una rutina personalizada
 * - Configurar series y repeticiones
 * - Guardar la rutina en memoria
 * 
 * Conceptos clave aplicados:
 * ✅ Una sola fuente de verdad (Controlador compartido)
 * ✅ Separacion entre UI y modelo de datos
 * ✅ Validaciones de usuario
 * ✅ Manejo de estado en memoria
 */

package vista;

import modelo.*;
import controlador.ControladorEjercicios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogoRutina extends JDialog {
    
    // ===== CONTEXTO =====
    private Usuario usuario;
    private Rutina rutinaExistente;
    private ControladorEjercicios controlador;

    // 🔥 Lista REAL de ejercicios (NO depender de la tabla)
    private List<Ejercicio> listaEjercicios;

    // Lista temporal para construir la rutina
    private List<DetalleRutina> detallesTemporales;

    // ===== UI =====
    private JTextField txtNombreRutina;
    private JTable tablaIzquierda;
    private JTable tablaDerecha;
    private DefaultTableModel modeloIzquierda;
    private DefaultTableModel modeloDerecha;
    private JSpinner spnSeries;
    private JSpinner spnReps;

    /**
     * Constructor principal
     */
    public DialogoRutina(JFrame parent, Rutina rutina, Usuario usuario, ControladorEjercicios controladorEjercicios) {
        super(parent, true);

        this.usuario = usuario;
        this.rutinaExistente = rutina;

        // 🔥 CLAVE: usar la MISMA instancia compartida
        this.controlador = controladorEjercicios;

        this.detallesTemporales = new ArrayList<>();

        setTitle(rutinaExistente == null ? "Crear Nueva Rutina" : "Editar Rutina");
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearGUI();

        // Si es edición, cargar datos existentes
        if (rutinaExistente != null) {
            txtNombreRutina.setText(rutinaExistente.getNombre());
            detallesTemporales.addAll(rutinaExistente.getDetalles());
            refrescarTablaDerecha();
        }
    }

    /**
     * Construcción de la interfaz gráfica
     */
    private void crearGUI() {

        setLayout(new BorderLayout(10, 10));

        // ===== PANEL SUPERIOR =====
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorte.add(new JLabel("Nombre de la rutina:"));
        txtNombreRutina = new JTextField(25);
        panelNorte.add(txtNombreRutina);
        add(panelNorte, BorderLayout.NORTH);

        // ===== PANEL CENTRAL =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);

        // Tabla izquierda (ejercicios disponibles)
        modeloIzquierda = new DefaultTableModel(new String[]{"ID", "Ejercicio", "Tipo"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaIzquierda = new JTable(modeloIzquierda);
        JScrollPane scrollIzq = new JScrollPane(tablaIzquierda);
        scrollIzq.setBorder(new TitledBorder("Ejercicios Disponibles"));

        // Tabla derecha (rutina)
        modeloDerecha = new DefaultTableModel(new String[]{"#", "Ejercicio", "Series", "Reps"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaDerecha = new JTable(modeloDerecha);
        JScrollPane scrollDer = new JScrollPane(tablaDerecha);
        scrollDer.setBorder(new TitledBorder("Mi Rutina"));

        splitPane.setLeftComponent(scrollIzq);
        splitPane.setRightComponent(scrollDer);

        add(splitPane, BorderLayout.CENTER);

        // ===== PANEL INFERIOR =====
        JPanel panelSur = new JPanel(new BorderLayout());

        // Configuración (series y reps)
        JPanel panelConfig = new JPanel();
        panelConfig.add(new JLabel("Series:"));
        spnSeries = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        panelConfig.add(spnSeries);

        panelConfig.add(new JLabel("Reps:"));
        spnReps = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));
        panelConfig.add(spnReps);

        panelSur.add(panelConfig, BorderLayout.NORTH);

        // Botones
        JPanel panelBotones = new JPanel();

        JButton btnAgregar = new JButton("Agregar >>");
        btnAgregar.addActionListener(e -> agregarEjercicio());

        JButton btnQuitar = new JButton("<< Quitar");
        btnQuitar.addActionListener(e -> quitarEjercicio());

        JButton btnGuardar = new JButton("Guardar Rutina");
        btnGuardar.addActionListener(e -> guardarRutina());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnQuitar);
        panelBotones.add(btnGuardar);

        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);

        // 🔥 Cargar datos reales
        cargarEjerciciosIzquierda();
    }

    /**
     * Carga ejercicios desde el controlador
     */
    private void cargarEjerciciosIzquierda() {

        modeloIzquierda.setRowCount(0);

        // 🔥 Guardamos la lista REAL
        listaEjercicios = controlador.getTodosLosEjercicios();

        System.out.println("Ejercicios cargados: " + listaEjercicios.size());

        for (Ejercicio e : listaEjercicios) {
            modeloIzquierda.addRow(new Object[]{
                e.getId(),
                e.getNombre(),
                e.getTipo()
            });
        }
    }

    /**
     * Agregar ejercicio a la rutina
     */
    private void agregarEjercicio() {

        int fila = tablaIzquierda.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio primero");
            return;
        }

        // 🔥 USAR OBJETO REAL
        Ejercicio ejercicio = listaEjercicios.get(fila);

        // Validar duplicado
        for (DetalleRutina d : detallesTemporales) {
            if (d.getEjercicio().getId() == ejercicio.getId()) {
                JOptionPane.showMessageDialog(this, "Ejercicio ya agregado");
                return;
            }
        }

        int series = (int) spnSeries.getValue();
        int reps = (int) spnReps.getValue();

        DetalleRutina detalle = new DetalleRutina(
            detallesTemporales.size() + 1,
            ejercicio,
            series,
            reps
        );

        detallesTemporales.add(detalle);

        refrescarTablaDerecha();

        System.out.println("Ejercicio agregado: " + ejercicio.getNombre());
    }

    /**
     * Quitar ejercicio
     */
    private void quitarEjercicio() {

        int fila = tablaDerecha.getSelectedRow();
        if (fila < 0) return;

        detallesTemporales.remove(fila);
        refrescarTablaDerecha();
    }

    /**
     * Refresca tabla derecha
     */
    private void refrescarTablaDerecha() {

        modeloDerecha.setRowCount(0);

        for (DetalleRutina d : detallesTemporales) {
            modeloDerecha.addRow(new Object[]{
                d.getOrden(),
                d.getEjercicio().getNombre(),
                d.getSeries(),
                d.getRepeticiones()
            });
        }
    }

    /**
     * Guardar rutina
     */
    private void guardarRutina() {

        String nombre = txtNombreRutina.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre");
            return;
        }

        if (detallesTemporales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un ejercicio");
            return;
        }

        Rutina nueva = new Rutina(
            usuario.getRutinas().size() + 1,
            nombre,
            usuario.getId()
        );

        for (DetalleRutina d : detallesTemporales) {
            nueva.agregarEjercicio(d.getEjercicio(), d.getSeries(), d.getRepeticiones());
        }

        usuario.agregarRutina(nueva);

        JOptionPane.showMessageDialog(this, "Rutina guardada correctamente");

        dispose();
    }
}