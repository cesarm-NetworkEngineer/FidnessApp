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
 * Reflexión:
 * En mis años como instructor, siempre digo a mis estudiantes:
 * "Un PDF bien formateado es como una carta de presentación de tu trabajo".
 * 
 * @author César Alonso Morera Alpízar
 * @version 2.0 - Abril 2026
 */
public class ExportadorPDF implements Exportador {
    
    // ============================================================
    // COLORES CORPORATIVOS (usando com.itextpdf.text.BaseColor)
    // ============================================================
    // Usamos los mismos colores que la interfaz gráfica:
    // - Azul oscuro corporativo (#191970) para encabezados
    // - Gris claro para mantener la coherencia visual
    // ============================================================
    private static final BaseColor COLOR_AZUL_CORPORATIVO = new BaseColor(25, 25, 112);  // Midnight Blue
    private static final BaseColor COLOR_GRIS_CLARO = new BaseColor(240, 240, 245);
    private static final BaseColor COLOR_BLANCO = new BaseColor(255, 255, 255);
    private static final BaseColor COLOR_GRIS_TEXTO = new BaseColor(100, 100, 120);
    
    /**
     * ============================================================
     * EXPORTAR RUTINA A PDF
     * ============================================================
     * 
     * @param rutina La rutina de entrenamiento a exportar
     * @param rutaDestino Ruta donde se guardará el archivo PDF
     * @return true si la exportación fue exitosa
     * @throws Exception Si ocurre algún error durante la generación del PDF
     */
    @Override
    public boolean exportar(Rutina rutina, String rutaDestino) throws Exception {
        
        // ========================================================
        // PASO 1: VALIDACIONES DE ENTRADA
        // ========================================================
        // Validar que la rutina no sea nula
        if (rutina == null) {
            throw new IllegalArgumentException("❌ No se puede exportar: La rutina es nula");
        }
        
        // Validar que la rutina tenga al menos un ejercicio
        if (rutina.getDetalles() == null || rutina.getDetalles().isEmpty()) {
            throw new IllegalStateException("❌ No se puede exportar: La rutina no tiene ejercicios");
        }
        
        // Validar que la ruta de destino sea válida
        if (rutaDestino == null || rutaDestino.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Ruta de destino inválida");
        }
        
        // Asegurar que la extensión sea .pdf (como buena práctica)
        if (!rutaDestino.toLowerCase().endsWith(".pdf")) {
            rutaDestino += ".pdf";
        }
        
        System.out.println("📄 Generando PDF profesional para la rutina: " + rutina.getNombre());
        System.out.println("   Ejercicios a incluir: " + rutina.getDetalles().size());
        
        // ========================================================
        // PASO 2: GENERACIÓN DEL PDF
        // ========================================================
        // Usamos try-with-resources para asegurar que el archivo se cierre
        // correctamente, incluso si ocurre una excepción
        try (FileOutputStream fos = new FileOutputStream(rutaDestino)) {
            
            // Crear documento en tamaño carta (8.5" x 11")
            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, fos);
            
            // Abrir el documento para comenzar a escribir
            document.open();
            
            // ====================================================
            // SECCIÓN 1: TÍTULO Y ENCABEZADO
            // ====================================================
            
            // Fuente para el título principal (20 puntos, negrita)
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, COLOR_AZUL_CORPORATIVO);
            Paragraph titulo = new Paragraph("🏋️ FidnessApp - Mi Rutina de Entrenamiento", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);
            
            // Línea decorativa para separar el título del contenido
            Paragraph lineaDecorativa = new Paragraph("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            lineaDecorativa.setAlignment(Element.ALIGN_CENTER);
            lineaDecorativa.setSpacingAfter(10);
            document.add(lineaDecorativa);
            
            document.add(Chunk.NEWLINE);
            
            // ====================================================
            // SECCIÓN 2: INFORMACIÓN DE LA RUTINA
            // ====================================================
            
            // Definir fuentes
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font italicaFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            
            // Nombre de la rutina
            Paragraph nombreRutina = new Paragraph();
            nombreRutina.add(new Chunk("📌 NOMBRE: ", boldFont));
            nombreRutina.add(new Chunk(rutina.getNombre(), normalFont));
            nombreRutina.setSpacingAfter(8);
            document.add(nombreRutina);
            
            // Fecha de creación
            Paragraph fecha = new Paragraph();
            fecha.add(new Chunk("📅 FECHA DE CREACIÓN: ", boldFont));
            String fechaFormateada = rutina.getFechaCreacionFormateada();
            fecha.add(new Chunk(fechaFormateada, normalFont));
            fecha.setSpacingAfter(8);
            document.add(fecha);
            
            // Total de ejercicios
            Paragraph totalEjercicios = new Paragraph();
            totalEjercicios.add(new Chunk("📊 TOTAL DE EJERCICIOS: ", boldFont));
            totalEjercicios.add(new Chunk(String.valueOf(rutina.getDetalles().size()), normalFont));
            totalEjercicios.setSpacingAfter(15);
            document.add(totalEjercicios);
            
            // ====================================================
            // SECCIÓN 3: TABLA DE EJERCICIOS
            // ====================================================
            
            // Crear tabla de 4 columnas: #, Ejercicio, Series, Repeticiones
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);           // Ocupar todo el ancho
            tabla.setSpacingBefore(15);              // Espacio antes de la tabla
            tabla.setSpacingAfter(20);               // Espacio después de la tabla
            
