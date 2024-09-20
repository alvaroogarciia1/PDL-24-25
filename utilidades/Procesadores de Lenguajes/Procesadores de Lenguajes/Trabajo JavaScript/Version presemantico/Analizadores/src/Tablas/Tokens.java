package Tablas;

public class Tokens {
    
    private String codigo;
    private Object atributo = null;
    
    public Tokens(String codigo, String atributo){
        this.codigo = codigo;
        this.atributo = atributo;
    }

    public Tokens(String codigo, int atributo){
        this.codigo = codigo;
        this.atributo = atributo;
    }

    public Tokens(String codigo){
        this.codigo = codigo;
    }
    
    public String getCodigo(){
        return codigo;
    }

    public Object getAtributo(){
        return atributo;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public void setAtributo(String atributo){
        this.atributo = atributo;
    }

    public void setAtributo(int atributo){
        this.atributo = atributo;
    }

    public String toString(){
        if(this.atributo != null){
            return "<" + getCodigo() + " , " + getAtributo() + " >";
        } 
        else{
            return "<" + getCodigo() + ", >";
        }
    }
}