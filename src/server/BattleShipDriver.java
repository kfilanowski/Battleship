package server;

/**
 * Initializes a BattleServer for the BattleShip Game.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class BattleShipDriver {
	/** The server running the game logic for battleship. **/
	BattleServer server;
	
	/**
	 * Default constructor for default parameters in BattleServer.
	 */
	public BattleShipDriver() {
		server = new BattleServer();
	}
	
	/**
	 * Constructor for BattleShipDriver. Initializes a BattleServer
	 * on a specified port.
	 * @param port - The port number to run the battleship server on.
	 */
	public BattleShipDriver(int port) {
		server = new BattleServer(port);
	}
	
	/**
	 * Constructor for BattleShipDriver. Intiailizies a BattleServer on a
	 * specified port and specified game board size.
	 * @param port - The port number to run the battleship server on.
	 * @param size - The size of the game board.
	 */
	public BattleShipDriver(int port, int gridSize) {
		server = new BattleServer(port, gridSize);
	}
	
	/**
	 * Calls the listen in BattleServer to listen for requests.
	 */
	private void listen() {
		server.listen();
	}
	
	/**
	 * Prints a usage message and exits the program.
	 */
	private static final void PrintUsageAndExit() {
		System.out.println("Usage: java BattleShipDriver [port] [size]");
		System.exit(1);
	}
	
	/**
	 * Main driver of the server. 
	 * @param args Command line arguments.
	 * The two command line arguments are the port number and the size of
	 * the board.
	 */
	public static void main(String[] args) {
		// Declaring the driver. 
		BattleShipDriver driver = null;
		try {
			if (args.length == 2) {
				driver = new BattleShipDriver(Integer.parseInt(args[0]),
						Integer.parseInt(args[1]));
			} else if (args.length == 1) {
				driver = new BattleShipDriver(Integer.parseInt(args[0]));
			} else {
				driver = new BattleShipDriver();
			}
			// Listen for requests
			driver.listen();
		} catch (NumberFormatException ex) {
			PrintUsageAndExit();
		}
	}
}
