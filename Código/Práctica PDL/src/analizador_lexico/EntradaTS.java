package analizador_lexico;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase para modelar las entradas de la tabla de símbolos.
 */
public class EntradaTS {

    /**
     * El nombre del identificador.
     */
    private String lexema;

    /**
     * El tipo del identificador.
     */
    private String tipo;

    /**
     * El desplazamiento del identificador (en funciones NO HAY).
     */
    private int desplazamiento;

    /**
     * Los parámetros del identificador (SOLO hay en funciones).
     * String --> Tipo del parámetro.
     * Integer --> Modo del parámetro (1 por referencia o 2 por valor).
     */
    private Map<String, Integer> parametros;

    /**
     * El tipo devuelto del identificador (SOLO hay en funciones).
     */
    private String tipoDevuelto;

    /**
     * La etiqueta asignada al identificador (SOLO hay en funciones).
     */
    private String etiqueta;

    /**
     * Constructor de la clase EntradaTS.
     *
     * @param lexema El nombre del identificador.
     * @param tipo   El tipo del identificador.
     */
    public EntradaTS(String lexema, String tipo) {
        this.lexema = lexema;
        this.tipo = tipo;
    }

    /**
     * Devuelve el nombre del identificador.
     * 
     * @return El nombre del identificador.
     */
    public String getLexema() {
        return lexema;
    }

    /**
     * Establece el nombre del identificador.
     * 
     * @param lexema El nuevo nombre del identificador.
     */
    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    /**
     * Devuelve el tipo del identificador.
     * 
     * @return El tipo del identificador.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo del identificador.
     * 
     * @param tipo El nuevo tipo del identificador.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve el desplazamiento del identificador.
     * 
     * @return El desplazamiento del identificador.
     */
    public int getDesplazamiento() {
        return desplazamiento;
    }

    /**
     * Establece el desplazamiento del identificador.
     * 
     * @param desplazamiento El nuevo desplazamiento del identificador.
     */
    public void setDesplazamiento(int desplazamiento) {
        this.desplazamiento = desplazamiento;
    }

    /**
     * Devuelve los parámetros del identificador.
     * 
     * @return Los parámetros del identificador.
     */
    public Map<String, Integer> getParametros() {
        return parametros;
    }

    /**
     * Establece el parámetro.
     * 
     * @param tipo El nuevo tipo del parámetro.
     * @param modo El nuevo modo del parámetro.
     */
    public void setParametros(String tipo, Integer modo){
        this.parametros.put(tipo, modo);
    }

    /**
     * Devuelve el tipo devuelto del identificador.
     * 
     * @return El tipo devuelto del identificador.
     */
    public String getTipoDevuelto() {
        return tipoDevuelto;
    }

    /**
     * Establece el tipo devuelto del identificador.
     * 
     * @param tipoDevuelto El nuevo tipo devuelto del identificador.
     */
    public void setTipoDevuelto(String tipoDevuelto) {
        this.tipoDevuelto = tipoDevuelto;
    }

    /**
     * Devuelve la etiqueta asignada al identificador.
     * 
     * @return La etiqueta asignada al identificador.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Establece la etiqueta asignada al identificador.
     * 
     * @param etiqueta La nueva etiqueta asignada al identificador.
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Devuelve en forma de cadena (con un formato establecido) las entradas de la tabla de símbolos.
     * 
     * @return La entrada en forma de cadena.
     */
    @Override
    public String toString(){
        int i=1;
        String r = "* LEXEMA : '" + lexema + "'\n ATRIBUTOS:\n + tipo: '" + tipo;
        /*Se asume que el tamaño del atributo de esta clase "parametros"
        * es > 1 cuando el tipo es "funcion" */
        if(tipo.equals("funcion")){
            r+="'\n  + numParam: " + parametros.size();
            for(Entry<String, Integer> e:parametros.entrySet()){
                String tipoP = e.getKey();
                Integer modoP = e.getValue();
                r+="\n   + TipoParam" + i + " : '" + tipoP + "\n    + ModoParam" + i + " : " + modoP;
                i++;
            }
        }else{
            r+= "'\n Despl: " + desplazamiento;
        }
        return r;
    }

}
