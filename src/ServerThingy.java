import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThingy implements Runnable {
    
    Socket sock;
    Scanner in;

    public ServerThingy(Socket sock) {
        this.sock = sock;
        try {
        in = new Scanner(sock.getInputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() { // The actual processing of information. We'll just print out what clients send.
        String text;
        while (true) {
            text = in.nextLine();    
            System.out.println("Server Thingy Received: " + text);
        }
    }
}