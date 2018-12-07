package server;

import common.ConnectionAgent;

import java.io.IOException;
import java.net.Socket;

/**
 * This class is a Thread that accepts clients connecting to the BattleServer's server socket.
 * This is in its own class because when calling serverSocket.accept, the program hangs. Thus,
 * we must put it in its own class and thread so that it does not hang the entire program up.
 * @author Jeriah Caplinger
 * @author Kevin Filanowski
 * @version December 2018
 */
public class ServerAcceptThread implements Runnable {
    /** A reference to our battleServer object  */
    private BattleServer battleServer;

    /**
     * Constructor for this class. It only accepts a BattleServer object for
     * future referencing
     * @param server our BattleServer object
     */
    protected ServerAcceptThread(BattleServer server){
        this.battleServer = server;
    }


    /**
     * Our Thread method. This thread constantly waits for player's joining the game.
     * It then gives that connection to the BattleServer
     */
    public void run(){
        // while the server socket is not closed
        while(!this.battleServer.getServerSocket().isClosed()){
            try {
                // we accept the client's connection
                Socket socket = this.battleServer.getServerSocket().accept();
                // We make a new connection agent for this socket
                ConnectionAgent agent = new ConnectionAgent(socket);
                // We pass the connection agent to battle server
                this.battleServer.addConnectAgent(agent);
                // We start the connection agent up as a thread
                (new Thread(agent)).start();
            }catch (IOException ioe){
                System.out.println("Caught IOException in serverAcceptThread run method");
            }
        }
    }
}
