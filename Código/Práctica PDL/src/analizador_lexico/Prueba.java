package analizador_lexico;

public class Prueba {

    public static void main(String[] args) {
        TablaDeSimbolos t = new TablaDeSimbolos(1, "Contenido de la tabla");
        System.out.println("TABLA VACIA: \n" + t);
        EntradaTS e1 = new EntradaTS("HOLA", "entero"),
                e2 = new EntradaTS("ALFON", "entero");
        System.out.println("------------- ETIQUETAS ---------------");
        System.out.println(e1 + "\n\n" + e2);
        System.out.println("------------- LAS AÑADIMOS ---------------");
        t.añadirEntradaTablaDeSimbolos(e1);
        System.out.println(t.toString());
        t.añadirEntradaTablaDeSimbolos(e2);
        System.out.println(t.toString());
        System.out.println("------------- MODIFICAMOS SUS ATRIBUTOS ---------------");
        t.completarAtributosEntradaTablaDeSimbolos(e1, 2, null, null, null);
        System.out.println(t.toString());
        t.completarAtributosEntradaTablaDeSimbolos(e2, 2, null, null, null);
        System.out.println(t.toString());
        System.out.println("------------- CONSULTAMOS LAS ENTRADAS ---------------");
        System.out.println(t.consultarEntradaTablaDeSimbolos("ALFON", "entero"));
        System.out.println(t.consultarEntradaTablaDeSimbolos("PEPSSSE", "entero"));
        System.out.println("------------- CONSULTAMOS LAS ENTRADAS x2 ---------------");
        System.out.println(t.getEntradaTablaDeSimbolos("PEPE", "funcion"));
        System.out.println(t.getEntradaTablaDeSimbolos("HOLA", "entero"));
        System.out.println("Terminé.");
    }

}
