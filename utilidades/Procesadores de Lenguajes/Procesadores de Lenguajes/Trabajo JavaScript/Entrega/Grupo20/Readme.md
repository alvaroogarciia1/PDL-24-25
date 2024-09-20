# Procesador de lenguajes
Este es el procesador de lenguajes desarrollado por el grupo 20 para la asignatura de Procesadores de lenguajes por:
- Elías Dorado Rodríguez
    - **DNI**: 50578243R
    - **Matrícula**: a18i018
    - **Correo**: e.dorado@alumnos.upm.es  

- Roberto José Peris Acedo
    - **DNI**: 53815090N
    - **Matrícula**: a18i014
    - **Correo**: rj.peris@alumnos.upm.es  

- Álvaro Vega Motrel
    - **DNI**: 47312774A
    - **Matrícula**: a18i045
    - **Correo**: alvaro.vega.motrel@alumnos.upm.es
  

## Ejecución

Para la correcta ejecución del procesador, se debe introducir en la carpeta que contenga el archivo **Analizadores.jar**  un archivo llamado _PIdG20.txt_ que contenga el código a procesar. 

### **En Windows:**
Ejecutar el archivo **run_windows.bat**

### **En Linux/UNIX:**
Ejecutar el archivo **run_unix.sh**  
&nbsp;  
&nbsp;    
**La correcta ejecución del procesador generará como resultado los siguientes archivos:**
- _parse.txt_
- _TablaSimbolos.txt_
- _Tokens.txt_
&nbsp;   
&nbsp;  

## Técnica de análisis:
**Descendente con tablas**

## Elementos reconocidos por el procesador:
- **Sentencia repetitiva** _for_
- **Operadores:** _+_, _-_, _|=_, _&&_, _==_, _!=_
- **Comentario de línea** (_//_)
- **Tipos:** _function_, _void_, _boolean_, _string_, _int_
- **Cadena con comillas dobles** (_" "_)
- **Constantes lógicas** _true_ y _false_
