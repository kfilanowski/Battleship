package server;

import common.ConnectionAgent;
import common.MessageListener;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerAcceptThread implements Runnable {
    private BattleServer battleServer;


    protected ServerAcceptThread(BattleServer server){
        this.battleServer = server;
    }



    public void run(){

        while(!this.battleServer.getServerSocket().isClosed()){
            try {
                Socket socket = this.battleServer.getServerSocket().accept();
                Scanner in = new Scanner(socket.getInputStream());
                String line = in.nextLine();
                // server's input stream || client's output stream
                ConnectionAgent agent = new ConnectionAgent(socket);
                (new Thread(agent)).start();
                this.battleServer.addConnectAgent(agent, line);
            }catch (IOException ioe){
                System.out.println("Caught IOException in serverAcceptThread run method");
            }
        }
    }
}
