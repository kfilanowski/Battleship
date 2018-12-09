package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintStream;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
 * The client that is responsible for connecting to its connection agent
 * to communicate with the server. Passes along messages.
 * 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version December 2018
 */
public class BattleClient extends MessageSource implements MessageListener {
	/** The address of the server to connect to. */
	private InetAddress host;
	/** The port number to connect to. */
	private int port;
	/** The username of the client. */
	private String username;
	/** Connection agent to communicate with the server. */
	private ConnectionAgent agent;
	/** The message listener printing output. */
	PrintStreamMessageListener printer;
	
	/**
	 * Constructor for a client in the BattleShip game.
	 * 
	 * @param host     - A string representation of the host name.
	 * @param port 	   - The port number to connect on the server.
	 * @param username - The username of the player.
	 * @throws UnknownHostException - Thrown if the IP address could not be
	 * 								  found on the specified host name.
	 * @throws IOException          - Thrown if some sort of I/O error occurs.
	 */
	public BattleClient(String host, int port, String username, 
				PrintStream out) throws UnknownHostException, IOException {
		this(InetAddress.getByName(host), port, username, out);
	}
	
	/**
	 * Constructor for a client in the BattleShip game.
	 * @param host     - The adress of the host in InetAddress form.
	 * @param port     - The port number to connect on the server.
	 * @param username - The username of the player.
	 * @throws IOException - Thrown if some sort of I/O error occurs.
	 */
	public BattleClient(InetAddress host, int port, String username,
	 					PrintStream out) throws IOException {
		this.host = host;
		this.port = port;
		this.username = username;
		// Lets the client's ConnectionAgent add the client to the agent's
		// list of message listeners to notify.
		this.agent = new ConnectionAgent(new Socket(this.host, port));
		this.agent.addMessageListener(this);
		printer = new PrintStreamMessageListener(out);
		addMessageListener(printer);
		// Throw it in a thread and start it.
		new Thread(agent).start();
	}
	
	/**
	 * Sends a /join command to the BattleServer.
	 */
	public void connect() {
		send("/join " + username);
	}

	/**
	 * Tells the ConnectionAgent to close the connection with the server.
	 */
	public void close() {
		agent.close();
	}

	/**
	 * Checks if the ConnectionAgent is currently connected with the server.
	 * 
	 * @return True if the socket is connected to the server socket.
	 *         False otherwise.
	 */
	public boolean isConnected() {
		return agent.isConnected();
	}
	
	/**
	 * Notifies the listeners to BattleClient of the message received
	 * from BattleClient's ConnectionAgent, which is a message from the server.
	 * 
	 * @param message - The message recieved from the MessageSource.
	 * @param source  - The source of the message that it came from.
	 */
	public void messageReceived(String message, MessageSource source) {
		notifyReceipt(message);
	}
	
	/**
	 * Closes the connection agent.
	 * @param MessageSource source - The MessageSource to close.
	 */
	public void sourceClosed(MessageSource source) {
		//what is this for?
	}
	
	/**
	 * Sends a message to the ConnectionAgent to pass it along to the server.
	 * @param message - The message to send to the server.
	 */
	public void send(String message) {
		agent.sendMessage(message);
	}
}
