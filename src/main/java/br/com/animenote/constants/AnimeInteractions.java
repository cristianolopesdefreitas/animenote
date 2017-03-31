package br.com.animenote.constants;

public enum AnimeInteractions {
	JA_VI("Já vi"),
	ESTOU_VENDO("Estou vendo"),
	QUERO_VER("Quero ver");
	
	private String value;

	private AnimeInteractions(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
