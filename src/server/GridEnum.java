package server;

/**
 * This enumeration holds common elements inside of a grid.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public enum GridEnum {
	Hit	  	('@'),
	Miss  	('X'),
	Blank	(' ');

	/** Holds the grid element symbol. */
	private final char name;

	/**
	 * Private constructor for the grid elements.
	 * @param name - The grid element's symbol.
	 */
	private GridEnum(char name) {
		this.name = name;
	}

	/**
	 * Retrieves the symbol for the enum.
	 * @return - The grid element's symbol.
	 */
	protected final char getName() {
		return name;
	}
}
