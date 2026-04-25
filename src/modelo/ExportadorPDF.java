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
 * Implementación de Exportador para formato PDF REAL usando iText.
 * 
 * Genera archivos PDF legítimos que pueden abrirse con cualquier lector PDF
 * (Adobe Acrobat, Chrome, Edge, Foxit, etc.)
 * 
 * @author César Alonso Morera Alpízar
 */
public class ExportadorPDF implements Exportador {
    
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
        
        // ========== GENERAR PDF REAL USANDO iTEXT ==========
        // Usar try-with-resources para asegurar cierre correcto del stream
        try (FileOutputStream fos = new FileOutputStream(rutaDestino)) {
            
            // Crear documento en tamaño carta
            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, fos);
            
            document.open();
            
            // 1. Título principal
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph titulo = new Paragraph("🏋️ FidnessApp - Mi Rutina", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);
            
            // Línea decorativa
            Paragraph linea = new Paragraph("━".repeat(60));
            linea.setAlignment(Element.ALIGN_CENTER);
            document.add(linea);
            document.add(Chunk.NEWLINE);
            
            // 2. Información de la rutina
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            
            Paragraph nombreRutina = new Paragraph();
            nombreRutina.add(new Chunk("📌 Nombre: ", boldFont));
            nombreRutina.add(new Chunk(rutina.getNombre(), normalFont));
            document.add(nombreRutina);
            
            Paragraph fecha = new Paragraph();
            fecha.add(new Chunk("📅 Fecha: ", boldFont));
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            fecha.add(new Chunk(fechaActual, normalFont));
            document.add(fecha);
            
            Paragraph totalEjs = new Paragraph();
            totalEjs.add(new Chunk("📊 Total ejercicios: ", boldFont));
            totalEjs.add(new Chunk(String.valueOf(rutina.getDetalles().size()), normalFont));
            document.add(totalEjs);
            
            document.add(Chunk.NEWLINE);
            
            // 3. Tabla de ejercicios
            PdfPTable tabla = new PdfPTable(4); // 4 columnas: #, Ejercicio, Series, Reps
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(15);
            tabla.setSpacingAfter(15);
            
            // Anchos de columnas
            float[] columnWidths = {0.5f, 3f, 0.8f, 0.8f};
            tabla.setWidths(columnWidths);
            
            // Encabezados
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            BaseColor headerColor = new BaseColor(70, 130, 200);
            
            String[] headers = {"#", "Ejercicio", "Series", "Reps"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                tabla.addCell(cell);
            }
            
            // Datos de los ejercicios
            for (DetalleRutina detalle : rutina.getDetalles()) {
                // Columna 1: Orden
                PdfPCell cellOrden = new PdfPCell(new Phrase(String.valueOf(detalle.getOrden()), normalFont));
                cellOrden.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellOrden.setPadding(6);
                tabla.addCell(cellOrden);
                
                // Columna 2: Nombre del ejercicio
                tabla.addCell(new Phrase(detalle.getEjercicio().getNombre(), normalFont));
                
                // Columna 3: Series
                PdfPCell cellSeries = new PdfPCell(new Phrase(String.valueOf(detalle.getSeries()), normalFont));
                cellSeries.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cellSeries);
                
                // Columna 4: Repeticiones
                PdfPCell cellReps = new PdfPCell(new Phrase(String.valueOf(detalle.getRepeticiones()), normalFont));
                cellReps.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cellReps);
            }
            
            document.add(tabla);
            
            // 4. Mensaje motivacional al final
            document.add(Chunk.NEWLINE);
            Paragraph lineaFinal = new Paragraph("━".repeat(60));
            lineaFinal.setAlignment(Element.ALIGN_CENTER);
            document.add(lineaFinal);
            
            Font motiFont = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC);
            Paragraph motivacion = new Paragraph(
                "✨ ¡Felicidades! Has creado tu rutina personalizada.\n" +
                "La constancia es la clave del éxito. ¡Tú puedes! 💪",
                motiFont
            );
            motivacion.setAlignment(Element.ALIGN_CENTER);
            motivacion.setSpacingBefore(10);
            document.add(motivacion);
            
            document.add(Chunk.NEWLINE);
            
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
            Paragraph footer = new Paragraph("Generado por FidnessApp - Tu compañero de entrenamiento", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            // Cerrar documento
            document.close();
            
            return true;
            
        } catch (Exception e) {
            throw new Exception("Error al generar el PDF: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String getExtension() {
        return ".pdf";
    }
    
    @Override
    public String getNombreFormato() {
        return "Documento PDF (formato real con iText)";
    }
}