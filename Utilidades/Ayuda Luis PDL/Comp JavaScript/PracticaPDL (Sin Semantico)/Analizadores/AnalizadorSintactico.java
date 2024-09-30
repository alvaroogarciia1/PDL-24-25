package Analizadores;

import java.util.*;
import java.io.*;

public class AnalizadorSintactico {

    ArrayList<String> terminales;
    static Map<String, String> tabla;
    static String parse = "Descendete";
    File tokens;

    public AnalizadorSintactico(File f) {

        // Archivo con los tokens
        tokens = f;

        // ArrayList para los no Terminales
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
        tabla = new HashMap<String, String>();
        // Regla 1. E -> R 1
        tabla.put("E id", "1 R 1");
        tabla.put("E entero", "1 R 1");
        tabla.put("E parenta", "1 R 1");
        tabla.put("E cadena", "1 R 1");
        // Regla 2. 1 -> || R 1
        tabla.put("1 or", "2 or R 1");
        // Regla 3. 1 -> lambda
        tabla.put("1 puntoycoma", "3 lambda");
        tabla.put("1 parentc", "3 lambda");
        tabla.put("1 coma", "3 lambda");
        // Regla 4. R -> U 2
        tabla.put("R id", "4 U 2");
        tabla.put("R parenta", "4 U 2");
        tabla.put("R entero", "4 U 2");
        tabla.put("R cadena", "4 U 2");
        // Regla 5. 2 -> == U 2 
        tabla.put("2 igualigual", "5 igualigual U 2");
        // Regla 6. 2 -> lambda
        tabla.put("2 puntoycoma", "6 lambda");
        tabla.put("2 parentc", "6 lambda");
        tabla.put("2 coma", "6 lambda");
        tabla.put("2 or", "6 lambda");
        // Regla 7. U -> V 3
        tabla.put("U id", "7 V 3");
        tabla.put("U parenta", "7 V 3");
        tabla.put("U entero", "7 V 3");
        tabla.put("U cadena", "7 V 3");
        // Regla 8. 3 -> G V 3
        tabla.put("3 mas", "8 G V 3");
        tabla.put("3 menos", "8 G V 3");
        // Regla 9. 3 -> lambda
        tabla.put("3 puntoycoma", "9 lambda");
        tabla.put("3 parentc", "9 lambda");
        tabla.put("3 coma", "9 lambda");
        tabla.put("3 or", "9 lambda");
        tabla.put("3 igualigual", "9 lambda");
        // Regla 10. G -> +
        tabla.put("G mas", "10 mas");
        // Regla 11. G -> -
        tabla.put("G menos", "11 menos");
        // Regla 12. V -> Z 4
        tabla.put("V id", "12 Z 4");
        tabla.put("V parenta", "12 Z 4");
        tabla.put("V entero", "12 Z 4");
        tabla.put("V cadena", "12 Z 4");
        // Regla 13. 4 -> / Z 4
        tabla.put("4 division", "13 division Z 4");
        // Regla 14. 4 -> lambda
        tabla.put("4 puntoycoma", "14 lambda");
        tabla.put("4 parentc", "14 lambda");
        tabla.put("4 coma", "14 lambda");
        tabla.put("4 or", "14 lambda");
        tabla.put("4 mas", "14 lambda");
        tabla.put("4 menos", "14 lambda");
        // Regla 15. Z -> id O
        tabla.put("Z id", "15 id O");
        // Regla 16. Z -> ( E )
        tabla.put("Z parenta", "16 parenta E parentc");
        // Regla 17. Z -> entero
        tabla.put("Z entero", "17 entero");
        // Regla 18. Z -> cadena
        tabla.put("Z cadena", "18 cadena");
        // Regla 19. O -> lambda
        tabla.put("O puntoycoma", "19 lambda");
        tabla.put("O parentc", "19 lambda");
        tabla.put("O coma", "19 lambda");
        tabla.put("O or", "19 lambda");
        tabla.put("O igualigual", "19 lambda");
        tabla.put("O mas", "19 lambda");
        tabla.put("O menos", "19 lambda");
        tabla.put("O division", "19 lambda");
        // Regla 20. O -> ( L )
        tabla.put("O parenta", "20 parenta L parentc");
        // Regla 21. S -> id D ;
        tabla.put("S id", "21 id D puntoycoma");
        // Regla 22. S -> alert ( E ) ;
        tabla.put("S alert", "22 alert parenta E parentc puntoycoma");
        // Regla 23. S -> input ( id ) ;
        tabla.put("S input", "23 input parenta id parentc puntoycoma");
        // Regla 24. S -> return X ;
        tabla.put("S return", "24 return X puntoycoma");
        // Regla 25. D -> = E
        tabla.put("D igual", "25 igual E");
        // Regla 26. D -> |= E
        tabla.put("D origual", "26 origual E");
        // Regla 27. D -> ( L )
        tabla.put("D parenta","27 parenta L parentc");
        // Regla 28. L -> E Q
        tabla.put("L id", "28 E Q");
        tabla.put("L parentc", "28 E Q");
        tabla.put("L entero", "28 E Q");
        tabla.put("L cadena", "28 E Q");
        // Regla 29. L -> lambda
        tabla.put("L parenta", "29 lambda");
        // Regla 30. Q -> , E Q
        tabla.put("Q coma", "30 coma E Q");
        // Regla 31. Q -> lambda
        tabla.put("Q parentc", "31 lambda");
        // Regla 32. X -> E
        tabla.put("X id", "32 E");
        tabla.put("X parenta", "32 E");
        tabla.put("X entero", "32 E");
        tabla.put("X cadena", "32 E");
        // Regla 33. X -> lambda
        tabla.put("X puntoycoma", "33 lambda");
        // Regla 34. B -> if ( E ) S
        tabla.put("B if", "34 if parenta E parentc S");
        // Regla 35. B -> let T id ;
        tabla.put("B let", "35 let T id puntoycoma");
        // Regla 36. B -> S
        tabla.put("B id", "36 S");
        tabla.put("B input", "36 S");
        tabla.put("B alert", "36 S");
        tabla.put("B return", "36 S");
        // Regla 37. B -> switch ( E ) { Y }
        tabla.put("B switch", "37 switch parenta E parentc corcha Y corchc");
        // Regla 38. Y -> case entero : C M 
        tabla.put("Y case", "38 case entero dospuntos C M");
        // Regla 39. M -> break ; N
        tabla.put("M break", "39 break puntoycoma N");
        // Regla 40. M -> N
        tabla.put("M corchc", "40 N");
        tabla.put("M case", "40 N");
        tabla.put("M default", "40 N");
        // Regla 41. N -> Y
        tabla.put("N case", "41 Y");
        // Regla 42. N -> default : C 
        tabla.put("N default", "42 default dospuntos C");
        // Regla 43. N -> lambda
        tabla.put("N corchc", "43 lambda");
        // Regla 44. T -> number
        tabla.put("T number", "44 number");
        // Regla 45. T -> boolean
        tabla.put("T boolean", "45 boolean");
        //Regla 46. T -> string
        tabla.put("T string", "46 string");
        // Regla 47. F -> function H id ( A ) { C }
        tabla.put("F function", "47 function H id parenta A parentc corcha C corchc");
        //Regla 48. H -> T
        tabla.put("H number", "48 T");
        tabla.put("H boolean", "48 T");
        tabla.put("H string", "48 T");
        // Regla 49. H -> lambda
        tabla.put("H id", "49 lambda");
        //Regla 50. A -> T id K
        tabla.put("A number", "50 T id K");
        tabla.put("A boolean", "50 T id K");
        tabla.put("A string", "50 T id K");
        //Regla 51. A -> lambda
        tabla.put("A parentc","51 lambda");
        //Regla 52. K -> , T id K
        tabla.put("K coma", "52 coma T id K");
        //Regla 53. K -> lambda
        tabla.put("K parentc", "53 lambda");
        //Regla 54. C -> B C
        tabla.put("C if", "54 B C");
        tabla.put("C let", "54 B C");
        tabla.put("C id", "54 B C");
        tabla.put("C input", "54 B C");
        tabla.put("C alert", "54 B C");
        tabla.put("C return", "54 B C");
        tabla.put("C switch", "54 B C");
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

    public String metodo() throws IOException {
        Stack<String> pila = new Stack<String>();
        pila.push("$");
        pila.push("P");
        FileReader fr = new FileReader(tokens);
        BufferedReader br = new BufferedReader(fr);
        String linea[] = br.readLine().split("<|,| ");
        String a = linea[1].toLowerCase();
        String pos = "";
        String consecuente[];
        while(!pila.peek().equals("eof")){
            pos = "";
            if(pila.peek().equals("lambda")) {
                pila.pop();
            }
            else if(terminales.contains(pila.peek())){
                //System.out.println(a + " " + pila.peek());
                if(pila.peek().equals(a)){
                    pila.pop();
                    linea = br.readLine().split("<|,| ");
                    a = linea[1].toLowerCase();
                } else {
                    return "Error. Token no valido";
                }
            } else {
                pos += pila.peek() + " " + a;
                //System.out.println(pos);
                if(tabla.containsKey(pos)){
                    pila.pop();
                    consecuente = tabla.get(pos).split(" ");
                    parse += " " + consecuente[0];
                    for(int i = consecuente.length -1; i > 0; i--){
                        pila.push(consecuente[i]); 
                    }
                } else {
                    return "Error. Casilla vacia";
                }
            }   
            //System.out.println(imprimirPila(pila) + "\n-------------");
        }
        if(!a.equals("eof")) return "Error. No EOF";
        fr.close();
        br.close();
        return parse;
    }
    
    public String imprimirPila(Stack<String> pila){
        Iterator<String> it = pila.iterator();
        String x = it.next();
        while(it.hasNext()){
            x = it.next() +"\n" + x;
        }
        return x;
    }
}