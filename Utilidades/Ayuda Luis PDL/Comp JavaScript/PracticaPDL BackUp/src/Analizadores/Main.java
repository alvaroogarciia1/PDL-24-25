package Analizadores;

import java.io.*;

public class Main {
    
    public static void main(String[] args) throws IOException {
        
        File codigoFuente = new File("PIdG74.txt");

        // Archivo escritura para el parse
        File ficheroParse = new File("parse.txt");
        FileWriter escrituraParse = new FileWriter(ficheroParse);

        // Analizador Sintactico 
        AnalizadorSintactico AS = new AnalizadorSintactico(codigoFuente);
        escrituraParse.write(AS.parse());
        escrituraParse.close();
    }
}