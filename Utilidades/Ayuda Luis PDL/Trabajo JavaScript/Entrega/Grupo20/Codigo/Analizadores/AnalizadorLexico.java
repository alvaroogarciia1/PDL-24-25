package Analizadores;

import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.io.File;

import Tablas.*;

public class AnalizadorLexico {

    // Partimos que recibimos un fichero con un codigo 
    static FileReader fich; 
    static int estado;
    static int elem;
    static String cadena;
    static int valor; 
    static boolean cont;
    int linea = 1;
    static TablaPR tabla = new TablaPR();
    static Stack<TablaSimbolos> ptr;
    int numeroTabla = 1;
    int contID = 0;
    public boolean zona_dec = false;
    public boolean zona_dec_input = false;
    GestorErrores gestorErrores;

    public AnalizadorLexico(File leerFich, GestorErrores gestorErrores){

      ptr = new Stack<TablaSimbolos>();
      ptr.push(new TablaSimbolos("Tabla de Simbolos Principal", numeroTabla++));
      this.gestorErrores = gestorErrores;
      try {
        fich = new FileReader(leerFich);
      } catch (Exception e) {
        e.printStackTrace();
      }                                  // Se mete en fich la lectura del fichero dado
    }

    public void crearTS(String nombre){
      ptr.push(new TablaSimbolos("Tabla de Simbolos de la Funcion " + nombre, numeroTabla++));
    }

    public Stack<TablaSimbolos> getTS(){
      return ptr;
    }

