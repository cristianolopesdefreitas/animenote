package br.com.animenote.constants;

public enum UserPostInteractions {
	CURTIR("Curtir"),
	DESCURTIR("Descurtir");
	
	private String value;

	private UserPostInteractions(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
