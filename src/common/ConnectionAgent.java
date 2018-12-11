package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Essentially the messenger between a client and a server, the ConnectionAgent
 * notifies its listener of the messages it recieves from the client/server,
 * and is also able to send messages to the client/server. It is a multipurpose
 * class, serving both the client and the server.
 */
public class ConnectionAgent extends MessageSource implements Runnable {
	/** The client socket that the client creates or the server accepts. */
	private Socket socket;
	/** A scanner to read messages sent to this ConnectionAgent. */
	private Scanner in;
	/** The output stream that the ConnectionAgent writes to. */
	private PrintStream out;

	/**
	 * Constructor for a ConnectionAgent that accepts a Socket. 
	 * This connection agent will be waiting for input from the sender
	 * constantly. It can also send messages.
	 * 
	 * @param socket - The socket to listen from and send messages to.
	 */
	public ConnectionAgent(Socket socket) {
		this.socket = socket;
		try {
			// server's output stream || client's input stream
			in = new Scanner(socket.getInputStream());
			// server's input stream || client's output stream
			out = new PrintStream(socket.getOutputStream());
		} catch (IOException ioe){
			System.out.print(ioe.getMessage());
		}
	}

	/**
	 * Sends a message to the socket.
	 * @param message - The string message we wish to send.
	 */
	public void sendMessage(String message) {
		out.println(message);
		out.flush();
	}

	/**
	 * This tells whether the socket is connected or not.
	 * @return True if the socket is connected, false otherwise.
	 */
	public boolean isConnected() {
		return !this.socket.isClosed();
	}

	/**
	 * Closes this Connection Agent.
	 */
	public void close() {
		try {
			// we first close this socket
			this.socket.close();
			this.in.close();
			this.out.close();
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
		// we then notify our message source that the client is done
		closeMessageSource();
	}

	/**
	 * Our implemented method for threading. 
	 * It constantly waits on messages from the socket.
	 * Once a message is received it notifies the message listener that 
	 * a message has been received.
	 */
	public void run() {
		try {
			// while our socket is connected
			while(isConnected()){
				// This line will throw an exception if client abrupts connection.
				// we try to get a message that was sent to us
				String input = in.nextLine();
				// we notify our message listener that we received a message
				super.notifyReceipt(input);
			}
		// this occurs when there is an abrupt quit
		}catch(NoSuchElementException nsee) {
			// we simply close this connection agent down
			close();
		}
	}
}
