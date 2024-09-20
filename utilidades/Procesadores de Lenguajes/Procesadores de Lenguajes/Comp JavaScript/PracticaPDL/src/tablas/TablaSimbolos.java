package tablas;

import java.util.*;

public class TablaSimbolos {
    
    public Map<Integer,ObjetoTS> simbolos;
    private String nombre;
    private int numeroTabla;
    private int despl;

    public TablaSimbolos(String nombre, int numeroTabla){
        this.simbolos = new HashMap<Integer, ObjetoTS>();
        this.nombre = nombre;
        this.numeroTabla = numeroTabla;
        this.despl = 0;
    }

    public int getNumeroTabla(){
        return numeroTabla;
    }

    public int getDespl(){
        return this.despl;
    }

    public void setDespl(int d){
        this.despl = d;
    }

    public void anadir(String simbolo, Integer posicion){
        ObjetoTS obj = new ObjetoTS(simbolo);
        simbolos.put(posicion, obj);
    }

    public boolean buscar(String simbolo){
        Iterator<Integer> it = simbolos.keySet().iterator();
        while(it.hasNext()){
            if(simbolos.get(it.next()).getLexema().equals(simbolo)) return true;
        }        
        return false;
    }

    public boolean buscarPos(Integer posicion){
        Iterator<Integer> it = simbolos.keySet().iterator();
        while(it.hasNext()){
            if(posicion == it.next()) return true;
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

    public int compareTo(TablaSimbolos ts1, TablaSimbolos ts2){
        if(ts1.numeroTabla < ts2.numeroTabla) return 0;
        else return -1;
    }

    public String imprimir(){
        String r = nombre + " # " + (numeroTabla+1) +  ":\n";
        Iterator<Integer> it = simbolos.keySet().iterator();
        while(it.hasNext()){
            ObjetoTS obj = simbolos.get(it.next());
            //System.out.println(obj.getLexema());
            r += obj.imprimirId(); 
        }   
        r += "\n";
        //System.out.println(r);
        return r;
    }  
}