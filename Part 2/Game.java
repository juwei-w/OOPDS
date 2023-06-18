import java.util.ArrayList;
// import java.util.HashSet;

public class Game extends Deck{
    // Deck
    public static ArrayList<Card> cards;

    // Player
    private static ArrayList<Player> players;
    private static Player player1, player2, player3, player4;
    
        public Game() {
            super(); // -------------- Create a deck (Inheritance)
            cards = getCards(); // --- Deck class function

            players = new ArrayList<Player>();

            player1 = new Player();
            players.add(player1);

            player2 = new Player();
            players.add(player2);

            player3 = new Player();
            players.add(player3);

            player4 = new Player();
            players.add(player4);

        }

        public static ArrayList<Card> getDeck() {
            return cards;
        }

        public static ArrayList<Player> getPlayers() {
            return players;
        }

        public static Player getPlayer1() {
            return player1;
        }

        public static Player getPlayer2() {
            return player2;
        }

        public static Player getPlayer3() {
            return player3;
        }

        public static Player getPlayer4() {
            return player4;
        }
    }
