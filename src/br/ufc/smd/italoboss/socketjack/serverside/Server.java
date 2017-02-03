package br.ufc.smd.italoboss.socketjack.serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author italo_boss
 */
public class Server extends Thread {
    
    private final ServerGUI serverGUI;

    public Server(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }
    
    @Override
    public void run() {
        try {
            int port = Integer.parseInt(serverGUI.port);
            System.out.println("Starting server...");
            ServerSocket server = new ServerSocket(port, serverGUI.maxPlayers);
            
            System.out.println("Server running at port: " + port);
            while (true) {
                Socket client = server.accept();
                System.out.println("Client connected. IP: " + client.getInetAddress().getHostAddress());
                serverGUI.numberOfPlayers++;
                new ControllerPlayer(client, serverGUI).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