    public Tokens leerToken() throws IOException{
      Tokens token = null;
      estado = 0;
      cont = true;
      cadena = "";
      valor = 0; 
      while(cont){                                                // Seguimos leyendo hasta que no haya mas elementos en el fichero cambiar porque no se acaba aqui, ya que -1 es EOF y se ve igualmente
        switch(estado){                                           // Un caso por cada estado del automata
          case 0: 
            if(elem == 32 || elem == 9 || elem == '\n' || elem == 13){             // Miramos si el caracter a leer es un delimitador
              leer();                                             // Leemos next caracter
            }

            else if(Character.isLetter(elem)){                    // Miramos si el caracter es una letra
              cadena = cadena + (char) elem;                      // Concatenamos la letra en cadena
              estado = 1; 
              leer();
            }
            
            else if(Character.isDigit(elem)){                     // Miramos si el caracter es un digito
              cadena = cadena + (char) elem;                      // Lo metemos en la variable cadena (pero es un numero)
              estado = 2;
              leer();
            }

            else if(elem == 61){                                  // Miramos si el caracter es un =
              estado = 3;
              leer();
            }

            else if(elem == 33){                                  // Miramos si el caracter es un !
              estado = 4;   
              leer();
            }

            else if(elem == 38){                                  // Miramos si el caracter es un &
              estado = 5;
              leer();
            }

            else if(elem == 47){                                  // Miramos si el caracter es un /
              estado = 6;
              leer();
            }

            else if(elem == 34){                                  // Miramos si el caracter es un "
              cadena += (char) elem;
              estado = 7;
              leer();
            }

            else if(elem == 124){                                  // Miramos si el caracter es un |
              estado = 8;
              leer();
            }

            else if(elem == 123){                                  // Miramos si el caracter es un {
              estado = 11;
            }

            else if(elem == 125){                                  // Miramos si el caracter es un }
              estado = 12;
            }

            else if(elem == -1){                                  // Miramos si el caracter es un EOF
              estado = 13;
            }

            else if(elem == 44){                                  // Miramos si el caracter es un ,
              estado = 14;
            }

            else if(elem == 59){                                  // Miramos si el caracter es un ;
              estado = 15;
            }

            else if(elem == 43){                                  // Miramos si el caracter es un +
              estado = 16;
            }

            else if(elem == 45){                                  // Miramos si el caracter es un -
              estado = 17;
            }

            else if(elem == 40){                                  // Miramos si el caracter es un (
              estado = 18;
            }

            else if(elem == 41){                                  // Miramos si el caracter es un )
              estado = 19;
            }

            else{
              System.out.println("ERROR: el caracter " + (char) elem + " no es valido. En la linea " + linea);
              gestorErrores.error(linea, 1, "" + (char) elem);
              estado = 0;
              leer();
            }
            break;

            case 1:
              if(Character.isLetter(elem) || elem == 95){                           // Miramos si el siguiente elemento es una letras
                cadena += (char) elem;
                leer(); 
              }
              else if(Character.isDigit(elem)){                                     // Miramos si el siguiente elemento es un digito
                cadena += (char) elem;
                estado = 9;
                leer();
              }else{                                                                // Si recibimos O.C. (lamnda)
                estado = 20;
              }
              break;
                
            case 2: 
              if(Character.isDigit(elem)){                                          // Miramos si el siguiente elemento es un digito
                cadena += (char) elem; 
                leer();
              }
              else{                                                                 // Si recibimos O.C. (lamnda)    
                estado = 22;
              }
              break;
              
            case 3: 
              if(elem == 61){                                                       // Si recibimos un =
                estado = 23;
              }
              else{                                                                 // Si recibimos O.C. (lamnda)  
                estado = 24;
              }
              break;

            case 4:
              if(elem == 61){                                                       // Si recibimos un =
                estado = 25;
              }
              else if(!(elem == 32 || elem == 9 || elem == '\n' || elem == 13)){
                System.out.println("ERROR: el carácter " + (char) elem + " no es válido. No forman  el desigual !=. En la linea " + linea);
                gestorErrores.error(linea, 1, "" + (char) elem);
                estado = 0;
              }

              else{
                System.out.println("ERROR: no existe el token '!'. En la linea " + linea);
                gestorErrores.error(linea, 1, "" + (char) elem);
                estado = 0;
              }
              break;
            
            case 5:
              if(elem == 38){                                                       // Si recibimos & 
                estado = 26;
                leer();
              }else{
                System.out.println("ERROR: el carácter " + (char) elem + " no es válido. No forma AND lógico. En la linea " + linea);
                gestorErrores.error(linea, 1, "" + (char) elem);
                estado = 0;
              }
              break;
            
            case 6:
              if(elem == 47){                                                       // Si recibimos /
                estado = 10;
              }
              else{
                System.out.println("ERROR: el caracter " + (char) elem + " no es valido. Los comentarios son con 2 barras: //. En la linea " + linea);
                gestorErrores.error(linea, 1, "" + (char) elem);
                estado = 0;
              }
              break;
            
            case 7:
              if(elem == 34){                                               // Si recibimos "
                  cadena += (char) elem;
                  estado = 27;
              }
              else if (!(elem == 10 || elem == 13)){                            // Si recibimos un c (un caracter distinto a delimitador)  
                cadena += (char) elem;
                leer();

              } else{
                System.out.println("ERROR: el caracter " + (char) elem + " no es válido. Se debe cerrar la cadena con comillas de cierre. En la linea " + linea);
                gestorErrores.error(linea, 1, "" + (char) elem);
                estado = 0;
              }
              break;
            case 8: 
              if(elem == 61){                                                     // Si recibimos un =
                estado = 28;
              }else{
                System.out.println("ERROR: el caracter " + (char) elem + " no es un O-lógico. Carácter no reconocido. En la linea " + linea);
                gestorErrores.error(linea, 1, "" + (char) elem);
                estado = 0;
              }
            break;
            case 9: 
              if (Character.isLetter(elem) || Character.isDigit(elem) || elem == 95){           // Si recibimos una letra o un digito
                cadena += (char) elem;
                leer();
              }else{                                                              // Si recibimos O.C. (lamnda)  
                estado = 21;

              }
              break;
            case 10:
              if(elem != '\n' && elem != -1){                        // Si recibimos c (cualquier caracter menos delimitador)
                cadena += (char) elem;
                leer(); 
              } else{                                                             // Si recibimos un delimitador
                estado = 0;
                cadena = "";
              }
            break;

            case 11: 
            token = new Tokens("CORCHA");
            cont = false;
            leer();
            break;

            case 12: 
            token = new Tokens("CORCHC");
            cont = false;
            leer();
            break;

            case 13: 
            token = new Tokens("EOF");
            cont = false;
            leer();
            break;

            case 14: 
            token = new Tokens("COMA");
            cont = false;
            leer();
            break;

            case 15: 
            token = new Tokens("PYC");
            cont = false;
            leer();
            break;

            case 16: 
            token = new Tokens("SUMA");
            cont = false;
            leer();
            break;

            case 17: 
            token = new Tokens("RESTA");
            cont = false;
            leer();
            break;

            case 18: 
            token = new Tokens("PARENTA");
            cont = false;
            leer();
            break;

            case 19: 
            token = new Tokens("PARENTC");
            cont = false;
            leer();
            break;

            case 20:                                                                        // Mirar si es Palabra Reservada
            if(tabla.buscar(cadena.toUpperCase())){
              token = new Tokens(cadena.toUpperCase());
              cadena = "";
            }
            else{                                                                           // Y si no lo es miramos si es un id o si no lo añadimos a la tabla de simbolos
              if(zona_dec){
                if(!ptr.peek().buscar(cadena)){
                  ptr.peek().insertar(cadena,contID++);
                  token = new Tokens("ID",ptr.peek().getPos(cadena));
                }
                else{
                  System.out.println("Error en linea " + linea + " variable ya inicializada");
                  gestorErrores.error(linea, 2, cadena);
                  token = new Tokens("ID",ptr.peek().getPos(cadena));
                } 
              }
              else if(zona_dec_input){
                if(!ptr.elementAt(0).buscar(cadena) && !ptr.peek().buscar(cadena)){
                  ptr.peek().insertar(cadena,contID++);
                  token = new Tokens("ID",ptr.peek().getPos(cadena));
                }
                else if(ptr.peek().buscar(cadena)){
                  token = new Tokens("ID", ptr.peek().getPos(cadena));
                }
                else if(ptr.elementAt(0).buscar(cadena)){
                  token = new Tokens("ID",ptr.elementAt(0).getPos(cadena));
                }
              }
              else{
                if(ptr.peek().buscar(cadena)) token = new Tokens("ID",ptr.peek().getPos(cadena));
                else if(ptr.elementAt(0).buscar(cadena)) token = new Tokens("ID", ptr.elementAt(0).getPos(cadena));
                else{
                  System.out.println("Error no existe la variable '" + cadena + "' en linea " + linea);
                  gestorErrores.error(linea, 3, cadena);
                  ptr.elementAt(0).insertar(cadena,contID++);
                  ptr.elementAt(0).tablaSimbolos.get(contID-1).setTipo("int");
                  ptr.elementAt(0).tablaSimbolos.get(contID-1).setDespl(ptr.elementAt(0).getDesp());
                  ptr.elementAt(0).setDesp(ptr.elementAt(0).getDesp()+1);
                  token = new Tokens("ID",ptr.elementAt(0).getPos(cadena));
                } 
              }
            }
            cont = false;
            break;
            
            case 21:                                                                        // ID en tabla de simbolos

            if(zona_dec){
              if(!ptr.peek().buscar(cadena)){
                ptr.peek().insertar(cadena, contID++);
                token = new Tokens("ID",ptr.peek().getPos(cadena));
              }
              else{
                System.out.println("Error en linea " + linea + " variable ya inicializada");
                gestorErrores.error(linea, 2, cadena);
              } 
            }
            else if(zona_dec_input){
              if(!ptr.elementAt(0).buscar(cadena) && !ptr.peek().buscar(cadena)){
                ptr.peek().insertar(cadena,contID++);
                token = new Tokens("ID",ptr.peek().getPos(cadena));
              }
              else if(ptr.peek().buscar(cadena)){
                token = new Tokens("ID", ptr.peek().getPos(cadena));
              }
              else if(ptr.elementAt(0).buscar(cadena)){
                token = new Tokens("ID",ptr.elementAt(0).getPos(cadena));
              }
            }
            else{
              if(ptr.peek().buscar(cadena)) token = new Tokens("ID",ptr.peek().getPos(cadena));
              else if(ptr.elementAt(0).buscar(cadena)) token = new Tokens("ID", ptr.elementAt(0).getPos(cadena));
              else{
                System.out.println("Error no existe la variable '" + cadena + "' en linea " + linea);
                gestorErrores.error(linea, 3, cadena);
                ptr.elementAt(0).insertar(cadena,contID++);
                ptr.elementAt(0).tablaSimbolos.get(contID-1).setTipo("int");
                ptr.elementAt(0).tablaSimbolos.get(contID-1).setDespl(ptr.elementAt(0).getDesp());
                ptr.elementAt(0).setDesp(ptr.elementAt(0).getDesp()+1);
                token = new Tokens("ID",ptr.elementAt(0).getPos(cadena));
                
              } 
            }
            cont = false;
            break;

            case 22:                                                                        // Entero del valor
            valor = Integer.parseInt(cadena);
            if(valor <= 32767){
              token = new Tokens("ENTERO",valor);
              cadena = "";
              cont = false;
            }
            else{
              System.out.println("ERROR: el valor es superior al permitido. En la linea " + linea);
              gestorErrores.error(linea, 4, "");
              token = new Tokens("ENTERO",valor);
              cadena = "";
              cont = false;
              //estado = 0;
            }
            
            break;

            case 23: 
            token = new Tokens("IGUIGU");
            cont = false;
            leer();
            break;

            case 24: 
            token = new Tokens("IGUAL");
            cont = false;
            break;

            case 25: 
            token = new Tokens("DESIGU");
            cont = false;
            leer();
            break;

            case 26: 
            token = new Tokens("AND");
            cont = false;
            break;

            case 27:                                                                            // Length CADENA <= 64 + 2 ("")
              if(cadena.length()>66){
                System.out.println("ERROR: la cadena supera la longitud máxima de 63 caracteres. En la linea " + linea);
                gestorErrores.error(linea, 4, "");
                token = new Tokens("CADENA",cadena);
                cadena = "";
                cont = false;
                //estado = 0;
              }
              else{
                token = new Tokens("CADENA",cadena);
                cadena = "";
                cont = false;
              }
            leer();
            break;

            case 28:
            token = new Tokens("OLOG");
            cont = false;
            leer();
            break;
        }

      }
      return token;
    }

    public void leer(){
      try {
        elem = fich.read();
        if(elem == '\n') linea++;
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
