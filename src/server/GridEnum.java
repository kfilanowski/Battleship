package server;

/**
 * This enumeration holds common elements inside of a grid.
 * 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public enum GridEnum {
	Bar   ('|'),
	Hit	  ('@'),
	Miss  ('X'),
	Blank (' ');

	/** Holds the grid element symbol. */
	private final char symbol;

	/**
	 * Private constructor for the grid elements.
	 * 
	 * @param symbol - The grid element's symbol.
	 */
	private GridEnum(char symbol) {
		this.symbol = symbol;
	}

	/**
	 * Retrieves the symbol for the enum.
	 * 
	 * @return - The grid element's symbol.
	 */
	protected final char getSymbol() {
		return symbol;
	}

	/**
	 * A toString to convert the symbol to a string.
	 * 
	 * @return - The grid element's symbol.
	 */
	public String toString() {
		return "" + symbol;
	}
}
