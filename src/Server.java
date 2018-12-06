import java.net.*;
import java.io.*;

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSock = null;
        Socket sock = null;

        try {
            serverSock = new ServerSocket(7887);
            System.out.println("Server Running...");
            while (true) {
                sock = serverSock.accept();
                new Thread(new ServerThingy(sock)).start(); // We send each client to a new thread.
            }
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}