package server;

import common.MessageSource;
import java.net.ServerSocket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * BattleServer processes client requests for the BattleShip Game.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class BattleServer {
	/** The default port for the server. */
	private static final int DEFAULT_PORT = 8674;
	/** The port for the battle server. */
	private int port;
	/** The server socket. */
	private ServerSocket serverSocket;
	/** TODO */
	private int current;
	/** Game class for game logic. */
	Game game;
	
	/**
	 * Default Constructor for BattleServer.
	 * Uses the default port and size.
	 */
	public BattleServer() {
		this(DEFAULT_PORT);
	}
	
	/**
	 * Constructor for BattleServer with a specified port.
	 * Uses the default size.
	 * @param port - The port number to run the battleship server on.
	 */
	public BattleServer(int port) {
		this.port = port;
		game = new Game();
	}
	
	/**
	 * Constructor for BattleServer with a specified port and size.
	 * @param port - The port number to run the battleship server on.
	 * @param size - The size of the game board.
	 */
	public BattleServer(int port, int gridSize) {
		this.port = port;
		game = new Game(gridSize);
	}
	
	/**
	 * 
	 */
	public void listen() {
		// TESTING GAMEPLAY, NOT ACTUAL LISTEN METHOD.
		Scanner in = new Scanner(System.in);
		int x, y;
		boolean hit;
		game.addPlayer();
		
		do {
			System.out.println("Enter coordinates to attack");
			try {
				x = in.nextInt();
				y = in.nextInt();
				hit = game.shoot(0, x, y);
				System.out.println("Coordinates were hit?: " + hit);
				System.out.println(game.getPublicGrid(0));
			} catch (CoordinateOutOfBoundsException ex) {
				System.out.println("Coordinates are not on the game board.\n"
									+ "Please pick another set of coordinates:");
			} catch (IllegalCoordinateException ex) {
				System.out.println("Coordinates were already hit.\n"
									+ "Please pick another set of coordinates:");
			} catch (InputMismatchException ex) {
				System.out.println("Please pick valid coordinates.");
				System.exit(1);
			}
		}
		while (true);
	}
	
	/**
	 * 
	 * @param message
	 */
	public void broadcast(String message) {
		
	}
	
	/**
	 * 
	 * @param message
	 * @param source
	 */
	public void messageReceived(String message, MessageSource source) {
		
	}
	
	/**
	 * 
	 * @param source
	 */
	public void sourceClosed(MessageSource source) {
		
	}
}
