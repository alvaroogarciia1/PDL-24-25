package tablas;

import java.util.*;

public class TablaSimbolos {
    
    private Map<Integer,ObjetoTS> simbolos;
    private String nombre;
    private int pos;

    public TablaSimbolos(String nombre){
        this.simbolos = new HashMap<Integer, ObjetoTS>();
        this.nombre = nombre;
        this.pos = 0;
    }

    public void anadir(String simbolo){
        ObjetoTS obj = new ObjetoTS(simbolo);
        simbolos.put(pos, obj);
        pos++;
    }

    public boolean buscar(String simbolo){
        Iterator<Integer> it = simbolos.keySet().iterator();
        while(it.hasNext()){
            if(simbolos.get(it.next()).getLexema().equals(simbolo)) return true;
        }        
        return false;
    }

    public int getPos(String simbolo){
        Iterator<Integer> it = simbolos.keySet().iterator();
        while(it.hasNext()){
            Integer aux = it.next();
            if(simbolos.get(aux).getLexema().equals(simbolo)) return aux;
        }    
        return -1;
    }

    // SOLO VALE SIN ANALIZADOR SEMANTICO ---->>>> CAMBIAR 
    public String imprimir(){
        String r = nombre + " # 1:\n";
        Iterator<Integer> it = simbolos.keySet().iterator();
        while(it.hasNext()){
            ObjetoTS obj = simbolos.get(it.next());
            if(it.hasNext()) r += "* LEXEMA : '" + obj.getLexema() + "'\n";
            else  r += "* LEXEMA : '" + obj.getLexema() + "'";
        }   
        return r;
    }  
}