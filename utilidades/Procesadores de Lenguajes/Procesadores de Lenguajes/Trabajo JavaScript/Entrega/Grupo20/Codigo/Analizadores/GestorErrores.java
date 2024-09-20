package Analizadores;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GestorErrores{

    String nombre_fich;
    static String error = "Errores:";
    boolean imprimir = false;

    public GestorErrores(){
        
    }
    
    public void error(int linea, int code, String car) throws IOException{
        switch(code){
            case 1: 
                error += "\nError 1: El caracter '" + car + "' no es valido. Linea: " + linea;
                break;
            case 2: 
                error += "\nError 2: La variable '" + car + "' ya esta inicializada. Linea: " + linea;
                break;
            case 3: 
                error += "\nError 3: La variable '" + car + "' no existe. Linea: " + linea;
                break;
            case 4:
                error += "\nError 4: Valor fuera de rango. Linea: " + linea;
                break;
            case 5:
                error += "\nError 5: Tipo de expresion incorrecta. Linea: " + linea;
                break;
            case 6: 
                error += "\nError 6: Tipo del id '" + car + "' no coincidentes. Linea: " + (linea-1);
                break;
            case 7: 
                error += "\nError 7: Los parametros de llamada de la funcion '" + car + "' no coinciden. Linea " + linea;
                break;
            case 8:    
                error += "\nError 8: Tipo no coincidente. Linea " + linea;
                break;
            case 9: 
                error += "\nError 9: La funcion no devuelve el tipo inicializado. Linea " + (linea-1);
                break;
            case 10: 
                error += "\nError 10: Token invalido. Linea " + linea;
                break;
            case 11:
                error += "\nError 11: Sentencia mal escrita. Linea " + linea;
                break; 
            case 12:
                error += "\nError 12: La funcion '" + car + "' no existe. Linea " + linea;
                break;

        }
        imprimir = true;
        if(code == 1 || code == 10 || code == 11){
            imprimir();
            System.exit(1);
        }
    }
    
    public void imprimir() throws IOException{
        if(imprimir){
            File errores = new File("errores.txt");
            FileWriter fwError = new FileWriter(errores);
            fwError.write(error);
            fwError.close();
        }
    }
}