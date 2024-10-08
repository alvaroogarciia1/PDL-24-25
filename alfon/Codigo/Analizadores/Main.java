package Analizadores;

import java.io.*;
import tablas.Token;

public class Main {

    public static void main(String[] args) throws IOException {

        File codigoFuente = new File("C:\\Users\\amari\\OneDrive\\Documentos\\GitHub\\PDL-24-25\\alfon\\Codigo\\Analizadores\\prueba2.txt");
        //GestorErrores gestorErrores = new GestorErrores("C:\\Users\\amari\\OneDrive\\Documentos\\GitHub\\PDL-24-25\\alfon\\Codigo\\Analizadores\\errores.txt");

        // Instanciar el Analizador Léxico
        AnalizadorLexico AL = new AnalizadorLexico(codigoFuente/* , gestorErrores*/);

        // Leer tokens en un bucle
        Token token;
        do {
            token = AL.leerToken();  // Leer un token
        } while (!token.getCodigoToken().equals("EOF"));  // Repetir hasta encontrar el token EOF

        // Cerrar el archivo de tokens
        AL.cerrarArchivoTokens();
    }
}
