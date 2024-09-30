package Analizadores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import Tablas.*;

public class AnalizadorSintactico {
    ArrayList<String> terminales;
    Map<String, String> tabla;
    Stack<Trio> pila;
    Stack<Trio> pilaAux;
    String parse;
    Gramatica g;
    AnalizadorLexico AL;
    int posIdFuncionActual;
    ArrayList<String> parametrosComp;
    static Tokens token;
    static ArrayList<TablaSimbolos> ats;
    static FileWriter escribirTokens;
    static FileWriter escribirTS;
    static FileWriter escribirParse;
    //static FileWriter escribirPila;
    GestorErrores gestorErrores;


    public AnalizadorSintactico(File leerFich) throws IOException{
        escribirTokens = new FileWriter("Tokens.txt");
        escribirTS = new FileWriter("TablaSimbolos.txt");
        escribirParse = new FileWriter("parse.txt");
        //escribirPila = new FileWriter("pila.txt");
        gestorErrores = new GestorErrores();
        AL = new AnalizadorLexico(leerFich, gestorErrores);
        parse = "Descendente";
        pila = new Stack<Trio>();
        pilaAux = new Stack<Trio>();
        pila.push(new Trio("$","",""));
        pila.push(new Trio("P","",""));

        g = new Gramatica();
        terminales = g.getTerminales();
        tabla = g.getTablas();
        ats = new ArrayList<TablaSimbolos>();
        token = new Tokens("");
        AL.leer();


    }

    public boolean esAccionSem(String aux){
        if(aux.charAt(0) == '{') return true;
        return false;
    }

