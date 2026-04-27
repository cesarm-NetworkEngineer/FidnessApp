@echo off
echo ======================================
echo   FIDNESS APP - EJECUCION DIRECTA
echo ======================================
echo.

REM Establecer classpath con todos los JARs necesarios
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;build\classes
set CLASSPATH=%CLASSPATH%;lib\sqlite-jdbc-3.42.0.0.jar
set CLASSPATH=%CLASSPATH%;lib\slf4j-api-2.0.9.jar
set CLASSPATH=%CLASSPATH%;lib\slf4j-simple-2.0.9.jar

REM Ejecutar la aplicacion
java -cp "%CLASSPATH%" main.FidnessApp

pause
