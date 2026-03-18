# 🏋️‍♂️ Fidness App - Tu compañero de entrenamiento

*"Una aplicación que nació de ver a mis estudiantes llegar al gimnasio con papeles arrugados en la bolsa"*

## 📝 ¿De qué va esto?

Cuando empecé a diseñar esta solución, me puse en los zapatos de mis estudiantes de Hotmart y la Universidad Cenfotec. Ellos necesitan algo intuitivo, que refleje cómo piensa realmente un entrenador. 

Esta no es solo una aplicación para entregar un proyecto. Son los bloques con los que mis alumnos (y ahora yo) construiremos conocimiento real sobre programación cliente/servidor.

## 👨‍💻 El culpable de todo esto

**César Alonso Morera Alpízar**
- Estudiante de Ingeniería en Sistemas (a punto de graduarse, ¡ya casi!)
- Instructor en Hotmart y Universidad Cenfotec (me la paso explicando esto)
- Experiencia en Infinite Computer Solutions, DXC Technology, Syniverse (ahí aprendí lo que es presión de verdad)

> *"En mis años dando soporte, aprendí que la seguridad no es negociable. Por eso acá las contraseñas van con hash, igual que protegemos redes empresariales."*

## 📋 ¿Qué necesito para que esto corra?

- **Java 17** o superior (nada de versiones viejas, porfa)
- **NetBeans IDE** (el que usamos en clase, pero cualquier IDE sirve)
- **Windows, Linux o macOS** (no discrimino sistemas operativos)

## 🚀 Manos a la obra: cómo ejecutar

### Opción 1: La fácil (desde NetBeans)
1. Abrís NetBeans (seguro ya lo tenés abierto para esto)
2. File → Open Project → Buscás la carpeta `FidnessApp`
3. Click derecho sobre el proyecto → Clean and Build (por si las moscas)
4. Ejecutás la clase `main.FidnessApp` y ¡a magic happens!

### Opción 2: La de pro (terminal)
```bash
cd FidnessApp
javac -d bin src/**/*.java
java -cp bin main.FidnessApp