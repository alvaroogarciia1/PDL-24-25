package Analizadores;

import java.util.*;
import java.io.*;
import tablas.*;

class Par {

    String izq;
    String dcha;

    public Par(String izq, String dcha) {
        this.izq = izq; 
        this.dcha = dcha;
    }
}

public class AnalizadorSintactico {

    static AnalizadorLexico AL;
    GramaticaAS g;
    ArrayList<String> terminales;
    Map<String, String> accionesSinSem;
    String parse = "Descendente";
    File codigoFuente;
    static File ficheroTokens;
    File ficheroTablaSimbolos;
    static ArrayList<String> parametrosComp;

    public AnalizadorSintactico(File f) throws IOException {

        // Archivos
        codigoFuente = f;
        ficheroTokens = new File("tokens.txt");
        ficheroTablaSimbolos = new File("tablaSimbolos.txt");

        // Anañizador Lexico
        AL = new AnalizadorLexico(f);

        // Gramatica del AS: Terminales y Tabla
        g = new GramaticaAS();
        terminales = g.getTerminales();
        accionesSinSem = g.getTabla();

    }

    // Metodo para un Analizador Descedente Predictivo con Tabla
    public String parse() throws IOException {
        // El metodo empieza con una pila que contiene $ P
        Stack<Par> pila = new Stack<Par>();
        Stack<Par> aux = new Stack<Par>();
        ArrayList<TablaSimbolos> imprimirPTS = new ArrayList<TablaSimbolos>();
        FileWriter escrituraTokens = new FileWriter(ficheroTokens);
        pila.push(new Par("$", "-"));
        pila.push(new Par("P", "-"));
        Token token = AL.leerToken();
        String posId = "";
        if(token.getIdentifier().equals("ID")) posId = (String)token.getAttribute();
        if (token != null)
            escrituraTokens.write(token.toString() + "\n");
        String identificador = token.getIdentifier().toLowerCase();
        String pos = "";
        String consecuente[];
        // Mientras el token leido no sea final del fichero
        while (!pila.peek().izq.equals("eof")) {
            pos = "";
            // Si la cima es lambda, se desapila el simbolo
            if (pila.peek().izq.equals("lambda")) {
                pila.pop();
            }
            // Si la cima es un terminal
            else if (terminales.contains(pila.peek().izq)) {
                // System.out.println(identificador);
                // Si la cima es igual al token actual se desapila y se llama al siguiente token
                if (pila.peek().izq.equals(identificador)) {
                    if(token.getIdentifier().equals("ID")){
                        posId = "" + token.getAttribute();
                        pila.peek().dcha = posId;//pasamos la posicion del id en la ts como atributo
                    }
                    aux.push(pila.pop());
                    token = AL.leerToken();
                    if (token != null)
                        escrituraTokens.write(token.toString() + "\n");
                    if(token == null) break;
                    identificador = token.getIdentifier().toLowerCase();
                } else {
                    System.out.println("Error. Token no valido");
                    break;
                }
                // Si es accion semantica
            } else if (esAccionSemantica(pila.peek().izq)) {
                ejecutarAccSem(pila.peek(), pila, aux);
                pila.pop();
            // Si la cima es un no terminal
            } else {
                pos += pila.peek().izq + " " + identificador;
                // System.out.println(pos);
                // Si existe en la tabla la casilla Tabla[NoTerminal][TokenActual]
                // desapilamos el no terminal y apilamos sus consecuentes para el token actual
                if (accionesSinSem.containsKey(pos)) {
                    aux.push(pila.pop());
                    consecuente = accionesSinSem.get(pos).split(" ");
                    parse += " " + consecuente[0];
                    if (Integer.parseInt(consecuente[0]) == 55 && AL.getPila().peek().getNumeroTabla() != 0) {
                        imprimirPTS.add(AL.getPila().pop());
                    } else if (Integer.parseInt(consecuente[0]) == 57) {
                        if (AL.getPila().peek().getNumeroTabla() != 0)
                            imprimirPTS.add(AL.getPila().pop());
                        AL.setCreaTabla(true);
                    } else if(Integer.parseInt(consecuente[0]) == 27){
                        AL.setEsFunction(true);
                    }
                    for (int i = consecuente.length - 1; i > 0; i--) {
                        if(consecuente[i].equals("id")){
                            pila.push(new Par(consecuente[i], posId));
                        } 
                        else pila.push(new Par(consecuente[i], "-"));
                    }
                } else {
                    System.out.println("Error. Casilla vacia");
                    break;
                }
            }
            //System.out.println(imprimirPila(pila, aux) + "\n-------------");
        }
        // Si el token final es EOF habremos llegado al final del programa
        if (!identificador.equals("$") || !pila.peek().izq.equals("P")) {
            System.out.println("Error. No $");
        }
        imprimirPTS.add(AL.getPila().pop());
        imprimirTablas(imprimirPTS);
        escrituraTokens.close();
        return parse;
    }

