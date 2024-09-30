package Analizadores;

import java.io.File;
import java.io.IOException;

public class Main {
    static File leerFich;
    static AnalizadorSintactico AS;
   
    public static void main(String[] args) throws IOException{
        leerFich = new File("PIdG20.txt");
        AS = new AnalizadorSintactico(leerFich);
        AS.parse();
    }
}