    public void exeAccionSem(Trio p, Stack<Trio> pila, Stack<Trio> pilaAux) throws IOException{
        if(p.izq.equals("{1}")){                                    // {1} Pop(2)
            pilaAux.pop();                                          // P
            pilaAux.pop();                                          // B
        }

        else if(p.izq.equals("{2}")){                               // {2} Pop(2)
            pilaAux.pop();                                          // P
            pilaAux.pop();                                          // F
        }

        else if(p.izq.equals("{3}")){                               // {3} Pop(1)
            pilaAux.pop();                                          // eof
        }

        else if(p.izq.equals("{4}")){                               // {4} lambda
        }

        else if(p.izq.equals("{5.1}")){                             // {5.1} zona_dec = true
            AL.zona_dec = true;
        }  
        
        else if(p.izq.equals("{5.2}")){                             // {5.2}
        //System.out.println(AL.getTS().peek().getDesp());
            int posID = Integer.parseInt(pilaAux.pop().der);        // posID
            String tipoT = pilaAux.pop().der;                       // T.tipo
            pilaAux.pop();                                          // let
            if(!AL.getTS().peek().tablaSimbolos.get(posID).getTipo().isEmpty()){
                System.out.println("Variable ya inicializada en linea " + AL.linea);
                //gestorErrores.error(AL.linea, 2, AL.getTS().peek().tablaSimbolos.get(posID).getLexema());
                pilaAux.peek().med = "error";
                pilaAux.peek().der = "error";
            }
            else{
                AL.getTS().peek().tablaSimbolos.get(posID).setTipo(tipoT);
                AL.getTS().peek().tablaSimbolos.get(posID).setDespl(AL.getTS().peek().getDesp());
                if(tipoT.equals("int") || tipoT.equals("log")){
                    AL.getTS().peek().setDesp(AL.getTS().peek().getDesp()+1);
                    //System.out.println("Desplazado: " + AL.getTS().peek().getDesp());
                }
                if(tipoT.equals("string")){
                    AL.getTS().peek().setDesp(AL.getTS().peek().getDesp()+64);
                }
                pilaAux.peek().med = "void";                        // El valor de retorno de un let es distinto a error a si que por defecto usamos void
            }
            AL.zona_dec = false;
        }
        
        else if(p.izq.equals("{5.3}")){
            pilaAux.pop();                                          // ;
        }

        else if(p.izq.equals("{6}")){                               // {6} {if (E.tipo = log && S.tipo = tipo.ok) then B.tipo := tipo.ok else B.tipo := tipo.error; B.tiporet := S.tiporet}
            String tipoS = pilaAux.peek().der;                      // S.tipo
            String tiporetS = pilaAux.pop().med;                    // S.tiporet
            pilaAux.pop();                                          // )
            String tipoE = pilaAux.pop().der;                       // E.tipo 
            pilaAux.pop();                                          // (
            pilaAux.pop();                                          // if
            if(tipoE.equals("log") && !tipoS.equals("error"))
                pilaAux.peek().med = tiporetS;                      // B.tiporet = S.tiporet              
            else{
                pilaAux.peek().der = "error";                       // B.tiporet = error    
                System.out.println("Error 6");                      // B.tiporet = error  
                gestorErrores.error(AL.linea, 5, "");
            }
        }

        else if(p.izq.equals("{7}")){                               // {7} {B.tipo := S.tipo; B.tiporet := S.tiporet}
            Trio trio_aux = pilaAux.pop();                          // S
            String aux = trio_aux.med;                              // S.tipo 
            String aux2 = trio_aux.der;                             // S.tiporet
            pilaAux.peek().med = aux;                               // B.tiporet = S.tiporet              
            pilaAux.peek().der = aux2;                              // B.tipo = S.tip
        }

        else if(p.izq.equals("{8}")){                               // {8} {if(I.tipo != tipo.error && E.tipo = log && I1.tipo != tipo.error) then B.tipo := C.tipo; B.tiporet := C.tiporet; else B.tipo := tipo.error}
            pilaAux.pop();                                          // }
            String tipoC = pilaAux.peek().der;                      // C.tipo
            String tiporetC = pilaAux.pop().med;                   // C.tiporet
            pilaAux.pop();                                          // {
            pilaAux.pop();                                          // )
            String tipoI1 = pilaAux.pop().der;                      // I1.tipo
            pilaAux.pop();                                          // ;
            String tipoE = pilaAux.pop().der;                       // E.tipo
            pilaAux.pop();                                          // ;
            String tipoI = pilaAux.pop().der;                       // I.tipo
            pilaAux.pop();                                          // (
            pilaAux.pop();                                          // for

            if(!tipoI.equals("error") && tipoE.equals("log") && !tipoI1.equals("error")){
                pilaAux.peek().med = tiporetC;                      // B.tiporet = C.tiporet              
                pilaAux.peek().der = tipoC;                         // B.tipo = C.tipo
            }else{
                pilaAux.peek().der = "error";                       // B.tipo = error
                System.out.println("Error 8");                      // B.tipo = error
                gestorErrores.error(AL.linea, 5, "");
            }
        }

        else if(p.izq.equals("{9}")){                               // {9} {T.tipo := int} {Aux-1 = int}
            pilaAux.pop();                                          // Pop(1)
            pilaAux.peek().der = "int";                             // La derecha de la cima de la pila aux es int               
        }

        else if(p.izq.equals("{10}")){                              // {10} {T.tipo := string} {Aux-1 = string}
            pilaAux.pop();                                          // Pop(1)
            pilaAux.peek().der = "string";                          // La derecha de la cima de la pila aux es string               
        }

        else if(p.izq.equals("{11}")){                              // {11} {T.tipo := log} {Aux-1 = log}
            pilaAux.pop();                                          // Pop(1)
            pilaAux.peek().der = "log";                             // La derecha de la cima de la pila aux es log               
        }

        else if(p.izq.equals("{12}")){                              // {12} {if(buscarTipoTS(id.pos) = D.tipo) then S.tipo := tipo.ok else S.tipo := tipo.error} | {if(buscarTipoTS(Aux-2) = Aux-1) then Aux-3 := tipo.ok else Aux-3 := tipo.error}
            pilaAux.pop();                                          // ;
            String aux = pilaAux.pop().der;                         // D.tipo
            int posID = Integer.parseInt(pilaAux.pop().der);        // Sacas variable posID

            if(AL.getTS().peek().buscarPos(posID)){
                if(!AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals(aux)){
                    pilaAux.peek().der = "error";
                    System.out.println("Error 12 en linea " + AL.linea);
                    if(aux.equals("function")){
                        gestorErrores.error(AL.linea, 12, AL.getTS().peek().tablaSimbolos.get(posID).getLexema());
                        AL.getTS().elementAt(0).tablaSimbolos.remove(posID);
                        AL.getTS().elementAt(0).setDesp(AL.getTS().elementAt(0).getDesp()-1);
                    } 
                    else gestorErrores.error(AL.linea, 6, AL.getTS().peek().tablaSimbolos.get(posID).getLexema());
                }
            }
            else if(AL.getTS().elementAt(0).buscarPos(posID)){
                if(!AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipo().equals(aux)){
                    pilaAux.peek().der = "error";
                    System.out.println("Error 12");
                    if(aux.equals("function")){
                        gestorErrores.error(AL.linea, 12, AL.getTS().elementAt(0).tablaSimbolos.get(posID).getLexema());
                        AL.getTS().elementAt(0).tablaSimbolos.remove(posID);
                        AL.getTS().elementAt(0).setDesp(AL.getTS().elementAt(0).getDesp()-1);
                    } 
                    else gestorErrores.error(AL.linea, 6, AL.getTS().elementAt(0).tablaSimbolos.get(posID).getLexema());
                }
            }
            pilaAux.peek().med = "void";
        }

        else if(p.izq.equals("{13}")){                              // {13} {if(E.tipo = int || E.tipo = string) then S.tipo := tipo.ok ; S.tiporet = tipo.ok else S.tipo := tipo.error} || {if(Aux-2 = int || Aux-2 = string) then Aux-5 := tipo.ok ; Aux-5 = tipo.ok else Aux-5 := tipo.error}
            pilaAux.pop();                                          // ;
            pilaAux.pop();                                          // )
            String aux = pilaAux.pop().der;                         // E.tipo
            pilaAux.pop();                                          // (
            pilaAux.pop();                                          // print
            if(!(aux.equals("int") || aux.equals("string"))){
                pilaAux.peek().der = "error";
                System.out.println("Error 13");
                gestorErrores.error(AL.linea, 5, "");
            }
            else pilaAux.peek().med = "void";
        }

        else if(p.izq.equals("{14.1}")){                            // {14.1} la zona de declaracion del input esta a true
            AL.zona_dec_input = true;
        }

        else if(p.izq.equals("{14.2}")){                            // {14.2} {if(buscarTipoTS(id.pos) = int || buscarTipoTS(id.pos) = string) then S.tipo := tipo.ok; S.tiporet = tipo.ok else S.tipo := tipo.error} | {if(buscarTipoTS(Aux-2) = int || buscarTipoTS(Aux-2) = string) then Aux-5 := tipo.ok; Aux-5 = tipo.ok else Aux-5 := tipo.error}
            pilaAux.pop();                                          // ;
            pilaAux.pop();                                          // )
            int posID = Integer.parseInt(pilaAux.pop().der);        // Sacas variable posID
            pilaAux.pop();                                          // (
            pilaAux.pop();                                          // input

            if(AL.getTS().peek().buscarPos(posID)){
                if(AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals("")){
                    AL.getTS().peek().tablaSimbolos.get(posID).setTipo("int");
                    AL.getTS().peek().tablaSimbolos.get(posID).setDespl(AL.getTS().peek().getDesp());
                    AL.getTS().peek().setDesp(AL.getTS().peek().getDesp()+1);
                } 
                else if(!(AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals("int") || AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals("string"))){
                    System.out.println("Error 14");
                    pilaAux.peek().der = "error";
                    gestorErrores.error(AL.linea, 5, "");
                }
            }

            else if(AL.getTS().elementAt(0).buscarPos(posID)){
                if(!(AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipo().equals("int") || AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipo().equals("string"))){
                    System.out.println("Error 14");
                    pilaAux.peek().der = "error";
                    gestorErrores.error(AL.linea, 5, "");
                }
            }
            else{
                System.out.println("Error 14");
                pilaAux.peek().der = "error";
                gestorErrores.error(AL.linea, 3, "No se como se ha dado este caso");
            }
            pilaAux.peek().med = "void";
            AL.zona_dec_input = false;
        }

        else if(p.izq.equals("{15}")){                              // {15} {If X.tipo != tipo.error) then S.tipo := tipo.ok else S.tipo := tipo.error; S.tiporet := X.tipo}
            pilaAux.pop();                                          // Pop return
            String aux = pilaAux.pop().der;                         // X.tipo
            pilaAux.pop();                                          // Pop ;
            if(aux.equals("error")){                                // Si X es error, S es error
                pilaAux.peek().der = aux;                           // Si es error, la derecha de la cima de la pila es error
            }else{
                pilaAux.peek().med = aux;                           // El medio de la cima de la pila aux es aux 
            }
                          
        }
        else if(p.izq.equals("{16}")){                              // {16} {D.tipo := E.tipo} Pop(2) {Aux-2 = Aux-0}
            String aux = pilaAux.pop().der;                         // E.tipo
            pilaAux.pop();
            pilaAux.peek().der = aux;                               // D.tipo
        }

        else if(p.izq.equals("{17}")){                              // {17} {D.tipo := L.tipo} {Aux-3 = Aux-1} Pop(3)
            pilaAux.pop();                                          
            pilaAux.pop();                                          // L.tipo
            pilaAux.pop();
            Trio D = new Trio(pilaAux.peek().izq, pilaAux.peek().med, pilaAux.pop().der); // D
            int posId = Integer.parseInt(pilaAux.peek().der);       //obtenemos la posicion de la pila para poder extraer la lista de parametros

            if(AL.getTS().elementAt(0).tablaSimbolos.get(posId).getTipoPar().isEmpty() && !parametrosComp.isEmpty()) {
                System.out.println("Error 17");                     // En caso de que sean voids no tienen argumentos por lo que se comparan si ambos estan vacios
                gestorErrores.error(AL.linea, 7, AL.getTS().elementAt(0).tablaSimbolos.get(posId).getLexema());
            }
            else if(!AL.getTS().elementAt(0).tablaSimbolos.get(posId).getTipoPar().isEmpty() && parametrosComp.isEmpty()) {
                System.out.println("Error 17");
                gestorErrores.error(AL.linea, 7, AL.getTS().peek().tablaSimbolos.get(posId).getLexema());
            }
            else{
                ArrayList<String> parametros = AL.getTS().elementAt(0).tablaSimbolos.get(posId).getTipoPar();
                if(!compara(parametros, parametrosComp)){
                    System.out.println("Error 17");
                    gestorErrores.error(AL.linea, 7, AL.getTS().elementAt(0).tablaSimbolos.get(posId).getLexema());
                }
            }
            pilaAux.push(D);                                        //devolvemos la D a la pila
            pilaAux.peek().der = "function";
        }

        else if(p.izq.equals("{18}")){                              // {18} {X.tipo := E.tipo} Pop(1) {Aux-2 = Aux-1}
            String aux = pilaAux.pop().der;                            // E.tipo
            pilaAux.peek().der = aux;                                  // X.tipo
        }

        else if(p.izq.equals("{19}")){                              // {19} {X.tipo := void} {Aux-1 = void}
            pilaAux.peek().der = "void";                            // La derecha de la cima de la pila aux es void     
        }

        else if(p.izq.equals("{20}")){                              // {20}
            String tiporetC = pilaAux.peek().med;                   // C1.tiporet
            String tipoC = pilaAux.pop().der;                       // C.tipo
            String tiporetB = pilaAux.peek().med;                   // B.tiporet
            String tipoB = pilaAux.pop().der;                       // B.tipo
            if(tipoC.equals("error") || tipoB.equals("error")){
                System.out.println("Error 20");
                pilaAux.peek().med = "error";
                pilaAux.peek().der = "error";
                gestorErrores.error(AL.linea, 8, "");
            }
            else{
                if(tiporetB.equals("void")){
                    pilaAux.peek().med = tiporetC;
                }
                else if(tiporetC.equals("void")){
                    pilaAux.peek().med = tiporetB;
                }
                else if(tiporetC.equals(tiporetB)){
                    pilaAux.peek().med = tiporetC;
                }
                else{
                    System.out.println("Error 20");
                    pilaAux.peek().med = "error";
                    pilaAux.peek().der = "error";
                    gestorErrores.error(AL.linea, 8, "");
                }
            }
        }

        else if(p.izq.equals("{21}")){                              // {21} {C.tipo := tipo.ok ; C.tiporet := void}
            pilaAux.peek().med = "void";                            // El medio de la cima de la pila aux (C) es void
            
        }

        else if(p.izq.equals("{22.1}")){
            String aux = pilaAux.pop().der;                         // E.tipo
            if(aux.equals("int") || aux.equals("string") ||aux.equals("log")){
                parametrosComp = new ArrayList<String>();
                parametrosComp.add(aux);
            }
            else{
                System.out.println("Error 22");
                pilaAux.peek().der = "error";
                gestorErrores.error(AL.linea, 8, "");
            }
        }

        else if(p.izq.equals("{22.2}")){                            // {22.2} Pop(Q)
            pilaAux.pop();
        }

        else if(p.izq.equals("{23}")){                              // {23} {L.tipo := void} {Aux-1 = void}
            parametrosComp = new ArrayList<String>();
            parametrosComp.add("void");     
        }

        else if(p.izq.equals("{24.1}")){
            String aux = pilaAux.pop().der;                         // E.tipo
            pilaAux.pop();
            if(aux.equals("int") || aux.equals("string") ||aux.equals("log")){
                parametrosComp.add(aux);
            }
            else{
                System.out.println("Error 24");
                pilaAux.peek().der = "error";
                gestorErrores.error(AL.linea, 8, "");
            }
        }

        else if(p.izq.equals("{24.2}")){                            // {24.2} Pop(Q)
            pilaAux.pop();
        }

        else if(p.izq.equals("{25}")){                              // {25} {} 
        }

        else if(p.izq.equals("{26.1}")){                            // {26.1} Zona_dec = true
            AL.zona_dec = true;
        }

        else if(p.izq.equals("{26.2}")){                            // {26.2} Crear TSL
            Trio H = pilaAux.pop();                                 // H
            posIdFuncionActual = Integer.parseInt(pilaAux.peek().der);       // posIdFuncionActual
            AL.crearTS(AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).getLexema()); // Creamos una TS con el nombre del id de la funcion
            pilaAux.push(H);                                        // Devolvemos la H
        }

