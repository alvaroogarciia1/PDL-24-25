package Analizadores;

import java.io.*;
import java.util.*;
import tablas.*;

public class Main {
    
    public static void main(String[] args) throws IOException {
        // Archivo de lectura con codigo principal
        File codigoFuente = new File("PIdG74.txt");

        // Archivo de escritura para los Tokens
		File ficheroTokens = new File("tokens.txt");
        FileWriter escrituraTokens = new FileWriter(ficheroTokens);

        // Archivo de escritura para la tabla de simbolos
        File ficheroTablaSimbolos = new File("tablaSimbolos.txt");
        FileWriter escrituraTablaSimbolos = new FileWriter(ficheroTablaSimbolos);

        // ArrayList para la jerarquia de las Tablas de simbolos 
        ArrayList<TablaSimbolos> ats = new ArrayList<TablaSimbolos>(); 
        ats.add(new TablaSimbolos("Tabla Programa Principal")); // Tabla del programa principal(vacia)

        // Analizador Lexico. Genera los tokens.
		AnalizadorLexico AL = new AnalizadorLexico(codigoFuente, ats);
		Token token = new Token("");
        AL.leer();
        while(!token.getIdentifier().equals("EOF")){
			token=AL.leerToken();
            if(token != null) escrituraTokens.write(token.toString() + "\n");
        }
        escrituraTokens.close();

        // Archivo escritura para el parse
        File ficheroParse = new File("parse.txt");
        FileWriter escrituraParse = new FileWriter(ficheroParse);

        // Analizador Sintactico 
        AnalizadorSintactico AS = new AnalizadorSintactico(ficheroTokens);
        escrituraParse.write(AS.metodo());
        escrituraParse.close();

        escrituraTablaSimbolos.write(ats.get(0).imprimir());
        escrituraTablaSimbolos.close();
    }
}