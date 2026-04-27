/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControladorEjercicios;
import modelo.Rutina;
import modelo.Usuario;
import modelo.DetalleRutina;
import persistencia.GestorDatos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.border.TitledBorder;

/**
 * ===============================================================
 * PANEL DE MIS RUTINAS - FIDNESS APP
 * ===============================================================
 * 
 * Autor: César Alonso Morera Alpízar
 * 
 * Descripción:
 * Panel que permite al usuario gestionar sus rutinas de entrenamiento.
 * 
 * Diseño:
 * - Fondo gris claro (#F5F5FA)
 * - Texto en azul oscuro (#191970) para títulos y etiquetas
 * - Botones con texto AZUL OSCURO sobre fondo gris claro
 * - Texto en gris oscuro (#323246) para contenido de tablas
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Abril 2026
 */
public class PanelMisRutinas extends JPanel {
    
    // ============================================================
    // ATRIBUTOS
    // ============================================================
    
    private final Usuario usuarioActual;
    private final ControladorEjercicios controladorEjercicios;
    
    private DefaultListModel<Rutina> modeloLista;
    private JList<Rutina> listaRutinas;
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;
    
    private JButton btnNuevaRutina;
    private JButton btnEditarRutina;
    private JButton btnEliminarRutina;
    private JButton btnExportarPDF;
    private JButton btnRefrescar;
    
    // 🎨 Colores profesionales
    private static final Color COLOR_FONDO = new Color(245, 245, 250);     // Gris muy claro
    private static final Color COLOR_TEXTO_AZUL = new Color(25, 25, 112);   // Azul oscuro (MidnightBlue)
    private static final Color COLOR_TEXTO_NORMAL = new Color(50, 50, 70);   // Gris oscuro
    private static final Color COLOR_TABLA_FONDO = Color.WHITE;
    private static final Color COLOR_ENCABEZADO = new Color(220, 220, 230); // Gris para encabezados
    private static final Color COLOR_BOTON_FONDO = new Color(240, 240, 245); // Gris claro
    private static final Color COLOR_BOTON_TEXTO = new Color(25, 25, 112);   // 🔵 AZUL OSCURO para texto
    private static final Color COLOR_BOTON_BORDE = new Color(25, 25, 112);   // Borde azul oscuro
    
    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    
    /**
     * Constructor del panel
     * 
     * @param usuario usuario actual
     * @param controladorEjercicios instancia compartida del controlador
     */
    public PanelMisRutinas(Usuario usuario, ControladorEjercicios controladorEjercicios) {
        this.usuarioActual = usuario;
        this.controladorEjercicios = controladorEjercicios;
        initComponents();
        cargarRutinas();
    }
    
    // ============================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    // ============================================================
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(COLOR_FONDO);
        
