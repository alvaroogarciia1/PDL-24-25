package tablas;

public class ObjetoTS {
	private int desp;
	private String lexema;
	private String tipo;
	private int numParam;
	private String tipoParam;
	private int modoParam;
	private String tipoRetorno;
	private String etiqFuncion;
	private String param;
    
    //Sin Analizador Semantico
    public ObjetoTS(String lexema) {
        this.lexema = lexema;		
    }

    //----------------------------------------------------

	//Variables
	public ObjetoTS(String lexema, String tipo, int desp) {
		this.lexema = lexema;
		this.tipo = tipo;
		this.desp = desp;		
    }
    
    //Funciones
	public ObjetoTS(String lexema, String tipo, int numParam, String tipoParam, int modoParam, String tipoRetorno, String etiqFuncion) {
		this.lexema = lexema;
        this.tipo = tipo;
        this.numParam = numParam;
        this.tipoParam = tipoParam;
        this.modoParam = modoParam; // 1-Valor  2-Referencia
        this.tipoRetorno = tipoRetorno;
        this.etiqFuncion = etiqFuncion;		
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

	public String getTipoParam() {
		return tipoParam;
	}

	public void setTipoParamXX(String tipoParam) {
		this.tipoParam = tipoParam;
	}

	public int getModoParamXX() {
		return modoParam;
	}

	public void setModoParam(int modoParamXX) {
		this.modoParam = modoParamXX;
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
    
    public String imprimir(){
        return "";
    }
}