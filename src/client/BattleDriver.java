package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The driver for a Battleship client. 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class BattleDriver {
	BattleClient client;

	/**
	 * Constructor for a Battle Driver. Creates a client.
	 * @param host - The hostname of the server to connect to.
	 * @param port - The port number to connect to.
	 * @param username - The username of the player.
	 * @throws UnknownHostException - Thrown if the hostname could not
	 * resolve to an IP Address.
	 */
	public BattleDriver(InetAddress host, int port, String username)
			throws UnknownHostException {
		client = new BattleClient(host, port, username);
	}

	/**
	 * Prints the usage message and exits the program.
	 */
	private static final void printUsageAndExit() {
		System.out.println("java BattleDriver <hostname> <port> <username>");
		System.exit(1);
	}

	/**
	 * 
	 * @param args - Command line arguments, which include:
	 * hostname, port number, and user nickname. All are required.
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			printUsageAndExit();
		}
		try {
			BattleDriver driver = new BattleDriver(
					InetAddress.getByName(args[0]),
					Integer.parseInt(args[1]), args[2]);
			
		} catch (NumberFormatException ex) {
			printUsageAndExit();
		} catch (UnknownHostException ex) {
			System.out.println(ex.getMessage());
			printUsageAndExit();
		}

	}
}
