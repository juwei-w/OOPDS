import java.util.ArrayList;
import java.util.LinkedList;
// import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Boom {
    Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Game game = new Game();

        game.distribute();
        game.play();
        // game.evaluateWinner();

    }
}

class Player {
    protected List<Card> playerCards = new ArrayList<Card>();
    protected boolean turn = false;
    protected int score = 0;

    // Return player cards
    public List<Card> getPlayerCards() {
        return playerCards;
    }
}

class Game {
    // Deck
    private Deck deck;
    private List<Card> cards;

    // Player
    private List<Player> players;
    private Player player1, player2, player3, player4;

    // Center
    private List<Card> startCard = new ArrayList<Card>();
    private List<Card> center = new LinkedList<Card>();

    // Game
    boolean continueGame = true;

    public Game() {
        deck = new Deck();
        cards = deck.getCards();

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


    public void distribute() {
        for (int i = 0; i < 7; ++i) {
            for (Player player : players) {
                // Random
                Random random = new Random();
                int rand = random.nextInt(99999);

                // Deck size
                int deckSize = cards.size();

                // Number within deck
                int numWithinDeck = rand % deckSize;

                // Get a random card from deck
                Card randomCard = cards.get(numWithinDeck);

                // Add card into player's deck
                player.playerCards.add(randomCard);

                // Remove card from the deck
                cards.remove(randomCard);
            }
        }
        Random random = new Random();
        int rand = random.nextInt(99999);
        int deckSize = cards.size();
        int numWithinDeck = rand % deckSize;
        Card randomCard = cards.get(numWithinDeck);

        center.add(randomCard);
        startCard.add(randomCard);

        cards.remove(randomCard);
    }


    public int startTurn() {
        if (center.size() == 1 && startCard.contains(center.get(0))) { // ----- Determine first player in Trick 1
            if (startCard.toString().contains("A") || startCard.toString().contains("5") || startCard.toString().contains("9") || startCard.toString().contains("K")) {
                player1.turn = true;
            } else if (startCard.toString().contains("2") || startCard.toString().contains("6") || startCard.toString().contains("10")) {
                player2.turn = true;
            } else if (startCard.toString().contains("3") || startCard.toString().contains("7") || startCard.toString().contains("J")) {
                player3.turn = true;
            } else if (startCard.toString().contains("4") || startCard.toString().contains("8") || startCard.toString().contains("Q")) {
                player4.turn = true;
            }
        }
        for (Player player : players) {
            if (player.turn) {
                return (players.indexOf(player) + 1);
            }
        }
        return 0;
    }


    public Player endGameCheck() {
        for (Player player : players) {
            if (player.score >= 100){
                continueGame = false;
                return player;
            }
        }
        return null;
    }


    public void play() {
        Scanner scan = new Scanner(System.in);
        Player currentPlayer = null;
        Player LostPlayer = null;
        String playCard; // ----- Player input card
        Card playedCard = null; // ----- Valid player card played
                
        int trickNum = 1;
        while (continueGame) {
            // ========== 1 ROUND PLAY ========== //
            while ((trickNum == 1 && center.size() < 5) || (trickNum != 1 && center.size() < 4)) {
                System.out.println("Trick #" + trickNum);
                System.out.println("Player1: " + player1.playerCards);
                System.out.println("Player2: " + player2.playerCards);
                System.out.println("Player3: " + player3.playerCards);
                System.out.println("Player4: " + player4.playerCards);
                System.out.println("Center : " + center + " Size: " + center.size());
                System.out.println("Deck: " + cards);
                System.out.println("Score  : Player1 =  " + player1.score + " | Player2 =  " + player2.score + " | Player3 =  " + player3.score + " | Player4 = " + player4.score);
                System.out.println("Turn   : Player" + startTurn());
                System.out.print("> ");

                playCard = scan.nextLine();
                currentPlayer = currentPlayer(); // Find current active player
                if (playCard.equals("s")) { // ----- Start new game
                    System.out.println("");
                    System.out.println("===== Start a new game =====");
                    Game game = new Game();
                    game.distribute();
                    game.play();            
                }
                else if (playCard.equals("x")) { // ----- Exit game
                    System.out.println("===== Exit Game =====");
                    System.exit(0);
                }
                else if (playCard.equals("d")) { // Draw card
                    System.out.println("Player draws a card");
                }
                else if (playCard.equals("card")) { // Card played by current player
                    System.out.println("Display a card played by current player");
                }
                else {
                    playedCard = isPlayerCard(currentPlayer, playCard);
                }
                
                System.out.println();
                            
                if (currentPlayer != null) {
                    if (playedCard != null) {                             // ---------- Played card is one of player's card
                        if (isValidMove(playedCard)) {                    // ---------- Played card is valid move
                            currentPlayer.playerCards.remove(playedCard); // ---------- Remove from player's deck
                            center.add(playedCard);                       // ---------- Add to the center

                            // Move to the next player's turn
                            int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
                            Player nextPlayer = players.get(nextPlayerIndex);
                            currentPlayer.turn = false;
                            nextPlayer.turn = true;
                        } else {
                            System.out.println("Invalid move! Please play a card that follows the suit or rank of the lead card.");
                        }
                    } else {
                        System.out.println("Player does not have this card.");
                    }
                }
            }

            // ========== WINNING CHECK ========== //
            Player winner = evaluateWinner(trickNum);

            if (winner != null) {
                winner.score++;
                System.out.println("*** Player " + (players.indexOf(winner) + 1) + " wins Trick #" + trickNum + " ***");
                System.out.println("");
                
                // ---------- Only winner active turn ---------- //
                for (Player player : players) {
                    player.turn = false;
                }
                winner.turn = true;
                center.clear();
            } 
            else {
                System.out.println("No one won");
            }

            // ========== CONTINUE GAME CHECK ========== //
            LostPlayer = endGameCheck();
            trickNum ++;
        }
        System.out.println("Player " + (players.indexOf(LostPlayer) + 1) + " lost the game.");
        scan.close();
    }


    public Player currentPlayer(){
        // Find current player
        for (Player player : players) {
            if (player.turn) {
                return player;
            }
        }
        System.out.println("No current player found!");
        return null;
    }


    private Card isPlayerCard(Player player, String playedCard) {
        for (Card card : player.playerCards) {
            if (card.toString().equals(playedCard)) {
                return card;
            }
        }
        System.out.println("Player does not have this card!");
        return null;
    }


    private boolean isValidMove(Card card) {
        Card leadCard;

        if (center.size() == 1 && startCard.contains(center.get(0))) { // ----- Has lead card as start card (Trick 1)
            leadCard = center.get(0);
            return card.getSuit().equals(leadCard.getSuit()) || card.getRank().equals(leadCard.getRank()); // card same suit or same rank  
        }
        else if (center.size() >= 0) { // ----- Center start from empty (Trick 2 and above)
            if (center.size() == 0) {
                leadCard = card;
                return true;
            }
            else {
                leadCard = center.get(0);
                return card.getSuit().equals(leadCard.getSuit()) || card.getRank().equals(leadCard.getRank()); // card same suit or same rank  
            }
        }
        return false;
    }


    protected Player evaluateWinner(int trickNum) {
        // Card
        Card leadCard = center.get(0);
        Card highestRankCard = leadCard;
        int setHighest = 0;

        // Player
        int startPlayerIndex = startTurn() - 1;
        Player winner = null;
        
        
        // ----- Change highest card to the first played card in center ----- // (Only get the highest card among players)
        if (trickNum == 1) { // ----- Prevent Trick 2 all same rank scenario getting wrong winner
            while (highestRankCard == leadCard) {
                highestRankCard = center.get(setHighest);
                setHighest++;
            }
        }

        // System.out.println(center);
        // System.out.println("Start Player: " + startTurn());
        // System.out.println("Highest Card: " + highestRankCard);
        // System.out.println("");
        
        
        for (Card card : center) {
            // System.out.println("Lead Card: " + leadCard + " Center " + (center.indexOf(card) + 1) + ": " + card);
            // ---------- Dont compare own self ---------- //
            if (leadCard == card) {
                // System.out.println("Lead Card");
            }
            // ---------- Same number no need to compare ---------- //
            else if (compareRanks(leadCard.getRank(), card.getRank()) == 0) {
                // System.out.println("No Need Compare");
            }
            // ---------- Same suit compare and get the highest rank ---------- //
            else if (card.getSuit().equals(leadCard.getSuit())) {
                // System.out.println(highestRankCard + " & " + card + " Compare");
                
                if (compareRanks(highestRankCard.getRank(), card.getRank()) < 0) { // ----- Card > highestRankCard
                    highestRankCard = card;
                }
                // System.out.println("Highest Card: " + highestRankCard);
            }
            // System.out.println("");
        }

        // ---------- Trick 1 with no higher rank ---------- //
        if (center.size() == 5 && highestRankCard == center.get(0)) {
            winner = players.get(startPlayerIndex); // First to play player wins
        }
        // ---------- Trick 1 ---------- //
        else if (center.size() == 5) { 
            center.remove(0); // ----------------------------- Center size = 4
            int cardIndex = center.indexOf(highestRankCard);
            // System.out.println(cardIndex + " + " + startPlayerIndex + " % 4");
            winner = players.get((cardIndex + startPlayerIndex) % 4);
        }
        // ---------- Trick 2 and above ---------- //
        else {
            int cardIndex = center.indexOf(highestRankCard);
            winner = players.get((cardIndex + startPlayerIndex) % 4);
        }
        return winner; // Return player index (0 - 3)
    }


    private int compareRanks(String rank1, String rank2) {
        int value1 = getRankValue(rank1);
        //System.out.println("J" + value1);

        int value2 = getRankValue(rank2);
        //System.out.println("J" + value2);
        return Integer.compare(value1, value2);
    }


    private int getRankValue(String rank) {
        switch (rank) {
            case "A":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "10":
                return 10;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            default:
                throw new IllegalArgumentException("Invalid rank: " + rank);
        }
    }


}

class Card {
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public String toString() {
        return suit + rank;
    }
}

class Deck {
    private List<Card> cards;

    // Create a deck of cards
    public Deck() {
        cards = new ArrayList<Card>();
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
    public List<Card> getCards() {
        return cards;
    }
}
