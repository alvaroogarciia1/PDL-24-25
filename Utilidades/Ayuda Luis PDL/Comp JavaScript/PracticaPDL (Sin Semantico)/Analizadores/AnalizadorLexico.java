package Analizadores;

import java.util.*;
import java.io.*;
import tablas.*;

public class AnalizadorLexico {

	static FileReader fr;
	private int caracter;
	private int linea;
	private TablaPalabrasR tpr;
	private ArrayList<TablaSimbolos> ats;
    
	public AnalizadorLexico(File fileName,  ArrayList<TablaSimbolos> ats) { 
		this.ats = ats;
		this.linea = 1; 
		this.tpr= new TablaPalabrasR();
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Token leerToken() {
		Token token = null;
		int estado =0;
		int valor = 0;
		String lexema = "";
		boolean genToken= false;
		while(!genToken){
			switch(estado) {
			case 0:
				if(caracter == 13 || caracter== 32 || caracter == 9 || caracter == '\n'){ //delimitador
					leer();
				} else if (caracter == -1) {
					token = new Token("EOF");
					genToken  = true;
				} else if(Character.isLetter(caracter)) {
					estado = 8;
					lexema=lexema+ (char)caracter;
					leer();
				} else if(Character.isDigit(caracter)) {
					estado = 4;
					lexema = lexema + (char)caracter;
					leer();
				} else if(caracter == '/') {
					estado = 1;
					leer();
				} else if(caracter == '"') {
					lexema = lexema + (char)caracter;
					estado = 6;
					leer();
				} else if(caracter =='=') {
					estado =10;
					leer();
				} else if(caracter == '|') {
					estado = 13;
					leer();
				} else if(caracter == ';' || caracter == ','|| caracter == '(' || caracter == ')' || caracter == '{'
						|| caracter == '}' || caracter == ':' || caracter == '+' 
						|| caracter == '-') {
					estado = 16;
				} else {
					System.out.println("Error en el caracter: "+ (char)caracter +  " Linea: "+ linea);
					estado=0;
					leer();
				}
				break;
			case 1:
				if(caracter == '/') {
					estado = 2;
					leer();
				} else {
					estado = 3;
				}
				break;
			case 2:
				if(caracter == '\n' || caracter == 13){
					estado = 0;
					leer();
				} else {
					leer();
				}
				break;
			case 3:
				token = new Token("DIVISION");
				genToken=true;
				break;
			case 4:
				if(Character.isDigit(caracter)) {
					lexema = lexema + (char)caracter;
					leer();
				} else {
					estado = 5;
				}
				break;
			case 5:
				try {
					valor = Integer.parseInt(lexema);
					token = new Token("ENTERO", valor);
				}catch(Exception e){
					System.out.println("Valor fuera de rango. Linea: " + linea);
					caracter = -1;
				}
				genToken=true;
				break;
			case 6:
				if(caracter == '"') {
					lexema = lexema + (char)caracter;
					estado =7;
					leer();
				} else if(!(caracter == 10 || caracter == 13)) {
					lexema = lexema + (char)caracter;
					leer();
				} else {
					System.out.println("Cadena sin comillas de cierre. Linea: " + (linea-1));
					genToken = true;
					caracter = -1;
				}
				break;
			case 7:
				token = new Token("CADENA",lexema);
				genToken=true;
				break;
			case 8:
				if(Character.isDigit(caracter) || Character.isLetter(caracter) || caracter == '_'){
					lexema = lexema + (char)caracter;
					leer();
				} else {
					estado =9;
				}
				break;
			case 9:
				if(tpr.buscar(lexema)){
					token = new Token(lexema.toUpperCase()); 
				} else {
					
					if(!ats.get(0).buscar(lexema)){
						ats.get(0).anadir(lexema);
						token = new Token("ID",ats.get(0).getPos(lexema));
					} else {
						token = new Token("ID",ats.get(0).getPos(lexema));
					}
				}   
				genToken=true;
				break;
			case 10:
				if(caracter=='='){
					estado=11;
					leer();
				} else{
					estado=12;
				}
				break;   
			case 11:
				token = new Token("IGUALIGUAL");
				genToken=true;
				break;
			case 12:
				token = new Token("IGUAL");
				genToken=true;
				break;
			case 13:
				if(caracter == '=') {
					estado = 14;
					leer();
				} else if(caracter == '|') {
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
				genToken=true; 
                break;
			case 15:
				token = new Token("OR");
				genToken= true;
				break;
			case 16:
				if(caracter == ';') {
					token = new Token("PUNTOYCOMA");
					genToken=true;
				} else if(caracter == ',') {
					token = new Token("COMA");
					genToken=true;
				} else if(caracter == '(') {
					token = new Token("PARENTA");
					genToken=true;
				} else if(caracter == ')') {   
					token = new Token("PARENTC");
					genToken=true;
				} else if(caracter == '{') {
					token = new Token("CORCHA");
					genToken=true;
				} else if(caracter == '}') {
					token = new Token("CORCHC");
					genToken=true;
				} else if (caracter == ':'){
					token = new Token("DOSPUNTOS");
					genToken=true;
				} else if(caracter == '+') {
					token = new Token("MAS");
					genToken=true;
				} else if(caracter == '-') {
					token = new Token("MENOS");
					genToken=true;
				}
				leer();
				break;
			default:
				genToken=true;
                return null;
			}
		}
		return token;
	}

	public int getCaracter(){
		return this.caracter;
	}

	public void leer(){
		try{
			this.caracter = fr.read();
			if(caracter == '\n') linea++;
		}catch(IOException e){
			System.out.println("Error en lectura de caracter");
		}
	}
}