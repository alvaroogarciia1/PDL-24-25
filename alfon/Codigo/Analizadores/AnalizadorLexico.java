package Analizadores;

import java.util.*;
import java.io.*;
import tablas.*;

public class AnalizadorLexico {

	static FileReader fr;
	private BufferedWriter writer;  // Atributo para escribir los tokens en el archivo
	private int caracter;
	private int linea;
	private TablaPalabrasR tpr;
	private Stack<TablaSimbolos> pts;
	private boolean creaTabla;
	private boolean esInput;
	private boolean esLet;
	private boolean esFunction;
	private boolean esSentencia;
	private boolean parametros;
	private String lexemaAsignacion;
	static private int numeroTabla = 0;
	static int posId = 0;
	//GestorErrores gestorErrores;

	public AnalizadorLexico(File fileName/*, GestorErrores gestorErrores*/) {
		pts = new Stack<TablaSimbolos>();
		pts.push(new TablaSimbolos("Tabla Programa Principal", numeroTabla++));
		this.linea = 1;
		this.tpr = new TablaPalabrasR();
		creaTabla = false;
		esInput = false;
		esLet = false;
		esFunction = false;
		esSentencia = false;
		parametros = false;
		//this.gestorErrores = gestorErrores;

		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
            fr = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        // Inicializar el BufferedWriter para escribir los tokens en tokens.txt
        try {
            File archivoTokens = new File("tokens.txt");  // Se crea en la misma carpeta
            writer = new BufferedWriter(new FileWriter(archivoTokens));
        } catch (IOException e) {
            System.err.println("Error al crear el archivo tokens.txt: " + e.getMessage());
        }
	}

	public Stack<TablaSimbolos> getPila() {
		return pts;
	}

	public void setParametros(boolean b) {
		parametros = b;
	}

	public void setCreaTabla(boolean b) {
		creaTabla = b;
	}

	public void setEsInput(boolean b) {
		esInput = b;
	}

	public boolean getEsInput() {
		return esInput;
	}

	public void setEsLet(boolean b) {
		esLet = b;
	}

	public boolean getEsLet() {
		return esLet;
	}

	public void setEsFunction(boolean b) {
		esFunction = b;
	}

	public boolean getEsFunction() {
		return esFunction;
	}

	public void setEsSentencia(boolean b) {
		esSentencia = b;
	}

	public boolean getEsSentencia() {
		return esSentencia;
	}

	public int getLinea() {
		return linea;
	}

