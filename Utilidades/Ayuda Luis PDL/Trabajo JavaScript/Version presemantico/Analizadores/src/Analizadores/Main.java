package Analizadores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Tablas.*;

public class Main {
    
    static FileWriter escribirTokens;
    static FileWriter escribirTS;
    static FileWriter escribirParse;
    static File leerFich;
    static Tokens token;
    static TablaSimbolos ts;

    
    public static void main(String[] args) throws IOException{
        leerFich = new File("PIdG20.txt");
        escribirTokens = new FileWriter("Tokens.txt");
        escribirTS = new FileWriter("TablaSimbolos.txt");
        escribirParse = new FileWriter("parse.txt");
        ts = new TablaSimbolos("Tabla de Simbolos Principal", 1);
        AnalizadorLexico AL = new AnalizadorLexico(leerFich,ts);
        AnalizadorSintactico AS = new AnalizadorSintactico();
        

        token = new Tokens("");
        AL.leer();
        while(!token.getCodigo().equals("EOF")){
            token = AL.leerToken();
            if(token.getCodigo().length() > 0) escribirTokens.write(token.toString() + "\n");
            AS.parse(token);
       }

       escribirTS.write(ts.imprimir());
       if(AS.getPila().peek().equals("$")) escribirParse.write(AS.getParse());;

       escribirParse.close();
       escribirTS.close();
       escribirTokens.close();
    }
}
