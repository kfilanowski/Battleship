package server;

import java.net.ServerSocket;

import common.MessageSource;

/**
 * 
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
