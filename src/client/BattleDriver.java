package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * The driver for a Battleship client. 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class BattleDriver {
	/** A client to play the Battleship game and to connect to BattleServer. */
	BattleClient client;
	/** Scanner to read input from the keyboard. */
	Scanner in;

	/**
	 * Constructor for a Battle Driver. Creates a client.
	 * @param host - The hostname of the server to connect to.
	 * @param port - The port number to connect to.
	 * @param username - The username of the player.
	 * @throws UnknownHostException - Thrown if the hostname could not
	 * 								  resolve to an IP Address.
	 */
	public BattleDriver(InetAddress host, int port, String username)
			throws UnknownHostException, IOException {
		in = new Scanner(System.in);
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
	 * Parses input from the keyboard and sends them to the client.
	 */
	private void parseInput() {
		// For now, let's just send anything that the client writes.
		System.out.println("Ready for input!");
		while (true) {
			client.send(in.nextLine());
		}

		/*while (true) {
			System.out.println("Listening for client input:");
			String input = in.next();

			switch (input) {
			case "/join": {
				
			} break;
			case "/attack": {

			}
			break;
			case "/play": {

			}
			break;
			case "/show": {

			}
			break;
			case "/quit": {

			}
			break;
			default: System.out.println("Invalid command: " + input);
			}
		}*/
	}

	/**
	 * The main driver of a client program. Attempts to communicate with
	 * the server.
	 * @param args - Command line arguments, which include:
	 * 				 hostname, port number, and user nickname.
	 *               All are required.
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			printUsageAndExit();
		}
		try {
			BattleDriver driver = new BattleDriver(
								  InetAddress.getByName(args[0]),
								  Integer.parseInt(args[1]), args[2]);
			driver.client.connect();
			driver.parseInput();
		} catch (NumberFormatException ex) {
			printUsageAndExit();
		} catch (UnknownHostException ex) {
			System.out.println(ex.getMessage());
			printUsageAndExit();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			printUsageAndExit();
		}
	}
}