            // Configurar anchos de columnas (proporciones)
            float[] columnWidths = {0.6f, 3.5f, 0.8f, 0.8f};
            tabla.setWidths(columnWidths);
            
            // ====================================================
            // ENCABEZADOS DE LA TABLA
            // ====================================================
            // 🔴 CORREGIDO: Usar BaseColor.BLANCO en lugar de Color.WHITE
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, COLOR_BLANCO);
            
            String[] headers = {"#", "Ejercicio", "Series", "Reps"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(COLOR_AZUL_CORPORATIVO);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(10);
                tabla.addCell(cell);
            }
            
            // ====================================================
            // DATOS DE LOS EJERCICIOS
            // ====================================================
            for (DetalleRutina detalle : rutina.getDetalles()) {
                // Columna 1: Número de orden
                PdfPCell cellOrden = new PdfPCell(new Phrase(String.valueOf(detalle.getOrden()), normalFont));
                cellOrden.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellOrden.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellOrden.setPadding(8);
                tabla.addCell(cellOrden);
                
                // Columna 2: Nombre del ejercicio
                String nombreEjercicio = "Ejercicio #" + detalle.getIdEjercicio();
                if (detalle.getEjercicio() != null) {
                    nombreEjercicio = detalle.getEjercicio().getNombre();
                }
                PdfPCell cellEjercicio = new PdfPCell(new Phrase(nombreEjercicio, normalFont));
                cellEjercicio.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellEjercicio.setPadding(8);
                tabla.addCell(cellEjercicio);
                
                // Columna 3: Series
                PdfPCell cellSeries = new PdfPCell(new Phrase(String.valueOf(detalle.getSeries()), normalFont));
                cellSeries.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellSeries.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellSeries.setPadding(8);
                tabla.addCell(cellSeries);
                
                // Columna 4: Repeticiones
                PdfPCell cellReps = new PdfPCell(new Phrase(String.valueOf(detalle.getRepeticiones()), normalFont));
                cellReps.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellReps.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellReps.setPadding(8);
                tabla.addCell(cellReps);
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
                        String prefijo = detalle.getEjercicio() != null ? 
                            "   • " + detalle.getEjercicio().getNombre() + ": " : 
                            "   • Ejercicio: ";
                        Paragraph nota = new Paragraph(prefijo + detalle.getNotas(), italicaFont);
                        nota.setSpacingAfter(5);
                        document.add(nota);
                    }
                }
                document.add(Chunk.NEWLINE);
            }
            
            // ====================================================
            // SECCIÓN 5: PIE DE PÁGINA Y MENSAJE MOTIVACIONAL
            // ====================================================
            
            // Línea de cierre
            Paragraph lineaFinal = new Paragraph("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            lineaFinal.setAlignment(Element.ALIGN_CENTER);
            lineaFinal.setSpacingBefore(20);
            document.add(lineaFinal);
            
            // Mensaje motivacional (¡porque todos necesitamos ánimo!)
            Font motiFont = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, COLOR_AZUL_CORPORATIVO);
            Paragraph motivacion = new Paragraph(
                "✨ ¡Felicidades! Has creado tu rutina personalizada.\n" +
                "   La constancia es la clave del éxito. ¡Tú puedes! 💪",
                motiFont
            );
            motivacion.setAlignment(Element.ALIGN_CENTER);
            motivacion.setSpacingBefore(15);
            document.add(motivacion);
            
            document.add(Chunk.NEWLINE);
            
            // Firma de la aplicación
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, COLOR_GRIS_TEXTO);
            Paragraph footer = new Paragraph("Generado por FidnessApp - Tu compañero de entrenamiento", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);
            
            // Fecha y hora de generación del PDF
            String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph fechaGeneracion = new Paragraph("Documento generado el: " + ahora, footerFont);
            fechaGeneracion.setAlignment(Element.ALIGN_CENTER);
            document.add(fechaGeneracion);
            
            // ====================================================
            // PASO 3: CIERRE DEL DOCUMENTO
            // ====================================================
            document.close();
            
            System.out.println("✅ PDF generado exitosamente en: " + rutaDestino);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error crítico al generar el PDF: " + e.getMessage());
            throw new Exception("Error al generar el PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * ============================================================
     * EXTENSIÓN DEL ARCHIVO
     * ============================================================
     * 
     * @return La extensión .pdf para los archivos generados
     */
    @Override
    public String getExtension() {
        return ".pdf";
    }
    
    /**
     * ============================================================
     * NOMBRE DEL FORMATO
     * ============================================================
     * 
     * @return Descripción legible del formato de exportación
     */
    @Override
    public String getNombreFormato() {
        return "Documento PDF (formato profesional con iText)";
    }
}