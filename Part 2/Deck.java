import java.util.ArrayList;
// import java.util.Set;
// import java.util.HashSet;

public class Deck {
    private static ArrayList<Card> cards = new ArrayList<Card>();

    // Create a deck of cards
    public Deck() {
        String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] suits = { "c", "d", "h", "s" };

        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(rank, suit);
                cards.add(card);
            }
        }
    }

    // Return full deck cards
    public static ArrayList<Card> getCards() {
        return cards;
    }
}
