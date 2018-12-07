package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * The driver for a Battleship client to play the BattleShip game.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version December 2018
 */
public class BattleDriver {
	/** A client to play the Battleship game and to connect to BattleServer. */
	private BattleClient client;
	/** Scanner to read input from the keyboard. */
	private Scanner in;
	/** Argument length requirement for the /join command. */
	private final int JOIN_LENGTH = 2;
	/** Argument length requirement for the /attack command. */
	private final int ATTACK_LENGTH = 4;
	/** Argument length requirement for the /play command. */
	private final int PLAY_LENGTH = 1;
	/** Argument length requirement for the /show command. */
	private final int SHOW_LENGTH = 2;
	/** Argument length requirement for the /quit command. */
	private final int QUIT_LENGTH = 1;

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
	 * 
	 * @param command
	 */
	private final void printInvalidCommand(String command) {
		System.out.println("Invalid command: " + command);
	}

	/**
	 * Parses input from the keyboard and sends them to the client.
	 * Also ensures that the correct number of arguments are sent,
	 * while type checking certain arguments.
	 */
	private void parseInput() {
		// Holds the command lin arguments.
		String[] input;

		while (client.isConnected()) {
			// Read in command line arguments.
			input = in.nextLine().toLowerCase().split(" ");
			switch (input[0]) {
			case "/join": {
				if (checkInputLength(input, JOIN_LENGTH))
					client.send(String.join(" ", input));
				else
					printInvalidCommand(String.join(" ", input));
			} break;
			case "/attack": {
				if (checkInputLength(input, ATTACK_LENGTH)) {
					try {
						Integer.parseInt(input[ATTACK_LENGTH - 1]);
						Integer.parseInt(input[ATTACK_LENGTH - 2]);
					} catch (NumberFormatException ex) {
						printInvalidCommand(String.join(" ", input));
						parseInput();
					}
					client.send(String.join(" ", input));
				} else
					printInvalidCommand(String.join(" ", input));
			} break;
			case "/play": {
				if (checkInputLength(input, PLAY_LENGTH))
					client.send(input[0]);
				else
					printInvalidCommand(String.join(" ", input));
			} break;
			case "/show": {
				if (checkInputLength(input, SHOW_LENGTH)) 
					client.send(String.join(" ", input));
				else
					printInvalidCommand(String.join(" ", input));
			} break;
			case "/quit": {
				if (checkInputLength(input, QUIT_LENGTH)) 
					client.send(input[0]);
				else
					printInvalidCommand(String.join(" ", input));
			} break;
			default: printInvalidCommand(String.join(" ", input));
			}
		}
	}

	/**
	 * Checks the number of arguments of the input string with the required
	 * number of arguments needed for a specific command.
	 * @param input - The input string containing arguments.
	 * @param length - The length to check.
	 * @return True if the amount of arguments that input has matches the
	 *         length parameter. False otherwise.
	 */
	private boolean checkInputLength(String[] input, int length) {
		return input.length == length;
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
