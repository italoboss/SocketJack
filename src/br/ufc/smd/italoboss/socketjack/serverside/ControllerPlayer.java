package br.ufc.smd.italoboss.socketjack.serverside;

import br.ufc.smd.italoboss.socketjack.Player;
import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author italo_boss
 */
public class ControllerPlayer extends Thread
{
    private final Socket client;
    private ServerGUI serverGUI;
    
    protected Player player;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    public ControllerPlayer(Socket client) {
        this.client = client;
    }

    public ControllerPlayer(Socket client, ServerGUI gui) {
        this.client = client;
        this.serverGUI = gui;
    }

    @Override
    public void run() {
        try {
            this.player = new Player(serverGUI.getNumberOfPlayers());
            serverGUI.controllers.add(this);
            System.out.println(" - In game!");
            
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(player);
            
            input = new ObjectInputStream(client.getInputStream());
            player = (Player) input.readObject();
            serverGUI.setPlayerInGame(player);
            
            String message = String.valueOf(input.readObject());
            while (!"END".equals(message)) {
                System.out.println(" -> OK! PLAYER " + player.getNumber() + ": " + message);
                processMessage(message);
//                output.writeObject(action.trim() + "\n");
                message = String.valueOf(input.readObject());
            }
            input.close();
            output.close();
            client.close();
        } 
        catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(ControllerPlayer.class.getName()).log(Level.SEVERE, null, e);
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(ControllerPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void processMessage(String message)
    {
        // Label name example: 'player1_C1'
        String card, strLabel;
        int nc;
        JLabel lbCard;
        switch(message.trim()) {
            case Player.HIT : 
                card = player.hitting();
                nc = player.getHand().size();
                strLabel = "player" + player.getNumber() + "_C" + nc;
                lbCard = (JLabel) serverGUI.getComponentByName(strLabel);
                lbCard.setVisible(true);
                lbCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/" 
                        + card + ".png")));
                break;
                
            case Player.STAND : 
                player.standing();
                break;
                
            case Player.DOUBLE : 
                card = player.doublingDown();
                nc = player.getHand().size();
                strLabel = "player" + player.getNumber() + "_C" + nc;
                lbCard = (JLabel) serverGUI.getComponentByName(strLabel);
                lbCard.setVisible(true);
                lbCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/" 
                        + card + ".png")));
                break;
        }
    }
    
    public static final String START = "START";
}
