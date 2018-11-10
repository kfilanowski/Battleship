package server;

public enum GridEnum {
	Hit('@'),
	Miss('X'),
	Blank(' ');

	private final char name;

	private GridEnum(char name) {
		this.name = name;
	}

	protected final char getName() {
		return name;
	}
}
