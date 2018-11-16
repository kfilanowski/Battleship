package server;

import common.MessageSource;

import java.awt.*;
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
	 * @param gridSize - The size of the game board.
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
		String options = "";
		int x, y;
		boolean hit;
		game.addPlayer();
		
		do {
			System.out.println("Enter coordinates to attack");
			// ************ gives user options for demonstration purposes
			System.out.println("Or enter 'x' for more options");
			try {
				options = in.next();
				if(options.toLowerCase().equals("x")){
					this.options(in);
					System.out.println("Enter coordinates to attack");
					x = in.nextInt();
				}else{
					x = Integer.parseInt(options);
				}
				//************* end user options for demonstration purposes
				//x = in.nextInt();
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
			}catch(GameOverException goe){
				System.out.println("GAME OVER! All ships have been SUNK! Good game!");
				System.exit(1);
			}
		}
		while (true);
	}


	/**
	 * Gives the user options for demonstration purposes
	 * @param in scanner connected to the keyboard
	 */
	private void options(Scanner in){
		boolean go = true;
		String result = "";
		while(go) {
			System.out.println("Enter:\n'Pub' for the public grid\n'Pri' for private grid" +
					"\n'q' to quit the options screen\n'Q!' to quit the game.");
			result = in.next();
			if(result.toLowerCase().equals("pub")){
				System.out.println(this.game.getPublicGrid(0));
			}else if(result.toLowerCase().equals("pri")){
				System.out.println(game.getPrivateGrid(0));
			}else if(result.toLowerCase().equals("q")){
				go = false;
			}else if(result.toLowerCase().equals("q!")){
				System.out.println("Quitting game");
				System.exit(1);
			}else{
				System.out.println("Command not recognized, please enter a command from" +
						" the list");
			}
		}
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