	public Token leerToken() throws IOException {
		Token token = null;
		int estado = 0;
		int valor = 0;
		String lexema = "";
		boolean genToken = false;
		while (!genToken) {
			if ((lexema.equals("igual") || lexema.equals("origual")) && lexemaAsignacion != null) {
				pts.elementAt(0).anadir(lexema, posId++);
				token = new Token("ID", pts.elementAt(0).getPos(lexema));
				lexemaAsignacion = null;
			} /*else if (lexemaAsignacion != null) {
				gestorErrores.producirError(1, linea, "");
			}*/
			if(caracter != '(' || !esFunction){
				esFunction = false;
			}
			switch (estado) {
				case 0:
					if (caracter == 13 || caracter == 0 || caracter == 32 || caracter == 9 || caracter == '\n') { // delimitador
						leer();
					} else if (caracter == -1) {
						token = new Token("EOF", null);
						genToken = true;
					} else if (Character.isLetter(caracter)) {
						estado = 8;
						lexema = lexema + (char) caracter;
						leer();
					} else if (Character.isDigit(caracter)) {
						estado = 4;
						lexema = lexema + (char) caracter;
						leer();
					} else if (caracter == '/') { // SOLO COMENTARIOS
						estado = 1;
						leer();
					} else if (caracter == '\'') {
						lexema = lexema + (char) caracter;
						estado = 6;
						leer();
					} else if (caracter == '=') {
						estado = 10;
						leer();
					} else if (caracter == '&') {
						estado = 13;
						leer();
					} else if (caracter == '*') {
						estado = 14;
						leer();
					} else if (caracter == ';' || caracter == ',' || caracter == '(' || caracter == ')'
							|| caracter == '{' || caracter == '}' || caracter == ':' || caracter == '+') {
						estado = 16;
					} else {
						/*gestorErrores.producirError(2, linea, "" + (char) caracter);*/
						estado = 0;
						leer();
					}
					break;
				case 1:
					if (caracter == '*') {
						estado = 2;
						leer();
					}
					break;
				case 2:
					if (caracter == '*' ) {
						estado = 3;
						leer();
					} else {
						leer();
					}
					break;
				case 3:
				if (caracter == '/') {
					estado = 0;
					leer();
				} else {
					estado = 2;
					leer(); // ESTO NO SE SI TIENE Q ESTAR O NO
				}
					break;
				case 4:
					if (Character.isDigit(caracter)) {
						lexema = lexema + (char) caracter;
						leer();
					} else {
						estado = 5;
					}
					break;
				case 5:
					try {
						valor = Integer.parseInt(lexema);
						token = new Token("ENTERO", valor);
					} catch (Exception e) {
						/*gestorErrores.producirError(3, linea, "");*/
					}
					genToken = true;
					break;
				case 6:
					if (caracter == '\'') {
						lexema = lexema + (char) caracter;
						estado = 7;
						leer();
					} else if (!(caracter == 10 || caracter == 13)) {
						lexema = lexema + (char) caracter;
						leer();
					} else {
						/*gestorErrores.producirError(4, linea, "");*/
					}
					break;
				case 7:
					token = new Token("CADENA", lexema);
					genToken = true;
					break;
				case 8:
					if (Character.isDigit(caracter) || Character.isLetter(caracter) || caracter == '_') {
						lexema = lexema + (char) caracter;
						leer();
					} else {
						estado = 9;
					}
					break;
				case 9:
					if (tpr.buscar(lexema)) {
						if (lexema.equals("input")) {
							esInput = true;
						}
						if (lexema.equals("let")) {
							esLet = true;
						}
						token = new Token(lexema.toUpperCase(), null);
					} else {
						// LET
						if (esLet) {
							if (!pts.peek().buscar(lexema)) {
								pts.peek().anadir(lexema, posId++);
								token = new Token("ID", pts.peek().getPos(lexema));
								esLet = false;
							} /*else {
								gestorErrores.producirError(6, linea, "");
							}*/
							// INPUT
						} else if (esInput) {
							if (!pts.peek().buscar(lexema) && !pts.elementAt(0).buscar(lexema)) {
								pts.elementAt(0).anadir(lexema, posId++);
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema)).setTipo("entero");
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema))
										.setDesp(pts.elementAt(0).getDespl());
								pts.elementAt(0).setDespl(pts.elementAt(0).getDespl() + 2);
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} else if (pts.peek().buscar(lexema)) {
								token = new Token("ID", pts.peek().getPos(lexema));
							} else {
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							}
							esInput = false;
							// LLAMADA FUNCIONES
						} else if (esFunction) {
							System.out.println(pts.elementAt(0).simbolos.get(11).getLexema());
							System.out.println(lexema);
;							if (!pts.elementAt(0).buscar(lexema)) {
								/*gestorErrores.producirError(5, linea, "");*/
							} else {
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							}
							esFunction = false;
							// DECLARACION FUNCIONES
						} else if (creaTabla) {
							if (!pts.elementAt(0).buscar(lexema)) {
								pts.elementAt(0).anadir(lexema, posId++);
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema))
										.setEtiqFuncion("Etiq_" + lexema);
								String nombreTabla = "Tabla Funcion " + lexema;
								pts.push(new TablaSimbolos(nombreTabla, numeroTabla++));
								creaTabla = false;
							} /*else {
								gestorErrores.producirError(6, linea, "");
							}*/
							// SENTENCIAS
						} else if (esSentencia) {
							if (pts.peek().buscar(lexema)) {
								token = new Token("ID", pts.peek().getPos(lexema));
							} else if (pts.elementAt(0).buscar(lexema)) {
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} /*else {
								gestorErrores.producirError(1, linea, "");

							}*/
							esSentencia = false;
							// PARAMETROS
						} else if (parametros) {
							pts.peek().anadir(lexema, posId++);
							token = new Token("ID", pts.peek().getPos(lexema));
							// ASIGNACION (= Ó |=)token = new Token
						} else {
							if (!pts.peek().buscar(lexema) && !pts.elementAt(0).buscar(lexema)) {
								pts.elementAt(0).anadir(lexema, posId++);
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema)).setTipo("entero");
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema))
										.setDesp(pts.elementAt(0).getDespl());
								pts.elementAt(0).setDespl(pts.elementAt(0).getDespl() + 2);
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} else if (pts.peek().buscar(lexema)) {
								token = new Token("ID", pts.peek().getPos(lexema));
							} else {
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							}
						}
						// boolean x = true;
						// if (pts.peek().getNumeroTabla() != 0 && pts.peek().buscar(lexema)) {
						// 	System.out.println("lexema: " + lexema + " posId:" + pts.peek().getPos(lexema)
						// 			+ " NumTabla: " + pts.peek().getNumeroTabla());
						// 	x = false;
						// }
						// if (pts.elementAt(0).buscar(lexema) && x) {
						// 	System.out.println("lexema: " + lexema + " posId:" + pts.elementAt(0).getPos(lexema)
						// 			+ " NumTabla: " + pts.elementAt(0).getNumeroTabla());
						// }
					}
					genToken = true;
					break;
				case 10:
					if (caracter == '=') {
						estado = 11;
						leer();
					} else {
						estado = 12;
					}
					break;
				case 11:
					token = new Token("IGUIGU", null);
					genToken = true;
					break;
				case 12:
					token = new Token("IGUAL", null);
					genToken = true;
					break;
				case 13:
					if (caracter == '&') {
						estado = 15;
						leer();
					} /*else {
						gestorErrores.producirError(8, linea, "");
					}*/
					break;
				case 14:
					if (caracter == '='){
						estado = 21;
						leer();
					}
					break;
				case 15:
					token = new Token("AND", null);
					genToken = true;
					break;
				case 16:
					if (caracter == ';') {
						token = new Token("PUNTYCOM", null);
						genToken = true;
					} else if (caracter == ',') {
						token = new Token("COMA", null);
						genToken = true;
					} else if (caracter == '(') {
						token = new Token("PARENTABRE", null);
						genToken = true;
					} else if (caracter == ')') {
						token = new Token("PARENTCIERRA", null);
						genToken = true;
					} else if (caracter == '{') {
						token = new Token("CORCHABRE", null);
						genToken = true;
					} else if (caracter == '}') {
						token = new Token("CORCHCIERRA", null);
						genToken = true;
					} else if (caracter == ':') {
						token = new Token("DOSPUNTOS", null);
						genToken = true;
					} else if (caracter == '+') {
						token = new Token("SUMA", null);
						genToken = true;
					
					}
					leer();
					break;
				case 21:
					token = new Token("ASIGMULT", null);
					genToken = true;
					break;
				default:
					genToken = true;
					return null;
			}

			// Escribir el token en el archivo
            if (token != null && writer != null) {
                writer.write(token.toString());
                writer.newLine();
            }
		}

		return token;
	}

	// Cerrar el BufferedWriter cuando se termine el análisis léxico
	public void cerrarArchivoTokens() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo tokens.txt: " + e.getMessage());
        }
    }

	public int getCaracter() {
		return this.caracter;
	}

	public void leer() throws IOException {
		try {
			this.caracter = fr.read();
			if (caracter == '\n')
				linea++;
		} catch (IOException e) {
			/*gestorErrores.producirError(9, linea, "");*/
		}
	}
}