package tablas;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class GramaticaAS{

    Map<String, String> tabla;
    ArrayList<String> terminales;

    public GramaticaAS(){

        terminales = new ArrayList<String>();
        terminales.add("if");
        terminales.add("let");
        terminales.add("id");
        terminales.add("input");
        terminales.add("alert");
        terminales.add("eof");
        terminales.add("parenta");
        terminales.add("puntoycoma");
        terminales.add("parentc");
        terminales.add("coma");
        terminales.add("corcha");
        terminales.add("dospuntos");
        terminales.add("corchc");
        terminales.add("entero");
        terminales.add("number");
        terminales.add("boolean");
        terminales.add("function");
        terminales.add("string");
        terminales.add("return");
        terminales.add("cadena");
        terminales.add("switch");
        terminales.add("case");
        terminales.add("default");
        terminales.add("break");
        terminales.add("igual");
        terminales.add("or");
        terminales.add("origual");
        terminales.add("igualigual");
        terminales.add("mas");
        terminales.add("menos");
        terminales.add("division");

         // Tabla de la gramatica
        /* Estructura de la tabla -> HashMap:
            - Key -> String. Formado por "Fila Columna" (Variable "pos")
            - Value -> String. Formado por "NºRegla ConsecuenteDeLaRegla" (Variable consecuente[])
        */
        tabla = new HashMap<String, String>();
        // Regla 1. E -> R 1
        tabla.put("E id", "1 R 1 {1}");
        tabla.put("E entero", "1 R 1 {1}");
        tabla.put("E parenta", "1 R 1 {1}");
        tabla.put("E cadena", "1 R 1 {1}");
        // Regla 2. 1 -> || R 1
        tabla.put("1 or", "2 or R 1 {2}");
        // Regla 3. 1 -> lambda
        tabla.put("1 puntoycoma", "3 lambda {3}");
        tabla.put("1 parentc", "3 lambda {3}");
        tabla.put("1 coma", "3 lambda {3}");
        // Regla 4. R -> U 2
        tabla.put("R id", "4 U 2 {4}");
        tabla.put("R parenta", "4 U 2 {4}");
        tabla.put("R entero", "4 U 2 {4}");
        tabla.put("R cadena", "4 U 2 {4}");
        // Regla 5. 2 -> == U 2 
        tabla.put("2 igualigual", "5 igualigual U 2 {5}");
        // Regla 6. 2 -> lambda
        tabla.put("2 puntoycoma", "6 lambda {6}");
        tabla.put("2 parentc", "6 lambda {6}");
        tabla.put("2 coma", "6 lambda {6}");
        tabla.put("2 or", "6 lambda {6}");
        // Regla 7. U -> V 3
        tabla.put("U id", "7 V 3 {7}");
        tabla.put("U parenta", "7 V 3 {7}");
        tabla.put("U entero", "7 V 3 {7}");
        tabla.put("U cadena", "7 V 3 {7}");
        // Regla 8. 3 -> G V 3
        tabla.put("3 mas", "8 G V 3 {8}");
        tabla.put("3 menos", "8 G V 3 {8}");
        // Regla 9. 3 -> lambda
        tabla.put("3 puntoycoma", "9 lambda {9}");
        tabla.put("3 parentc", "9 lambda {9}");
        tabla.put("3 coma", "9 lambda {9}");
        tabla.put("3 or", "9 lambda {9}");
        tabla.put("3 igualigual", "9 lambda {9}");
        // Regla 10. G -> +
        tabla.put("G mas", "10 mas ");
        // Regla 11. G -> -
        tabla.put("G menos", "11 menos");
        // Regla 12. V -> Z 4
        tabla.put("V id", "12 Z 4 {12}");
        tabla.put("V parenta", "12 Z 4 {12}");
        tabla.put("V entero", "12 Z 4 {12}");
        tabla.put("V cadena", "12 Z 4 {12}");
        // Regla 13. 4 -> / Z 4
        tabla.put("4 division", "13 division Z 4 {13}");
        // Regla 14. 4 -> lambda
        tabla.put("4 puntoycoma", "14 lambda {14}");
        tabla.put("4 parentc", "14 lambda {14}");
        tabla.put("4 coma", "14 lambda {14}");
        tabla.put("4 or", "14 lambda {14}");
        tabla.put("4 mas", "14 lambda {14}");
        tabla.put("4 menos", "14 lambda {14}");
        tabla.put("4 igualigual","14 lambda {14}");
        // Regla 15. Z -> id O
        tabla.put("Z id", "15 id O {15}");
        // Regla 16. Z -> ( E )
        tabla.put("Z parenta", "16 parenta E parentc {16}");
        // Regla 17. Z -> entero
        tabla.put("Z entero", "17 entero {17}");
        // Regla 18. Z -> cadena
        tabla.put("Z cadena", "18 cadena {18}");
        // Regla 19. O -> lambda
        tabla.put("O puntoycoma", "19 lambda {19}");
        tabla.put("O parentc", "19 lambda {19}");
        tabla.put("O coma", "19 lambda {19}");
        tabla.put("O or", "19 lambda {19}");
        tabla.put("O igualigual", "19 lambda {19}");
        tabla.put("O mas", "19 lambda {19}");
        tabla.put("O menos", "19 lambda {19}");
        tabla.put("O division", "19 lambda {19}");
        // Regla 20. O -> ( L )
        tabla.put("O parenta", "20 parenta L parentc {20}");
        // Regla 21. S -> id D ;
        tabla.put("S id", "21 id D puntoycoma {21}");
        // Regla 22. S -> alert ( E ) ;
        tabla.put("S alert", "22 alert parenta E parentc puntoycoma {22}");
        // Regla 23. S -> input ( id ) ;
        tabla.put("S input", "23 input parenta id {23.1} parentc puntoycoma {23.2}");
        // Regla 24. S -> return X ;
        tabla.put("S return", "24 return X puntoycoma");
        // Regla 25. D -> = E
        tabla.put("D igual", "25 igual E {25}");
        // Regla 26. D -> |= E
        tabla.put("D origual", "26 origual E {26}");
        // Regla 27. D -> ( L )
        tabla.put("D parenta","27 parenta L parentc {27}");
        // Regla 28. L -> E Q
        tabla.put("L id", "28 E {28} Q {28.1}");
        tabla.put("L parenta", "28 E {28} Q {28.1}");
        tabla.put("L entero", "28 E {28} Q {28.1}");
        tabla.put("L cadena", "28 E {28} Q {28.1}");
        // Regla 29. L -> lambda
        tabla.put("L parentc", "29 lambda {29}");
        // Regla 30. Q -> , E Q
        tabla.put("Q coma", "30 coma E {30} Q {30.1}");
        // Regla 31. Q -> lambda
        tabla.put("Q parentc", "31 lambda {31}");
        // Regla 32. X -> E
        tabla.put("X id", "32 E {32}");
        tabla.put("X parenta", "32 E {32}");
        tabla.put("X entero", "32 E {32}");
        tabla.put("X cadena", "32 E {32}");
        // Regla 33. X -> lambda
        tabla.put("X puntoycoma", "33 lambda {33}");
        // Regla 34. B -> if ( E ) S
        tabla.put("B if", "34 if parenta E parentc S {34}");
        // Regla 35. B -> let T id ;
        tabla.put("B let", "35 let T id puntoycoma {35}");
        // Regla 36. B -> S
        tabla.put("B id", "36 S");
        tabla.put("B input", "36 S");
        tabla.put("B alert", "36 S");
        tabla.put("B return", "36 S");
        // Regla 37. B -> switch ( E ) { Y }
        tabla.put("B switch", "37 switch parenta E parentc corcha Y corchc {37}");
        // Regla 38. Y -> case entero : C M 
        tabla.put("Y case", "38 case entero dospuntos C M");
        // Regla 39. M -> break ; N
        tabla.put("M break", "39 break puntoycoma N");
        // Regla 40. M -> N
        tabla.put("M corchc", "40 N");
        tabla.put("M case", "40 N ");
        tabla.put("M default", "40 N");
        // Regla 41. N -> Y
        tabla.put("N case", "41 Y");
        // Regla 42. N -> default : C 
        tabla.put("N default", "42 default dospuntos C");
        // Regla 43. N -> lambda
        tabla.put("N corchc", "43 lambda");
        // Regla 44. T -> number
        tabla.put("T number", "44 number {44}");
        // Regla 45. T -> boolean
        tabla.put("T boolean", "45 boolean {45}");
        //Regla 46. T -> string
        tabla.put("T string", "46 string {46}");
        // Regla 47. F -> function H id ( A ) { C }
        tabla.put("F function", "47 function H {47.1} id {47.2} parenta A parentc {47.3} corcha C corchc");
        //Regla 48. H -> T
        tabla.put("H number", "48 T {48}");
        tabla.put("H boolean", "48 T {48}");
        tabla.put("H string", "48 T {48}");
        // Regla 49. H -> lambda
        tabla.put("H id", "49 lambda {49}");
        //Regla 50. A -> T id K
        tabla.put("A number", "50 T id {50.1} K {50.2}");
        tabla.put("A boolean", "50 T id {50.1} K {50.2}");
        tabla.put("A string", "50 T id {50.1} K {50.2}");
        //Regla 51. A -> lambda
        tabla.put("A parentc","51 lambda {51}");
        //Regla 52. K -> , T id K
        tabla.put("K coma", "52 coma T id {52.1} K {52.2}");
        //Regla 53. K -> lambda
        tabla.put("K parentc", "53 lambda {53}");
        //Regla 54. C -> B C
        tabla.put("C if", "54 B C");
        tabla.put("C let", "54 B C");
        tabla.put("C id", "54 B C");
        tabla.put("C input", "54 B C");
        tabla.put("C alert", "54 B C");
        tabla.put("C return", "54 B C");
        tabla.put("C switch", "54 B ");
        //Regla 55. C -> lambda
        tabla.put("C corchc", "55 lambda");
        tabla.put("C case", "55 lambda");
        tabla.put("C break", "55 lambda");
        tabla.put("C default", "55 lambda");
        //Regla 56. P -> BP
        tabla.put("P if", "56 B P");
        tabla.put("P let", "56 B P");
        tabla.put("P id", "56 B P");
        tabla.put("P input", "56 B P");
        tabla.put("P alert", "56 B P");
        tabla.put("P return", "56 B P");
        tabla.put("P switch", "56 B P");
        //Regña 57. P -> FP
        tabla.put("P function", "57 F P");
        //Regla 58. P -> lambda
        tabla.put("P $", "58 lambda");
        //Regla 59. P-> eof
        tabla.put("P eof","59 eof");
    }
    public ArrayList<String> getTerminales(){
        return terminales;
    }

    public Map<String, String> getTabla(){
        return tabla;
    }
    
}