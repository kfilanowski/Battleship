package server;

import common.MessageListener;
import common.MessageSource;
import common.ConnectionAgent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;

/**
 * BattleServer processes client requests for the BattleShip Game.
 * 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version December 2018
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
	HashMap<String, ConnectionAgent> agents;
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
	 * 
	 * @param port - The port number to run the battleship server on.
	 */
	public BattleServer(int port) throws IOException {
		this.port = port;
		game = new Game();
		agents = new HashMap<String, ConnectionAgent>();
		usernames = new ArrayList<String>();
		serverSocket = new ServerSocket(port);
	}
	
	/**
	 * Constructor for BattleServer with a specified port and size.
	 * 
	 * @param port 	   - The port number to run the battleship server on.
	 * @param gridSize - The size of the game board.
	 */
	public BattleServer(int port, int gridSize) throws IOException {
		this.port = port;
		game = new Game(clamp(gridSize));
		agents = new HashMap<String, ConnectionAgent>();
		usernames = new ArrayList<String>();
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Clamps the gridSize between the minimum and maximum value.
	 * This serves two purposes:
	 * 1) Forces the grid to be larger than a ship, since it would be not
	 *    be possible to place a ship of size 5 into a grid of size 4.
	 * 2) Forces the grid to be within reasonable size to prevent out of 
	 *    memory erorrs by creating a gridsize too large.
	 * 
	 * @param gridSize - The gridSize specified by the command line args.
	 * @return The gridSize, clamped between the minimum and maximum value.
	 *         If MIN_GRID_SIZE <= gridSize <= MAX_GRID_SIZE, 
	 *		   then the gridSize does not change.
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
	 * Creates a new thread to isolate the serverSocket.accept(),
	 * and listens for new connections.
	 */
	public void listen() {
		new Thread(new ServerAcceptThread()).start();
	}

	/**
	 * Adds the BattleServer as a listener to a specified subject.
	 * 
	 * @param agent - The agent that will update it's listeners with messages.
	 */
	protected void addConnectAgent(ConnectionAgent agent){
		agent.addMessageListener(this);
	}

	/**
	 * Causes the player's turn to pass on to the next player, and cycle
	 * back to the front if we have reached the last player.
	 */
	private void nextTurn() {
		if (current >= usernames.size()-1) {
			current = 0;
		} else {
			current++;
		}
		broadcast(usernames.get(current) + " it is your turn.");
	}

	/**
	 * Parses the command that the server recieved, and decides what to do
	 * with that command. There are 5 commands:
	 * /join: Allows a new player to join the game, given that the game
	 *        has not already started.
	 * /play: Starts the game by assigning a random grid to each user, and
	 *        opens up the other commands for usage.
	 * /attack: Attacks a specified player's coordinates.
	 * /quit: Gracefully exits the game.
	 * /show: Displays the specified user's game board, revealing ships only
	 *        if it is your own board.
	 * 
	 * @param message - The message sent to the server to parse.
	 * @param agent - The ConnectionAgent that sent this message.
	 * @throws CoordinateOutOfBoundsException - Thrown if the coordinates
	 * 											specified are not on the grid.
	 * @throws IllegalCoordinateException - Thrown if the specified player has
	 * 										already been attacked at the
	 * 										specified coordinates.
	 * @throws InputMismatchException - Thrown if the coordinates are not
	 * 									valid integers.
	 */
	private void parseCommands(String message, ConnectionAgent agent) throws
	 		CoordinateOutOfBoundsException, IllegalCoordinateException, 
	 		InputMismatchException {
		String[] command = message.split(" ");
		switch(command[0]) {
			case "/join":   joinCommand(command, agent);   break;
			case "/play":   playCommand(command, agent);   break;
			case "/attack": attackCommand(command, agent); break;
			case "/show":   showCommand(command, agent);   break;
			case "/quit":   quitCommand(agent);            break;
		}
	}

	/**
	 * Called when a new message is recieved from any of the clients.
	 *
	 * @param message - The message the server recieves from a client.
	 * @param source  - The messageSource that sent the message.
	 */
	public void messageReceived(String message, MessageSource source) {
		try {
			parseCommands(message, (ConnectionAgent)source);
		} catch (CoordinateOutOfBoundsException 
								| IllegalCoordinateException ex) {
			((ConnectionAgent) source).sendMessage(ex.getMessage());
		} catch (InputMismatchException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * The logic behind the /attack command. Given that it is the player's
	 * turn, /attack attacks the specified player's coordinates.
	 * 
	 * @param command - The arguments sent to the server from the client.
	 * @param agent   - The ConnectionAgent that sent this message.
	 * @throws CoordinateOutOfBoundsException - Thrown if the coordinates 
	 * 											specified are not on the grid.
	 * @throws IllegalCoordinateException - Thrown if the specified player has
	 *                                      already been attacked at the 
	 *                                      specified coordinates.
	 * @throws InputMismatchException - Thrown if the coordinates are not
	 *                                  valid integers.
	 */
	private void attackCommand(String[] command, ConnectionAgent agent) throws
				CoordinateOutOfBoundsException, IllegalCoordinateException, 
												InputMismatchException{
		// Holds the username of the current player.
		String username = findUsername(agent);
		// Holds the result of a hit or miss to that player.
		boolean hitOrMiss;
		// If the game is being played and the
		// specified person to attack is playing.
		if (game.isGameStarted() && agents.containsKey(command[1])) {
			// If it is the current players turn and
			// they are not attacking themselves.
			if (usernames.get(current).equals(username) 
				&& !username.equals(command[1])) {
				try {
					hitOrMiss = game.shoot(command[1], 
					Integer.parseInt(command[2]), Integer.parseInt(command[3]));
					// we tell everyone who the shots were fired at and by who
					broadcast("Shots fired at " + command[1] 
										+ " by " + username);
					// we tell everyone if the shot hit or missed
					broadcast("Did the shot hit?: " + hitOrMiss);
				// if the hit resulted	in the player losing their last ship
				} catch(GameOverException goe){
					broadcast("HIT & SUNK every ship for player: " 
											+ command[1] + "\n");
					// we boot that player from the game
					agents.get(command[1]).sendMessage("Enter any key to exit\n");
					quitCommand(agents.get(command[1]));
				}
				// we then let the next player go.
				nextTurn();
			// if they are attacking themselves
			} else if (username.equals(command[1])) {
				agent.sendMessage("You cannot attack yourself. Try again.");
			// if it is not their turn
			} else {
				agent.sendMessage("Sorry, it is currently not your turn.");
			}
		// if the game has not begun yet
		} else if (!game.isGameStarted()){
			agent.sendMessage("Game not in progress");
		// if the specified player to attack is not in the game
		} else if (!agents.containsKey(command[1])){
			agent.sendMessage(command[1] + " is not a valid player.");
			// we give them a list of valid users
			agent.sendMessage(printValidUsers());
		}
	}

	/**
	 * The logic behind the /quit command. Closes the connections to and from
	 * the specified user.
	 * 
	 * @param agent - The ConnectionAgent to stop communicating with.
	 */
	private void quitCommand(ConnectionAgent agent){
		// we close the client's connection agent
		agent.close();
		// we get the user name of the client
		String agentsName = findUsername(agent);
		// if it is not null
		if (!agentsName.equals("")) {
			// we remove them from the game list
			game.removePlayer(agentsName);
			// we remove them from our connection agent hash map
			agents.remove(agentsName);
			// Remove the username from the list of turns.
			usernames.remove(agentsName);
			// we decrement current because if we do not, 
			// then a player's turn is skipped.
			if(this.current > 0){
				current--;
			}
			// We tell everyone the player has left.
			broadcast(agentsName + " surrendered.");
		}
		// If the game has started and there is only one player left.
		if (game.getTotalPlayers() == 1 && game.isGameStarted()) {
			// We tell the player that the game is ending
			// because there is only one player.
			broadcast("You are the only player left in the Game.." +
					"\nCongratulations you WON!!!\nGame is ending."
					+ "\nEnter any key to exit\n");
			game.setGameStarted(false);
			// we find that single user's name
			for (String agent1 : agents.keySet()) {
				agents.get(agent1).close();
				// we remove the agent from our connection agent hash map.
				agents.remove(agent1);
				// we remove that player from the game grid list
				game.removePlayer(agent1);
				// remove the username from the list of turns
				usernames.remove(agentsName);
				// We decrement current because if we do not, 
				// then a player's turn is skipped.
				if(this.current > 0){
					current--;
				}
			}
		}
	}

	/**
	 * Prints the registered users that are currently in the game.
	 * 
	 * @return A formatted string of the registered users in the game.
	 */
	private String printValidUsers() {
		StringBuilder sendThis = new StringBuilder();
		sendThis.append("Valid Users Are:\n");
		for (String user : agents.keySet()) {
			sendThis.append(user + "\n");
		}
		return sendThis.toString();
	}

	/**
	 * The logic behind the /join command. Registers a user to the server.
	 * 
	 * @param command - The command arguments sent by the client.
	 * @param agent   - The client's ConnectionAgent that sent the message.
	 */
	private void joinCommand(String[] command, ConnectionAgent agent){
		String joined = findUsername(agent);
		if(!game.isGameStarted() && joined.equals("")) {
			String name = command[1];
			int number = 1;
			while(agents.containsKey(name)){
				name = name + number;
				number++;
			}
			agents.put(name, agent);
			usernames.add(name);
			broadcast("!!! " + name + " has joined.");
		}else if (game.isGameStarted() && joined.equals("")){
			agent.sendMessage("Game is already begun... Sorry.. "
			 + "You are being kicked... :)\nEnter any key to exit\n");
			agent.close();
		} else if (!joined.equals("")){
			agent.sendMessage("You are already joined.");
		}
	}
	
	/**
	 * The logic behind the /play command. Starts the game.
	 * 
	 * @param command - The command arguments sent by the client.
	 * @param agent   - The client's ConnectionAgent that sent the message.
	 */
	private void playCommand(String[] command, ConnectionAgent agent){
		// if we have enough players and the game is not already started
		if(agents.size() >= 2 && !game.isGameStarted()){
			// we start the game
			game.setGameStarted(true);
			// adds each player username to game.addPlayer()
			for(String user : agents.keySet()){
				game.addPlayer(user);
			}
			broadcast("The game begins!");
			broadcast(usernames.get(current) + " it is your turn.");
			// If we do not have enough players and
			// the game is not already started.
		}else if(agents.size() < 2 && !game.isGameStarted()){
			agent.sendMessage("Not enough players to play the game.");
			// if the game is already started
		}else if(game.isGameStarted()){
			agent.sendMessage("Game already in progress");
		}
	}

	/**
	 * The logic being the /show command. Outputs a specific player's
	 * grid specified by the client's command request.
	 * 
	 * @param command - The command arguments sent by the client.
	 * @param agent   - The client's ConnectionAgent that sent the message.
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
			agent.sendMessage(printValidUsers());
		}
	}

	/**
	 * Finds the username to a given ConnectionAgent.
	 * 
	 * @param agent - The ConnectionAgent.
	 * @return The username attached to this ConnectionAgent.
	 */
	private String findUsername(ConnectionAgent agent) {
		for (String key : agents.keySet()) {
			if (agents.get(key) == agent) {
				return key;
			}
		}
		return "";
	}

	/**
	 * Sends a message to all clients connected to the server.
	 * 
	 * @param message - The message to send to every client registered.
	 */
	public void broadcast(String message) {
		for(ConnectionAgent agent: agents.values()){
			agent.sendMessage(message);
		}
	}

	/**
	 * Tells us when a client has abruptly quit and handles it accordingly
	 * @param source
	 */
	public void sourceClosed(MessageSource source) {
		// we need to get our connection agent from the source
		ConnectionAgent agent = (ConnectionAgent) source;
		// we find which connection agent is closed
		String agentsName = findUsername(agent);
		// if it is not null
		if (!agentsName.equals("")) {
			// we remove them from the game list
			game.removePlayer(agentsName);
			// we remove them from our connection agent hash map
			agents.remove(agentsName);
			// Remove the username from the list of turns.
			usernames.remove(agentsName);
			// we decrement current because if we do not,
			// then a player's turn is skipped.
			if(this.current > 0){
				current--;
			}
			// we tell everyone the player has left
			broadcast(agentsName + " surrendered.");
		}
		// if the game has started and there is only one player left
		if (game.getTotalPlayers() == 1 && game.isGameStarted()) {
			// we tell the player that the game is ending because
			// there is only one player
			broadcast("You are the only player left in the Game.." +
					"\nCongratulations you WON!!!\nGame is ending."
					+ "\nEnter any key to exit\n");
			game.setGameStarted(false);
			// we find that single user's name
			for (String agent1 : agents.keySet()) {
				agents.get(agent1).close();
				// we remove the agent from our connection agent hash map
				agents.remove(agent1);
				// we remove that player from the game grid list
				game.removePlayer(agent1);
				// remove the username from the list of turns
				usernames.remove(agentsName);
				// we decrement current because if we do not,
				//  then a player's turn is skipped
				if(this.current > 0){
					current--;
				}
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
		 * Our Thread method. This thread constantly waits for player's joining
		 * the game. It then gives that connection to the BattleServer.
		 */
		@Override
		public void run() {
			while (!serverSocket.isClosed()) {
				try {
					Socket socket = serverSocket.accept();
					ConnectionAgent agent = new ConnectionAgent(socket);
					// We pass the connection agent to battle server.
					addConnectAgent(agent);
					// We start the connection agent up as a thread.
					(new Thread(agent)).start();
				} catch (IOException ioe) {
					System.out.println("Caught IOException.");
				}
			}
		}
	}
}
