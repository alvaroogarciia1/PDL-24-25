package Tablas;

import java.util.ArrayList;

public class Parametros {
    
        String lexema;
        String tipo;
        int despl;
        int numPar;
        ArrayList<String> tipoPar;
        String tipoDev;
        String etiqueta;
    
        public Parametros(String lexema){
            this.lexema = lexema;
            this.tipo = "";
            this.despl = 0;
            this.numPar = 0;
            tipoPar = new ArrayList<String>();
            this.tipoDev = "";
            this.etiqueta = "";
        }
    
        public String getLexema(){
            return lexema;
        }
    
        public void setLexema(String lexema){
            this.lexema = lexema;
        }
    
        public String getTipo(){
            return tipo;
        }
    
        public void setTipo(String tipo){
            this.tipo = tipo;
        }
    
        public int getDespl(){
            return despl;
        }
    
        public void setDespl(int despl){
            this.despl = despl;
        }
    
        public int getNumPar(){
            return numPar;
        }
    
        public void setNumPar(int numPar){
            this.numPar = numPar;
        }
    
        public String getTipoDev(){
            return tipoDev;
        }
    
        public void setTipoDev(String tipoDev){
            this.tipoDev = tipoDev;
        }
    
        public ArrayList<String> getTipoPar(){
            return this.tipoPar;
        }
    
        public void setTipoPar(String tipoPar){
            this.tipoPar.add(tipoPar);
        }
    
        public String getEtiqueta(){
            return etiqueta;
        }
    
        public void setEtiqueta(String etiqueta){
            this.etiqueta = etiqueta;
        }
    
        public String imprimir(){
        String r = "";
    
        if(!this.getTipo().equals("function")){
            r = "* LEXEMA: '" + this.getLexema() + "'\n ATRIBUTOS: \n    + tipo: '" + this.getTipo() + "' \n     + despl: " + this.getDespl() + "\n";
        }
        else{
            r = "* LEXEMA: '" + this.getLexema() + "'\n ATRIBUTOS: \n    + tipo: '" + this.getTipo() + "' \n     + numParam: " + this.getNumPar() + "\n";
            for(int i = 0; i < this.tipoPar.size(); i++){
                if(!tipoPar.get(i).equals("void")){
                    r += "      + TipoParam" + (i+1) + ": '" + tipoPar.get(i) + "'\n";
                }
            }
            r += "     + TipoRetorno: '" + this.getTipoDev() + "'\n     + EtiqFuncion: '" + this.getEtiqueta() + "'\n";
        }
        return r;
        }
}
