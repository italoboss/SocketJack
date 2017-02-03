package br.ufc.smd.italoboss.socketjack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author italo_boss
 */
public class Player implements Serializable {
    
    private String name;
    private int number;
    private int bet = 10;
    private int money = 50;
    private int defaultBet;
    private ArrayList<String> hand = new ArrayList<>();
    private boolean ready = false;
    private boolean finished = false;

    public Player(int number) {
        this.number = number;
    }
    
    public Player(int number, int bet) {
        this.number = number;
        this.bet = bet;
    }
    
    public Player(String name, int money, int defaultBet) {
        this.name = name;
        this.money = money;
        this.defaultBet = defaultBet;
        this.bet = defaultBet;
    }
    
    public String hitting() {
        String card = Cards.PACK.get(Cards.CARD_INDEX);
        hand.add(card);
        Cards.CARD_INDEX++;
        return card;
    }
    
    public void standing() {
        this.ready = true;
    }
    
    public String doublingDown() {
        this.bet *= 2;
        standing();
        return hitting();
    }
    
    public int solveHand(ArrayList<String> dealerHand) 
    {
        int myTotal = Cards.sumCardsValue(this.hand);
        int dealerTotal = Cards.sumCardsValue(dealerHand);
        int initialBet = this.bet;
        if (dealerTotal > 21) {
            this.bet *= 2;
        }
        else if (myTotal > 21) {
            this.bet = 0;
        }
        else if (dealerTotal == myTotal) {
            // do nothing
        }
        else if (dealerTotal == 21 && dealerHand.size() == 2) {
            this.bet = 0;
        }
        else if (dealerTotal > myTotal) {
            this.bet = 0;
        }
        else if (myTotal == 21 && this.hand.size() == 2) {
            this.bet += (this.bet * 1.5);
        }
        else {
            this.bet *= 2;
        }
        setMoney(this.money + this.bet);
        return this.bet - initialBet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public ArrayList<String> getHand() {
        return hand;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }

    public void resetBet()
    {
        this.bet = this.defaultBet;
        setMoney(this.money - this.bet);
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
    
    public int getBet() {
        return bet;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getDefaultBet() {
        return defaultBet;
    }

    public void setDefaultBet(int defaultBet) {
        this.defaultBet = defaultBet;
        this.bet = defaultBet;
    }
    
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isReady() {
        return ready;
    }
    
    public static final String HIT = "HITTING";
    public static final String STAND = "STANDING";
    public static final String DOUBLE = "DOUBLING_DOWN";
}
