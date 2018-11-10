package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.MessageSource;

/**
 * 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class BattleClient {
	/** The address of the server to connect to. */
	private InetAddress host;
	/** The port number to connect to. */
	private int port;
	/** The username of the client. */
	private String username;
	
	/**
	 * Constructor for a client in the BattleShip game.
	 * @param host - A string representation of the host name.
	 * @param port - The port number to connect on the server.
	 * @param username - The username of the player.
	 * @throws UnknownHostException - Thrown if the IP address could not be
	 * found on the specified host name.
	 */
	public BattleClient(String host, int port, String username)
			throws UnknownHostException {
		this.host = InetAddress.getByName(host);
		this.port = port;
		this.username = username;
	}
	
	/**
	 * Constructor for a 
	 * @param host - The adress of the host in InetAddress form.
	 * @param port - The port number to connect on the server.
	 * @param username - The username of the player.
	 */
	public BattleClient(InetAddress host, int port, String username) {
		this.host = host;
		this.port = port;
		this.username = username;
	}
	
	public void connect() {
		
	}
	
	public void messageReceived(String message, MessageSource source) {
		
	}
	
	public void sourceClosed(MessageSource source) {
		
	}
	
	public void send(String message) {
		
	}
}
