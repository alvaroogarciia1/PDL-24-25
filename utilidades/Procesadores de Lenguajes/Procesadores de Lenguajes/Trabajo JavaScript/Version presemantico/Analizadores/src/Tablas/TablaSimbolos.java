package Tablas;

import java.util.*;

public class TablaSimbolos {
    private HashMap<Integer,String> tablaSimbolos;
    private String nombre_tabla;
    private int pos;
    private int numeroTabla;

    public TablaSimbolos(String nombre_tabla, int numeroTabla){
        tablaSimbolos = new HashMap<Integer, String>();
        this.nombre_tabla = nombre_tabla;
        this.pos = 0;
        this.numeroTabla = numeroTabla;
    }

    public void insertar (String lexema){
        tablaSimbolos.put(pos, lexema);
        pos++;
    }

    public boolean buscar(String lexema){   
        return tablaSimbolos.containsValue(lexema);
    }

    public int getPos(String lexema){
        Iterator<Integer> it = tablaSimbolos.keySet().iterator();
        while(it.hasNext()){
            int x = it.next();
            if(tablaSimbolos.get(x).equals(lexema)) return x;
        }
        return -1;
    }

    public String imprimir(){
        Iterator<Integer> it = tablaSimbolos.keySet().iterator();
        String r = nombre_tabla + " #" + numeroTabla + ":\n";
        while(it.hasNext()){
            if(it.hasNext()) r += " * LEXEMA : '" + tablaSimbolos.get(it.next()) + "' \n";
            else r += " * LEXEMA : '" + tablaSimbolos.get(it.next()) + "'";
        }
        return r;
    }
}
