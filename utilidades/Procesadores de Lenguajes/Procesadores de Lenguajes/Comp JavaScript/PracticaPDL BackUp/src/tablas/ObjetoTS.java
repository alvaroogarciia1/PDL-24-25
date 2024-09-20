package tablas;

import java.util.ArrayList;

public class ObjetoTS {
	private int desp;
	private String lexema;
	private String tipo;
	private int numParam;
	private ArrayList<String> parametros;
	private String tipoRetorno;
	private String etiqFuncion;
	private String param;
    
    public ObjetoTS(String lexema) {
		this.lexema = lexema;	
		parametros = new ArrayList<String>();
    }
	
	public int getDesp() {
		return desp;
	}

	public void setDesp(int desp) {
		this.desp = desp;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getNumParam() {
		return numParam;
	}

	public void setNumParam(int numParam) {
		this.numParam = numParam;
	}

	public ArrayList<String> getTipoParam() {
		return this.parametros;
	}

	public void setTipoParam(String tipoParam) {
		this.parametros.add(tipoParam);
	}


	public String getTipoRetorno() {
		return tipoRetorno;
	}

	public void setTipoRetorno(String tipoRetorno) {
		this.tipoRetorno = tipoRetorno;
	}

	public String getEtiqFuncion() {
		return etiqFuncion;
	}

	public void setEtiqFuncion(String etiqFuncion) {
		this.etiqFuncion = etiqFuncion;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
    
    public String imprimirId(){
		String r = "";
		if(!this.getTipo().equals("funcion")) r = "* LEXEMA : '" + this.getLexema() + "'\n  ATRIBUTOS :\n    + tipo : '" + this.getTipo() + "'\n    + desplazamiento : '" + this.getDesp() + "'\n";
		else {
			r = "* LEXEMA : '" + this.getLexema() + "'\n  ATRIBUTOS :\n    + tipo : '" + this.getTipo() + "'\n    + NumParametros : '" + this.getNumParam() + "'\n" ;
			for(int i=0; i<this.parametros.size(); i++){
				r += "    + tipoParametro" + i + " : '" + parametros.get(i) + "'\n";
			}
			r += "    + tipoRetorno : '" + this.getTipoRetorno() + "'\n    + etiquetaFucnion : '" + this.getEtiqFuncion() + "'\n";
		}
		return r;
	}

}