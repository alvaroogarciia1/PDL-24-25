package tablas;

public class TablaPalabrasR {

    private String reservadas[];
    
    public TablaPalabrasR(){
    	this.reservadas =new String [13];
    	this.reservadas[0]="number";
    	this.reservadas[1]="string";
    	this.reservadas[2]="boolean";
    	this.reservadas[3]="let";
    	this.reservadas[4]="switch";
    	this.reservadas[5]="case";
    	this.reservadas[6]="function";
    	this.reservadas[7]="return";
    	this.reservadas[8]="input";
    	this.reservadas[9]="break";
    	this.reservadas[10]="alert";
    	this.reservadas[11]="if";
    	this.reservadas[12]="default";
    }
    
    public boolean buscar(String palabra){
    	for(int i=0; i<reservadas.length; i++){
    			if(reservadas[i].equals(palabra)) return true;
    	}
        return false;
    }
}
