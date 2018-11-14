package server;

import java.util.ArrayList;

/**
 * The main game logic for BattleShip.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class Game {
	/** Holds the grids for the game. Player 0 will be grid at index 0, etc. */
	ArrayList<Grid> gridList;
	/** The default size of the grid for the game. */
	private static final int DEFAULT_GRID_SIZE = 10;
	/** The size of the grid for the game. */
	private int gridSize;
	
	/**
	 * Default constructor for the Game.
	 * Uses the default grid size.
	 */
	public Game() {
		this(DEFAULT_GRID_SIZE);
	}
	
	/**
	 * Constructor for the Game.
	 * Initializes a game board with a specified size.
	 * @param size - The size of the game.
	 */
	public Game(int gridSize) {
		gridList = new ArrayList<Grid>();
		this.gridSize = gridSize;
	}
	
	/**
	 * Adds a new player to the game.
	 */
	public void addPlayer() {
		gridList.add(new Grid(gridSize));
	}
	
	/**
	 * Remove a player from the game.
	 * @param playerNumber - Which player to remove from the game.
	 */
	public void removePlayer(int playerNumber) {
		gridList.remove(playerNumber);
	}
	
	/**
	 * Returns the public formatted grid of a specific player.
	 * @param playerNumber - Which player's grid to retrieve.
	 * @return - A formatted string containing the grid of a specific player.
	 */
	public String getPublicGrid(int playerNumber) {
		return gridList.get(playerNumber).getPublicGrid();
	}
	
	/**
	 * Returns the private (Player only) formatted grid of a specific player.
	 * @param playerNumber - Which player's grid to retrieve.
	 * @return - A formatted string containing the grid of a specific player.
	 */
	public String getPrivateGrid(int playerNumber) {
		return gridList.get(playerNumber).getPrivateGrid();
	}
	
	/**
	 * Attempt to shoot to a specific coordinate on a specific player's grid.
	 * @param x - The x coordinate on the grid.
	 * @param y - The y coordinate on the grid.
	 * @return - True if coordinate was a hit, false if it was a miss.
	 */
	public Boolean shoot(int playerNumber, int x, int y) throws 
			CoordinateOutOfBoundsException, IllegalCoordinateException {
		if (x >= gridSize || x < 0) {
			// Tells the client that this coordinate was out of bounds.
			throw new CoordinateOutOfBoundsException(); 
		}
		// NOTE: Need to make sure that server handles a valid playerNumber.
		if (!gridList.get(playerNumber).isValidShot(x, y)) {
			// Tells the client that this coordinate was already hit.
			throw new IllegalCoordinateException(); 
		}

		// Return true if coordinate was a hit, false if it was a miss.
		return gridList.get(playerNumber).shoot(x, y);
	}
}
