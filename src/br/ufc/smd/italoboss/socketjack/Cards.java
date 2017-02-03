package br.ufc.smd.italoboss.socketjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author italo_boss
 */
public class Cards {
    
    private static final List<String> DECK = Arrays.asList(
        "E_2", "E_3", "E_4", "E_5", "E_6", "E_7", "E_8", "E_9", "E_10", "E_J", "E_Q", "E_K", "E_A",
        "P_2", "P_3", "P_4", "P_5", "P_6", "P_7", "P_8", "P_9", "P_10", "P_J", "P_Q", "P_K", "P_A",
        "O_2", "O_3", "O_4", "O_5", "O_6", "O_7", "O_8", "O_9", "O_10", "O_J", "O_Q", "O_K", "O_A",
        "C_2", "C_3", "C_4", "C_5", "C_6", "C_7", "C_8", "C_9", "C_10", "C_J", "C_Q", "C_K", "C_A"
    );
    private static final int NUMBER_OF_DECKS = 3;
    
    public static final ArrayList<String> PACK = new ArrayList<String>();
    public static int CARD_INDEX = 0;
    public static final int NUMBER_OF_CARDS = DECK.size() * NUMBER_OF_DECKS;
    
    public static void initializePack()
    {
        for (int i = 0; i < NUMBER_OF_DECKS; i++) {
            PACK.addAll(DECK);
        }
        Collections.shuffle(PACK);
        System.out.print("PACK (" + PACK.size() + ") = { ");
        for (String string : PACK) {
            System.out.print(string + ", ");
        }
        System.out.println("}");
    }
    
    public static int sumCardsValue(ArrayList<String> hand)
    {
        int sum = 0, countAce = 0;
        for (String card : hand) {
            sum += getCardValue(card);
            if (card.endsWith("A")) countAce++;
        }
        if (countAce > 0) {
            for (int i = 0; i < countAce; i++) {
                if (sum <= 11) sum += 10;
            }
        }
        return sum;
    }
    
    private static int getCardValue(String card)
    {
        int value = 0;
        card = card.substring(2);
        if (card.equals("J") || card.equals("Q") || card.equals("K"))
            value = 10;
        else if (card.equals("A"))
            value = 1;
        else
            value = Integer.parseInt(card);
        return value;
    }
    
}
