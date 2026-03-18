/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package modelo;

/**
 * Enumerado para clasificar los ejercicios del catálogo.
 * 
 * En redes usamos VLANs para segmentar tráfico. Acá estos son mis VLANs:
 * separamos pierna de brazo, pecho de abdomen... pura lógica de ingeniero.
 * 
 * Cuando estudiaba para las certificaciones de networking, entendí que
 * segmentar es ordenar. Y el orden es la base de cualquier sistema.
 * 
 * @author César Alonso Morera Alpízar
 */
public enum TipoEjercicio {
    PIERNA,      // Cuádriceps, femoral, gemelos... las piernas nunca se saltan
    ESPALDA,     // Dorsales, romboides, trapecio... la espalda es la base
    BRAZO,       // Bíceps, tríceps, antebrazo... para lucir la camisa
    PECHO,       // Press, aperturas, fondos... el clásico de todo principiante
    ABDOMEN,     // Core, abdominales... los odia todo el mundo pero hay que hacerlos
    GLUTEO,      // Glúteo mayor, medio... más importante de lo que creen
    FULLBODY;    // Ejercicios que trabajan todo el cuerpo de una sola vez
    
    /**
     * Método útil para llenar combos en la interfaz gráfica.
     * 
     * En mis clases de Java ME, veía estudiantes tratando de formatear
     * strings a mano en cada ventana. Un desastre. Mejor tenerlo aquí,
     * una vez y ya. Como el principio DRY (Don't Repeat Yourself).
     * 
     * @return Array con los nombres bonitos (primera letra mayúscula)
     */
    public static String[] getNombres() {
        TipoEjercicio[] valores = TipoEjercicio.values();
        String[] nombres = new String[valores.length];
        
        for (int i = 0; i < valores.length; i++) {
            String nombre = valores[i].name();
            // Primera letra mayúscula, el resto minúscula
            // Así "PIERNA" se convierte en "Pierna" - más profesional
            nombres[i] = nombre.charAt(0) + nombre.substring(1).toLowerCase();
        }
        return nombres;
    }
    
    /**
     * Convierte un string a TipoEjercicio (case insensitive)
     * 
     * Útil cuando recibimos datos de la interfaz. El usuario puede escribir
     * "pierna", "PIERNA" o "Pierna" - todo funciona igual.
     * En soporte técnico aprendí: el usuario siempre escribe como se le da la gana.
     * 
     * @param texto el texto a convertir
     * @return el TipoEjercicio correspondiente, o PIERNA por defecto
     */
    public static TipoEjercicio fromString(String texto) {
        for (TipoEjercicio tipo : TipoEjercicio.values()) {
            if (tipo.name().equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        return PIERNA; // Por defecto, por si acaso
    }
}