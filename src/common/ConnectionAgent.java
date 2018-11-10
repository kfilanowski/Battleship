package common;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionAgent {
	/**  **/
	private Socket socket;
	/**  **/
	private Scanner in;
	/**  **/
	private PrintStream out;
	/**  **/
	private Thread thread;
	
	public ConnectionAgent(Socket socket) {
		
	}
	
	public void sendMessage(String message) {
		
	}
	
	public boolean isConnected() {
		return false;
	}
	
	public void close() {
		
	}
	
	public void run() {
		
	}
	
}
