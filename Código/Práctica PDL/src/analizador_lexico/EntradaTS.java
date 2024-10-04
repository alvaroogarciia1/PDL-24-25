package analizador_lexico;

/**
 * Clase para modelizar las entradas de la tabla de símbolos.
 */
public class EntradaTS {

    /**
     * El tipo del identificador.
     */
    private String tipoEntradaTS;

    /**
     * El desplazamiento del identificador.
     */
    private int desplazamientoEntradaTS;

    /**
     * El nombre del identificador o lexema.
     */
    private String lexemaEntradaTS;

    /**
     * Constructor de la clase EntradaTS.
     * 
     * @param tipoEntradaTS           EL tipo del identificador.
     * @param desplazamientoEntradaTS El desplazamiento del identificador.
     * @param lexemaEntradaTS         El nombre del identificador o lexema.
     */
    public EntradaTS(String tipoEntradaTS, int desplazamientoEntradaTS, String lexemaEntradaTS) {
        this.tipoEntradaTS = tipoEntradaTS;
        this.desplazamientoEntradaTS = desplazamientoEntradaTS;
        this.lexemaEntradaTS = lexemaEntradaTS;
    }

    /**
     * Devuelve eL tipo del identificador.
     * 
     * @return El tipo del identificador.
     */
    public String getTipoEntradaTS() {
        return tipoEntradaTS;
    }

    /**
     * Establece el nuevo tipo del identificador.
     * 
     * @param tipoEntradaTS El nuevo tipo del identificador.
     */
    public void setTipoEntradaTS(String tipoEntradaTS) {
        this.tipoEntradaTS = tipoEntradaTS;
    }

    /**
     * Devuelve eL desplazamiento del identificador.
     * 
     * @return El desplazamiento del identificador.
     */
    public int getDesplazamientoEntradaTS() {
        return desplazamientoEntradaTS;
    }

    /**
     * Establece el nuevo desplazamiento del identificador.
     * 
     * @param desplazamientoEntradaTS El nuevo desplazamiento del identificador.
     */
    public void setDesplazamientoEntradaTS(int desplazamientoEntradaTS) {
        this.desplazamientoEntradaTS = desplazamientoEntradaTS;
    }

    /**
     * Devuelve el nombre del identificador o lexema.
     * 
     * @return El nombre del identificador o lexema.
     */
    public String getLexemaEntradaTS() {
        return lexemaEntradaTS;
    }

    /**
     * Establece el nuevo nombre del identificador o lexema.
     * 
     * @param lexemaEntradaTS El nuevo nombre del identificador o lexema.
     */
    public void setLexemaEntradaTS(String lexemaEntradaTS) {
        this.lexemaEntradaTS = lexemaEntradaTS;
    }

    // IMPLEMENTAR CON EL FORMATO QUE SE TENGA QUE IMPLEMENTAR QUE NO ME ACUERDO :D
    @Override
    public String toString() {
        return null;
    }
}
