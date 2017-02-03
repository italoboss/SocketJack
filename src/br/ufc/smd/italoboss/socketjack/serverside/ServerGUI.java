package br.ufc.smd.italoboss.socketjack.serverside;

import br.ufc.smd.italoboss.socketjack.Cards;
import br.ufc.smd.italoboss.socketjack.Player;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author italo_boss
 */
public class ServerGUI extends javax.swing.JFrame {
    
    protected final String port;
    protected int numberOfPlayers = 0;
    protected final int maxPlayers = 3;
    protected ArrayList<ControllerPlayer> controllers;
    protected Player dealer;
    /**
     * Creates new form MainGUI
     * @param port
     */
    public ServerGUI(String port) {
        this.port = port;
        initComponents();
        createComponentMap();
        initializePlayersVisibility();
        initializeCardsVisibility();
        startServer();
    }
    
    private void initializePlayersVisibility()
    {
        lbPlayer1Name.setVisible(false);    lbBetIcon1.setVisible(false);   lbBetP1.setVisible(false);
        lbPlayer2Name.setVisible(false);    lbBetIcon2.setVisible(false);   lbBetP2.setVisible(false);
        lbPlayer3Name.setVisible(false);    lbBetIcon3.setVisible(false);   lbBetP3.setVisible(false);
    }

    private void initializeCardsVisibility()
    {
        dealer_C1.setVisible(false);    dealer_C2.setVisible(false);    dealer_C3.setVisible(false);    dealer_C4.setVisible(false);    dealer_C5.setVisible(false);
        player1_C1.setVisible(false);   player1_C2.setVisible(false);   player1_C3.setVisible(false);   player1_C4.setVisible(false);   player1_C5.setVisible(false);
        player2_C1.setVisible(false);   player2_C2.setVisible(false);   player2_C3.setVisible(false);   player2_C4.setVisible(false);   player2_C5.setVisible(false);
        player3_C1.setVisible(false);   player3_C2.setVisible(false);   player3_C3.setVisible(false);   player3_C4.setVisible(false);   player3_C5.setVisible(false);
    }
    
    private void startServer()
    {
        this.controllers = new ArrayList<>();
        this.dealer = new Player(0);
        Cards.initializePack();
        Cards.CARD_INDEX = 0;
        this.setTitle(this.getTitle() + " (at port " + this.port + ")");
        new Server(this).start();
    }
    
    private void startGame() throws IOException
    {
        this.dealer = playerHit(dealer);
        for (ControllerPlayer ctrlPlayer : controllers) {
            ctrlPlayer.output.writeObject("START\n");
            ctrlPlayer.player = playerHit(ctrlPlayer.player);
        }
        this.dealer = playerHit(dealer);
        this.dealer_C2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png")));
        for (ControllerPlayer ctrlPlayer : controllers) {
            ctrlPlayer.player = playerHit(ctrlPlayer.player);
        }
    }
    
    private void turn()
    {
        boolean ready = true;
        for (ControllerPlayer ctrlPlayer : controllers) {
            ready &= ctrlPlayer.player.isReady();
        }
        if (ready) {
            String card = dealer.getHand().get(1);
            this.dealer_C2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/" + card + ".png")));
        }
        else
            JOptionPane.showMessageDialog(rootPane, "Waiting players...");
    }
    
