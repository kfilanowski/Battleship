package server;

/**
 * Holds ship enumerations that contain the symbol and the size
 * of each ship.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public enum Ship {
	Carrier		('C', 5),
	BattleShip  ('B', 4),
	Cruiser		('R', 3),
	Submarine	('S', 3),
	Destroyer	('D', 2);

	/** Symbol for the ship. **/
	private final char symbol;
	/** Size of the ship. **/
	private final int size;

	/**
	 * Private Constructor for the ship.
	 * @param symbol - Symbol for the ship.
	 * @param size   - Size of the ship.
	 */
	private Ship(char symbol, int size) {
		this.symbol = symbol;
		this.size = size;
	}

	/**
	 * Retrieves the symbol of the ship. 
	 * @return The symbol of the specified ship.
	 */
	protected final char getSymbol() {
		return symbol;
	}

	/**
	 * Retrieves the size of the ship.
	 * @return - The size of the specified ship.
	 */
	protected final int size() {
		return size;
	}
}
