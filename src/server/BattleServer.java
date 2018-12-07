package server;

import common.MessageListener;
import common.MessageSource;
import common.ConnectionAgent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * BattleServer processes client requests for the BattleShip Game.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class BattleServer implements MessageListener {
	/** The default port for the server. */
	private static final int DEFAULT_PORT = 8674;
	/** The maximum grid size for the grid. */
	private static final int MAX_GRID_SIZE = 100;
	/** The minimum grid size for the grid. */
	private static final int MIN_GRID_SIZE = 5;
	/** The port for the battle server. */
	private int port;
	/** The server socket. */
	private ServerSocket serverSocket;
	/** The index of the current player's turn. */
	private int current;
	/** Game class for game logic. */
	protected Game game;
	/** A map of connection agents. */
	HashMap<String, ConnectionAgent> agents; // maybe not even useful. might be able to do one arraylist only.
	/** An ArrayList of usernames in the game. Used to decide the turn. */
	ArrayList<String> usernames;
	
	/**
	 * Default Constructor for BattleServer.
	 * Uses the default port and size.
	 */
	public BattleServer() throws IOException {
		this(DEFAULT_PORT);
	}
	
	/**
	 * Constructor for BattleServer with a specified port.
	 * Uses the default size.
	 * @param port - The port number to run the battleship server on.
	 */
	public BattleServer(int port) throws IOException {
		this.port = port;
		game = new Game();
		agents = new HashMap<String, ConnectionAgent>();
		usernames = new ArrayList<String>();
		serverSocket = new ServerSocket(port);


		Thread acceptSockets = new Thread(new ServerAcceptThread());
		acceptSockets.start();
		//this.ourMessageAgent = new ConnectionAgent(null);
	}
	
	/**
	 * Constructor for BattleServer with a specified port and size.
	 * @param port - The port number to run the battleship server on.
	 * @param gridSize - The size of the game board.
	 */
	public BattleServer(int port, int gridSize) throws IOException {
		this.port = port;
		game = new Game(clamp(gridSize));
		agents = new HashMap<String, ConnectionAgent>();
		usernames = new ArrayList<String>();
		serverSocket = new ServerSocket(port);


		Thread acceptSockets = new Thread(new ServerAcceptThread());
		acceptSockets.start();
	}

	/**
	 * Clamps the gridSize between the minimum and maximum value. This serves two
	 * purposes: 1) Forces the grid to be larger than a ship, since it would be not
	 * be possible to place a ship of size 5 into a grid of size 4. 2) Forces the
	 * grid to be within reasonable size to prevent out of memory erorrs by creating
	 * a gridsize too large.
	 * @param gridSize - The gridSize specified by the command line args.
	 * @return - The gridSize, clamped between the minimum and maximum value. If
	 *         MIN_GRID_SIZE <= gridSize <= MAX_GRID_SIZE, then gridSize does not
	 *         change.
	 */
	private final int clamp(int gridSize) {
		if (gridSize < MIN_GRID_SIZE) {
			gridSize = MIN_GRID_SIZE;
		} else if (gridSize > MAX_GRID_SIZE) {
			gridSize = MAX_GRID_SIZE;
		}
		return gridSize;
	}
	
	/**
	 * 
	 */
	public void listen() {


	}


	/**
	 * 
	 * @param agent
	 */
	protected void addConnectAgent(ConnectionAgent agent){
		agent.addMessageListener(this);

		//String[] userNameArray = user.split(" ");
		//String name = userNameArray[1];
		//System.out.println(name);

		//agents.put(name, agent);

	}

	/**
	 * 
	 */
	private void nextTurn() {
		if (current >= usernames.size()) {
			current = 0;
		} else {
			current++;
		}
	}


	/**
	 * 
	 * @param message
	 * @param agent
	 */
	private void parseCommands(String message, ConnectionAgent agent) {
		String[] command = message.split(" ");
		switch(command[0]) {
			case "/join":
				joinCommand(command, agent);
				break;
			case "/play":
				playCommand(command, agent);
				break;
			case "/attack":
				attackCommand(command, agent);
				break;
			case "/quit":
				quitCommand(command, agent);
				break;
			case "/show":
				showCommand(command, agent);
				break;
		}
	}


	/**
	 * 
	 * @param command
	 * @param agent
	 */
	private void attackCommand(String[] command, ConnectionAgent agent) throws
	CoordinateOutOfBoundsException, IllegalCoordinateException, InputMismatchException,
	GameOverException {
		if(!game.isGameStarted() && agents.containsKey(command[1])){
			if (usernames.get(current).equals(command[1]) ) {
				game.shoot(command[1], Integer.parseInt(command[2]),
										 Integer.parseInt(command[3]));
				nextTurn();
			} else {
				agent.sendMessage("Sorry, it is currently not your turn.");
			}
		} else if(game.isGameStarted()){
			agent.sendMessage("Game not in progress");
		}else if(!agents.containsKey(command[1])){
			agent.sendMessage(command[1] + " is not a valid player.");
			agent.sendMessage(printValidUsers(agent));
		}
	}

	private String printValidUsers(ConnectionAgent agent) {
		StringBuilder sendThis = new StringBuilder();
		sendThis.append("Valid Users Are:\n");
		for (String user : agents.keySet()) {
			sendThis.append(user + "\n");
		}
		return sendThis.toString();
	}


	/**
	 * 
	 * @param command
	 * @param agent
	 */
	private void joinCommand(String[] command, ConnectionAgent agent){
		if(!game.isGameStarted()) {
			String name = command[1];
			System.out.println(name);
			agents.put(name, agent);
			usernames.add(name);
		}else{
			agent.sendMessage("Game is already begun... Sorry.. You are being kicked..." +
					" :)");
			agent.close();
		}
	}

	/**
	 * 
	 * @param command
	 * @param agent
	 */
	private void playCommand(String[] command, ConnectionAgent agent){
		// if we have enough players and the game is not already started
		if(agents.size() >= 2 && game.isGameStarted()){
			// we start the game
			game.setGameStarted(true);
			// adds each player username to game.addPlayer()
			for(String user : agents.keySet()){
				game.addPlayer(user);
			}
			// tell everyone the game has started
			broadcast("The game begins");
			// 	if we do not have enough players and the game is not already started
		}else if(agents.size() < 2 && game.isGameStarted()){
			agent.sendMessage("Not enough players to play the game");
			// if the game is already started
		}else if(!game.isGameStarted()){
			agent.sendMessage("Game already in progress");
		}
	}

	/**
	 * 
	 * @param command
	 * @param agent
	 */
	private void showCommand(String[] command, ConnectionAgent agent) {
		if (game.isGameStarted() && agents.containsKey(command[1])) {
			if (findUsername(agent).equalsIgnoreCase(command[1])) {
				agent.sendMessage(game.getPrivateGrid(command[1]));
			} else {
				agent.sendMessage(game.getPublicGrid(command[1]));
			}
		} else if (!game.isGameStarted()) {
			agent.sendMessage("Game not in progress");
		} else {
			agent.sendMessage("Invalid user: " + command[1]);
			agent.sendMessage(printValidUsers(agent));
		}
	}


	/**
	 * Finds the username to a given ConnectionAgent.
	 * @param agent - The ConnectionAgent.
	 * @return The username attached to this ConnectionAgent.
	 */
	private String findUsername(ConnectionAgent agent) {
		for (String key : agents.keySet()) {
			if (agents.get(key).getSocket().getLocalPort()
			== agent.getSocket().getLocalPort()) {
				return key;
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param message
	 * @param source
	 */
	public void messageReceived(String message, MessageSource source) {
		try {
		//parseCommands(message, (ConnectionAgent)source);
		} catch () {

		}

		System.out.println("The server has recieved a message!! " + message);
		((ConnectionAgent)source).sendMessage("I recieved your message: " + message);
	}

	/**
	 * 
	 * @param message
	 */
	public void broadcast(String message) {
		for(ConnectionAgent agent: agents.values()){
			agent.sendMessage(message);
		}
	}


	/**
	 * 
	 * @param source
	 */
	public void sourceClosed(MessageSource source) {

	}


	/**
	 * 
	 * @return
	 */
	protected ServerSocket getServerSocket(){
		return this.serverSocket;
	}


	// TESTS GAMEPLAY. Delete this later.
	public void testGameplay() {
		Scanner in = new Scanner(System.in);
		String options = "";
		int x, y;
		boolean hit;
		game.addPlayer("username test");

		do {
			System.out.println("Enter coordinates to attack");
			// ************ gives user options for demonstration purposes
			System.out.println("Or enter 'x' for more options");
			try {
				options = in.next();
				if (options.toLowerCase().equals("x")) {
					this.options(in);
					System.out.println("Enter coordinates to attack");
					x = in.nextInt();
				} else {
					x = Integer.parseInt(options);
				}
				// ************* end user options for demonstration purposes
				// x = in.nextInt();
				y = in.nextInt();
				hit = game.shoot("username test", x, y);
				System.out.println("Coordinates were hit?: " + hit);
				System.out.println(game.getPublicGrid("username test"));
			} catch (CoordinateOutOfBoundsException ex) {
				System.out.println(
						"Coordinates are not on the game board.\n" + "Please pick another set of coordinates:");
			} catch (IllegalCoordinateException ex) {
				System.out.println("Coordinates were already hit.\n" + "Please pick another set of coordinates:");
			} catch (InputMismatchException ex) {
				System.out.println("Please pick valid coordinates.");
				System.exit(1);
			} catch (GameOverException goe) {
				System.out.println("GAME OVER! All ships have been SUNK! Good game!");
				System.exit(1);
			}
		} while (true);
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
				System.out.println(this.game.getPublicGrid("username test"));
			}else if(result.toLowerCase().equals("pri")){
				System.out.println(game.getPrivateGrid("username test"));
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
	 * This class will be a seperate Thread that accepts clients connecting to
	 * the BattleServer's server socket. This is in its own class because when
	 * calling serverSocket.accept, the program hangs. Thus, we must put it in
	 * its own class and thread so that it does not hang the entire program up.
	 * 
	 * @author Jeriah Caplinger
	 * @author Kevin Filanowski
	 * @version December 2018
	 */
	private class ServerAcceptThread implements Runnable {
		/**
		 * Our Thread method. This thread constantly waits for player's joining the
		 * game. It then gives that connection to the BattleServer
		 */
		@Override
		public void run() {
			// while the server socket is not closed
			while (!serverSocket.isClosed()) {
				try {
					// we accept the client's connection
					Socket socket = serverSocket.accept();
					// We make a new connection agent for this socket
					ConnectionAgent agent = new ConnectionAgent(socket);
					// We pass the connection agent to battle server
					addConnectAgent(agent);
					// We start the connection agent up as a thread
					(new Thread(agent)).start();
				} catch (IOException ioe) {
					System.out.println("Caught IOException in serverAcceptThread run method");
				}
			}
		}
	}
}
