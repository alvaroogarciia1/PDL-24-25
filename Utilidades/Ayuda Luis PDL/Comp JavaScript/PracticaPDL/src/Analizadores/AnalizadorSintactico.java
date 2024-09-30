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
    public String parse = "Descendente";
    File codigoFuente;
    static File ficheroTokens;
    File ficheroTablaSimbolos;
    static ArrayList<String> parametrosComp;
    GestorErrores gestorErrores;
    static int posIdFuncionActual;
    File pilas;
    FileWriter fwPilas;
    boolean finale = false;

    public AnalizadorSintactico(File f) throws IOException {

        // Archivos
        codigoFuente = f;
        ficheroTokens = new File("tokens.txt");
        ficheroTablaSimbolos = new File("tablaSimbolos.txt");
        pilas = new File("pilas.txt");
        fwPilas = new FileWriter(pilas);
        
        // Gestor Errores
        gestorErrores = new GestorErrores("errores.txt");

        // Anañizador Lexico
        AL = new AnalizadorLexico(f, gestorErrores);

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
        if(token.getIdentifier().equals("ID")) posId += token.getAttribute();
        if (token != null)
            escrituraTokens.write(token.toString() + "\n");
        String identificador = token.getIdentifier().toLowerCase();
        String pos = "";
        String consecuente[];
        // Mientras el token leido no sea final del fichero
        while (!pila.peek().izq.equals("$")) {
            pos = "";
            // Si la cima es lambda, se desapila el simbolo
            if (pila.peek().izq.equals("lambda")) {
                pila.pop();
            }
            // Si la cima es un terminal
            else if (terminales.contains(pila.peek().izq)) {
                // System.out.println("identidicador: " +  identificador + " Cima Pila P: " + pila.peek().izq);
                // Si la cima es igual al token actual se desapila y se llama al siguiente token
                if (pila.peek().izq.equals(identificador)) {
                    if(token.getIdentifier().equals("ID")){
                        posId = "" + token.getAttribute();
                        pila.peek().dcha = posId;//pasamos la posicion del id en la ts como atributo
                    }
                    aux.push(pila.pop());
                    token = AL.leerToken();
                    if (token != null && !finale)
                        escrituraTokens.write(token.toString() + "\n");
                        if(token.getIdentifier().equals("EOF")) finale = true;

                    identificador = token.getIdentifier().toLowerCase();
                } else {
                    gestorErrores.producirError(10, AL.getLinea(), token.getIdentifier());
                }
                // Si es accion semantica
            } else if (esAccionSemantica(pila.peek().izq)) {
                if(pila.peek().izq.equals("{47.4}")) imprimirPTS.add(AL.getPila().pop());
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
                    gestorErrores.setParse(parse);
                    if (Integer.parseInt(consecuente[0]) == 57) {
                        if (AL.getPila().peek().getNumeroTabla() != 0)
                            imprimirPTS.add(AL.getPila().pop());
                        AL.setCreaTabla(true);
                    } else if(Integer.parseInt(consecuente[0]) == 22 || Integer.parseInt(consecuente[0]) == 25  || Integer.parseInt(consecuente[0]) == 26 || Integer.parseInt(consecuente[0]) == 28 || Integer.parseInt(consecuente[0]) == 30 || Integer.parseInt(consecuente[0]) == 32 || Integer.parseInt(consecuente[0]) == 34 || Integer.parseInt(consecuente[0]) == 37){
                        AL.setEsSentencia(true);
                    } else if(Integer.parseInt(consecuente[0]) == 21 ) {
                        AL.setEsFunction(true);
                    }
                    for (int i = consecuente.length - 1; i > 0; i--) {
                        if(consecuente[i].equals("id")){
                            pila.push(new Par(consecuente[i], posId));
                        } 
                        else pila.push(new Par(consecuente[i], "-"));
                    }
                } else { 
                    gestorErrores.producirError(11, AL.getLinea(), pos);
                }
            }
            //imprimirPilas(pila, aux, fwPilas);
        }
        // Si el token final es EOF habremos llegado al final del programa
        if (!identificador.equals("$") && !aux.peek().izq.equals("P")) {
            gestorErrores.producirError(12, AL.getLinea(), "");
        }
        if(gestorErrores.getImprimir()){
            while(!AL.getPila().isEmpty()){
                imprimirPTS.add(AL.getPila().pop()); 
            }
            imprimirTablas(imprimirPTS);
        } else {
            gestorErrores.imprimirError(gestorErrores.getError());
        }
        escrituraTokens.close();
        fwPilas.close();
        return parse;
    }

    public void escribirParse(String s) throws IOException{
        File ficheroParse = new File("parse.txt");
        FileWriter escrituraParse = new FileWriter(ficheroParse);
        escrituraParse.write(s);
        escrituraParse.close();
    }

    public boolean esAccionSemantica(String acc /* {XX} */) {
        if (acc.charAt(0) == '{')
            return true;
        return false;
    }

    public void ejecutarAccSem(Par p, Stack<Par> principal, Stack<Par> aux) throws IOException {
        if (p.izq.equals("{1}")) {
            String tipo1=aux.pop().dcha;//1
            String tipoR=aux.pop().dcha;//R
            if(tipo1.equals("void") || (tipo1.equals("boolean") && tipoR.equals("boolean"))){
                aux.peek().dcha= tipoR;
            }
            else{
                gestorErrores.producirError(13, AL.getLinea(), "");
            }
        } else if (p.izq.equals("{2}")) {
            String tipo1=aux.pop().dcha;//1
            String tipoR=aux.pop().dcha;//R
            aux.pop();//||
            if(tipo1.equals("void") || (tipo1.equals("boolean") && tipoR.equals("boolean"))){
                aux.peek().dcha= tipoR;
            }
            else{
                gestorErrores.producirError(13,AL.getLinea(), "");
            }
        } else if (p.izq.equals("{3}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{4}")) {
            String tipo2=aux.pop().dcha;//2
            String tipoU=aux.pop().dcha;//U
            if(tipo2.equals("void")){
                aux.peek().dcha= tipoU;
            } else if(tipo2.equals(tipoU)){
                aux.peek().dcha = "boolean";
            }
            else{
                gestorErrores.producirError(13,AL.getLinea(), "");
            }
        } else if (p.izq.equals("{5}")) {
            String tipo2=aux.pop().dcha;// 2
            String tipoU=aux.pop().dcha;// U
            aux.pop();//==
            if(tipo2.equals("void")){
                aux.peek().dcha= tipoU;
            } else if(tipo2.equals(tipoU)){
                aux.peek().dcha = "boolean";
            }
            else{
                gestorErrores.producirError(13,AL.getLinea(), "");
            }
        } else if (p.izq.equals("{6}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{7}")) {
            String tipo3=aux.pop().dcha;//3
            String tipoV=aux.pop().dcha;//V
            if(tipo3.equals("void") || tipo3.equals(tipoV)){
                aux.peek().dcha= tipoV;
            }
            else{
                gestorErrores.producirError(13,AL.getLinea(), "");
            }
        } else if (p.izq.equals("{8}")) {
            String tipo3=aux.pop().dcha;//3
            String tipoV=aux.pop().dcha;//V
            //aux.pop(); 
            aux.pop(); // mas
            aux.pop();// G
            if(tipo3.equals("void") || (tipo3.equals("entero") && tipoV.equals("entero"))){
                aux.peek().dcha= tipoV;
            }
            else{
                gestorErrores.producirError(13,AL.getLinea(), "");
            }
        } else if (p.izq.equals("{9}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{12}")) {
            String tipo4=aux.pop().dcha;//4
            String tipoZ=aux.pop().dcha;//Z
            if(tipo4.equals("void") || (tipo4.equals("entero") && tipoZ.equals("entero"))){
                aux.peek().dcha= tipoZ;
            }
            else{
                gestorErrores.producirError(13,AL.getLinea(), "");
            }
        } else if (p.izq.equals("{13}")) {
            String tipo4 = aux.pop().dcha;// 4
            String tipoZ = aux.pop().dcha;// Z
            aux.pop(); // /
            if(tipo4.equals("void") || (tipo4.equals("entero") && tipoZ.equals("entero"))){
                aux.peek().dcha= tipoZ;
            }
            else{
                gestorErrores.producirError(13,AL.getLinea(), "");
            }
        } else if (p.izq.equals("{14}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{15}")) {
            aux.pop(); // O
            int posId = Integer.parseInt(aux.pop().dcha);
            if(!AL.getPila().peek().buscarPos(posId) && !AL.getPila().elementAt(0).buscarPos(posId)) {
                gestorErrores.producirError(14,AL.getLinea(), "");
            }
            else if (AL.getPila().peek().buscarPos(posId)){ //si esta en la tabla de la funcion actual
                String tipo = AL.getPila().peek().simbolos.get(posId).getTipo();
                if(tipo.equals("function")){
                    aux.peek().dcha = AL.getPila().peek().simbolos.get(posId).getTipoRetorno();
                } else {
                    aux.peek().dcha = tipo;
                }
            } else { // si esta en la tabla de la funcion principal
                String tipo = AL.getPila().elementAt(0).simbolos.get(posId).getTipo();
                if(tipo.equals("function")){
                    aux.peek().dcha = AL.getPila().elementAt(0).simbolos.get(posId).getTipoRetorno();
                } else {
                    aux.peek().dcha = tipo;
                }
            }
        } else if (p.izq.equals("{16}")) {
            aux.pop(); // )
            String E = aux.pop().dcha;
            aux.pop(); // (
            aux.peek().dcha = E;
            
        } else if (p.izq.equals("{17}")) {
            aux.pop(); 
            aux.peek().dcha = "entero";            
        } else if (p.izq.equals("{18}")) {
            aux.pop();
            aux.peek().dcha = "string";
        } else if (p.izq.equals("{20}")) {
            aux.pop(); // )
            String L = aux.pop().dcha;
            aux.pop(); // (
            aux.peek().dcha= L;
        } else if (p.izq.equals("{21}")) {
            aux.pop(); // puntoycoma
            String tipo = aux.pop().dcha; // D
            int posId = Integer.parseInt(aux.pop().dcha); // id
            String tipoId = "";
            if(AL.getPila().peek().buscarPos(posId)){
                tipoId = AL.getPila().peek().simbolos.get(posId).getTipo();
            } else{
                tipoId = AL.getPila().elementAt(0).simbolos.get(posId).getTipo();
            }
            if(!tipo.equals(tipoId)){
                gestorErrores.producirError(15,AL.getLinea(), "1");
            }
        } else if (p.izq.equals("{22}")) {
            aux.pop(); //;
            aux.pop(); // )
            String tipoE = aux.pop().dcha;//E
            aux.pop(); // (
            aux.pop(); //alert
            if(tipoE.equals("string") || tipoE.equals("entero")){
                aux.peek().dcha= tipoE;
            }else if(tipoE.equals("boolean")) {
                gestorErrores.producirError(13, AL.getLinea(), "");
            }
            AL.setEsSentencia(false);
        } else if (p.izq.equals("{23.1}")) {
            int posId = Integer.parseInt(aux.pop().dcha); // id
            if (AL.getPila().peek().buscarPos(posId)){ //si esta en la tabla de la funcion actual
                String tipo = AL.getPila().peek().simbolos.get(posId).getTipo();
                if(!tipo.equals("entero") && !tipo.equals("string")) {
                    gestorErrores.producirError(16,AL.getLinea(), "");
                }
            } else if(AL.getPila().elementAt(0).buscarPos(posId)){ // Variable global
                String tipo = AL.getPila().elementAt(0).simbolos.get(posId).getTipo();
                if(!tipo.equals("entero") && !tipo.equals("string")){
                    gestorErrores.producirError(16,AL.getLinea(), "");
                    
                }
            }
            aux.pop(); // (
            aux.pop(); // input
        } else if (p.izq.equals("{23.2}")){
            aux.pop(); // ;
            aux.pop(); // )
        } else if (p.izq.equals("{24}")) { //return X ;
            aux.pop(); // ;
            String tipoX = aux.pop().dcha; // X
            
            if(!tipoX.equals(AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).getTipoRetorno())){
                gestorErrores.producirError(17,AL.getLinea(), "");
            } 
            aux.pop(); //return
        } else if (p.izq.equals("{25}")) {
            String tipo = aux.pop().dcha;//E
            aux.pop(); // =
            AL.setEsSentencia(false);
            aux.peek().dcha = tipo;
        } else if (p.izq.equals("{26}")) {
            String tipo = aux.pop().dcha;//E
            aux.pop(); // |=
            aux.peek().dcha = tipo;
            AL.setEsSentencia(false);
        } else if (p.izq.equals("{27}")) {
            aux.pop(); // )
            aux.pop(); // L
            aux.pop(); // (     
            Par D = new Par(aux.peek().izq, aux.pop().dcha); // D
            int posId = Integer.parseInt(aux.peek().dcha);//obtenemos la posicion de la pila para poder extraer la lista de parametros
            ArrayList<String> parametros = AL.getPila().elementAt(0).simbolos.get(posId).getTipoParam();
            if(!compara(parametros, parametrosComp)){
                gestorErrores.producirError(18,AL.getLinea(), "");
                
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
            AL.setEsSentencia(false);
        } else if (p.izq.equals("{29}")) {
            parametrosComp = new ArrayList<String>();
            parametrosComp.add("void");
        } else if (p.izq.equals("{30}")) {
            String tipo = aux.pop().dcha; // E
            aux.pop(); // ,
            parametrosComp.add(tipo);
        } else if(p.izq.equals("{30.1}")) {
            aux.pop(); //Q   
            AL.setEsSentencia(false);
        } else if (p.izq.equals("{31}")) {
            
        } else if (p.izq.equals("{32}")) {
            String tipo = aux.pop().dcha;
            aux.peek().dcha = tipo;
            AL.setEsSentencia(false);
        } else if (p.izq.equals("{33}")) {
            aux.peek().dcha="void";
        } else if (p.izq.equals("{34}")) {
            aux.pop();//S
            aux.pop();// )
            AL.setEsSentencia(false);
            String tipoE = aux.pop().dcha;
            if(!tipoE.equals("boolean")){
                gestorErrores.producirError(19,AL.getLinea(), "");
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
            if(tipo.equals("entero")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
            if(tipo.equals("boolean")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
            aux.pop(); // let
        } else if(p.izq.equals("{36}")){
            aux.pop();//S
        } else if(p.izq.equals("{37.1}")){
            String tipoE = aux.pop().dcha; // E
            if(!(tipoE.equals("entero"))) {
               gestorErrores.producirError(20,AL.getLinea(), "");
            }
            aux.pop(); // (
            aux.pop(); //switch    
            AL.setEsSentencia(false);
        } else if (p.izq.equals("{37.2}")) {
            aux.pop(); // }
            aux.pop(); // Y
            aux.pop(); // {
            aux.pop(); // )
        }  else if (p.izq.equals("{38}")) {
            aux.pop(); // M
            aux.pop(); // :
            aux.pop(); // entero
            aux.pop(); // case
        } else if (p.izq.equals("{39}")) {
            aux.pop(); // N
            aux.pop(); // ;
            aux.pop(); // break
        } else if (p.izq.equals("{40}")) {
            aux.pop(); // N
        } else if (p.izq.equals("{41}")) {
            aux.pop(); //Y
        } else if (p.izq.equals("{42}")) {
            aux.pop(); //:
            aux.pop(); //default
        } else if (p.izq.equals("{43}")) {
            aux.peek().dcha="void";
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
            posIdFuncionActual = Integer.parseInt(aux.pop().dcha); // id
            AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).setTipo("function");
            String tipoRet = aux.pop().dcha; // H
            aux.pop(); // function
            AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).setTipoRetorno(tipoRet);
            AL.setParametros(true);
        } else if (p.izq.equals("{47.3}")) {
            aux.pop(); // (
            aux.pop(); // A
            aux.pop(); // )
            AL.setParametros(false);
        } else if(p.izq.equals("{47.4}")){
            aux.pop();//{
            aux.pop();//}
        } else if (p.izq.equals("{48}")) {
            String h = aux.pop().dcha;
            aux.peek().dcha = h;
        } else if (p.izq.equals("{49}")) {
            aux.peek().dcha = "void";
        } else if (p.izq.equals("{50.1}")) {
            int posId = Integer.parseInt(aux.pop().dcha); // id 
            String tipo = aux.pop().dcha; // T
            AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).setTipoParam(tipo);
            AL.getPila().peek().simbolos.get(posId).setTipo(tipo);
            AL.getPila().peek().simbolos.get(posId).setDesp(AL.getPila().peek().getDespl()); // setDesplazamiento
            if(tipo.equals("string")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+64); // Actulizar desplazamiento de la tabla
            if(tipo.equals("entero")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
            if(tipo.equals("boolean")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
        } else if (p.izq.equals("{50.2}")) {
            aux.pop(); // K
        } else if (p.izq.equals("{51}")) {
            AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).setTipoParam("void");
        } else if (p.izq.equals("{52.1}")) {
            int posId = Integer.parseInt(aux.pop().dcha); // id 
            String tipo = aux.pop().dcha; // T
            AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).setTipoParam(tipo);
            AL.getPila().peek().simbolos.get(posId).setTipo(tipo);
            AL.getPila().peek().simbolos.get(posId).setDesp(AL.getPila().peek().getDespl()); // setDesplazamiento
            if(tipo.equals("string")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+64); // Actulizar desplazamiento de la tabla
            if(tipo.equals("entero")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
            if(tipo.equals("boolean")) AL.getPila().peek().setDespl(AL.getPila().peek().getDespl()+1); // Actulizar desplazamiento de la tabla
            aux.pop(); // coma
        } else if (p.izq.equals("{52.2}")) { 
            aux.pop(); // K
        } else if (p.izq.equals("{53}")) {
           AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).setNumParam(AL.getPila().elementAt(0).simbolos.get(posIdFuncionActual).getTipoParam().size());
        } else if (p.izq.equals("{54}")) {
            aux.pop();//
            aux.pop();
        } else if (p.izq.equals("{55}")) {
            aux.pop();
        } else if (p.izq.equals("{56}")) {
            aux.pop();//B
            aux.pop();//P
        } else if (p.izq.equals("{57}")) {
            aux.pop();//F
            aux.pop();//P
        } else if (p.izq.equals("{58}")) {

        } else if (p.izq.equals("{59}")) {
            aux.pop();//eof
        }
    }

    public boolean compara(ArrayList<String> a, ArrayList<String> b){
        boolean c = true;
        if(a.size() == b.size()){
            for(int i=0; i< a.size(); i++){
                //System.out.println(a.get(i) + "////" + b.get(i));
                if(!a.get(i).equals(b.get(i))){
                    c = false;
                }
            }
        }else{
            c = false;
        }
        return c;
    }

    /*
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
    */

    // Imprime todas las iteraciones de la pila
    public void imprimirPilas(Stack<Par> principal, Stack<Par> aux, FileWriter fw) throws IOException{
        int tamPrincipal = principal.size();
        int tamAux = aux.size();
        Iterator<Par> itPrincipal = principal.iterator();
        Iterator<Par> itAux = aux.iterator();
        Par pp = new Par("", "");
        Par pa = new Par("", "");
        String format = "%-10s %-5s %-5s %-10s %-10s";
        String formatSimplePP = "%-10s %-5s %s" ;
        String formatSimplePA = "%-16s %-5s %-10s %-10s";
        String formatHeaders = "%11s %18s";
        String imp = "--------------------------------------------\n";
        String linea = "";
        if(tamPrincipal >= tamAux){
            while(itPrincipal.hasNext()){
                while(itAux.hasNext()){
                    pp = itPrincipal.next();
                    pa = itAux.next();
                    linea =  String.format(format, pp.izq, pp.dcha,"|", pa.izq, pa.dcha);
                    imp = linea + "\n" + imp;
                }
                if(itPrincipal.hasNext()){
                    pp = itPrincipal.next();
                    linea = String.format(formatSimplePP, pp.izq, pp.dcha , "|");
                    imp = linea + "\n" + imp;
                }
            }
        } else{
            while(itAux.hasNext()){
                while(itPrincipal.hasNext()){
                    pp = itPrincipal.next();
                    pa = itAux.next();
                    linea =  String.format(format, pp.izq, pp.dcha, "|", pa.izq, pa.dcha);
                    imp = linea + "\n" + imp;
                }
                if(itAux.hasNext()){
                    pa = itAux.next();
                    linea = String.format(formatSimplePA,"", "|", pa.izq, pa.dcha);
                    imp = linea + "\n" + imp;
                }
            }
        }
        imp = "--------------------------------------------\n" + imp;
        linea = String.format(formatHeaders, "Principal", "Aux");
        imp = linea + "\n" + imp;
        imp = "--------------------------------------------\n" + imp;
        fw.write(imp);
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