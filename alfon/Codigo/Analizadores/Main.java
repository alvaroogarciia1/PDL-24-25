package Analizadores;

import java.io.*;
import tablas.Token;

public class Main {
    
    public static void main(String[] args) throws IOException {
        
        File codigoFuente = new File("C:\\Users\\amari\\OneDrive\\Documentos\\GitHub\\PDL-24-25\\alfon\\Codigo\\Analizadores\\prueba3.txt");
        GestorErrores gestorErrores = new GestorErrores("C:\\Users\\amari\\OneDrive\\Documentos\\GitHub\\PDL-24-25\\alfon\\Codigo\\Analizadores\\errores.txt");
        
        // Instanciar el Analizador Léxico
        AnalizadorLexico AL = new AnalizadorLexico(codigoFuente, gestorErrores);

        // Leer tokens en un bucle
        Token token;
        do {
            token = AL.leerToken();  // Leer un token
            System.out.println(token.toString());  // Imprimir el token en la consola
        } while (!token.getIdentifier().equals("EOF"));  // Repetir hasta encontrar el token EOF
    }
}
