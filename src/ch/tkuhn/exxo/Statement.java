package ch.tkuhn.exxo;

public class Statement {
	
	private final String id;
	private final String language;
	private final String text;
	
	public Statement(String id, String language, String text) {
		this.id = id;
		this.language = language;
		this.text = text;
	}
	
	public String getID() {
		return id;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isTrue() {
		return id.indexOf("+") > -1;
	}

}
