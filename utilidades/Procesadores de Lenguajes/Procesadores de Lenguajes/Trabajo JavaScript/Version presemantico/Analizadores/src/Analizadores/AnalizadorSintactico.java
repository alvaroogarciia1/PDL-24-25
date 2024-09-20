package Analizadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import Tablas.Tokens;

public class AnalizadorSintactico {
    ArrayList<String> terminales;
    Map<String, String> tabla;
    Stack<String> pila;
    String parse;


    public AnalizadorSintactico(){
        parse = "Descendente";
        pila = new Stack<String>();
        pila.push("$");
        pila.push("P");

        terminales = new ArrayList<String>();

        // Inicialiacion de los terminales de la gramatica
        terminales.add("id");
        terminales.add("entero");
        terminales.add("cadena");
        terminales.add("function");
        terminales.add("boolean");
        terminales.add("true");
        terminales.add("false");
        terminales.add("parenta");
        terminales.add("parentc");
        terminales.add("suma");
        terminales.add("resta");
        terminales.add("iguigu");
        terminales.add("desigu");
        terminales.add("and");
        terminales.add("igual");
        terminales.add("olog");
        terminales.add("let");
        terminales.add("int");
        terminales.add("string");
        terminales.add("print");
        terminales.add("input");
        terminales.add("pyc");
        terminales.add("coma");
        terminales.add("return");
        terminales.add("if");
        terminales.add("for");
        terminales.add("corcha");
        terminales.add("corchc");
        terminales.add("eof");

        // Gramatica sintáctica
        tabla = new HashMap<String,String>();
        // (1)P -> B P 
        tabla.put("P id","1 B P");
        tabla.put("P let", "1 B P");
        tabla.put("P print", "1 B P");
        tabla.put("P input", "1 B P");
        tabla.put("P return", "1 B P");
        tabla.put("P if", "1 B P");
        tabla.put("P for", "1 B P");

        // (2)P -> F P 
        tabla.put("P function","2 F P");

        // (3)P -> eof 
        tabla.put("P eof","3 eof");

        // (4)P -> lambda
        tabla.put("P $","4 lambda");

        // (5)B -> let T id ;
        tabla.put("B let","5 let T id pyc");

        // (6)B -> if ( E ) S
        tabla.put("B if", "6 if parenta E parentc S");

        // (7)B -> S
        tabla.put("B id", "7 S");
        tabla.put("B print", "7 S");
        tabla.put("B input", "7 S");
        tabla.put("B return", "7 S");

        // (8)B -> for ( I ; E ; I ) { C }
        tabla.put("B for","8 for parenta I pyc E pyc I parentc corcha C corchc");

        // (9)T -> int
        tabla.put("T int","9 int");

        // (10)T -> string 
        tabla.put("T string","10 string");

        // (11)T -> boolean
        tabla.put("T boolean","11 boolean");

        // (12)S -> id D ; 
        tabla.put("S id","12 id D pyc");

        // (13)S -> print ( E ) ;
        tabla.put("S print","13 print parenta E parentc pyc");

        // (14)S -> input ( id ) ;
        tabla.put("S input","14 input parenta id parentc pyc");

        // (15)S -> return X ;
        tabla.put("S return", "15 return X pyc");

        // (16)D -> M E 
        tabla.put("D igual","16 M E");
        tabla.put("D olog","16 M E");

        // (17)D -> ( L ) 
        tabla.put("D parenta", "17 parenta L parentc");

        // (18)X -> E
        tabla.put("X id","18 E");
        tabla.put("X entero","18 E");
        tabla.put("X cadena","18 E");
        tabla.put("X true","18 E");
        tabla.put("X false","18 E");
        tabla.put("X parenta","18 E");

        // (19)X -> lambda
        tabla.put("X pyc","19 lambda");

        // (20)C -> B C
        tabla.put("C id","20 B C");
        tabla.put("C let","20 B C");
        tabla.put("C print","20 B C");
        tabla.put("C input","20 B C");
        tabla.put("C return","20 B C");
        tabla.put("C if","20 B C");
        tabla.put("C for","20 B C");

        // (21)C -> lambda
        tabla.put("C corchc","21 lambda");

        // (22)L -> E Q
        tabla.put("L id","22 E Q");
        tabla.put("L entero","22 E Q");
        tabla.put("L cadena","22 E Q");
        tabla.put("L true","22 E Q");
        tabla.put("L false","22 E Q");
        tabla.put("L parenta","22 E Q");

        // (23)L -> lambda
        tabla.put("L parentc","23 lambda");

        // (24)Q -> , E Q
        tabla.put("Q coma","24 coma E Q");

        // (25)Q -> lambda
        tabla.put("Q parentc","25 lambda");

        // (26)F -> function id H ( A ) { C }
        tabla.put("F function","26 function id H parenta A parentc corcha C corchc");

        // (27)H -> T
        tabla.put("H boolean","27 T");
        tabla.put("H int","27 T");
        tabla.put("H string","27 T");

        // (28)H -> lambda
        tabla.put("H parenta","28 lambda");

        // (29)A -> T id K
        tabla.put("A boolean","29 T id K");
        tabla.put("A int","29 T id K");
        tabla.put("A string","29 T id K");

        // (30)A -> lambda
        tabla.put("A parentc","30 lambda");

        // (31)K -> , T id K
        tabla.put("K coma","31 coma T id K");

        // (32)K -> lambda
        tabla.put("K parentc","32 lambda");

        // (33)E -> R 1
        tabla.put("E id","33 R 1");
        tabla.put("E entero","33 R 1");
        tabla.put("E cadena","33 R 1");
        tabla.put("E true","33 R 1");
        tabla.put("E false","33 R 1");
        tabla.put("E parenta","33 R 1");

        // (34)1 -> && R 1
        tabla.put("1 and","34 and R 1");

        // (35)1 -> lambda
        tabla.put("1 parentc","35 lambda");
        tabla.put("1 pyc","35 lambda");
        tabla.put("1 coma","35 lambda");

        // (36)R -> U 2
        tabla.put("R id","36 U 2");
        tabla.put("R entero","36 U 2");
        tabla.put("R cadena","36 U 2");
        tabla.put("R true","36 U 2");
        tabla.put("R false","36 U 2");
        tabla.put("R parenta","36 U 2");

        // (37)2 -> G U 2
        tabla.put("2 iguigu","37 G U 2");
        tabla.put("2 desigu","37 G U 2");

        // (38)2 -> lambda
        tabla.put("2 parentc","38 lambda");
        tabla.put("2 and","38 lambda");
        tabla.put("2 pyc","38 lambda");
        tabla.put("2 coma","38 lambda");

        // (39)G -> ==
        tabla.put("G iguigu","39 iguigu");

        // (40)G -> !=
        tabla.put("G desigu","40 desigu");

        // (41)U -> V 3
        tabla.put("U id","41 V 3");
        tabla.put("U entero","41 V 3");
        tabla.put("U cadena","41 V 3");
        tabla.put("U true","41 V 3");
        tabla.put("U false","41 V 3");
        tabla.put("U parenta","41 V 3");

        // (42)3 -> J V 3
        tabla.put("3 suma","42 J V 3");
        tabla.put("3 resta","42 J V 3");

        // (43)3 -> lambda
        tabla.put("3 parentc","43 lambda");
        tabla.put("3 iguigu","43 lambda");
        tabla.put("3 desigu","43 lambda");
        tabla.put("3 and","43 lambda");  
        tabla.put("3 pyc","43 lambda");
        tabla.put("3 coma","43 lambda");

        // (44)J -> +
        tabla.put("J suma","44 suma");

        // (45)J -> -
        tabla.put("J resta","45 resta");

        // (46)V -> id Z
        tabla.put("V id","46 id Z");

        // (47)V -> ( E )
        tabla.put("V parenta","47 parenta E parentc");

        // (48)V -> entero
        tabla.put("V entero","48 entero");

        // (49)V -> cadena
        tabla.put("V cadena","49 cadena");

        // (50)V -> true
        tabla.put("V true","50 true");

        // (51)V -> false
        tabla.put("V false","51 false");

        // (52)Z -> ( L )
        tabla.put("Z parenta","52 parenta L parentc");

        // (53)Z -> lambda
        tabla.put("Z parentc","53 lambda");
        tabla.put("Z suma","53 lambda");
        tabla.put("Z resta","53 lambda");
        tabla.put("Z iguigu","53 lambda");
        tabla.put("Z desigu","53 lambda");
        tabla.put("Z and","53 lambda");
        tabla.put("Z pyc","53 lambda");
        tabla.put("Z coma","53 lambda");

        // (54)I -> id M E
        tabla.put("I id","54 id M E");

        // (55)I -> lambda
        tabla.put("I parentc","55 lambda");
        tabla.put("I pyc","55 lambda");

        // (56)M -> =
        tabla.put("M igual","56 igual");

        // (57)M -> |= 
        tabla.put("M olog","57 olog");
    }

    public String getParse(){
        return parse;
    }

    public Stack getPila(){
        return pila;
    }

    public void parse(Tokens token){

        String aux[];
        String union;
        int tamaño;
        boolean cont = false;
        String estado = token.getCodigo().toLowerCase();

        while(!cont){
//           System.out.println(estado);
//           System.out.println(pila.peek());
            if(terminales.contains(pila.peek())){
                if(pila.peek().equals(estado)){
                    pila.pop();
                    cont = true;
                }
                else{
                    System.out.println("Error: Token invalido");
                    pila.pop();
                }

            }
            else{
                union = pila.peek() + " " + estado;
                if(tabla.containsKey(union)){
                    aux = tabla.get(union).split(" ");
                    pila.pop();
                    tamaño = aux.length-1;
                    parse += " " + aux[0];
                    while(tamaño != 0) {
                        if(aux[tamaño].equals("lambda")){
                            tamaño--;
                        }else pila.push(aux[tamaño--]);
                    }
                }
                else {
                    System.out.println("Error: Celda vacía"); 
                    cont = true;
                }
            }
        }
        
        


    }
}
