package analizador_lexico;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase para modelar la Tabla de Símbolos.
 */
public class TablaDeSimbolos {

    /**
     * La Tabla de Símbolos.
     * Integer --> Posición de la entrada en la Tabla de Símbolos.
     * EntradaTS --> La entrada en la Tabla de Símbolos.
     */
    private Map<Integer, EntradaTS> tabla;

    /**
     * El número asignado a la tabla.
     */
    private int numTabla;

    /**
     * El mensaje de inicio asignado a la tabla (puede ser el simple nombre de la
     * tabla
     * o alguna especificación de ella).
     */
    private String mensaje;

    /**
     * La posicion de la entrada en la Tabla de Símbolos.
     */
    private int posicionEntradaTablaDeSimbolos;

    /**
     * Constructor de la clase TablaDeSímbolos.
     * 
     * @param numTabla El número asignado a la tabla.
     * @param mensaje  El mensaje de inicio asignado a la tabla (puede ser el simple
     *                 nombre de la
     *                 tabla
     *                 o alguna especificación de ella).
     */
    public TablaDeSimbolos(int numTabla, String mensaje) {
        this.tabla = new HashMap<Integer, EntradaTS>();
        this.numTabla = numTabla;
        this.mensaje = mensaje;
        this.posicionEntradaTablaDeSimbolos = 1;
    }

    /**
     * Devuelve la Tabla de Símbolos.
     * 
     * @return La Tabla de Símbolos.
     */
    public Map<Integer, EntradaTS> getTabla() {
        return tabla;
    }

    /**
     * Devuelve el número asignado a la tabla.
     * 
     * @return El número asignado a la tabla.
     */
    public int getNumTabla() {
        return numTabla;
    }

    /**
     * Establece el número asignado a la tabla.
     * 
     * @param numTabla El nuevo número asignado a la tabla.
     */
    public void setNumTabla(int numTabla) {
        this.numTabla = numTabla;
    }

    /**
     * Devuelve el mensaje de inicio asignado a la tabla (puede ser el simple nombre
     * de la tabla
     * o alguna especificación de ella).
     * 
     * @return El mensaje de inicio asignado a la tabla (puede ser el simple nombre
     *         de la tabla
     *         o alguna especificación de ella).
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje de inicio asignado a la tabla (puede ser el simple
     * nombre de la tabla
     * o alguna especificación de ella).
     * 
     * @param mensaje El nuevo mensaje de inicio asignado a la tabla (puede ser el
     *                simple nombre de la tabla
     *                o alguna especificación de ella).
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Devuelve la posicion de la entrada en la Tabla de Símbolos.
     * 
     * @return La posicion de la entrada en la Tabla de Símbolos.
     */
    public int getPosicionEntradaTablaDeSimbolos() {
        return posicionEntradaTablaDeSimbolos;
    }

    /**
     * Establece la posicion de la entrada en la Tabla de Símbolos.
     * 
     * @param posicionEntradaTablaDeSimbolos La nueva posicion de la entrada en la
     *                                       Tabla de Símbolos.
     */
    public void setPosicionEntradaTablaDeSimbolos(int posicionEntradaTablaDeSimbolos) {
        this.posicionEntradaTablaDeSimbolos = posicionEntradaTablaDeSimbolos;
    }

    /**
     * Destruye la Tabla de Símbolos (OJO, vaciar =/ destruir).
     */
    public void destruyeTablaDeSimbolos() {
        this.tabla = null;
    }

    /**
     * Vacía la Tabla de Símbolos (OJO, vaciar =/ destruir).
     */
    public void vaciaTablaDeSimbolos() {
        this.tabla.clear();
    }

    /**
     * Añadimos una nueva entrada a la Tabla de Símbolos.
     * 
     * @param entradaTS La entrada para la Tabla de Símbolos.
     */
    public void añadirEntradaTablaDeSimbolos(EntradaTS entradaTS) {
        this.tabla.put(posicionEntradaTablaDeSimbolos, entradaTS);
        posicionEntradaTablaDeSimbolos++;
    }

    /**
     * Completamos los atributos de la entrada {@code entradaTS} según corresponda
     * (si hay algunos atributos que no son necesarios, se pone null o 0 según
     * corresponda).
     * 
     * @param entradaTS      La entrada de la Tabla de Símbolos de la que vamos a
     *                       completar los atributos.
     * @param desplazamiento El desplazamiento de la entrada.
     * @param tipoDevuelto   El tipo devuelto de la entrada.
     * @param etiqueta       La etiqueta de la entrada.
     * @param parametros     Los parámetros de la entrada.
     */
    public void completarAtributosEntradaTablaDeSimbolos(EntradaTS entradaTS, int desplazamiento, String tipoDevuelto,
            String etiqueta, Map<String, Integer> parametros) {
        entradaTS.setDesplazamiento(desplazamiento);
        entradaTS.setTipoDevuelto(tipoDevuelto);
        entradaTS.setEtiqueta(etiqueta);
        if (parametros != null) {
            for (Entry<String, Integer> e : parametros.entrySet()) {
                String tipoP = e.getKey();
                Integer modoP = e.getValue();
                entradaTS.setParametros(tipoP, modoP);
            }
        }
    }

    /**
     * Buscamos en la Tabla de Símbolos si existe una entrada con lexema
     * {@code lexema} y tipo {@code tipo}.
     * 
     * @param lexema El lexema a buscar.
     * @param tipo   El tipo a buscar.
     * @return La entrada con el lexema y tipo indicado (y sus demás atributos si
     *         los tuviera), y null e.o.c.
     */
    public EntradaTS getEntradaTablaDeSimbolos(String lexema, String tipo) {
        EntradaTS r = null, aux = new EntradaTS(lexema, tipo), eTS = null;
        for (Entry<Integer, EntradaTS> t : tabla.entrySet()) {
            eTS = t.getValue();
            if (eTS.equals(aux)) {
                r = eTS;
                aux = null;
                break;
            }
        }
        return r;
    }

    /**
     * Devuelve en forma de cadena (con el formato correspondiente) la entrada (si
     * existe) con lexema {@code lexema} y tipo {@code tipo}.
     * 
     * @param lexema El lexema a buscar.
     * @param tipo   El tipo a buscar.
     * @return La entrada en forma de cadena si existe, y blanco e.o.c.
     */
    public String consultarEntradaTablaDeSimbolos(String lexema, String tipo) {
        return getEntradaTablaDeSimbolos(lexema, tipo) == null ? ""
                : getEntradaTablaDeSimbolos(lexema, tipo).toString();
    }

    /**
     * Devuelve en forma de cadena (con un formato establecido) el nombre y número
     * de la Tabla de Símbolos y sus entradas.
     * 
     * @return La tabla en forma de cadena.
     */
    @Override
    public String toString() {
        String r = mensaje + " # " + numTabla + " : \n";
        for (EntradaTS e : tabla.values()) {
            r += e.toString() + "\n";
        }
        return r + "\n---------------------------------------------------";
    }

}