        else if(p.izq.equals("{26.3}")){                            // {26.3} Zona_dec = false
            AL.zona_dec = false;
        }

        else if(p.izq.equals("{26.4}")){
            Trio parC = pilaAux.pop();                              // )
            Trio tipoA = pilaAux.pop();                             // A
            Trio parA = pilaAux.pop();                              // (
            Trio tipoH = pilaAux.pop();                             // H.tipo
            AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).setEtiqueta("Et" + (AL.numeroTabla-2) + "_" + AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).getLexema());
            AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).setTipo("function");
            AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).setTipoDev(tipoH.der);
            pilaAux.push(tipoH);
            pilaAux.push(parA);
            pilaAux.push(tipoA);
            pilaAux.push(parC);
        }

        else if(p.izq.equals("{26.5}")){
            String tipoC = pilaAux.peek().der;                      // C.tipo
            String tiporetC = pilaAux.pop().med;                    // C.tiporet
            pilaAux.pop();                                          // {
            pilaAux.pop();                                          // )
            pilaAux.pop();                                          // A
            pilaAux.pop();                                          // (
            String tipoH = pilaAux.pop().der;                       // H.tipo
            pilaAux.pop();                                          // idFuncion
            pilaAux.pop();                                          // function
            if(tipoC.equals("error")){
                System.out.println("error 26");
                gestorErrores.error(AL.linea, 5, "");
            }
            else{
                if(!tiporetC.equals(tipoH)){
                    System.out.println("Error: la funcion no devuelve el tipo inicializado");
                    gestorErrores.error(AL.linea, 9, "");
                }
            }
            ats.add(AL.getTS().pop());
        }

        else if(p.izq.equals("{26.6}")){
            pilaAux.pop();                                          // }
        }

        else if(p.izq.equals("{27}")){                              // {27} {H.tipo := T.tipo} Pop(1)
            String aux = pilaAux.pop().der;                            // T.tipo
            pilaAux.peek().der = aux;                                  // H.tipo
        }

        else if(p.izq.equals("{28}")){                              // {28} {H.tipo := void; H.ancho = 0} {Aux-1 = void}
            pilaAux.peek().der = "void";                            // La derecha de la cima de la pila aux es void     
        }

        else if(p.izq.equals("{29.1}")){                            // {29.1} {insertarTS(id.posTS,T.tipo,desplAct); desplAct = desplAct + T.ancho; A.tipo = T.tipo x K.tipo}
            int posID = Integer.parseInt(pilaAux.pop().der);        // Sacas variable posID
            String tipo = pilaAux.pop().der;
            AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).setTipoPar(tipo);     // Añades los tipos de los distintos parametros 
            AL.getTS().peek().tablaSimbolos.get(posID).setTipo(tipo);                           // Metemos el valor del tipo en la tabla actual
            AL.getTS().peek().tablaSimbolos.get(posID).setDespl(AL.getTS().peek().getDesp());   // Y metemos su desplazamiento
            if(tipo.equals("int") || tipo.equals("log"))                                    // Actualizamos el desplazamiento siguiente
                AL.getTS().peek().setDesp(AL.getTS().peek().getDesp()+1);
            if(tipo.equals("string"))
                AL.getTS().peek().setDesp(AL.getTS().peek().getDesp()+64);
        }

        else if(p.izq.equals("{29.2}")){                            // {29.2} Pop(K)
            pilaAux.pop();
        }

        else if(p.izq.equals("{30}")){                              // {30} {A.tipo := void} {Aux-1 = void}
            AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).setTipoPar("void");   
        }

        else if(p.izq.equals("{31.1}")){                            // {31.1} {insertarTS(id.posTS,T.tipo,desplAct); desplAct = desplAct + T.ancho; K.tipo = T.tipo x K1.tipo}
            int posID = Integer.parseInt(pilaAux.pop().der);        // Sacas variable posID
            String tipo = pilaAux.pop().der;
            pilaAux.pop();
            AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).setTipoPar(tipo);     // Añades los tipos de los distintos parametros 
            AL.getTS().peek().tablaSimbolos.get(posID).setTipo(tipo);                           // Metemos el valor del tipo en la tabla actual
            AL.getTS().peek().tablaSimbolos.get(posID).setDespl(AL.getTS().peek().getDesp());   // Y metemos su desplazamiento
            if(tipo.equals("int") || tipo.equals("log"))                                    // Actualizamos el desplazamiento siguiente
                AL.getTS().peek().setDesp(AL.getTS().peek().getDesp()+1);
            if(tipo.equals("string"))
                AL.getTS().peek().setDesp(AL.getTS().peek().getDesp()+64);
        }

        else if(p.izq.equals("{31.2}")){                            // {31.2} Pop(K)
            pilaAux.pop();
        }

        else if(p.izq.equals("{32}")){                              // {32} {K.tipo := void} {Aux-1 = void}
            AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).setNumPar(AL.getTS().elementAt(0).tablaSimbolos.get(posIdFuncionActual).getTipoPar().size());

        }

        else if(p.izq.equals("{33}")){                              // {33}  {if(1.tipo = void || (1.tipo = log && R.tipo = log)) then E.tipo := R.tipo else E.tipo := tipo_error} |  {if(Aux-0 = void || (Aux-0 = log && Aux-1 = log)) then Aux-3 := R.tipo else Aux-3 := tipo_error}
            String tipo1 = pilaAux.pop().der;                       // 1.tipo
            String tipoR = pilaAux.pop().der;                       // R.tipo
            if(tipo1.equals("void") || (tipo1.equals("log") && tipoR.equals("log"))){
                pilaAux.peek().der = tipoR;
            }
            else{
                pilaAux.peek().der = "error";
                System.out.println("Error 33");
                gestorErrores.error(AL.linea, 8, "");
            }
        }

        else if(p.izq.equals("{34}")){                              // {34}  {if(1.tipo = void || (1.tipo = log && R.tipo = log)) then 1.tipo := R.tipo else 1.tipo := tipo_error} |  {if(Aux-0 = void || (Aux-0 = log && Aux-1 = log)) then Aux-3 := R.tipo else Aux-3 := tipo_error}
            String tipo1 = pilaAux.pop().der;                       // 1.tipo
            String tipoR = pilaAux.pop().der;                       // R.tipo
            pilaAux.pop();                                          // &&
            if(tipo1.equals("void") || (tipo1.equals("log") && tipoR.equals("log"))){
                pilaAux.peek().der = tipoR;
            }
            else{
                pilaAux.peek().der = "error";
                System.out.println("Error 34");
                gestorErrores.error(AL.linea, 8, "");
            }
        }

        else if(p.izq.equals("{35}")){                              // {35} {1.tipo := void} {Aux-1 = void}
            pilaAux.peek().der = "void";                            // La derecha de la cima de la pila aux es void     
        }

        else if(p.izq.equals("{36}")){                              // {36} {if(2.tipo = void) then R.tipo := U.tipo else if(2.tipo = U.tipo) then R.tipo := log else R.tipo := tipo_error} | {if(Aux-0 = void) then Aux-3 := Aux-1 else if(Aux-0 = Aux-1) then Aux-3 := log else Aux-3 := tipo_error}
            String tipo2 = pilaAux.pop().der;                       // 2.tipo
            String tipoU = pilaAux.pop().der;                       // U.tipo
            if(tipo2.equals("void")){
                pilaAux.peek().der = tipoU;
            }
            else if(tipo2.equals(tipoU)){
                pilaAux.peek().der = "log";
            }
            else{
                pilaAux.peek().der = "error";
                System.out.println("Error 36 en linea " + AL.linea);
                gestorErrores.error(AL.linea, 8, "");
            }

        }

        else if(p.izq.equals("{37}")){                              // {37} {if(2.tipo = void) then 2.tipo := U.tipo else if(2.tipo = U.tipo) then 2.tipo := log else 2.tipo := tipo_error} | {if(Aux-0 = void) then Aux-3 := Aux-1 else if(Aux-0 = Aux-1) then Aux-3 := log else Aux-3 := tipo_error}
            String tipo2 = pilaAux.pop().der;                       // 2.tipo
            String tipoU = pilaAux.pop().der;                       // U.tipo
            pilaAux.pop();                                          // G.tipo
            if(tipo2.equals("void")){
                pilaAux.peek().der = tipoU;
            }
            else if(tipo2.equals(tipoU)){
                pilaAux.peek().der = "log";
            }
            else{
                pilaAux.peek().der = "error";
                System.out.println("Error 37");
                gestorErrores.error(AL.linea, 8, "");
            }

        }

        else if(p.izq.equals("{38}")){                              // {38} {2.tipo := void} {Aux-1 = void}
            pilaAux.peek().der = "void";                            // La derecha de la cima de la pila aux es void     
        }

        else if(p.izq.equals("{39}")){                              // {39} Pop(1)
            pilaAux.pop();                                          // iguigu
        }

        else if(p.izq.equals("{40}")){                              // {40} Pop(1)
            pilaAux.pop();                                          // desigu
        }

        else if(p.izq.equals("{41}")){                              // {41} {if(3.tipo = void || 3.tipo = V.tipo) then U.tipo := V.tipo else U.tipo := tipo_error} | {if(Aux-0 = void || Aux-0 = Aux-1) then Aux-2 := Aux-1 else Aux-2 := tipo_error}
            String tipo3 = pilaAux.pop().der;                       // 3.tipo
            String tipoV = pilaAux.pop().der;                       // V.tipo
            if(tipo3.equals("void") || tipo3.equals(tipoV)){
                pilaAux.peek().der = tipoV;
            }
            else{
                pilaAux.peek().der = "error";
                System.out.println("Error 41");
                gestorErrores.error(AL.linea, 8, "");
            }
        }

        else if(p.izq.equals("{42}")){                              // {42} {if(3.tipo = void || 3.tipo = V.tipo) then 3.tipo := V.tipo else 3.tipo := tipo_error} | {if(Aux-0 = void || Aux-0 = Aux-1) then Aux-2 := Aux-1 else Aux-2 := tipo_error}
            String tipo3 = pilaAux.pop().der;                       // 3.tipo
            String tipoV = pilaAux.pop().der;                       // V.tipo
            pilaAux.pop();                                          // J.tipo
            if(tipo3.equals("void") || tipo3.equals(tipoV)){
                pilaAux.peek().der = tipoV;
            }
            else{
                pilaAux.peek().der = "error";
                System.out.println("Error 42");
                gestorErrores.error(AL.linea, 8, "");
            }
        }

        else if(p.izq.equals("{43}")){                              // {43} {3.tipo := void} {Aux-1 = void}
            pilaAux.peek().der = "void";                            // La derecha de la cima de la pila aux es void     
        }

        else if(p.izq.equals("{44}")){                              // {44} Pop(1)
            pilaAux.pop();                                          // suma
        }

        else if(p.izq.equals("{45}")){                              // {45} Pop(1)
            pilaAux.pop();                                          // resta
        }

        else if(p.izq.equals("{46}")){                              // {46} {If(buscarTS(id.posTS) then V.tipo := buscarTipoTS(id.posTS) else V.tipo := tipo_error} | {If(buscarTS(Aux-1) then Aux-2. := buscarTipoTS(Aux-1) else Aux-2 := tipo_error}                     
            pilaAux.pop();                                          // Z
            int posID = Integer.parseInt(pilaAux.pop().der);        // Sacas variable posID

            if(AL.getTS().peek().buscarPos(posID)){
                if(AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals("function")) pilaAux.peek().der = AL.getTS().peek().tablaSimbolos.get(posID).getTipoDev();
                else pilaAux.peek().der = AL.getTS().peek().tablaSimbolos.get(posID).getTipo();
            }

            else if(AL.getTS().elementAt(0).buscarPos(posID)){
                if(AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipo().equals("function")) pilaAux.peek().der = AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipoDev();
                else pilaAux.peek().der = AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipo();
            }

            else {
                pilaAux.peek().der = "error";
                System.out.println("Error 46");
                gestorErrores.error(AL.linea, 5, "");
            }
        }

        else if(p.izq.equals("{47}")){                              // {47} {V.tipo := E.tipo} {Aux-3 = Aux-1} Pop(3)
            pilaAux.pop();                                          
            String aux = pilaAux.pop().der;                            // E.tipo
            pilaAux.pop();
            pilaAux.peek().der = aux;                                  // V.tipo
        }

        else if(p.izq.equals("{48}")){                              // {48} {V.tipo := int} {Aux-1 = int}
            pilaAux.pop();                                          // Pop(1)
            pilaAux.peek().der = "int";                             // La derecha de la cima de la pila aux es int     
        }

        else if(p.izq.equals("{49}")){                              // {49} {V.tipo := cadena} {Aux-1 = cadena}
            pilaAux.pop();                                          // Pop(1)
            pilaAux.peek().der = "string";                          // La derecha de la cima de la pila aux es int     
        }

        else if(p.izq.equals("{50}")){                              // {50} {V.tipo := log} {Aux-1 = log}
            pilaAux.pop();                                          // Pop(1)
            pilaAux.peek().der = "log";                             // La derecha de la cima de la pila aux es log     
        }

        else if(p.izq.equals("{51}")){                              // {51} {V.tipo := log} {Aux-1 = log}
            pilaAux.pop();                                          // Pop(1)
            pilaAux.peek().der = "log";                             // La derecha de la cima de la pila aux es log     
        }

        else if(p.izq.equals("{52}")){                              // {52} {Z.tipo := L.tipo} {Aux-3 = Aux-1} Pop(3)
            pilaAux.pop();                                          
            String aux = pilaAux.pop().der;                            // L.tipo
            pilaAux.pop();
            pilaAux.peek().der = aux;                                  // Z.tipo
        }
        
        else if(p.izq.equals("{53}")){                              // {53} {Z.tipo := void} {Aux-1 = void}
            pilaAux.peek().der = "void";                            // La derecha de la cima de la pila aux es void     
        }

        else if(p.izq.equals("{54}")){                              // {54} {If(buscarTipoTS(id.posTS) = E.tipo) then I.tipo := tipo.ok else I.tipo := tipo.error} | {If(buscarTipoTS(Aux-2.posTS) = Aux.tipo) then Aux-3.tipo := tipo.ok else Aux-3.tipo := tipo.error}
            String aux = pilaAux.pop().der;                         // E.tipo                                        
            pilaAux.pop();                                          // M.tipo
            int posID = Integer.parseInt(pilaAux.pop().der);        // Sacas variable posID

            if(AL.getTS().peek().buscarPos(posID)){
                if(!AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals(aux)){
                    pilaAux.peek().der = "error";
                    System.out.println("Error 54");
                    gestorErrores.error(AL.linea, 6, AL.getTS().peek().tablaSimbolos.get(posID).getLexema());
                }
            }
            else if(AL.getTS().elementAt(0).buscarPos(posID)){
                if(!AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipo().equals(aux)){
                    pilaAux.peek().der = "error";
                    System.out.println("Error 54");
                    gestorErrores.error(AL.linea, 6, AL.getTS().elementAt(0).tablaSimbolos.get(posID).getLexema());
                }
            }
            else{
                pilaAux.peek().der = "error";
                System.out.println("Error 54");
                gestorErrores.error(AL.linea, 5, "");
            }
        }

        else if(p.izq.equals("{55}")){                              // {55} {I.tipo := void} {Aux-1 = void}
            pilaAux.peek().der = "void";                            // La derecha de la cima de la pila aux es void     
        }

        else if(p.izq.equals("{56}")){                              // {56} Pop(1)
            pilaAux.pop();                                          // igual
        }

        else if(p.izq.equals("{57}")){                              // {57} Pop(1)
            pilaAux.pop();                                          // olog
        }
    }

    public void parse() throws IOException{

        String aux[];
        String union;
        int tamaño;
        token = AL.leerToken();
        String estado = token.getCodigo().toLowerCase();
        if(token.getCodigo().length() > 0) escribirTokens.write(token.toString() + "\n");

        while(!pila.peek().izq.equals("$")){
            if(terminales.contains(pila.peek().izq)){
                if(pila.peek().izq.equals(estado)){
                    if(estado.equals("id")){
                        pila.peek().der = token.getAtributo().toString();
                    }
                    pilaAux.add(pila.pop());
                    if(!token.getCodigo().equals("EOF")){
                        token = AL.leerToken();
                        estado = token.getCodigo().toLowerCase();
                        if(token.getCodigo().length() > 0) escribirTokens.write(token.toString() + "\n");
                    }
                }
                else{
                    System.out.println("Error: Token invalido");
                    gestorErrores.error(AL.linea, 10, "");
                }
            }
            else if(esAccionSem(pila.peek().izq)){                  // Si es accion semantica
                exeAccionSem(pila.pop(), pila, pilaAux);
            }
            else{                                                   // Si el siguiente elemento es un no terminal
                union = pila.peek().izq + " " + estado;             // Se une el el inicio de la pila con el estado para buscarlo en la tabla
                if(tabla.containsKey(union)){                       // Si existe la regla dada se mete en la pila y se actualiza el parse
                    aux = tabla.get(union).split(" ");
                    pilaAux.add(pila.pop());
                    tamaño = aux.length-1;
                    parse += " " + aux[0];
                    while(tamaño != 0) {
                        if(aux[tamaño].equals("lambda")){
                            tamaño--;
                        }else pila.push(new Trio(aux[tamaño--],"",""));
                    }
                }
                else {
                    System.out.println("Error: Celda vacía"); 
                    gestorErrores.error(AL.linea, 11, "");
                }
            }
            //imprimirPilas(pila, pilaAux, escribirPila);
        }

        gestorErrores.imprimir();
        if(pila.peek().izq.equals("$") && pilaAux.peek().izq.equals("P")) escribirParse.write(parse);

        while(!AL.getTS().isEmpty()){
            ats.add(AL.getTS().pop()); 
        }
        Collections.sort(ats, (ts1, ts2) -> ts1.compareTo(ts2, ts1));

        for (TablaSimbolos ts : ats) {
            escribirTS.write(ts.imprimir());
        }
        
        escribirTS.close();
        //escribirPila.close();
        escribirParse.close();
        escribirTokens.close();
    }

    public boolean compara(ArrayList<String> a, ArrayList<String> b){
        boolean c = true;
        if(a.size() == b.size()){
            for(int i=0; i< a.size(); i++){
                if(!a.get(i).equals(b.get(i))){
                    c = false;
                }
            }
        }
        else{
            c = false;
        }
        return c;
    }

    /*public int eliminado(int posID){
        int sol = 0;
        if(AL.getTS().peek().buscarPos(posID) && AL.getTS().elementAt(0).buscarPos(posID) && AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals("")){
            AL.getTS().peek().tablaSimbolos.remove(posID);
            sol = 1;
        }
        else if(AL.getTS().peek().buscarPos(posID) && AL.getTS().peek().tablaSimbolos.get(posID).getTipo().equals("")){
            AL.getTS().peek().tablaSimbolos.remove(posID);
            sol = -1;
        }
        else if(AL.getTS().elementAt(0).buscarPos(posID) && AL.getTS().elementAt(0).tablaSimbolos.get(posID).getTipo().equals("")){
            AL.getTS().elementAt(0).tablaSimbolos.remove(posID);
            sol = -1;
        }
        return sol;
    }*/

    // Imprime todas las iteraciones de la pila
