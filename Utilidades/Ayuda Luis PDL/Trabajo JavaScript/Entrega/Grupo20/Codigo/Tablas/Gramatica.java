package Tablas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gramatica {
    ArrayList<String> terminales;
    Map<String, String> tabla;
    
    public Gramatica(){
        terminales = new ArrayList<String>();
        tabla = new HashMap<String,String>();

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

        // GRAMATICA 
        // (1)P -> B P 
        tabla.put("P id","1 B P {1}");
        tabla.put("P let", "1 B P {1}");
        tabla.put("P print", "1 B P {1}");
        tabla.put("P input", "1 B P {1}");
        tabla.put("P return", "1 B P {1}");
        tabla.put("P if", "1 B P {1}");
        tabla.put("P for", "1 B P {1}");

        // (2)P -> F P 
        tabla.put("P function","2 F P {2}");

        // (3)P -> eof 
        tabla.put("P eof","3 eof {3}");

        // (4)P -> lambda
        tabla.put("P $","4 lambda {4}");

        // (5)B -> let T id ;
        tabla.put("B let","5 let {5.1} T id {5.2} pyc {5.3}");

        // (6)B -> if ( E ) S
        tabla.put("B if", "6 if parenta E parentc S {6}");

        // (7)B -> S
        tabla.put("B id", "7 S {7}");
        tabla.put("B print", "7 S {7}");
        tabla.put("B input", "7 S {7}");
        tabla.put("B return", "7 S {7}");

        // (8)B -> for ( I ; E ; I ) { C }
        tabla.put("B for","8 for parenta I pyc E pyc I parentc corcha C corchc {8}");

        // (9)T -> int
        tabla.put("T int","9 int {9}");

        // (10)T -> string 
        tabla.put("T string","10 string {10}");

        // (11)T -> boolean
        tabla.put("T boolean","11 boolean {11}");

        // (12)S -> id D ; 
        tabla.put("S id","12 id D pyc {12}");

        // (13)S -> print ( E ) ;
        tabla.put("S print","13 print parenta E parentc pyc {13}");

        // (14)S -> input ( id ) ;
        tabla.put("S input","14 input {14.1} parenta id parentc pyc {14.2}");

        // (15)S -> return X ;
        tabla.put("S return", "15 return X pyc {15}");

        // (16)D -> M E 
        tabla.put("D igual","16 M E {16}");
        tabla.put("D olog","16 M E {16}");

        // (17)D -> ( L ) 
        tabla.put("D parenta", "17 parenta L parentc {17}");

        // (18)X -> E
        tabla.put("X id","18 E {18}");
        tabla.put("X entero","18 E {18}");
        tabla.put("X cadena","18 E {18}");
        tabla.put("X true","18 E {18}");
        tabla.put("X false","18 E {18}");
        tabla.put("X parenta","18 E {18}");

        // (19)X -> lambda
        tabla.put("X pyc","19 lambda {19}");

        // (20)C -> B C
        tabla.put("C id","20 B C {20}");
        tabla.put("C let","20 B C {20}");
        tabla.put("C print","20 B C {20}");
        tabla.put("C input","20 B C {20}");
        tabla.put("C return","20 B C {20}");
        tabla.put("C if","20 B C {20}");
        tabla.put("C for","20 B C {20}");

        // (21)C -> lambda
        tabla.put("C corchc","21 lambda {21}");

        // (22)L -> E Q
        tabla.put("L id","22 E {22.1} Q {22.2}");
        tabla.put("L entero","22 E {22.1} Q {22.2}");
        tabla.put("L cadena","22 E {22.1} Q {22.2}");
        tabla.put("L true","22 E {22.1} Q {22.2}");
        tabla.put("L false","22 E {22.1} Q {22.2}");
        tabla.put("L parenta","22 E {22.1} Q {22.2}");

        // (23)L -> lambda
        tabla.put("L parentc","23 lambda {23}");

        // (24)Q -> , E Q
        tabla.put("Q coma","24 coma E {24.1} Q {24.2}");

        // (25)Q -> lambda
        tabla.put("Q parentc","25 lambda {25}");

        // (26)F -> function id H ( A ) { C }
        tabla.put("F function","26 {26.1} function id H {26.2} parenta A {26.3} parentc {26.4} corcha C {26.5} corchc {26.6}");

        // (27)H -> T
        tabla.put("H boolean","27 T {27}");
        tabla.put("H int","27 T {27}");
        tabla.put("H string","27 T {27}");

        // (28)H -> lambda
        tabla.put("H parenta","28 lambda {28}");

        // (29)A -> T id K
        tabla.put("A boolean","29 T id {29.1} K {29.2}");
        tabla.put("A int","29 T id {29.1} K {29.2}");
        tabla.put("A string","29 T id {29.1} K {29.2}");

        // (30)A -> lambda
        tabla.put("A parentc","30 lambda {30}");

        // (31)K -> , T id K
        tabla.put("K coma","31 coma T id {31.1} K {31.2}");

        // (32)K -> lambda
        tabla.put("K parentc","32 lambda {32}");

        // (33)E -> R 1
        tabla.put("E id","33 R 1 {33}");
        tabla.put("E entero","33 R 1 {33}");
        tabla.put("E cadena","33 R 1 {33}");
        tabla.put("E true","33 R 1 {33}");
        tabla.put("E false","33 R 1 {33}");
        tabla.put("E parenta","33 R 1 {33}");

        // (34)1 -> && R 1
        tabla.put("1 and","34 and R 1 {34}");

        // (35)1 -> lambda
        tabla.put("1 parentc","35 lambda {35}");
        tabla.put("1 pyc","35 lambda {35}");
        tabla.put("1 coma","35 lambda {35}");

        // (36)R -> U 2
        tabla.put("R id","36 U 2 {36}");
        tabla.put("R entero","36 U 2 {36}");
        tabla.put("R cadena","36 U 2 {36}");
        tabla.put("R true","36 U 2 {36}");
        tabla.put("R false","36 U 2 {36}");
        tabla.put("R parenta","36 U 2 {36}");

        // (37)2 -> G U 2
        tabla.put("2 iguigu","37 G U 2 {37}");
        tabla.put("2 desigu","37 G U 2 {37}");

        // (38)2 -> lambda
        tabla.put("2 parentc","38 lambda {38}");
        tabla.put("2 and","38 lambda {38}");
        tabla.put("2 pyc","38 lambda {38}");
        tabla.put("2 coma","38 lambda {38}");

        // (39)G -> ==
        tabla.put("G iguigu","39 iguigu {39}");

        // (40)G -> !=
        tabla.put("G desigu","40 desigu {40}");

        // (41)U -> V 3
        tabla.put("U id","41 V 3 {41}");
        tabla.put("U entero","41 V 3 {41}");
        tabla.put("U cadena","41 V 3 {41}");
        tabla.put("U true","41 V 3 {41}");
        tabla.put("U false","41 V 3 {41}");
        tabla.put("U parenta","41 V 3 {41}");

        // (42)3 -> J V 3
        tabla.put("3 suma","42 J V 3 {42}");
        tabla.put("3 resta","42 J V 3 {42}");

        // (43)3 -> lambda
        tabla.put("3 parentc","43 lambda {43}");
        tabla.put("3 iguigu","43 lambda {43}");
        tabla.put("3 desigu","43 lambda {43}");
        tabla.put("3 and","43 lambda {43}");  
        tabla.put("3 pyc","43 lambda {43}");
        tabla.put("3 coma","43 lambda {43}");

        // (44)J -> +
        tabla.put("J suma","44 suma {44}");

        // (45)J -> -
        tabla.put("J resta","45 resta {45}");

        // (46)V -> id Z
        tabla.put("V id","46 id Z {46}");

        // (47)V -> ( E )
        tabla.put("V parenta","47 parenta E parentc {47}");

        // (48)V -> entero
        tabla.put("V entero","48 entero {48}");

        // (49)V -> cadena
        tabla.put("V cadena","49 cadena {49}");

        // (50)V -> true
        tabla.put("V true","50 true {50}");

        // (51)V -> false
        tabla.put("V false","51 false {51}");

        // (52)Z -> ( L )
        tabla.put("Z parenta","52 parenta L parentc {52}");

        // (53)Z -> lambda
        tabla.put("Z parentc","53 lambda {53}");
        tabla.put("Z suma","53 lambda {53}");
        tabla.put("Z resta","53 lambda {53}");
        tabla.put("Z iguigu","53 lambda {53}");
        tabla.put("Z desigu","53 lambda {53}");
        tabla.put("Z and","53 lambda {53}");
        tabla.put("Z pyc","53 lambda {53}");
        tabla.put("Z coma","53 lambda {53}");

        // (54)I -> id M E
        tabla.put("I id","54 id M E {54}");

        // (55)I -> lambda
        tabla.put("I parentc","55 lambda {55}");
        tabla.put("I pyc","55 lambda {55}");

        // (56)M -> =
        tabla.put("M igual","56 igual {56}");

        // (57)M -> |= 
        tabla.put("M olog","57 olog {57}");
    }

    public ArrayList<String> getTerminales(){
        return terminales;
    }

    public Map<String, String> getTablas(){
        return tabla;
    }
}
