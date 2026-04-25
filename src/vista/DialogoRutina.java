/*
 * FidnessApp - Diálogo para crear y editar rutinas de ejercicios
 * 
 * Esta clase permite al usuario armar su rutina personalizada seleccionando
 * ejercicios de una lista, configurando series y repeticiones, y finalmente
 * guardando o exportando su rutina a PDF.
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Con exportación PDF
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
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.TitledBorder;

/**
 * Diálogo para crear o editar una rutina
 * La interfaz se divide en dos paneles:
 * - Izquierda: ejercicios disponibles en el sistema
 * - Derecha: ejercicios que el usuario ha seleccionado para su rutina
 */
public class DialogoRutina extends JDialog {
    
    // ========== ATRIBUTOS ==========
    private Usuario usuario;
    private Rutina rutinaExistente;
    private ControladorEjercicios controlador;
    
    // Componentes de la interfaz
    private JTextField txtNombreRutina;
    private JTable tablaEjerciciosDisponibles;
    private JTable tablaEjerciciosSeleccionados;
    private DefaultTableModel modeloDisponibles;
    private DefaultTableModel modeloSeleccionados;
    private List<DetalleRutina> detallesTemporales;
    
    // Controles
    private JSpinner spnSeries;
    private JSpinner spnRepeticiones;
    
    // Botones
    private JButton btnAgregar;
    private JButton btnQuitar;
    private JButton btnGuardar;
    private JButton btnExportarPDF;
    private JButton btnCancelar;
    
    /**
     * Constructor del diálogo
     */
    public DialogoRutina(JFrame parent, Rutina rutina, Usuario usuario) {
        super(parent, true);
        
        this.usuario = usuario;
        this.rutinaExistente = rutina;
        this.controlador = new ControladorEjercicios();
        this.detallesTemporales = new ArrayList<>();
        
        initComponents();
        configurarVentana();
        cargarDatos();
    }
    
