package Tablas;

import java.util.*;

public class TablaSimbolos {
    public HashMap<Integer,Parametros> tablaSimbolos;
    private String nombre_tabla;
    private int desp;
    private int numeroTabla;

    public TablaSimbolos(String nombre_tabla, int numeroTabla){
        this.tablaSimbolos = new HashMap<Integer, Parametros>();
        this.nombre_tabla = nombre_tabla;
        this.numeroTabla = numeroTabla;
        this.desp = 0;
    }

    public int getDesp(){
        return desp;
    }

    public void setDesp(int desp){
        this.desp = desp;
    }

    public void insertar(String lexema, int pos){
        tablaSimbolos.put(pos, new Parametros(lexema));
    }

    public boolean buscar(String lexema){   
        Iterator<Integer> it = tablaSimbolos.keySet().iterator();
        while(it.hasNext()){
            if(tablaSimbolos.get(it.next()).getLexema().equals(lexema)) return true; 
        }
        return false;
    }

    public boolean buscarPos(Integer posicion){
        Iterator<Integer> it = tablaSimbolos.keySet().iterator();
        while(it.hasNext()){
            if(posicion == it.next()) return true;
        }        
        return false;
    }

    public int getPos(String lexema){
        Iterator<Integer> it = tablaSimbolos.keySet().iterator();
        while(it.hasNext()){
            int x = it.next();
            if(tablaSimbolos.get(x).getLexema().equals(lexema)) return x;
        }
        return -1;
    }

    public int compareTo(TablaSimbolos ts1, TablaSimbolos ts2){
        if(ts1.numeroTabla < ts2.numeroTabla) return 0;
        else return -1;
    }

    public String imprimir(){
        Iterator<Integer> it = tablaSimbolos.keySet().iterator();
        String r = nombre_tabla + " #" + numeroTabla + ":\n";
        while(it.hasNext()){
            r += tablaSimbolos.get(it.next()).imprimir();
        }
        r += "\n";
        return r;
    }
}


