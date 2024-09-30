package Tablas;

import java.util.*;

public class TablaPR {

    private HashMap<Integer,String> tablaPR = new HashMap<Integer, String>();

    public TablaPR(){

        tablaPR.put(0,"FUNCTION");
        tablaPR.put(1,"BOOLEAN");
        tablaPR.put(2,"TRUE");
        tablaPR.put(3,"FALSE");
        tablaPR.put(4,"LET");
        tablaPR.put(5,"INT");
        tablaPR.put(6,"STRING");
        tablaPR.put(7,"PRINT");
        tablaPR.put(8,"INPUT");
        tablaPR.put(9,"RETURN");
        tablaPR.put(10,"IF");
        tablaPR.put(11,"FOR");

    }

    public boolean buscar(String cadena){
        return tablaPR.containsValue(cadena);
    }
}

