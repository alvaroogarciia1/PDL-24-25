package Analizadores;

import java.io.*;

public class Main {
    
    public static void main(String[] args) throws IOException {
        
        File codigoFuente = new File("PIdG74.txt");
        
        // Analizador Sintactico 
        AnalizadorSintactico AS = new AnalizadorSintactico(codigoFuente);
        String parse = AS.parse();
        AS.escribirParse(parse);
    }
}