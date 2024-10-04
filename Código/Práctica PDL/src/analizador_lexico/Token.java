package analizador_lexico;

/**
 * Clase para modelizar Tokens.
 */
public class Token {

    /**
     * El código que identifica el tipo de Token.
     */
    private String codigoToken;

    /**
     * El atributo del Token (puede ser un número (Integer), una cadena (String) o
     * nulo).
     */
    private Object atributo;

    /**
     * Constructor de la clase Token.
     *
     * @param codigoToken El código que identifica el tipo de Token.
     * @param atributo    El atributo del Token (puede ser un número (Integer), una
     *                    cadena (String) o nulo).
     */
    public Token(String codigoToken, Object atributo) {
        this.codigoToken = codigoToken;
        this.atributo = atributo;
        validarAtributo();
    }

    /**
     * Devuelve el código que identifica el tipo de Token.
     *
     * @return El código que identifica el tipo de Token.
     */
    public String getCodigoToken() {
        return codigoToken;
    }

    /**
     * Establece el código que identifica el tipo de Token.
     *
     * @param codigoToken El nuevo código que identifica el tipo de Token.
     */
    public void setCodigoToken(String codigoToken) {
        this.codigoToken = codigoToken;
    }

    /**
     * Devuelve el atributo del Token (puede ser un número (Integer), una cadena
     * (String) o nulo).
     *
     * @return El atributo del Token (puede ser un número (Integer), una cadena
     *         (String) o nulo).
     */
    public Object getAtributo() {
        return atributo;
    }

    /**
     * Establece el atributo del Token (puede ser un número (Integer), una cadena
     * (String) o nulo).
     *
     * @param atributo El nuevo atributo del Token (puede ser un número (Integer),
     *                 una cadena (String) o nulo).
     */
    public void setAtributo(Object atributo) {
        this.atributo = atributo;
        // Comprobamos si el nuevo atributo es correcto.
        validarAtributo();
    }

    /**
     * Devuelve una representación en cadena del Token.
     *
     * @return Una cadena con el código y el atributo del Token.
     */
    @Override
    public String toString() {
        String r = "<" + codigoToken;
        if (atributo == null) {
            r += ", >";
        }
        r += (", " + atributo + ">");
        return r;
    }

    /**
     * Valida el atributo del Token en función de su código.
     *
     * @throws IllegalArgumentException Si el atributo no es válido para el código
     *                                  del Token.
     */
    private void validarAtributo() {
        // Si es un entero...
        if (codigoToken.equals("ENTERO") || codigoToken.equals("ID")) {
            if (!(atributo instanceof Integer)) {
                throw new IllegalArgumentException(
                        "El atributo debe ser un número (Integer) para el código " + codigoToken);
            }
            // Si es una cadena...
        } else if (codigoToken.equals("CADENA")) {
            if (!(atributo instanceof String)) {
                throw new IllegalArgumentException(
                        "El atributo debe ser una cadena (String) para el código " + codigoToken);
            }
        } else {
            if (atributo != null) {
                throw new IllegalArgumentException("No debe haber atributo para el código " + codigoToken);
            }
        }
    }

}
