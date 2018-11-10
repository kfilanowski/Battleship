package server;

public enum Ship {
	Carrier('C', 5),
	BattleShip('B', 4),
	Cruiser('R', 3),
	Submarine('S', 3),
	Destroyer('D', 2);
	
	private final char name;
	private final int size;
	
	private Ship(char name, int size) {
		this.name = name;
		this.size = size;
	}
	
	protected final char getName() {
		return name;
	}
	
	protected final int size() {
		return size;
	}
}
