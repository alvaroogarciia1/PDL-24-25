package Analizadores;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


public class GestorErrores {
    
    static String error = "Errores:";
    File errores;
    FileWriter escritura;
    BufferedWriter bufferEscritura;
    String parse;
    boolean imprimir = true;

    public GestorErrores(String f) throws IOException {

    }

    public String getError(){
        return error;
    }

    public boolean getImprimir(){
        return imprimir;
    }

    public void setParse(String p){
        parse = p;
    }

    public void producirError(int n, int linea, String caracter) throws IOException{
    
        switch(n){
            case 1: 
                error += "\nError 1. La variable no existe. Linea: " + linea;
                break;
            case 2:
                error += "\nError 2. Error en el caracter: " + caracter + " Linea: " + linea;
                break;
            case 3:
                error += "\nError 3. Valor fuera de rango. Linea: " + linea;
                break;
            case 4:
                error += "\nError 4. Cadena sin comillas de cierre. Linea: " + linea;
                break;
            case 5:
                error += "\nError 5. Funcion no declarada. Linea: " + linea;
                break;
            case 6:
                error += "\nError 6. Nombre de la variable ya en uso. Linea: " + linea;
                break;
            case 7:
                error += "\nError 7. Error lexico en el lexema: " + caracter + ". Linea: " + linea;
                break;
            case 8:
                error += "\nError 7. Caracter no esperado. Linea: " + linea;                
                break;
            case 9:
                error += "\nError 8. Fallo en lectura de caracter. Linea" + linea;
                break;
            case 10:
                error += "\nError 9. Token no valido: " + caracter + ". Linea: " + linea;
                break;
            case 11:
                error += "\nError 10. Sentencia mal escrita. Linea: "+ linea;
                System.out.println("Casilla vacia: [" + caracter  + "]. Linea: "+ linea);
                break;
            case 12:
                error += "\nError 11. No $";
                break;
            case 13:
                error += "\nError 12. Tipos de expresion incorrectos. Linea: " + linea;
                break;
            case 14:
                error += "\nError 13. Funcion no declarada. Linea: "+ linea;
                break;
            case 15: 
                error += "\nError 14. Tipos incorrectos en la asignacion. Linea: " + (linea-1);
                break;
            case 16:
                error += "\nError 15. Tipo de variable input incorrecta. Linea: " + linea;
                break;
            case 17:
                error += "\nError 16. Tipo de retorno no valido. Linea: " + (linea-1);
                break;
            case 18:
                error += "\nError 17. Error en los parametros de la llamada a una funcion. Linea: " + linea;
                break;
            case 19:
                error += "\nError 18. Tipos de la expresion incompatibles. Linea: " + linea;
                break;
            case 20:
                error += "\nError 19. Tipos del Switch incompatibles. Linea: " + linea + "\n";
                break;
        }

        if(n == 2 || n == 3 || n == 4 || n == 5 || n == 6 || n == 7 || n == 8 || n == 9 || n == 10 || n == 11){
            imprimirError(error);
            System.exit(n);
        }

        imprimir = false;

        File ficheroParse = new File("parse.txt");
        FileWriter escrituraParse = new FileWriter(ficheroParse);
        escrituraParse.write(parse);
        escrituraParse.close();
    }

    // Imprime errores
    public void imprimirError(String error) throws IOException{
        File errores = new File("errores.txt");
        FileWriter fwError = new FileWriter(errores);
        fwError.write(error);
        fwError.close();
    }
}