public void imprimirPilas(Stack<Trio> principal, Stack<Trio> aux, FileWriter fw) throws IOException{
    int tamPrincipal = principal.size();
    int tamAux = aux.size();
    Iterator<Trio> itPrincipal = principal.iterator();
    Iterator<Trio> itAux = aux.iterator();
    Trio pp = new Trio("", "", "");
    Trio pa = new Trio("", "", "");
    String format = "%-10s %-5s %-5s %-5s %-10s %-5s %-10s";
    String formatSimplePP = "%-10s %-5s %-5s %s" ;
    String formatSimplePA = "%-16s %-5s %-10s %-10s %-10s";
    String formatHeaders = "%11s %18s";
    String imp = "--------------------------------------------\n";
    String linea = "";
    if(tamPrincipal >= tamAux){
        while(itPrincipal.hasNext()){
            while(itAux.hasNext()){
                pp = itPrincipal.next();
                pa = itAux.next();
                linea =  String.format(format, pp.izq, pp.med, pp.der,"|", pa.izq, "Ret: " + pa.med, "Tipo: " + pa.der);
                imp = linea + "\n" + imp;
            }
            if(itPrincipal.hasNext()){
                pp = itPrincipal.next();
                linea = String.format(formatSimplePP, pp.izq, pp.med, pp.der , "|");
                imp = linea + "\n" + imp;
            }
        }
    } else{
        while(itAux.hasNext()){
            while(itPrincipal.hasNext()){
                pp = itPrincipal.next();
                pa = itAux.next();
                linea =  String.format(format, pp.izq, pp.med, pp.der, "|", pa.izq, "Ret: " + pa.med, "Tipo: " + pa.der);
                imp = linea + "\n" + imp;
            }
            if(itAux.hasNext()){
                pa = itAux.next();
                linea = String.format(formatSimplePA,"", "      |    ", pa.izq, "Ret: " + pa.med, "Tipo: " + pa.der);
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
}

/*class Par{
    
    String izq;
    String der;

    public Par(String izq, String der){
        this.izq = izq;
        this.der = der;
    }

}*/

class Trio{
    
    String izq;
    String med;
    String der;

    public Trio(String izq, String med, String der){
        this.izq = izq;                                         // Izquierda = Token
        this.med = med;                                         // Medio = tipo retorno
        this.der = der;                                         // derecha = tipo
    }

}
