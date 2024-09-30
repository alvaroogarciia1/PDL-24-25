package analizador_lexico;

/**
 * Clase para modelizar los Tokens.
 */
public class Token {

    /**
     * El código que representa el Token.
     */
    private String codigoToken;

    /**
     * El atributo del Token, puede ser un número (Integer), una cadena (String) o nulo.
     */
    private Object atributo;

    /**
     * Constructor de la clase Token.
     *
     * @param codigoToken El código que representa el Token.
     * @param atributo El atributo del Token, puede ser un número (Integer), una cadena (String) o nulo.
     */
    public Token(String codigoToken, Object atributo) {
        this.codigoToken = codigoToken;
        this.atributo = atributo;
        // Comprobamos que los atributos están correctos.
        validarAtributo();
    }

    /**
     * Método para validar el atributo basado en el código del Token.
     */
    private void validarAtributo() {
        // Para el caso de que el atributo sea un entero...
        if (codigoToken.equals("ENTERO") || codigoToken.equals("ID")) {
            if (!(atributo instanceof Integer)) {
                throw new IllegalArgumentException("El atributo debe ser un número (Integer) para el código " + codigoToken);
            }
        // Para el caso de que el atributo sea una cadena...
        } else if (codigoToken.equals("CADENA")) {
            if (!(atributo instanceof String)) {
                throw new IllegalArgumentException("El atributo debe ser una cadena (String) para el código " + codigoToken);
            }
        } else {
            // Si no es uno de los dos casos, no debe de tener atributo
            if (atributo != null) {
                throw new IllegalArgumentException("No debe haber atributo para el código " + codigoToken);
            }
        }
    }

    /**
     * Obtiene el código del Token.
     *
     * @return El código que representa el Token.
     */
    public String getCodigoToken() {
        return codigoToken;
    }

    /**
     * Establece el código del Token.
     *
     * @param codigoToken El nuevo código que representa el Token.
     */
    public void setCodigoToken(String codigoToken) {
        this.codigoToken = codigoToken;
    }

    /**
     * Obtiene el atributo del Token.
     *
     * @return El atributo del Token, que puede ser un número (Integer), una cadena (String) o nulo.
     */
    public Object getAtributo() {
        return atributo;
    }

    /**
     * Establece el atributo del Token.
     *
     * @param atributo El nuevo atributo del Token, puede ser un número (Integer), una cadena (String) o nulo.
     */
    public void setAtributo(Object atributo) {
        this.atributo = atributo;
        // Comprobamos que el atributo establecido es válido después de ser modificado.
        validarAtributo();
    }


    @Override
    public String toString() {
        if (atributo == null) {
			return "<" + codigoToken  + ", >";
        }
		return "<" + codigoToken + ", " + atributo + ">";
    }

    
}
