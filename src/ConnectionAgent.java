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
	}
	
	public void sendMessage(String message) {
	    super.notifyReceipt(message);
	}
	
	public boolean isConnected() {

	}
	
	public void close() {
	    try {
            this.socket.close();
        }catch (IOException ioe){
	        System.out.println("Caught IOExcetpion in connection agent");
        }
	}


    public void messageReceived(String message, MessageSource source){

    }


    public void sourceClosed(MessageSource source){

    }


	public void run() {
		
	}
	
}