    public boolean esAccionSemantica(String acc /* {XX} */) {
        if (acc.charAt(0) == '{')
            return true;
        return false;
    }

    public void ejecutarAccSem(Par p, Stack<Par> principal, Stack<Par> aux) {
        if (p.izq.equals("{1}")) {
            String tipo1=aux.pop().dcha;//1
            String tipoR=aux.pop().dcha;//R
            if(tipo1.equals("void") || (tipo1.equals("boolean") && tipoR.equals("boolean"))){
                aux.peek().dcha= tipoR;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }

        } else if (p.izq.equals("{2}")) {
            String tipo1=aux.pop().dcha;//1
            String tipoR=aux.pop().dcha;//R
            if(tipo1.equals("void") || (tipo1.equals("boolean") && tipoR.equals("boolean"))){
                aux.peek().dcha= tipoR;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }
            aux.pop();//||

        } else if (p.izq.equals("{3}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{4}")) {
            String tipo2=aux.pop().dcha;//2
            String tipoU=aux.pop().dcha;//U
            if(tipo2.equals("void") || tipo2.equals(tipoU)){
                aux.peek().dcha= tipoU;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }
            
        } else if (p.izq.equals("{5}")) {
            String tipo2=aux.pop().dcha;//2
            String tipoU=aux.pop().dcha;//U
            if(tipo2.equals("void") || tipo2.equals(tipoU)){
                aux.peek().dcha= tipoU;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }
            aux.pop();//==
        } else if (p.izq.equals("{6}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{7}")) {
            String tipo3=aux.pop().dcha;//
            String tipoV=aux.pop().dcha;//U
            if(tipo3.equals("void") || tipo3.equals(tipoV)){
                aux.peek().dcha= tipoV;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }
        } else if (p.izq.equals("{8}")) {
            String tipo3=aux.pop().dcha;//3
            String tipoV=aux.pop().dcha;//U
            if(tipo3.equals("void") || (tipo3.equals("entero") && tipoV.equals("entero"))){
                aux.peek().dcha= tipoV;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }
            aux.pop();//G
        } else if (p.izq.equals("{9}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{12}")) {
            String tipo4=aux.pop().dcha;//4
            String tipoZ=aux.pop().dcha;//Z
            if(tipo4.equals("void") || (tipo4.equals("entero") && tipoZ.equals("entero"))){
                aux.peek().dcha= tipoZ;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }
        } else if (p.izq.equals("{13}")) {
            String tipo3=aux.pop().dcha;//3
            String tipoV=aux.pop().dcha;//U
            if(tipo3.equals("void") || (tipo3.equals("entero") && tipoV.equals("entero"))){
                aux.peek().dcha= tipoV;
            }
            else{
                System.out.println("Error: tipos de expresion incorrectos.");
                return;
            }
            aux.pop();// /
        } else if (p.izq.equals("{14}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{15}")) {
            aux.pop(); // O
            int posId = Integer.parseInt(aux.pop().dcha);
            if(!AL.getPila().peek().buscar(AL.getPila().peek().simbolos.get(posId).getLexema()) && !AL.getPila().elementAt(0).buscar(AL.getPila().elementAt(0).simbolos.get(posId).getLexema())) {
                System.out.println("Error. Funcion no declarada");
                return;
            }
            else if (AL.getPila().peek().buscar(AL.getPila().peek().simbolos.get(posId).getLexema())){ //si esta en la tabla de la funcion actual
                aux.peek().dcha = AL.getPila().peek().simbolos.get(posId).getTipo();
            } else { // si esta en la tabla de la funcion principal
                aux.peek().dcha = AL.getPila().elementAt(0).simbolos.get(posId).getTipo();
            }
        } else if (p.izq.equals("{16}")) {
            aux.pop(); // )
            String E = aux.pop().dcha;
            aux.peek().dcha= E;
            aux.pop(); // (
        } else if (p.izq.equals("{17}")) {
            aux.pop(); 
            aux.peek().dcha = "entero";            
        } else if (p.izq.equals("{18}")) {
            aux.pop();
            aux.peek().dcha = "string";
        } else if (p.izq.equals("{19}")) {
            //nada
        } else if (p.izq.equals("{20}")) {
            aux.pop(); // )
            String L = aux.pop().dcha;
            aux.peek().dcha= L;
            aux.pop(); // (
        } else if (p.izq.equals("{21}")) {
            aux.pop(); // puntoycoma
            String tipo = aux.pop().dcha; // D
            int posId = Integer.parseInt(aux.pop().dcha); // id
            String tipoId = AL.getPila().peek().simbolos.get(posId).getTipo();
            if(!tipo.equals(tipoId)){
                System.out.println("Tipos incorrectos en la asignacion");
                return;
            }
        } else if (p.izq.equals("{22}")) {
            aux.pop(); //;
            aux.pop(); // )
            String tipoE = aux.pop().dcha;
            if(tipoE.equals("boolean") || tipoE.equals("entero")){
                aux.peek().dcha= tipoE;
            }
        } else if (p.izq.equals("{23.1}")) {
            int posId = Integer.parseInt(aux.pop().dcha); // id
            if (AL.getPila().peek().buscar(AL.getLexemaInput())){ //si esta en la tabla de la funcion actual
                String tipo = AL.getPila().peek().simbolos.get(posId).getTipo();
                if(!(tipo.equals("entero") && tipo.equals("string"))) {
                    System.out.println("Error. Tipo variable input incorrecto");
                    return;
                }
            } else if(AL.getPila().elementAt(0).buscar(AL.getLexemaInput())){ // Variable global
                String tipo = AL.getPila().elementAt(0).simbolos.get(AL.getPila().elementAt(0).getPos(AL.getLexemaInput())).getTipo();
                if(!tipo.equals("entero")){
                    System.out.println("Error. Tipo de variable input incorrecta");
                    return;
                }
            }
            aux.pop(); // (
            aux.pop(); // input
        } else if (p.izq.equals("{23.2}")){
            AL.setEsInput(true);
            aux.pop(); // ;
            aux.pop(); // )
        } else if (p.izq.equals("{24}")) { //return X ;
            aux.pop(); // ;
            String tipoX = aux.pop().dcha; // X
            if(!tipoX.equals(AL.getPila().elementAt(0).simbolos.get(AL.getPila().elementAt(0).simbolos.size()-1).getTipoRetorno())){
                System.out.println("Error. Tipo de retorno no valido");
                return;
            } 
            aux.pop(); //return
        } else if (p.izq.equals("{25}")) {
            String tipo = aux.pop().dcha;//E
            aux.pop(); // =
            aux.peek().dcha = tipo;
        } else if (p.izq.equals("{26}")) {
            String tipo = aux.pop().dcha;//E
            aux.pop(); // |=
            aux.peek().dcha = tipo;
        } else if (p.izq.equals("{27}")) {
            aux.pop(); // )
            aux.pop(); // L
            aux.pop(); // (     
            Par D = new Par(aux.peek().izq, aux.pop().dcha); // D
            int posId = Integer.parseInt(aux.peek().dcha);//obtenemos la posicion de la pila para poder extraer la lista de parametros
            ArrayList<String> parametros = AL.getPila().elementAt(0).simbolos.get(posId).getTipoParam();
            if(!compara(parametros, parametrosComp)){
                System.out.println("Error en los parametros de la llamada a una funcion");
                return;
            }
            aux.push(D);//devolvemos la D a la pila
            aux.peek().dcha = "function";
        } else if (p.izq.equals("{28}")) {
            String tipo = aux.pop().dcha; // E
            parametrosComp = new ArrayList<String>();
            parametrosComp.add(tipo);
        }
        else if (p.izq.equals("{28.1}")) {
            aux.pop(); // Q
        } else if (p.izq.equals("{29}")) {
            parametrosComp = new ArrayList<String>();
            parametrosComp.add("void");
        } else if (p.izq.equals("{30}")) {
            String tipo = aux.pop().dcha; // E
            parametrosComp.add(tipo);
        } else if(p.izq.equals("{30.1}")) {
            aux.pop(); //Q   
        } else if (p.izq.equals("{31}")) {
            
        } else if (p.izq.equals("{32}")) {
            String tipo = aux.pop().dcha;
            aux.peek().dcha = tipo;
        } else if (p.izq.equals("{33}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{34}")) {
            aux.pop();//S
            aux.pop();// )
            String tipoE = aux.pop().dcha;
            if(!tipoE.equals("boolean")){
                System.out.println("Error: Tipos de la expresion incompatibles.\n");
            }
            aux.pop();//(
            aux.pop();//if
        } else if (p.izq.equals("{35}")) {
            aux.pop(); // puntoycoma
            int posId = Integer.parseInt(aux.pop().dcha); // id
            String tipo = aux.pop().dcha;
            AL.getPila().peek().simbolos.get(posId).setTipo(tipo); // T y setTipo
            AL.getPila().peek().simbolos.get(posId).setDesp(AL.getPila().peek().getDespl()); // setDesplazamiento
            if(tipo.equals("string")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+64); // Actulizar desplazamiento de la tabla
            if(tipo.equals("entero")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+2); // Actulizar desplazamiento de la tabla
            if(tipo.equals("boolean")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
            aux.pop(); // let
        } else if (p.izq.equals("{37}")) {
            aux.pop(); // }
            aux.pop(); // Y
            aux.pop(); // {
            aux.pop(); // )
            String tipoE = aux.pop().dcha;
            if(!(tipoE.equals("entero"))) {
               System.out.println("Error: Tipos del Switch incompatibles.\n");
               return;
            }
            aux.pop(); // (
            aux.pop(); //switch    
            
        } else if (p.izq.equals("{43}")) {
            aux.peek().dcha="void";aux.peek().dcha="void";
        } else if (p.izq.equals("{44}")) {
            aux.pop();
            aux.peek().dcha = "entero";
        } else if (p.izq.equals("{45}")) {
            aux.pop();
            aux.peek().dcha = "boolean";
        } else if (p.izq.equals("{46}")) {
            aux.pop();
            aux.peek().dcha = "string";
        } else if (p.izq.equals("{47.1}")) {
            //nada
        } else if (p.izq.equals("{47.2}")) {
            int posId = Integer.parseInt(aux.pop().dcha); // id
            AL.getPila().elementAt(0).simbolos.get(posId).setTipo("funcion");
            AL.getPila().elementAt(0).simbolos.get(posId).setTipoRetorno(aux.pop().dcha);
            aux.pop();
        } else if (p.izq.equals("{47.3}")) {
            //nada
        } else if (p.izq.equals("{48}")) {
            String h = aux.pop().dcha;
            aux.peek().dcha = h;
        } else if (p.izq.equals("{49}")) {
            aux.peek().dcha = "void";
        } else if (p.izq.equals("{50.1}")) {
            int posId = Integer.parseInt(aux.pop().dcha); // id 
            String tipo = aux.pop().dcha; // T
            AL.getPila().elementAt(0).simbolos.get(AL.getPila().elementAt(0).simbolos.size()-1).setTipoParam(tipo);
            AL.getPila().peek().simbolos.get(posId).setTipo(tipo);
            AL.getPila().peek().simbolos.get(posId).setDesp(AL.getPila().peek().getDespl()); // setDesplazamiento
            if(tipo.equals("string")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+64); // Actulizar desplazamiento de la tabla
            if(tipo.equals("entero")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+2); // Actulizar desplazamiento de la tabla
            if(tipo.equals("boolean")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
        } else if (p.izq.equals("{50.2}")) {
            aux.pop();
        } else if (p.izq.equals("{51}")) {
            AL.getPila().elementAt(0).simbolos.get(AL.getPila().elementAt(0).simbolos.size()-1).setTipoParam("void");
        } else if (p.izq.equals("{52.1}")) {
            int posId = Integer.parseInt(aux.pop().dcha); // id 
            String tipo = aux.pop().dcha; // T
            AL.getPila().elementAt(0).simbolos.get(AL.getPila().elementAt(0).simbolos.size()-1).setTipoParam(tipo);
            AL.getPila().peek().simbolos.get(posId).setTipo(tipo);
            AL.getPila().peek().simbolos.get(posId).setDesp(AL.getPila().peek().getDespl()); // setDesplazamiento
            if(tipo.equals("string")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+64); // Actulizar desplazamiento de la tabla
            if(tipo.equals("entero")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+2); // Actulizar desplazamiento de la tabla
            if(tipo.equals("boolean")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
            aux.pop(); // coma
        } else if (p.izq.equals("{52.2}")) { 
            aux.pop();
        } else if (p.izq.equals("{53}")) {
           AL.getPila().elementAt(0).simbolos.get(AL.getPila().elementAt(0).simbolos.size()-1).setNumParam(AL.getPila().elementAt(0).simbolos.get(AL.getPila().elementAt(0).simbolos.size()-1).getTipoParam().size());
        } else if (p.izq.equals("{54}")) {
            
        } else if (p.izq.equals("{55}")) {
            //nada
        } else if (p.izq.equals("{56}")) {

        } else if (p.izq.equals("{57}")) {

        } else if (p.izq.equals("{58}")) {

        } else if (p.izq.equals("{59}")) {

        }
    }

    public boolean compara(ArrayList<String> a, ArrayList<String> b){
        boolean c = true;
        if(a.size() == b.size()){
            for(int i=0; i< a.size(); i++){
                if(!a.get(i).equals(b.get(i))){
                    c = false;
                }
            }
        }else{
            c = false;
        }
        return c;
    }

    // Imprime la pila
    public String imprimirPila(Stack<Par> pila, Stack<Par> aux) {
        Iterator<Par> it = pila.iterator();
        Iterator<Par> itaux = aux.iterator();
        String x = "";
        while (itaux.hasNext()) {
            Par p = itaux.next();
            x = p.izq + " - " + p.dcha + "\n" + x;
        }
        x = "\nAux:\n" + x;
        while (it.hasNext()) {
            Par p = it.next();
            x = p.izq + " - " + p.dcha + "\n" + x;
        }
        x = "Principal:\n" + x;
        return x;
    }

    // Imprime la tabla de simbolos
    public void imprimirTablas(ArrayList<TablaSimbolos> ats) throws IOException {
        FileWriter escrituraTS = new FileWriter(ficheroTablaSimbolos);
        Collections.sort(ats, (ts1, ts2) -> ts1.compareTo(ts2, ts1));
        for (TablaSimbolos ts : ats) {
            escrituraTS.write(ts.imprimir());
        }
        escrituraTS.close();
    }
}