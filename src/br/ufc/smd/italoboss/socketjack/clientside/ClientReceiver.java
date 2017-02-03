package br.ufc.smd.italoboss.socketjack.clientside;

import br.ufc.smd.italoboss.socketjack.Player;
import br.ufc.smd.italoboss.socketjack.serverside.ControllerPlayer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author italo_boss
 */
public class ClientReceiver extends Thread {
    
    private final ClientGUI clientGUI;
    protected boolean running = true;

    public ClientReceiver(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    @Override
    public void run() {
        try {
            System.out.println("Trying to connect...");
            int port = Integer.parseInt(clientGUI.port);
            clientGUI.socket = new Socket(clientGUI.address, port);
            System.out.println("Connected!");
            clientGUI.input = new ObjectInputStream(clientGUI.socket.getInputStream());
            Player me = (Player) clientGUI.input.readObject();
            me.setName(clientGUI.me.getName());
            me.setMoney(clientGUI.me.getMoney());
            me.setDefaultBet(clientGUI.me.getDefaultBet());
            clientGUI.me = me;
            clientGUI.writePlayerName("PLAYER: " + clientGUI.me.getName());
            clientGUI.writeServerMsg("IN GAME!");
            clientGUI.output = new ObjectOutputStream(clientGUI.socket.getOutputStream());
            clientGUI.output.writeObject(clientGUI.me);
            
            while (this.running) 
            {
                processMessage(clientGUI.input.readObject());
            }
        } 
        catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void processMessage(Object input) 
    {
        
        if (input instanceof Player) {
            Player p = (Player) input;
            String msg = "RESULT: ";
            if (p.getBet() > this.clientGUI.me.getBet())
                msg += "YOU WIN " + p.getBet() + "!";
            else
                msg += "YOU LOSE " + this.clientGUI.me.getBet() + "!";
            this.clientGUI.writeServerMsg(msg);
        }
        else {
            String message = String.valueOf(input);
            switch (message.trim()) 
            {
                case ControllerPlayer.START:
                    this.clientGUI.enableButtons(true);
                    this.clientGUI.writeServerMsg("STARTED! GOOD LUCK!");
                    break;
            }
        }
    }
    
}
