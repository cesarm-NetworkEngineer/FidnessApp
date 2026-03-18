/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import modelo.Ejercicio;
import modelo.TipoEjercicio;
import modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de guardar y cargar datos usando serialización.
 * 
 * En mis clases de Rocky Linux 9, siempre digo: 
 * "Si no guardas los datos, no existen".
 * La serialización es nuestra amiga para persistencia básica.
 * 
 * En Infinite Computer Solutions usábamos bases de datos enormes,
 * pero para un proyecto como este, la serialización alcanza y sobra.
 * Es como usar una libreta en lugar de una base de datos Oracle.
 * 
 * @author César Alonso Morera Alpízar
 */
public class GestorDatos {
    
    private static final String ARCHIVO_USUARIOS = "datos/usuarios.dat";
    private static final String ARCHIVO_EJERCICIOS = "datos/ejercicios.dat";
    
    /**
     * Guarda la lista de usuarios en archivo
     * 
     * @param usuarios lista de usuarios a guardar
     * @throws IOException si hay problemas de escritura
     */
    public static void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        crearDirectorioSiNoExiste();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
        }
    }
    
    /**
     * Carga la lista de usuarios desde archivo
     * 
     * @return lista de usuarios (vacía si no existe el archivo)
     * @throws IOException si hay problemas de lectura
     * @throws ClassNotFoundException si la clase no coincide
     */
    @SuppressWarnings("unchecked")
    public static List<Usuario> cargarUsuarios() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) {
            // Si no hay usuarios, devolvemos lista vacía
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(archivo))) {
            return (List<Usuario>) ois.readObject();
        }
    }
    
    /**
     * Guarda la lista de ejercicios en archivo
     */
    public static void guardarEjercicios(List<Ejercicio> ejercicios) throws IOException {
        crearDirectorioSiNoExiste();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_EJERCICIOS))) {
            oos.writeObject(ejercicios);
        }
    }
    
    /**
     * Carga la lista de ejercicios desde archivo
     * Si no existe, crea ejercicios por defecto para que la app no se vea vacía
     */
    @SuppressWarnings("unchecked")
    public static List<Ejercicio> cargarEjercicios() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_EJERCICIOS);
        if (!archivo.exists()) {
            // Si no hay ejercicios, creamos algunos por defecto
            return crearEjerciciosPorDefecto();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(archivo))) {
            return (List<Ejercicio>) ois.readObject();
        }
    }
    
    /**
     * Crea el directorio de datos si no existe
     */
    private static void crearDirectorioSiNoExiste() {
        File directorio = new File("datos");
        if (!directorio.exists()) {
            boolean creado = directorio.mkdirs();
            if (creado) {
                System.out.println("📁 Directorio 'datos' creado exitosamente");
            }
        }
    }
    
    /**
     * Ejercicios por defecto para que la app no se vea vacía
     * Estos son los que uso en mis clases para que los estudiantes tengan ejemplos
     */
    private static List<Ejercicio> crearEjerciciosPorDefecto() {
        List<Ejercicio> ejercicios = new ArrayList<>();
        
        ejercicios.add(new Ejercicio(1, "Press de Banca", 
            "Acostado en banca plana, baja la barra hasta el pecho y empuja hacia arriba. Mantén los codos a 45 grados del cuerpo.", 
            TipoEjercicio.PECHO, 
            "https://youtu.be/ejemplo-press", 
            "https://ejemplo.com/press.jpg"));
            
        ejercicios.add(new Ejercicio(2, "Sentadilla", 
            "Con barra sobre el trapecio, baja como si fueras a sentarte en una silla. Mantén la espalda recta y la mirada al frente.", 
            TipoEjercicio.PIERNA, 
            "https://youtu.be/ejemplo-sentadilla", 
            "https://ejemplo.com/sentadilla.jpg"));
            
        ejercicios.add(new Ejercicio(3, "Curl de Bíceps con Mancuernas", 
            "De pie, con codos pegados al cuerpo, sube las mancuernas hacia los hombros. Baja controlado.", 
            TipoEjercicio.BRAZO, 
            "https://youtu.be/ejemplo-curl", 
            "https://ejemplo.com/curl.jpg"));
            
        ejercicios.add(new Ejercicio(4, "Dominadas en Barra", 
            "Agarrar la barra con palmas al frente (agarre prono). Sube hasta que la barbilla supere la barra. Baja controlado.", 
            TipoEjercicio.ESPALDA, 
            "https://youtu.be/ejemplo-dominadas", 
            "https://ejemplo.com/dominadas.jpg"));
            
        ejercicios.add(new Ejercicio(5, "Plancha Abdominal", 
            "Apoyado en antebrazos y puntas de pies. Mantén el cuerpo recto como una tabla. No dejes caer la cadera.", 
            TipoEjercicio.ABDOMEN, 
            "https://youtu.be/ejemplo-plancha", 
            "https://ejemplo.com/plancha.jpg"));
            
        ejercicios.add(new Ejercicio(6, "Peso Muerto", 
            "Con barra en el suelo, agarre a lo ancho de hombros. Empuja con las piernas y sube manteniendo la espalda recta.", 
            TipoEjercicio.FULLBODY, 
            "https://youtu.be/ejemplo-pesomuerto", 
            "https://ejemplo.com/pesomuerto.jpg"));
        
        return ejercicios;
    }
}