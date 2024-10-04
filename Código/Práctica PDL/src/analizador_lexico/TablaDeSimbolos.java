package analizador_lexico;

import java.util.*;

/**
 * Clase para representar una Tabla de Símbolos.
 */
public class TablaDeSimbolos {

    /**
     * Almacenamiento de las entradas.
     */
    private Map<String, EntradaTS> tabla;

    /**
     * Constructor de la clase TablaDeSimbolos.
     */
    public TablaDeSimbolos() {
        tabla = new HashMap<>();
    }

}
