package server;

import java.util.HashMap;

/**
 * The main game logic for BattleShip.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class Game {
	/** Holds the grids for each player. */
	private HashMap<String, Grid> gridList;
	/** The default size of the grid for the game. */
	private static final int DEFAULT_GRID_SIZE = 10;
	/** The size of the grid for the game. */
	private int gridSize;
	/** Tells us if the game has already started or not */
	private boolean gameStarted;
	
	/**
	 * Default constructor for the Game.
	 * Uses the default grid size.
	 */
	protected Game() {
		this(DEFAULT_GRID_SIZE);
	}
	
	/**
	 * Constructor for the Game.
	 * Initializes a game board with a specified size.
	 * @param gridSize - The size of the game.
	 */
	protected Game(int gridSize) {
		gridList = new HashMap<String, Grid>();
		this.gridSize = gridSize;
		this.gameStarted = false;
	}
	
	/**
	 * Adds a new player to the game.
	 * @param username - The player's username to be used in the game.
	 */
	protected void addPlayer(String username) {
		gridList.put(username, new Grid(gridSize));
	}
	
	/**
	 * Remove a player from the game.
	 * @param username - The username of the player to remove from the game.
	 */
	protected void removePlayer(String username) {
		gridList.remove(username);
	}
	
	/**
	 * Returns the public formatted grid of a specific player.
	 * @param username - Which player's grid to retrieve by using their username.
	 * @return - A formatted string containing the grid of a specific player.
	 */
	protected String getPublicGrid(String username) {
		return gridList.get(username).getPublicGrid();
	}


	/**
	 * Sets the gameStarted variable to true or false
	 * @param gameStarted
	 */
	protected void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	/**
	 * Gets the boolean of our game started
	 * @return whether the game has been started or not
	 */
	protected boolean isGameStarted() {
		return gameStarted;
	}

	/**
	 * Returns the private (Player only) formatted grid of a specific player.
	 * @param username - Which player's grid to retrieve.
	 * @return - A formatted string containing the grid of a specific player.
	 */
	protected String getPrivateGrid(String username) {
		return gridList.get(username).getPrivateGrid();
	}
	
	/**
	 * Attempt to shoot to a specific coordinate on a specific player's grid.
	 * @param x - The x coordinate on the grid.
	 * @param y - The y coordinate on the grid.
	 * @return - True if coordinate was a hit, false if it was a miss.
	 * @throws CoordinateOutOfBoundsException - Thrown when the player chooses a
	 * coordinate that is not within the size of the grid.
	 * @throws IllegalCoordinateException - Thrown when the player chooses a
	 * coordinate that has already been attacked. 
	 * @throws GameOverException - Thrown when the game ends by sinking all ships.
	 */
	protected Boolean shoot(String username, int x, int y) throws
			CoordinateOutOfBoundsException, IllegalCoordinateException, GameOverException {
		if (x >= gridSize || x < 0) {
			// Tells the client that this coordinate was out of bounds.
			throw new CoordinateOutOfBoundsException(); 
		}
		// NOTE: Need to make sure that server handles a valid username.
		if (!gridList.get(username).isValidShot(x, y)) {
			// Tells the client that this coordinate was already hit.
			throw new IllegalCoordinateException(); 
		}

		// Return true if coordinate was a hit, false if it was a miss.
		// if the game is over, then shoot() throws GameOverException
		return gridList.get(username).shoot(x, y);
	}
}
