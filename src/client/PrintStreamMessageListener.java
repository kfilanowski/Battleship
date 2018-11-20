package client;

import java.io.PrintStream;

import common.MessageListener;
import common.MessageSource;

public class PrintStreamMessageListener implements MessageListener {
	private PrintStream out;
	
	public /*ctor?*/ PrintStreamMessageListener(PrintStream out) {
		
	}
	
	public void messageReceived(String message, MessageSource source) {
		
	}
	
	public void sourceClosed(MessageSource source) {
		
	}
}
