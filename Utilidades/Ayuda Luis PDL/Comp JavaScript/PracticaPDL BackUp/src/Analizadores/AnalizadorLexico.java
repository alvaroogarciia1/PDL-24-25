package Analizadores;

import java.util.*;
import java.io.*;
import tablas.*;

public class AnalizadorLexico {

	static FileReader fr;
	private int caracter;
	private int linea;
	private TablaPalabrasR tpr;
	private Stack<TablaSimbolos> pts;
	private boolean creaTabla;
	private boolean esInput;
	private boolean esLet;
	private boolean esFunction;
	private boolean esAsignacion;
	private String lexemaInput;
	private String lexemaAsignacion;
	static private int numeroTabla = 0;
	static int posId = 0;

	public AnalizadorLexico(File fileName) {
		pts = new Stack<TablaSimbolos>();
		pts.push(new TablaSimbolos("Tabla Programa Principal", numeroTabla++));
		this.linea = 1;
		this.tpr = new TablaPalabrasR();
		creaTabla = false;
		esInput = false;
		esLet = false;
		esFunction = false;
		esAsignacion = false;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Stack<TablaSimbolos> getPila() {
		return pts;
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
	
	public void setEsAsignacion(boolean b) {
		esAsignacion = b;
	}
	public boolean getEsAsignacion() {
		return esAsignacion;
	}

	public String getLexemaInput(){
		return lexemaInput;
	}

	public Token leerToken() {
		Token token = null;
		int estado = 0;
		int valor = 0;
		String lexema = "";
		boolean genToken = false;
		while (!genToken) {
			switch (estado) {
				case 0:
					if (caracter == 13 || caracter == 0 || caracter == 32 || caracter == 9 || caracter == '\n') { // delimitador
						leer();
					} else if (caracter == -1) {
						token = new Token("EOF");
						genToken = true;
					} else if (Character.isLetter(caracter)) {
						estado = 8;
						lexema = lexema + (char) caracter;
						leer();
					} else if (Character.isDigit(caracter)) {
						estado = 4;
						lexema = lexema + (char) caracter;
						leer();
					} else if (caracter == '/') {
						estado = 1;
						leer();
					} else if (caracter == '"') {
						lexema = lexema + (char) caracter;
						estado = 6;
						leer();
					} else if (caracter == '=') {
						estado = 10;
						leer();
					} else if (caracter == '|') {
						estado = 13;
						leer();
					} else if (caracter == ';' || caracter == ',' || caracter == '(' || caracter == ')'
							|| caracter == '{' || caracter == '}' || caracter == ':' || caracter == '+'
							|| caracter == '-') {
						estado = 16;
					} else {
						System.out.println("Error en el caracter: " + (char) caracter + " Linea: " + linea);
						estado = 0;
						leer();
					}
					break;
				case 1:
					if (caracter == '/') {
						estado = 2;
						leer();
					} else {
						estado = 3;
					}
					break;
				case 2:
					if (caracter == '\n' || caracter == 13) {
						estado = 0;
						leer();
					} else {
						leer();
					}
					break;
				case 3:
					token = new Token("DIVISION");
					genToken = true;
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
						System.out.println("Valor fuera de rango. Linea: " + linea);
						caracter = -1;
					}
					genToken = true;
					break;
				case 6:
					if (caracter == '"') {
						lexema = lexema + (char) caracter;
						estado = 7;
						leer();
					} else if (!(caracter == 10 || caracter == 13)) {
						lexema = lexema + (char) caracter;
						leer();
					} else {
						System.out.println("Cadena sin comillas de cierre. Linea: " + (linea - 1));
						genToken = true;
						caracter = -1;
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
					System.out.println(esAsignacion);
					if (tpr.buscar(lexema)) {
						if (lexema.equals("input")) {
							esInput = true;
						} if (lexema.equals("let")) {
							esLet = true;
						}
						token = new Token(lexema.toUpperCase());
					} else {
						// LET
						if(esLet && !pts.peek().buscar(lexema)){
							pts.peek().anadir(lexema);
							token = new Token("ID", pts.peek().getPos(lexema));
							esLet = false;
						// INPUT
						} else if(esInput){
							if(!pts.peek().buscar(lexema) && !pts.elementAt(0).buscar(lexema)){
								pts.elementAt(0).anadir(lexema);
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema)).setTipo("entero");
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema)).setDesp(pts.elementAt(0).getDespl());
								pts.elementAt(0).setDespl(pts.elementAt(0).getDespl()+2);
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} else if(!pts.peek().buscar(lexema) && pts.elementAt(0).buscar(lexema)){
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} else if(pts.peek().buscar(lexema) && !pts.elementAt(0).buscar(lexema)) {
								token = new Token("ID", pts.peek().getPos(lexema));
							}
							lexemaInput = lexema;
							esInput = false;
						// FUNCTION
						} else if(esFunction){
							if(!pts.elementAt(0).buscar(lexema)){
								System.out.println("Funcion no declarada. Linea: " + (linea - 1));
								caracter = -1;
							} else {
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							}
							esFunction = false;
						// DECLARACION FUNCIONES
						} else if (creaTabla) {
							if(!pts.elementAt(0).buscar(lexema)){
								pts.elementAt(0).anadir(lexema);
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
								pts.elementAt(0).simbolos.get(pts.elementAt(0).getPos(lexema)).setEtiqFuncion("Etiq_" + lexema);
								String nombreTabla = "Tabla Funcion " + lexema;
								pts.push(new TablaSimbolos(nombreTabla, numeroTabla++));
								creaTabla = false;
							} else {
								System.out.println("Nombre de la variable ya en uso. Linea: " + (linea-1));
								caracter = -1;
							}
						// Asignaciones (= ó |=)
						} else if(esAsignacion) {
							if(!pts.peek().buscar(lexema) && !pts.elementAt(0).buscar(lexema)){
								pts.elementAt(0).anadir(lexema);
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} else if(pts.elementAt(0).buscar(lexema)){
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} else if(pts.peek().buscar(lexema) && !pts.elementAt(0).buscar(lexema)){
								token = new Token("ID", pts.peek().getPos(lexema));
							} 
							esAsignacion = false;
						// En medio de alguna sentencia
						} else {
							if(pts.peek().buscar(lexema)){
								token = new Token("ID", pts.peek().getPos(lexema));
							} else if(pts.elementAt(0).buscar(lexema)){
								token = new Token("ID", pts.elementAt(0).getPos(lexema));
							} else {
								System.out.println("La variable no existe. Linea: " + (linea-1));
								caracter = -1;
							}
						}
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
					token = new Token("IGUALIGUAL");
					genToken = true;
					break;
				case 12:
					token = new Token("IGUAL");
					genToken = true;
					break;
				case 13:
					if (caracter == '=') {
						estado = 14;
						leer();
					} else if (caracter == '|') {
						estado = 15;
						leer();
					} else {
						System.out.println("Carcater no esperado. Linea: " + linea);
						genToken = true;
						caracter = -1;
					}
					break;
				case 14:
					token = new Token("ORIGUAL");
					genToken = true;
					break;
				case 15:
					token = new Token("OR");
					genToken = true;
					break;
				case 16:
					if (caracter == ';') {
						token = new Token("PUNTOYCOMA");
						genToken = true;
					} else if (caracter == ',') {
						token = new Token("COMA");
						genToken = true;
					} else if (caracter == '(') {
						token = new Token("PARENTA");
						genToken = true;
					} else if (caracter == ')') {
						token = new Token("PARENTC");
						genToken = true;
					} else if (caracter == '{') {
						token = new Token("CORCHA");
						genToken = true;
					} else if (caracter == '}') {
						token = new Token("CORCHC");
						genToken = true;
					} else if (caracter == ':') {
						token = new Token("DOSPUNTOS");
						genToken = true;
					} else if (caracter == '+') {
						token = new Token("MAS");
						genToken = true;
					} else if (caracter == '-') {
						token = new Token("MENOS");
						genToken = true;
					}
					leer();
					break;
				default:
					genToken = true;
					return null;
			}
		}
		return token;
	}

	public int getCaracter() {
		return this.caracter;
	}

	public void leer() {
		try {
			this.caracter = fr.read();
			if (caracter == '\n')
				linea++;
		} catch (IOException e) {
			System.out.println("Error en lectura de caracter");
		}
	}
}