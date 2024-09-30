package tablas;

import java.util.*;

public class TablaSimbolos {
    
    public Map<Integer,ObjetoTS> simbolos;
    private String nombre;
    private int numeroTabla;
    private int pos;
    private int despl;

    public TablaSimbolos(String nombre, int numeroTabla){
        this.simbolos = new HashMap<Integer, ObjetoTS>();
        this.nombre = nombre    ;
        this.numeroTabla = numeroTabla;
        this.pos = 0; // Pos para los id
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

    public void anadir(String simbolo){
        ObjetoTS obj = new ObjetoTS(simbolo);
        simbolos.put(pos, obj);
        pos++;
    }

    public void eliminar(String id){
        int p = this.getPos(id);
        this.simbolos.remove(p);
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