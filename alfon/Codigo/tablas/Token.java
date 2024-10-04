package tablas;

public class Token {
	
	private String id;
	private Object atributo;

	public Token(String id) {
		this.id = id;
		this.atributo = null;
	}

	public Token(String id, int attr) {
		this.id = id;
		this.atributo = attr;
    }
    
	public Token(String id, String attr) {
		this.id = id;
		this.atributo = attr;
	}

	public String getIdentifier() {
		return this.id;
	}

	public void setIdentifier(String identifier) {
		this.id = identifier;
	}

	public Object getAttribute() {
		return this.atributo;
	}

	public void setAttribute(String attribute) {
		this.atributo = attribute;
	}

	public String toString() {
		if (this.atributo == null) {
			return "<" + this.id + ", >";
        }
		return "<" + this.id + ", " + this.atributo + ">";
    }
}