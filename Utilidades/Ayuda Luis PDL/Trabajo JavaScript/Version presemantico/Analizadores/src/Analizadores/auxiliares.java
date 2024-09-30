package Analizadores;
import java.util.Scanner;

public class auxiliares {

        public static void main(String[] args){
    
            // Pedir numero por consola
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduzca un numero: ");
    
            // Variables
            String cuadrado = "";
            int n = sc.nextInt(); 
            int cuadradoActual = 1;
            int guiones = 2*n;
            int i = 0;
    
            if(n%4 != 0 || n < 0) {
                // Error. Laberinto imposible
                System.out.println("Laberinto imposible");
            } else {
                // Mitad superior. Iremos decreciendo.
                for(; i<n/2; i++){
                    if(i%2 == 0){
                        cuadrado = horizontal(cuadradoActual,guiones,cuadrado);
                        cuadradoActual++;
                        guiones -= 8; 
                    } else {
                        cuadrado = vertical(cuadradoActual,guiones,cuadrado);
                    }
                    cuadrado += "\n";             
                }
                // Mitad inferior
                for(; i<n; i++){
                    if(i%2 != 0){
                        cuadradoActual--;
                        guiones += 8;
                        cuadrado = horizontal(cuadradoActual,guiones,cuadrado);
                    } else {
                        cuadrado = vertical(cuadradoActual,guiones,cuadrado);
                    }
                    cuadrado += "\n";
                }
                System.out.println(cuadrado);
            }
            // Cerramos consola
            sc.close();
        }

        static String horizontal(int aux, int aux2, String aux3){
            // Poner palos cuadrados más grandes que el actual al principio
            for(int p=0; p<aux-1; p++){
                aux3 += "|   ";
            }
            // Poner guiones
            for(int g=0; g<aux2; g++){
                aux3 += "-";
            }
            // Pones palos cuadrados más grandes que el actual al final
            for(int p=0; p<aux-1; p++){
                aux3 += "   |";
            }
            return aux3;
        }

        static String vertical(int aux, int aux2, String aux3){
            // Poner palos cuadrados más grandes que el actual al principio
            for(int p=0; p<aux-1; p++){
                aux3 += "|   ";
            }
            // Poner guiones
            for(int g=0; g<aux2; g++){
                aux3 += " ";
            }
            // Pones palos cuadrados más grandes que el actual al final
            for(int p=0; p<aux-1; p++){
                aux3 += "   |";
            }
            
            return aux3;
        }
    
    }
