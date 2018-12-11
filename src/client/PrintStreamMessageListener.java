package client;

import java.io.PrintStream;
import common.MessageListener;
import common.MessageSource;

/**
 * This object controls the output of the results. We can utput the
 * messages recieved from the server to any type of PrintStream object.
 * 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version December 2018
 */
public class PrintStreamMessageListener implements MessageListener {
	/** The output stream to display the message. */
	private PrintStream out;
	
	/**
	 * Constructor for a PrintStreamMessageListener.
	 * @param out - The PrintStream to output results to.
	 */
	public PrintStreamMessageListener(PrintStream out) {
		this.out = out;
	}
	
	/**
	 * This method is called when a message is received from its subject.
	 * 
	 * @param message - The message recieved from the MessageSource.
	 * @param source  - The source of the message that it came from.
	 */
	public void messageReceived(String message, MessageSource source) {
		out.println(message);
	}
	
	/**
	 * Closes this printstream and prints a useful message to the client
	 * @param source - The MessageSource.
	 */
	public void sourceClosed(MessageSource source) {
		this.out.close();
	}
}
