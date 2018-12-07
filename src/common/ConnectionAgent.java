package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionAgent extends MessageSource implements Runnable {
	/**  **/
	private Socket socket;
	/**  **/
	private Scanner in;
	/**  **/
	private PrintStream out;
	/**  **/
	private Thread thread;
	
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
		// // we run this object as a thread
		// thread = new Thread(this);
		// thread.start();
		
	}
	
	public void sendMessage(String message) {
		out.println(message);
		out.flush();
	}

	public boolean isConnected() {
		return this.socket.isConnected();
	}

	public Socket getSocket() {
		return socket;
	}
	
	public void close() {
		try {
			this.socket.close();
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
		closeMessageSource();
	}
	
	public void run() {
		while(isConnected()){
			String input = in.nextLine(); // This line will throw an exception if client abrupts connection.
			super.notifyReceipt(input);
		}
	}
}
