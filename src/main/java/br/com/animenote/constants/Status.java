package br.com.animenote.constants;

public enum Status {
	A("Ativo"),
	I("Inativo");
	
	private String value;

	private Status(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
