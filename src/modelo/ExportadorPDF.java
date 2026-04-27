/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ===============================================================
 * EXPORTADOR A PDF - FIDNESS APP
 * ===============================================================
 * 
 * Autor: César Alonso Morera Alpízar
 * 
 * Descripción:
 * Esta clase implementa la interfaz Exportador para generar archivos PDF
 * reales y profesionales utilizando la librería iText.
 * 
 * ¿Qué hace este exportador?
 * - Convierte una rutina de entrenamiento en un documento PDF legítimo
 * - El PDF puede abrirse con Adobe Acrobat, Chrome, Edge, Foxit, etc.
 * - Incluye tabla de ejercicios, formato profesional y mensajes motivacionales
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Abril 2026
 */
public class ExportadorPDF implements Exportador {
    
    private static final long serialVersionUID = 1L;
    
    // ============================================================
    // COLORES CORPORATIVOS
    // ============================================================
    private static final BaseColor COLOR_AZUL_CORPORATIVO = new BaseColor(25, 25, 112);  // Midnight Blue
    private static final BaseColor COLOR_BLANCO = new BaseColor(255, 255, 255);
    
    // ============================================================
    // EXPORTAR RUTINA A PDF
    // ============================================================
    
    @Override
    public boolean exportar(Rutina rutina, String rutaDestino) throws Exception {
        
        // ========== VALIDACIONES ==========
        if (rutina == null) {
            throw new IllegalArgumentException("❌ La rutina no puede ser nula");
        }
        
        if (rutina.getDetalles() == null || rutina.getDetalles().isEmpty()) {
            throw new IllegalStateException("❌ No se puede exportar una rutina sin ejercicios");
        }
        
        // Asegurar extensión .pdf
        if (!rutaDestino.toLowerCase().endsWith(".pdf")) {
            rutaDestino += ".pdf";
        }
        
        System.out.println("📄 Generando PDF para la rutina: " + rutina.getNombre());
        System.out.println("   Ejercicios a incluir: " + rutina.getDetalles().size());
        
        // ========== GENERAR PDF ==========
        try (FileOutputStream fos = new FileOutputStream(rutaDestino)) {
            
            // Crear documento en tamaño carta
            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, fos);
            document.open();
            
            // ====================================================
            // SECCIÓN 1: TÍTULO
            // ====================================================
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, COLOR_AZUL_CORPORATIVO);
            Paragraph titulo = new Paragraph("🏋️ FIDNESS APP - Mi Rutina de Entrenamiento", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);
            
            // Línea decorativa
            Paragraph linea = new Paragraph("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            linea.setAlignment(Element.ALIGN_CENTER);
            linea.setSpacingAfter(15);
            document.add(linea);
            
            // ====================================================
            // SECCIÓN 2: INFORMACIÓN DE LA RUTINA
            // ====================================================
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            
            Paragraph nombreRutina = new Paragraph();
            nombreRutina.add(new Chunk("📌 NOMBRE: ", boldFont));
            nombreRutina.add(new Chunk(rutina.getNombre(), normalFont));
            nombreRutina.setSpacingAfter(8);
            document.add(nombreRutina);
            
            Paragraph fecha = new Paragraph();
            fecha.add(new Chunk("📅 FECHA: ", boldFont));
            fecha.add(new Chunk(rutina.getFechaCreacionFormateada(), normalFont));
            fecha.setSpacingAfter(8);
            document.add(fecha);
            
            Paragraph totalEj = new Paragraph();
            totalEj.add(new Chunk("📊 TOTAL EJERCICIOS: ", boldFont));
            totalEj.add(new Chunk(String.valueOf(rutina.getDetalles().size()), normalFont));
            totalEj.setSpacingAfter(20);
            document.add(totalEj);
            
            // ====================================================
            // SECCIÓN 3: TABLA DE EJERCICIOS
            // ====================================================
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(15);
            tabla.setSpacingAfter(20);
            
            // Anchos de columnas
            float[] columnWidths = {3f, 1f, 1f, 2f};
            tabla.setWidths(columnWidths);
            
            // Encabezados de la tabla (con fondo azul)
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_BLANCO);
            
            String[] headers = {"Ejercicio", "Series", "Reps", "Notas"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(COLOR_AZUL_CORPORATIVO);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(10);
                tabla.addCell(cell);
            }
            
