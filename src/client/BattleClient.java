package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
 * The Client that is responsible for connecting to its connection agent
 * to communicate with the server. Passes along messages.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
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
	
	/**
	 * Constructor for a client in the BattleShip game.
	 * @param host     - A string representation of the host name.
	 * @param port 	   - The port number to connect on the server.
	 * @param username - The username of the player.
	 * @throws UnknownHostException - Thrown if the IP address could not be
	 * 								  found on the specified host name.
	 * @throws IOException          - Thrown if some sort of I/O error occurs.
	 */
	public BattleClient(String host, int port, String username)
			throws UnknownHostException, IOException {
		this(InetAddress.getByName(host), port, username);
	}
	
	/**
	 * Constructor for a client in the BattleShip game.
	 * @param host     - The adress of the host in InetAddress form.
	 * @param port     - The port number to connect on the server.
	 * @param username - The username of the player.
	 * @throws IOException - Thrown if some sort of I/O error occurs.
	 */
	public BattleClient(InetAddress host, int port, String username) 
								throws IOException {
		this.host = host;
		this.port = port;
		this.username = username;
		// Lets the client's connection agent add the client to the agent's list
		// of message listeners to notify.
		this.agent = new ConnectionAgent(new Socket(this.host, port));
		this.agent.addMessageListener(this);
		// Throw it in a thread and start it.
		new Thread(agent).start();
	}
	
	/**
	 * Sends a join command to the BattleServer.
	 */
	public void connect() {
		send("/join " + username);
	}

	public void close() {
		agent.close();
	}

	/**
	 * 
	 */
	public boolean isConnected() {
		return agent.isConnected();
	}
	
	/**
	 * Prints the message received from its ConnectionAgent.
	 * @param message - The message recieved from the MessageSource.
	 * @param source  - The source of the message that it came from.
	 */
	public void messageReceived(String message, MessageSource source) {
		System.out.println(message);
	}
	
	/**
	 * 
	 */
	public void sourceClosed(MessageSource source) {
		
	}
	
	/**
	 * Sends a message to the ConnectionAgent to pass it along to the server.
	 * @param message - The message to send to the server.
	 */
	public void send(String message) {
		agent.sendMessage(message);
	}
}
