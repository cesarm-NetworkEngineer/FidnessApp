/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 * Interfaz que define el contrato para exportar rutinas.
 * 
 * En DXC Technology aprendí a pensar en extensiones, no en modificaciones.
 * Por eso creé esta interfaz. ¿Mañana alguien quiere exportar en HTML? 
 * Solo creamos ExportadorHTML. ¿En JSON para una app web? ExportadorJSON.
 * El sistema core no se toca. Eso es arquitectura limpia.
 * 
 * Es como los enchufes de la pared: definen un estándar (la interfaz)
 * y cada fabricante (implementación) puede hacer su aparato
 * siempre que respete el estándar.
 * 
 * @author César Alonso Morera Alpízar
 */
public interface Exportador extends Serializable {
    
    /**
     * Exporta una rutina a un formato específico
     * 
     * @param rutina La rutina a exportar
     * @param rutaDestino Ruta donde guardar el archivo
     * @return true si la exportación fue exitosa
     * @throws Exception Si ocurre algún error durante la exportación
     */
    boolean exportar(Rutina rutina, String rutaDestino) throws Exception;
    
    /**
     * Obtiene la extensión de archivo que usa este exportador
     * 
     * @return Ej: ".pdf", ".txt", ".json"
     */
    String getExtension();
    
    /**
     * Nombre descriptivo del formato
     * 
     * @return Ej: "Documento PDF", "Archivo de texto"
     */
    String getNombreFormato();
}