            // Datos de los ejercicios
            for (DetalleRutina detalle : rutina.getDetalles()) {
                // Columna 1: Nombre del ejercicio
                String nombreEjercicio = "Ejercicio #" + detalle.getIdEjercicio();
                if (detalle.getEjercicio() != null) {
                    nombreEjercicio = detalle.getEjercicio().getNombre();
                }
                PdfPCell cellEjercicio = new PdfPCell(new Phrase(nombreEjercicio, normalFont));
                cellEjercicio.setPadding(8);
                tabla.addCell(cellEjercicio);
                
                // Columna 2: Series
                PdfPCell cellSeries = new PdfPCell(new Phrase(String.valueOf(detalle.getSeries()), normalFont));
                cellSeries.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellSeries.setPadding(8);
                tabla.addCell(cellSeries);
                
                // Columna 3: Repeticiones
                PdfPCell cellReps = new PdfPCell(new Phrase(String.valueOf(detalle.getRepeticiones()), normalFont));
                cellReps.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellReps.setPadding(8);
                tabla.addCell(cellReps);
                
                // Columna 4: Notas
                String notas = (detalle.getNotas() != null && !detalle.getNotas().isEmpty()) 
                                ? detalle.getNotas() : "—";
                tabla.addCell(notas);
            }
            
            document.add(tabla);
            
            // ====================================================
            // SECCIÓN 4: NOTAS ADICIONALES (si existen)
            // ====================================================
            boolean hayNotas = false;
            for (DetalleRutina detalle : rutina.getDetalles()) {
                if (detalle.getNotas() != null && !detalle.getNotas().trim().isEmpty()) {
                    hayNotas = true;
                    break;
                }
            }
            
            if (hayNotas) {
                Paragraph notasTitulo = new Paragraph("📝 NOTAS ADICIONALES:", boldFont);
                notasTitulo.setSpacingAfter(8);
                document.add(notasTitulo);
                
                for (DetalleRutina detalle : rutina.getDetalles()) {
                    if (detalle.getNotas() != null && !detalle.getNotas().trim().isEmpty()) {
                        String prefijo = (detalle.getEjercicio() != null) ? 
                            "   • " + detalle.getEjercicio().getNombre() + ": " : 
                            "   • Ejercicio: ";
                        Paragraph nota = new Paragraph(prefijo + detalle.getNotas());
                        nota.setSpacingAfter(5);
                        document.add(nota);
                    }
                }
                document.add(Chunk.NEWLINE);
            }
            
            // ====================================================
            // SECCIÓN 5: MENSAJE MOTIVACIONAL Y PIE DE PÁGINA
            // ====================================================
            Font motiFont = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, COLOR_AZUL_CORPORATIVO);
            Paragraph motivacion = new Paragraph(
                "✨ ¡Felicidades! Has creado tu rutina personalizada.\n" +
                "   La constancia es la clave del éxito. ¡Tú puedes! 💪",
                motiFont
            );
            motivacion.setAlignment(Element.ALIGN_CENTER);
            motivacion.setSpacingBefore(15);
            motivacion.setSpacingAfter(10);
            document.add(motivacion);
            
            // Pie de página
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
            Paragraph footer = new Paragraph("Generado por FidnessApp - Tu compañero de entrenamiento", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            // Fecha y hora de generación
            String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph fechaGeneracion = new Paragraph("Documento generado el: " + ahora, footerFont);
            fechaGeneracion.setAlignment(Element.ALIGN_CENTER);
            fechaGeneracion.setSpacingBefore(5);
            document.add(fechaGeneracion);
            
            // ========== CIERRE DEL DOCUMENTO ==========
            document.close();
            
            System.out.println("✅ PDF generado exitosamente en: " + rutaDestino);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error generando PDF: " + e.getMessage());
            throw new Exception("Error al generar PDF: " + e.getMessage(), e);
        }
    }
    
    // ============================================================
    // MÉTODOS DE LA INTERFAZ
    // ============================================================
    
    @Override
    public String getExtension() {
        return ".pdf";
    }
    
    @Override
    public String getNombreFormato() {
        return "Documento PDF (formato profesional)";
    }
}