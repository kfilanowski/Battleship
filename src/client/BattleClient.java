package client;

import java.util.Scanner;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
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
	 * @param host - A string representation of the host name.
	 * @param port - The port number to connect on the server.
	 * @param username - The username of the player.
	 * @throws UnknownHostException - Thrown if the IP address could not be
	 * found on the specified host name.
	 */
	public BattleClient(String host, int port, String username)
			throws UnknownHostException, IOException {
		this.host = InetAddress.getByName(host);
		this.port = port;
		this.username = username;
		this.agent = new ConnectionAgent(new Socket(this.host, port));
		//this.agent.addMessageListener((MessageListener) agent);
	}
	
	/**
	 * Constructor for a 
	 * @param host - The adress of the host in InetAddress form.
	 * @param port - The port number to connect on the server.
	 * @param username - The username of the player.
	 */
	public BattleClient(InetAddress host, int port, String username) 
								throws IOException {
		this.host = host;
		this.port = port;
		this.username = username;
		// lets the client's connection agent add the client to the agent's list
		// of message listeners to notify and other methods
		this.agent = new ConnectionAgent(new Socket(this.host, port));
		this.agent.addMessageListener(this);
		this.agent.sendMessage("/join " + username);
		Thread runAgent = new Thread(agent);
		runAgent.start();
	}
	
	public void connect() {
		send("/join " + username);
		


	}
	
	public void messageReceived(String message, MessageSource source) {
		System.out.println("got it" + message);
	}
	
	public void sourceClosed(MessageSource source) {
		
	}
	
	public void send(String message) {
		agent.sendMessage(message);
	}
}