        // ===== PANEL IZQUIERDO - LISTA DE RUTINAS =====
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_TEXTO_AZUL, 1),
            "📋 Mis Rutinas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            COLOR_TEXTO_AZUL
        ));
        panelIzquierdo.setPreferredSize(new Dimension(280, 0));
        panelIzquierdo.setBackground(COLOR_FONDO);
        
        modeloLista = new DefaultListModel<>();
        listaRutinas = new JList<>(modeloLista);
        listaRutinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaRutinas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listaRutinas.setForeground(COLOR_TEXTO_NORMAL);
        listaRutinas.setBackground(COLOR_TABLA_FONDO);
        
        JScrollPane scrollLista = new JScrollPane(listaRutinas);
        scrollLista.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 1));
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);
        
        add(panelIzquierdo, BorderLayout.WEST);
        
        // ===== PANEL CENTRAL - TABLA DE DETALLES =====
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_TEXTO_AZUL, 1),
            "🏋️ Ejercicios de la rutina",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            COLOR_TEXTO_AZUL
        ));
        panelCentral.setBackground(COLOR_FONDO);
        
        // Columnas de la tabla
        String[] columnas = {"#", "Ejercicio", "Series", "Repeticiones", "Notas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDetalle.setForeground(COLOR_TEXTO_NORMAL);
        tablaDetalle.setBackground(COLOR_TABLA_FONDO);
        tablaDetalle.setRowHeight(28);
        tablaDetalle.setSelectionBackground(new Color(200, 210, 230));
        tablaDetalle.setGridColor(new Color(230, 230, 240));
        tablaDetalle.setShowGrid(true);
        
        // Estilo del encabezado de la tabla
        JTableHeader header = tablaDetalle.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setForeground(COLOR_TEXTO_AZUL);
        header.setBackground(COLOR_ENCABEZADO);
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        
        // Anchos de columna
        tablaDetalle.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaDetalle.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaDetalle.getColumnModel().getColumn(2).setPreferredWidth(60);
        tablaDetalle.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaDetalle.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        JScrollPane scrollTabla = new JScrollPane(tablaDetalle);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 1));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // ===== PANEL DE BOTONES =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        // Crear botones con estilo (texto AZUL OSCURO, fondo gris claro)
        btnNuevaRutina = crearBotonConEstilo("➕ Nueva Rutina");
        btnEditarRutina = crearBotonConEstilo("✏️ Editar");
        btnEliminarRutina = crearBotonConEstilo("🗑️ Eliminar");
        btnExportarPDF = crearBotonConEstilo("📄 Exportar");
        btnRefrescar = crearBotonConEstilo("🔄 Refrescar");
        
        panelBotones.add(btnNuevaRutina);
        panelBotones.add(btnEditarRutina);
        panelBotones.add(btnEliminarRutina);
        panelBotones.add(btnExportarPDF);
        panelBotones.add(btnRefrescar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // ===== EVENTOS =====
        listaRutinas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleRutina();
            }
        });
        
        btnNuevaRutina.addActionListener(e -> nuevaRutina());
        btnEditarRutina.addActionListener(e -> editarRutina());
        btnEliminarRutina.addActionListener(e -> eliminarRutina());
        btnExportarPDF.addActionListener(e -> exportarPDF());
        btnRefrescar.addActionListener(e -> cargarRutinas());
    }
    
    /**
     * Crea un botón con estilo profesional
     * ✅ Texto en AZUL OSCURO
     * ✅ Fondo gris claro
     * ✅ Borde azul oscuro
     * ✅ Efecto hover
     */
    private JButton crearBotonConEstilo(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boton.setForeground(COLOR_BOTON_TEXTO);     // 🔵 Texto AZUL OSCURO
        boton.setBackground(COLOR_BOTON_FONDO);     // Fondo gris claro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BOTON_BORDE, 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover: fondo gris más oscuro, texto sigue azul
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
    
    /**
     * Carga las rutinas desde la base de datos
     */
    private void cargarRutinas() {
        modeloLista.clear();
        try {
            List<Rutina> rutinas = GestorDatos.cargarRutinasPorUsuario(usuarioActual.getId());
            for (Rutina r : rutinas) {
                modeloLista.addElement(r);
            }
            System.out.println("✅ Rutinas cargadas: " + rutinas.size());
        } catch (Exception e) {
            System.err.println("❌ Error cargando rutinas: " + e.getMessage());
        }
    }
    
    /**
     * Refresca la lista de rutinas
     */
    public void refrescar() {
        cargarRutinas();
        mostrarDetalleRutina();
    }
    
    /**
     * Muestra los ejercicios de la rutina seleccionada
     */
    private void mostrarDetalleRutina() {
        modeloTabla.setRowCount(0);
        
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada != null) {
            try {
                List<DetalleRutina> detalles = GestorDatos.cargarDetallesRutina(seleccionada.getId());
                for (DetalleRutina detalle : detalles) {
                    String nombreEjercicio = "Ejercicio #" + detalle.getIdEjercicio();
                    if (controladorEjercicios != null) {
                        modelo.Ejercicio ej = controladorEjercicios.buscarPorId(detalle.getIdEjercicio());
                        if (ej != null) {
                            nombreEjercicio = ej.getNombre();
                        }
                    }
                    modeloTabla.addRow(new Object[]{
                        detalle.getOrden(),
                        nombreEjercicio,
                        detalle.getSeries(),
                        detalle.getRepeticiones(),
                        detalle.getNotas() != null && !detalle.getNotas().isEmpty() ? detalle.getNotas() : "—"
                    });
                }
                System.out.println("📋 Detalles cargados: " + detalles.size());
            } catch (Exception e) {
                System.err.println("❌ Error cargando detalles: " + e.getMessage());
            }
        }
    }
    
    /**
     * Crea una nueva rutina
     */
    private void nuevaRutina() {
        DialogoRutina dialogo = new DialogoRutina(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            null, 
            usuarioActual,
            controladorEjercicios
        );
        dialogo.setVisible(true);
        refrescar();
    }
    
    /**
     * Edita la rutina seleccionada
     */
    private void editarRutina() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una rutina para editar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DialogoRutina dialogo = new DialogoRutina(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            seleccionada, 
            usuarioActual,
            controladorEjercicios
        );
        dialogo.setVisible(true);
        refrescar();
    }
    
    /**
     * Elimina la rutina seleccionada
     */
    private void eliminarRutina() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una rutina para eliminar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar la rutina '" + seleccionada.getNombre() + "'?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = GestorDatos.eliminarRutina(seleccionada.getId());
            if (eliminado) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Rutina eliminada correctamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescar();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error al eliminar la rutina", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Exporta la rutina seleccionada
     */
    private void exportarPDF() {
        Rutina seleccionada = listaRutinas.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una rutina para exportar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        fileChooser.setSelectedFile(new java.io.File(seleccionada.getNombre().replaceAll(" ", "_") + ".txt"));
        
        int resultado = fileChooser.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
                if (!ruta.endsWith(".txt")) {
                    ruta += ".txt";
                }
                
                try (java.io.FileWriter writer = new java.io.FileWriter(ruta)) {
                    writer.write("====================================\n");
                    writer.write("FIDNESS APP - RUTINA DE ENTRENAMIENTO\n");
                    writer.write("====================================\n\n");
                    writer.write("RUTINA: " + seleccionada.getNombre() + "\n");
                    writer.write("USUARIO: " + usuarioActual.getNombre() + "\n");
                    writer.write("FECHA: " + seleccionada.getFechaCreacionFormateada() + "\n");
                    writer.write("\nEJERCICIOS:\n");
                    writer.write("------------------------------------\n\n");
                    
                    List<DetalleRutina> detalles = GestorDatos.cargarDetallesRutina(seleccionada.getId());
                    int orden = 1;
                    for (DetalleRutina d : detalles) {
                        writer.write(orden++ + ". ");
                        if (controladorEjercicios != null) {
                            modelo.Ejercicio ej = controladorEjercicios.buscarPorId(d.getIdEjercicio());
                            if (ej != null) {
                                writer.write(ej.getNombre());
                            } else {
                                writer.write("Ejercicio #" + d.getIdEjercicio());
                            }
                        }
                        writer.write("\n   Series: " + d.getSeries() + " x Repeticiones: " + d.getRepeticiones() + "\n");
                        if (d.getNotas() != null && !d.getNotas().isEmpty()) {
                            writer.write("   Notas: " + d.getNotas() + "\n");
                        }
                        writer.write("\n");
                    }
                }
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Rutina exportada correctamente a:\n" + ruta, 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (HeadlessException | IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error al exportar: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}