    /**
     * Configura las propiedades básicas de la ventana
     */
    private void configurarVentana() {
        setTitle(rutinaExistente == null ? "Crear Nueva Rutina" : "Editar Rutina");
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /**
     * Construye todos los componentes de la interfaz gráfica
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // ========== PANEL SUPERIOR: NOMBRE ==========
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNombre.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        
        JLabel lblNombre = new JLabel("Nombre de la rutina:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        panelNombre.add(lblNombre);
        
        txtNombreRutina = new JTextField(30);
        panelNombre.add(txtNombreRutina);
        
        add(panelNombre, BorderLayout.NORTH);
        
        // ========== PANEL CENTRAL ==========
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel izquierdo: ejercicios disponibles
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Ejercicios Disponibles"));
        
        String[] colDisponibles = {"ID", "Ejercicio", "Tipo"};
        modeloDisponibles = new DefaultTableModel(colDisponibles, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false;
            }
        };
        tablaEjerciciosDisponibles = new JTable(modeloDisponibles);
        tablaEjerciciosDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEjerciciosDisponibles.setRowHeight(25);
        
        panelIzquierdo.add(new JScrollPane(tablaEjerciciosDisponibles), BorderLayout.CENTER);
        splitPane.setLeftComponent(panelIzquierdo);
        
        // Panel derecho: ejercicios seleccionados
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Mi Rutina"));
        
        String[] colSeleccionados = {"#", "Ejercicio", "Series", "Repeticiones"};
        modeloSeleccionados = new DefaultTableModel(colSeleccionados, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return column >= 2;
            }
        };
        tablaEjerciciosSeleccionados = new JTable(modeloSeleccionados);
        tablaEjerciciosSeleccionados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEjerciciosSeleccionados.setRowHeight(25);
        
        // Ajustar ancho de columnas
        tablaEjerciciosSeleccionados.getColumnModel().getColumn(0).setMaxWidth(60);
        tablaEjerciciosSeleccionados.getColumnModel().getColumn(2).setMaxWidth(80);
        tablaEjerciciosSeleccionados.getColumnModel().getColumn(3).setMaxWidth(100);
        
        panelDerecho.add(new JScrollPane(tablaEjerciciosSeleccionados), BorderLayout.CENTER);
        splitPane.setRightComponent(panelDerecho);
        
        add(splitPane, BorderLayout.CENTER);
        
        // ========== PANEL INFERIOR ==========
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        // Panel de configuración
        JPanel panelConfig = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelConfig.setBorder(BorderFactory.createTitledBorder("Configurar ejercicio"));
        
        panelConfig.add(new JLabel("Series:"));
        spnSeries = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
        spnSeries.setPreferredSize(new Dimension(60, 25));
        panelConfig.add(spnSeries);
        
        panelConfig.add(new JLabel("Repeticiones:"));
        spnRepeticiones = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        spnRepeticiones.setPreferredSize(new Dimension(60, 25));
        panelConfig.add(spnRepeticiones);
        
        btnAgregar = new JButton("Agregar >>");
        btnQuitar = new JButton("<< Quitar");
        panelConfig.add(btnAgregar);
        panelConfig.add(btnQuitar);
        
        panelInferior.add(panelConfig, BorderLayout.NORTH);
        
        // Panel de botones principales
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btnGuardar = new JButton("Guardar Rutina");
        btnExportarPDF = new JButton("Exportar a PDF");
        btnCancelar = new JButton("Cancelar");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnExportarPDF);
        panelBotones.add(btnCancelar);
        
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelInferior, BorderLayout.SOUTH);
        
        // ========== EVENTOS ==========
        btnAgregar.addActionListener(e -> agregarEjercicio());
        btnQuitar.addActionListener(e -> quitarEjercicio());
        btnGuardar.addActionListener(e -> guardarRutina());
        btnExportarPDF.addActionListener(e -> exportarAPDF());
        btnCancelar.addActionListener(e -> dispose());
    }
    
    /**
     * Carga los datos iniciales
     */
    private void cargarDatos() {
        List<Ejercicio> todosEjercicios = controlador.getTodosLosEjercicios();
        
        for (Ejercicio e : todosEjercicios) {
            modeloDisponibles.addRow(new Object[]{
                e.getId(), e.getNombre(), e.getTipo()
            });
        }
        
        if (rutinaExistente != null) {
            txtNombreRutina.setText(rutinaExistente.getNombre());
            detallesTemporales.addAll(rutinaExistente.getDetalles());
            actualizarTablaSeleccionados();
        }
    }
    
    /**
     * Agrega un ejercicio a la rutina
     */
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
        
        boolean yaExiste = detallesTemporales.stream()
            .anyMatch(d -> d.getEjercicio().getId() == id);
        
        if (yaExiste) {
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
    
    /**
     * Quita un ejercicio de la rutina
     */
    private void quitarEjercicio() {
        int fila = tablaEjerciciosSeleccionados.getSelectedRow();
        
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un ejercicio para quitar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Quitar este ejercicio de la rutina?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            detallesTemporales.remove(fila);
            
            for (int i = 0; i < detallesTemporales.size(); i++) {
                detallesTemporales.get(i).setOrden(i + 1);
            }
            
            actualizarTablaSeleccionados();
        }
    }
    
    /**
     * Actualiza la tabla de ejercicios seleccionados
     */
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
    
    // ========== MÉTODO DE EXPORTACIÓN A PDF ==========
    
    /**
     * Exporta la rutina actual a un archivo PDF
     */
    private void exportarAPDF() {
        String nombreRutina = txtNombreRutina.getText().trim();
        
        // Validaciones
        if (nombreRutina.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ponle un nombre a tu rutina antes de exportar",
                "Nombre requerido", JOptionPane.WARNING_MESSAGE);
            txtNombreRutina.requestFocus();
            return;
        }
        
        if (detallesTemporales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Agrega ejercicios a tu rutina antes de exportar",
                "Rutina vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Crear carpeta en el escritorio
        String carpetaDestino = System.getProperty("user.home") + "/Desktop/FidnessApp";
        File carpeta = new File(carpetaDestino);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        
        // Crear nombre del archivo
        String nombreLimpio = nombreRutina.replaceAll("[<>:\"/\\\\|?*]", "_").replaceAll("\\s+", "_");
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String nombreArchivo = "Rutina_" + nombreLimpio + "_" + timestamp + ".pdf";
        String rutaCompleta = carpetaDestino + File.separator + nombreArchivo;
        
        // Generar el PDF
        try (FileOutputStream fos = new FileOutputStream(rutaCompleta)) {
            
            // Crear documento PDF usando iText
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, fos);
            document.open();
            
            // Título
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph(
                "FidnessApp - Mi Rutina de Entrenamiento", titleFont);
            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(com.itextpdf.text.Chunk.NEWLINE);
            
            // Información de la rutina
            document.add(new com.itextpdf.text.Paragraph("Rutina: " + nombreRutina));
            document.add(new com.itextpdf.text.Paragraph("Usuario: " + usuario.getNombre()));
            document.add(new com.itextpdf.text.Paragraph("Fecha: " + new java.util.Date()));
            document.add(com.itextpdf.text.Chunk.NEWLINE);
            
            // Tabla de ejercicios
            com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10);
            tabla.setSpacingAfter(10);
            
            // Encabezados de la tabla
            String[] headers = {"#", "Ejercicio", "Series", "Repeticiones"};
            for (String header : headers) {
                com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(
                    new com.itextpdf.text.Phrase(header));
                cell.setBackgroundColor(new com.itextpdf.text.BaseColor(200, 200, 200));
                cell.setPadding(8);
                cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                tabla.addCell(cell);
            }
            
            // Datos de los ejercicios
            for (DetalleRutina detalle : detallesTemporales) {
                tabla.addCell(String.valueOf(detalle.getOrden()));
                tabla.addCell(detalle.getEjercicio().getNombre());
                tabla.addCell(String.valueOf(detalle.getSeries()));
                tabla.addCell(String.valueOf(detalle.getRepeticiones()));
            }
            
            document.add(tabla);
            document.add(com.itextpdf.text.Chunk.NEWLINE);
            
            // Mensaje motivacional
            com.itextpdf.text.Paragraph mensaje = new com.itextpdf.text.Paragraph(
                "¡Felicidades! Has creado tu rutina personalizada.\n" +
                "La constancia es la clave del exito. ¡Tu puedes!");
            mensaje.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(mensaje);
            
            document.close();
            
            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this,
                "Rutina exportada con exito!\n\nUbicacion: " + rutaCompleta,
                "Exportacion exitosa", JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir el PDF automáticamente
            Desktop.getDesktop().open(new File(rutaCompleta));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al exportar: " + e.getMessage() + 
                "\n\nVerifica que tengas la libreria iText agregada al proyecto",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ========== MÉTODO GUARDAR RUTINA ==========
    
    /**
     * Guarda la rutina en el sistema
     */
    private void guardarRutina() {
        String nombre = txtNombreRutina.getText().trim();
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingresa un nombre para la rutina",
                "Nombre requerido", JOptionPane.WARNING_MESSAGE);
            txtNombreRutina.requestFocus();
            return;
        }
        
        if (detallesTemporales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Agrega al menos un ejercicio a la rutina",
                "Rutina vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (rutinaExistente == null) {
                // Crear nueva rutina
                int nuevoId = (int) (System.currentTimeMillis() % 100000);
                Rutina nueva = new Rutina(nuevoId, nombre, usuario.getId());
                
                for (DetalleRutina d : detallesTemporales) {
                    nueva.agregarEjercicio(
                        d.getEjercicio(),
                        d.getSeries(),
                        d.getRepeticiones()
                    );
                }
                
                usuario.agregarRutina(nueva);
                
                JOptionPane.showMessageDialog(this,
                    "Rutina creada con exito!",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar rutina existente
                rutinaExistente.setNombre(nombre);
                rutinaExistente.getDetalles().clear();
                
                for (DetalleRutina d : detallesTemporales) {
                    rutinaExistente.agregarEjercicio(
                        d.getEjercicio(),
                        d.getSeries(),
                        d.getRepeticiones()
                    );
                }
                
                JOptionPane.showMessageDialog(this,
                    "Rutina actualizada con exito!",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}