    private void solve() throws IOException
    {
        int total = 0;
        for (ControllerPlayer ctrlPlayer : controllers) {
            Player player = ctrlPlayer.player;
            total += player.solveHand(dealer.getHand());
            JLabel label = (JLabel) getComponentByName("player" + player.getNumber() + "_Bet");
            label.setText(String.valueOf(player.getBet()));
            System.out.println("---> " + player.getBet() + "\t | " + player.getMoney());
            ctrlPlayer.output.writeObject(player);
            ctrlPlayer.output.reset();
        }
        int dealerBet = Integer.parseInt(lbBetDealer.getText());
        dealerBet -= total;
        lbBetDealer.setText(String.valueOf(dealerBet));
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            refreshGame();
        }
    }

    public void setPlayerInGame(Player player)
    {
        // player1_Name, player1_Bet, player1_BetIcon
        JLabel label = (JLabel) getComponentByName("player" + player.getNumber() + "_Name");
        label.setVisible(true);
        label.setText(player.getName());
        label = (JLabel) getComponentByName("player" + player.getNumber() + "_Bet");
        label.setVisible(true);
        player.resetBet();
        label.setText(String.valueOf(player.getBet()));
        label = (JLabel) getComponentByName("player" + player.getNumber() + "_BetIcon");
        label.setVisible(true);
        label = (JLabel) getComponentByName("player" + player.getNumber() + "_Money");
        label.setText(String.valueOf(player.getMoney()));
    }
    
    public Player playerHit(Player player)
    {
        String card = player.hitting();
        int nc = player.getHand().size();
        String strLabel = "player" + player.getNumber() + "_C" + nc;
        JLabel lbCard = (JLabel) getComponentByName(strLabel);
        lbCard.setVisible(true);
        lbCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/" + card + ".png")));
        return player;
    }
    
    private void refreshGame()
    {
        initializeCardsVisibility();
        for (ControllerPlayer ctrlPlayer : controllers) {
            Player player = ctrlPlayer.player;
            player.getHand().clear();
            player.resetBet();
            JLabel label = (JLabel) getComponentByName("player" + player.getNumber() + "_Bet");
            label.setText(String.valueOf(player.getBet()));
            label = (JLabel) getComponentByName("player" + player.getNumber() + "_Money");
            label.setText(String.valueOf(player.getMoney()));
        }
        dealer.getHand().clear();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btPlayGame = new javax.swing.JButton();
        btTurn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btEndGame = new javax.swing.JButton();
        btHit = new javax.swing.JButton();
        btSolve = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        dealer_C5 = new javax.swing.JLabel();
        dealer_C4 = new javax.swing.JLabel();
        dealer_C3 = new javax.swing.JLabel();
        dealer_C1 = new javax.swing.JLabel();
        dealer_C2 = new javax.swing.JLabel();
        lbBetIconDealer = new javax.swing.JLabel();
        lbBetDealer = new javax.swing.JLabel();
        player3_C5 = new javax.swing.JLabel();
        player3_C4 = new javax.swing.JLabel();
        player3_C3 = new javax.swing.JLabel();
        player3_C2 = new javax.swing.JLabel();
        player3_C1 = new javax.swing.JLabel();
        player2_C5 = new javax.swing.JLabel();
        player2_C4 = new javax.swing.JLabel();
        player2_C3 = new javax.swing.JLabel();
        player2_C2 = new javax.swing.JLabel();
        player2_C1 = new javax.swing.JLabel();
        player1_C5 = new javax.swing.JLabel();
        player1_C4 = new javax.swing.JLabel();
        player1_C3 = new javax.swing.JLabel();
        player1_C2 = new javax.swing.JLabel();
        player1_C1 = new javax.swing.JLabel();
        lbMoney3 = new javax.swing.JLabel();
        lbMoneyP3 = new javax.swing.JLabel();
        lbBetIcon6 = new javax.swing.JLabel();
        lbMoney2 = new javax.swing.JLabel();
        lbMoneyP2 = new javax.swing.JLabel();
        lbBetIcon5 = new javax.swing.JLabel();
        lbMoney1 = new javax.swing.JLabel();
        lbBetIcon4 = new javax.swing.JLabel();
        lbPlayer3Name = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbBetIcon3 = new javax.swing.JLabel();
        lbBetP3 = new javax.swing.JLabel();
        lbBetP2 = new javax.swing.JLabel();
        lbBetP1 = new javax.swing.JLabel();
        lbPlayer2Name = new javax.swing.JLabel();
        lbPlayer1Name = new javax.swing.JLabel();
        lbBetIcon1 = new javax.swing.JLabel();
        lbBetIcon2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lbMoneyP1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SocketJack 21");
        setName("frameHost"); // NOI18N

        jPanel3.setBackground(new java.awt.Color(82, 82, 82));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        btPlayGame.setText("PLAY!");
        btPlayGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPlayGameActionPerformed(evt);
            }
        });

        btTurn.setText("TURN");
        btTurn.setEnabled(false);
        btTurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTurnActionPerformed(evt);
            }
        });

        btEndGame.setText("END");
        btEndGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEndGameActionPerformed(evt);
            }
        });

        btHit.setText("HIT");
        btHit.setEnabled(false);
        btHit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHitActionPerformed(evt);
            }
        });

        btSolve.setText("SOLVE");
        btSolve.setEnabled(false);
        btSolve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSolveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(btPlayGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btTurn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btEndGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btHit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btSolve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btPlayGame, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btHit, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btSolve, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btEndGame, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        mainPanel.setBackground(new java.awt.Color(48, 106, 94));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dealer_C5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        dealer_C5.setName("player0_C5"); // NOI18N
        mainPanel.add(dealer_C5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, -1, -1));

        dealer_C4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        dealer_C4.setName("player0_C4"); // NOI18N
        mainPanel.add(dealer_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, -1, -1));

        dealer_C3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        dealer_C3.setName("player0_C3"); // NOI18N
        mainPanel.add(dealer_C3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, -1, -1));

        dealer_C1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        dealer_C1.setName("player0_C1"); // NOI18N
        mainPanel.add(dealer_C1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, -1, -1));

        dealer_C2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        dealer_C2.setName("player0_C2"); // NOI18N
        mainPanel.add(dealer_C2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, -1));

        lbBetIconDealer.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetIconDealer.setForeground(new java.awt.Color(255, 255, 255));
        lbBetIconDealer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbBetIconDealer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/coin.png"))); // NOI18N
        lbBetIconDealer.setName("player0_BetIcon"); // NOI18N
        mainPanel.add(lbBetIconDealer, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 90, 40, 30));

        lbBetDealer.setFont(new java.awt.Font("Haettenschweiler", 0, 22)); // NOI18N
        lbBetDealer.setForeground(new java.awt.Color(255, 255, 255));
        lbBetDealer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBetDealer.setText("100");
        lbBetDealer.setName("player0_Bet"); // NOI18N
        mainPanel.add(lbBetDealer, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 90, 50, 30));

        player3_C5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player3_C5.setName("player3_C5"); // NOI18N
        mainPanel.add(player3_C5, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 280, -1, -1));

        player3_C4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player3_C4.setName("player3_C4"); // NOI18N
        mainPanel.add(player3_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 300, -1, -1));

        player3_C3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player3_C3.setName("player3_C3"); // NOI18N
        mainPanel.add(player3_C3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 320, -1, -1));

        player3_C2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player3_C2.setName("player3_C2"); // NOI18N
        mainPanel.add(player3_C2, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 340, -1, -1));

        player3_C1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player3_C1.setName("player3_C1"); // NOI18N
        mainPanel.add(player3_C1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 360, -1, -1));

        player2_C5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player2_C5.setName("player2_C5"); // NOI18N
        mainPanel.add(player2_C5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 280, -1, -1));

        player2_C4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player2_C4.setName("player2_C4"); // NOI18N
        mainPanel.add(player2_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 300, -1, -1));

        player2_C3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player2_C3.setName("player2_C3"); // NOI18N
        mainPanel.add(player2_C3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 320, -1, -1));

        player2_C2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player2_C2.setName("player2_C2"); // NOI18N
        mainPanel.add(player2_C2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 340, -1, -1));

        player2_C1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player2_C1.setName("player2_C1"); // NOI18N
        mainPanel.add(player2_C1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 360, -1, -1));

        player1_C5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player1_C5.setName("player1_C5"); // NOI18N
        mainPanel.add(player1_C5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, -1));

        player1_C4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player1_C4.setName("player1_C4"); // NOI18N
        mainPanel.add(player1_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, -1, -1));

        player1_C3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player1_C3.setName("player1_C3"); // NOI18N
        mainPanel.add(player1_C3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, -1, -1));

        player1_C2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player1_C2.setName("player1_C2"); // NOI18N
        mainPanel.add(player1_C2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, -1));

        player1_C1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/back.png"))); // NOI18N
        player1_C1.setName("player1_C1"); // NOI18N
        mainPanel.add(player1_C1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, -1, -1));

        lbMoney3.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbMoney3.setForeground(new java.awt.Color(255, 255, 255));
        lbMoney3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMoney3.setText("Money:");
        lbMoney3.setName(""); // NOI18N
        mainPanel.add(lbMoney3, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 510, 50, 30));

        lbMoneyP3.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbMoneyP3.setForeground(new java.awt.Color(255, 255, 255));
        lbMoneyP3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMoneyP3.setText("50");
        lbMoneyP3.setName("player3_Money"); // NOI18N
        mainPanel.add(lbMoneyP3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 510, 40, 30));

        lbBetIcon6.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetIcon6.setForeground(new java.awt.Color(255, 255, 255));
        lbBetIcon6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBetIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/coin.png"))); // NOI18N
        lbBetIcon6.setName("player3_BetIcon"); // NOI18N
        mainPanel.add(lbBetIcon6, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 510, 40, 30));

        lbMoney2.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbMoney2.setForeground(new java.awt.Color(255, 255, 255));
        lbMoney2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMoney2.setText("Money:");
        lbMoney2.setName(""); // NOI18N
        mainPanel.add(lbMoney2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 510, 50, 30));

        lbMoneyP2.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbMoneyP2.setForeground(new java.awt.Color(255, 255, 255));
        lbMoneyP2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMoneyP2.setText("50");
        lbMoneyP2.setName("player2_Money"); // NOI18N
        mainPanel.add(lbMoneyP2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 510, 40, 30));

        lbBetIcon5.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetIcon5.setForeground(new java.awt.Color(255, 255, 255));
        lbBetIcon5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBetIcon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/coin.png"))); // NOI18N
        lbBetIcon5.setName("player2_BetIcon"); // NOI18N
        mainPanel.add(lbBetIcon5, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 510, 40, 30));

        lbMoney1.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbMoney1.setForeground(new java.awt.Color(255, 255, 255));
        lbMoney1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMoney1.setText("Money:");
        lbMoney1.setName(""); // NOI18N
        mainPanel.add(lbMoney1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 510, 50, 30));

        lbBetIcon4.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetIcon4.setForeground(new java.awt.Color(255, 255, 255));
        lbBetIcon4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBetIcon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/coin.png"))); // NOI18N
        lbBetIcon4.setName("player1_BetIcon"); // NOI18N
        mainPanel.add(lbBetIcon4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 510, 40, 30));

        lbPlayer3Name.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        lbPlayer3Name.setForeground(new java.awt.Color(255, 255, 255));
        lbPlayer3Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPlayer3Name.setText("Player 3");
        lbPlayer3Name.setName("player3_Name"); // NOI18N
        mainPanel.add(lbPlayer3Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 470, 230, 30));

        jLabel2.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Dealer");
        mainPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 360, 30));

        lbBetIcon3.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetIcon3.setForeground(new java.awt.Color(255, 255, 255));
        lbBetIcon3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbBetIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/coin.png"))); // NOI18N
        lbBetIcon3.setName("player3_BetIcon"); // NOI18N
        mainPanel.add(lbBetIcon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 230, 40, 30));

        lbBetP3.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetP3.setForeground(new java.awt.Color(255, 255, 255));
        lbBetP3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBetP3.setText("10");
        lbBetP3.setName("player3_Bet"); // NOI18N
        mainPanel.add(lbBetP3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 230, 50, 30));

        lbBetP2.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetP2.setForeground(new java.awt.Color(255, 255, 255));
        lbBetP2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBetP2.setText("10");
        lbBetP2.setName("player2_Bet"); // NOI18N
        mainPanel.add(lbBetP2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 50, 30));

        lbBetP1.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetP1.setForeground(new java.awt.Color(255, 255, 255));
        lbBetP1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBetP1.setText("10");
        lbBetP1.setName("player1_Bet"); // NOI18N
        mainPanel.add(lbBetP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 50, 30));

        lbPlayer2Name.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        lbPlayer2Name.setForeground(new java.awt.Color(255, 255, 255));
        lbPlayer2Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPlayer2Name.setText("Player 2");
        lbPlayer2Name.setName("player2_Name"); // NOI18N
        mainPanel.add(lbPlayer2Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 470, 230, 30));

        lbPlayer1Name.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        lbPlayer1Name.setForeground(new java.awt.Color(255, 255, 255));
        lbPlayer1Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPlayer1Name.setText("Player 1");
        lbPlayer1Name.setName("player1_Name"); // NOI18N
        mainPanel.add(lbPlayer1Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, 230, 30));

        lbBetIcon1.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetIcon1.setForeground(new java.awt.Color(255, 255, 255));
        lbBetIcon1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbBetIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/coin.png"))); // NOI18N
        lbBetIcon1.setName("player1_BetIcon"); // NOI18N
        mainPanel.add(lbBetIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 40, 30));

        lbBetIcon2.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbBetIcon2.setForeground(new java.awt.Color(255, 255, 255));
        lbBetIcon2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbBetIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/ufc/smd/italoboss/socketjack/images/coin.png"))); // NOI18N
        lbBetIcon2.setName("player2_BetIcon"); // NOI18N
        mainPanel.add(lbBetIcon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 230, 40, 30));

        jPanel1.setBackground(new java.awt.Color(48, 106, 94));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 150));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 42, 400, 140));

        lbMoneyP1.setFont(new java.awt.Font("Haettenschweiler", 0, 20)); // NOI18N
        lbMoneyP1.setForeground(new java.awt.Color(255, 255, 255));
        lbMoneyP1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMoneyP1.setText("50");
        lbMoneyP1.setName("player1_Money"); // NOI18N
        mainPanel.add(lbMoneyP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 510, 40, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1066, 617));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btPlayGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPlayGameActionPerformed
        try {
            startGame();
            btPlayGame.setEnabled(false);
            btEndGame.setEnabled(false);
            btTurn.setEnabled(true);
            btHit.setEnabled(false);
            btSolve.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btPlayGameActionPerformed

    private void btTurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTurnActionPerformed
        turn();
        btHit.setEnabled(true);
        btSolve.setEnabled(true);
    }//GEN-LAST:event_btTurnActionPerformed

    private void btEndGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEndGameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btEndGameActionPerformed

    private void btHitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHitActionPerformed
        playerHit(this.dealer);
    }//GEN-LAST:event_btHitActionPerformed

    private void btSolveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSolveActionPerformed
        try {
            solve();
            btPlayGame.setEnabled(true);
            btEndGame.setEnabled(true);
            btTurn.setEnabled(false);
            btHit.setEnabled(false);
            btSolve.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btSolveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEndGame;
    private javax.swing.JButton btHit;
    private javax.swing.JButton btPlayGame;
    private javax.swing.JButton btSolve;
    private javax.swing.JButton btTurn;
    private javax.swing.JLabel dealer_C1;
    private javax.swing.JLabel dealer_C2;
    private javax.swing.JLabel dealer_C3;
    private javax.swing.JLabel dealer_C4;
    private javax.swing.JLabel dealer_C5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbBetDealer;
    private javax.swing.JLabel lbBetIcon1;
    private javax.swing.JLabel lbBetIcon2;
    private javax.swing.JLabel lbBetIcon3;
    private javax.swing.JLabel lbBetIcon4;
    private javax.swing.JLabel lbBetIcon5;
    private javax.swing.JLabel lbBetIcon6;
    private javax.swing.JLabel lbBetIconDealer;
    private javax.swing.JLabel lbBetP1;
    private javax.swing.JLabel lbBetP2;
    private javax.swing.JLabel lbBetP3;
    private javax.swing.JLabel lbMoney1;
    private javax.swing.JLabel lbMoney2;
    private javax.swing.JLabel lbMoney3;
    private javax.swing.JLabel lbMoneyP1;
    private javax.swing.JLabel lbMoneyP2;
    private javax.swing.JLabel lbMoneyP3;
    private javax.swing.JLabel lbPlayer1Name;
    private javax.swing.JLabel lbPlayer2Name;
    private javax.swing.JLabel lbPlayer3Name;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel player1_C1;
    private javax.swing.JLabel player1_C2;
    private javax.swing.JLabel player1_C3;
    private javax.swing.JLabel player1_C4;
    private javax.swing.JLabel player1_C5;
    private javax.swing.JLabel player2_C1;
    private javax.swing.JLabel player2_C2;
    private javax.swing.JLabel player2_C3;
    private javax.swing.JLabel player2_C4;
    private javax.swing.JLabel player2_C5;
    private javax.swing.JLabel player3_C1;
    private javax.swing.JLabel player3_C2;
    private javax.swing.JLabel player3_C3;
    private javax.swing.JLabel player3_C4;
    private javax.swing.JLabel player3_C5;
    // End of variables declaration//GEN-END:variables

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
    
    private HashMap componentMap;
    
    private void createComponentMap() {
        componentMap = new HashMap<>();
        Component[] components = mainPanel.getComponents();
        for (Component component : components) 
            componentMap.put(component.getName(), component);
    }

    public Component getComponentByName(String name) {
        return componentMap.containsKey(name) ? (Component) componentMap.get(name) : null;
    }

}