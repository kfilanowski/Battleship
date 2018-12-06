import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
     public static void main(String[] args) {
     try {
        System.out.println("Client attempting to connect..");
        Socket sock = new Socket("localhost", 7887); // Connect to server
        PrintWriter writer = new PrintWriter(sock.getOutputStream());
            Scanner in = new Scanner(System.in);
            String text;
        while (true) {
            text = in.nextLine();
            writer.println(text); // Write simple messages to server.
            writer.flush();
        }
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
     }
 }   
}