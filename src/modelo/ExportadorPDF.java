/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Implementación de Exportador para formato PDF.
 * 
 * NOTA: Esta es una versión simplificada que genera un archivo de texto
 * con formato similar a PDF. En la versión final usaríamos iText o Apache PDFBox,
 * pero para el proyecto esto demuestra el concepto de polimorfismo.
 * 
 * En mis cursos de Java, siempre digo: "primero funciona, después optimiza".
 * Acá tenemos la funcionalidad básica de exportar. Si el cliente quiere
 * PDFs "de verdad", implementamos iText y listo.
 * 
 * @author César Alonso Morera Alpízar
 */
public class ExportadorPDF implements Exportador {
    private static final long serialVersionUID = 1L;
    
    @Override
    public boolean exportar(Rutina rutina, String rutaDestino) throws Exception {
        // Validaciones básicas (siempre hay que validar, en producción y en proyectos)
        if (rutina == null) {
            throw new IllegalArgumentException("La rutina no puede ser null");
        }
        if (rutaDestino == null || rutaDestino.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta destino es obligatoria");
        }
        
        // Asegurar extensión .pdf
        if (!rutaDestino.toLowerCase().endsWith(".pdf")) {
            rutaDestino += ".pdf";
        }
        
        // Generar contenido con formato "como si fuera PDF"
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaDestino))) {
            // Encabezado con estilo - como un documento real
            writer.println("=".repeat(70));
            writer.println("                🏋️‍♂️ FIDNESS - RUTINA DE ENTRENAMIENTO");
            writer.println("=".repeat(70));
            writer.println();
            
            // Datos de la rutina
            writer.println("📋 RUTINA: " + rutina.getNombre());
            writer.println("📅 FECHA: " + rutina.getFechaCreacionFormateada());
            writer.println("🔢 TOTAL EJERCICIOS: " + rutina.getTotalEjercicios());
            writer.println();
            
            // Si no hay ejercicios, mensaje especial
            if (rutina.getTotalEjercicios() == 0) {
                writer.println("⚠️ Esta rutina no tiene ejercicios asignados.");
                writer.println("   Agrega ejercicios desde la aplicación para verlos aquí.");
                writer.println();
            } else {
                // Tabla de ejercicios - formato legible
                writer.println("-".repeat(90));
                writer.printf("| %-4s | %-25s | %-6s | %-6s | %s%n", 
                    "Ord", "Ejercicio", "Series", "Reps", "Descripción");
                writer.println("-".repeat(90));
                
                for (DetalleRutina detalle : rutina.getDetalles()) {
                    Ejercicio ej = detalle.getEjercicio();
                    String descripcionCorta = ej.getDescripcion();
                    if (descripcionCorta.length() > 35) {
                        descripcionCorta = descripcionCorta.substring(0, 32) + "...";
                    }
                    
                    writer.printf("| %-4d | %-25s | %-6d | %-6d | %s%n",
                        detalle.getOrden(),
                        ej.getNombre(),
                        detalle.getSeries(),
                        detalle.getRepeticiones(),
                        descripcionCorta);
                }
                
                writer.println("-".repeat(90));
            }
            
            writer.println();
            writer.println("=".repeat(70));
            writer.println("📌 Documento generado por Fidness App");
            writer.println("   Entrena inteligente, no solo duro");
            writer.println("=".repeat(70));
        }
        
        return true;
    }
    
    @Override
    public String getExtension() {
        return ".pdf";
    }
    
    @Override
    public String getNombreFormato() {
        return "Documento PDF (formato texto)";
    }
}