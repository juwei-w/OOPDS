
import java.io.*;
import java.util.*;

public class Boom extends Game{
    public static Scanner scan = new Scanner(System.in);

    // Deck
    public static ArrayList<Card> cards = getDeck();

    // Player
    public static ArrayList<Player> players = getPlayers();
    public static Player player1, player2, player3, player4;
    
    // Center
    public static HashSet<Card> startCard = new HashSet<Card>();
    public static ArrayList<Card> center = new ArrayList<Card>();
    public static HashMap<Player, Card> playedCenter = new HashMap<Player, Card>();

    // Game
    static int trickNum;
    static Player currentPlayer = null;
    static Player roundWinner = null;
    static Player winPlayer = null;

    static Card leadCard;
    static String playCard; // ------------ Player input card
    static Card playedCard = null; // ----- Valid player card played
    static boolean continueGame = true;
    static boolean below100 = true;
    static int totalScore;

    // public static void main(String[] args) {

    // }
    static void startGame() {

        new Game();
        cards = getDeck();
        players = getPlayers();
        player1 = getPlayer1();
        player2 = getPlayer2();
        player3 = getPlayer3(); 
        player4 = getPlayer4();
        
        distribute();
        play();
    }

    // ========== DISTRIBUTE ========== //
    public static void distribute() {
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
        // for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int rand = random.nextInt(99999);
            int deckSize = cards.size();
            int numWithinDeck = rand % deckSize;
            Card randomCard = cards.get(numWithinDeck);
    
            center.add(randomCard);
            startCard.add(randomCard);
    
            cards.remove(randomCard);
        // }
    }

    
    // ========== START TURN ========== //
    public static int startTurn() {
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


    // ========== CURRENT PLAYER ========== //
    public static Player currentPlayer(){
        // Find current player
        for (Player player : players) {
            if (player.turn) {
                return player;
            }
        }
        System.out.println("No current player found!");
        return null;
    }
    
    
    // ========== CHECK PLAYABLE CARD ========== //
    public static void checkPlayable() {
        if (center.size() != 0) {
            leadCard = center.get(0);
            currentPlayer.playableCard = false;
            
            // System.out.println(leadCard);
            for (Card card : currentPlayer.playerCards) {
                // System.out.println(card + " : " + leadCard);
                if (card.getSuit() == leadCard.getSuit() || card.getRank() == leadCard.getRank()) {
                    currentPlayer.playableCard = true;
                    // System.out.println("Same");
                }
            }
            // System.out.println(currentPlayer.playableCard);
            if (!currentPlayer.playableCard) {
                System.out.println(" ");
                System.out.println("# Player " + (players.indexOf(currentPlayer) + 1) + " has no playable card on hand. #");
            }
        }
    }


    // ========== DISPLAY STATUS ========== //
    public static void display() {
        
        checkPlayable();
        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println("                Trick #" + trickNum);
        System.out.println("----------------------------------------");
        System.out.println("Player1: " + player1.playerCards);
        System.out.println("Player2: " + player2.playerCards);
        System.out.println("Player3: " + player3.playerCards);
        System.out.println("Player4: " + player4.playerCards);
        System.out.println("Center : " + center + " Size: " + center.size());
        System.out.println("Deck: " + cards);
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Score  : Player1 =  " + player1.score + " | Player2 =  " + player2.score + " | Player3 =  " + player3.score + " | Player4 = " + player4.score);
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Turn   : Player" + startTurn());
        System.out.print("> ");
    }

    
    // ========== IS PLAYER CARD ========== //
    static Card isPlayerCard() {
        for (Card card : currentPlayer.playerCards) {
            if (card.toString().equals(playCard)) {
                return card;
            }
        }
        return null;
    }


    // ========== IS VALID MOVE ========== //
    static boolean isValidMove() {
        if (center.size() == 1 && startCard.contains(center.get(0))) { // ----- Has lead card as start card (Trick 1)
            leadCard = center.get(0);
            return playedCard.getSuit().equals(leadCard.getSuit()) || playedCard.getRank().equals(leadCard.getRank()); // card same suit or same rank  
        }
        else if (center.size() >= 0) { // ----- Center start from empty (Trick 2 and above)
            if (center.size() == 0) {
                leadCard = playedCard;
                return true;
            }
            else {
                leadCard = center.get(0);
                return playedCard.getSuit().equals(leadCard.getSuit()) || playedCard.getRank().equals(leadCard.getRank()); // card same suit or same rank  
            }
        }
        return false;
    }
    
    
    // ========== COMPARE RANKS ========== //
    private static int compareRanks(String rank1, String rank2) {
        int value1 = getRankValue(rank1);
        int value2 = getRankValue(rank2);

        return Integer.compare(value1, value2);
    }


    // ========== GET RANK VALUE ========== //
    private static int getRankValue(String rank) {
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
    
    
    // ========== PLAY ========== //
    public static void play() {

        while (below100) {
            trickNum = 1;
            while (continueGame) { // ---------------------------------- # End the game #
                // ========== 1 ROUND ========== //
                while (playedCenter.size() < 4 && continueGame) { // ---------- # End the round #

                    winPlayer = endGameCheck(); // --------------------- # Check end game before player plays #

                    if (continueGame) { // ----------------------------- # End other player's turn #
                        startTurn();
                        currentPlayer = currentPlayer(); // --- Find current active player

                        display();
                        playCard = scan.nextLine();
                        System.out.println("");

                        // ========== COMMANDS ========== //
                        // -------------------------- //
                        // ----- Start new game ----- //
                        // -------------------------- //
                        if (playCard.equals("s")) { 
                            System.out.println("");
                            System.out.println("      ===== Start a new game =====     ");
                            // ----- Clear ----- //
                            for (Player player : players) {
                                player.playerCards.clear();
                                player.turn = false;
                                player.score = 0;
                            }
                            center.clear();
                            playedCenter.clear();
                            startCard.clear();
                            continueGame = true;

                            // ----- New Deck ----- //
                            cards.clear();
                            new Deck();
                            distribute();
                            play();
                        }
        
                        //---------------------- //
                        // ----- Exit game ----- //
                        //---------------------- //
                        else if (playCard.equals("x")) { 
                            System.out.println("         ===== Exit Game =====         ");
                            System.exit(0);
                        }
                        
                        //--------------------------- //
                        // ----- Save Load game ----- //
                        //--------------------------- //
                        else if (playCard.equals("load")) { 
                            System.out.println("Enter file name to load: ");
                            String fileName = scan.nextLine();
                            loadGame(fileName);
                        }
                        else if (playCard.equals("save")) { 
                            System.out.println("Enter file name to save: ");
                            String fileName = scan.nextLine();
                            saveGame(fileName);
                        }
                   
                        // --------------------- //
                        // ----- Draw card ----- //
                        // --------------------- //
                        else if (playCard.equals("d")) { 
                            // ----- Deck has cards ----- //
                            if (cards.size() != 0) {
                                Card draw = drawCard();
                                System.out.println("Player" + (players.indexOf(currentPlayer) + 1) + " drawed a card " + draw + ".\n");
                                playedCard = isPlayerCard();
                                
                                // cards.clear(); // ========================================== TESTING CODE
                            }
                            // ----- Deck no cards ----- //
                            else {
                                playedCard = null;
                                System.out.print("Deck is empty ");
                                
                                if (currentPlayer.playableCard == false) { // ----- Empty deck + No playable card
                                    System.out.print("and no playable card, skips Player" + (players.indexOf(currentPlayer) + 1) + " turn.");
        
                                    // Move to the next player's turn
                                    int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
                                    Player nextPlayer = players.get(nextPlayerIndex);
                                    currentPlayer.turn = false;
                                    nextPlayer.turn = true;
                                    
                                    Card emptyCard = new Card("", "nothing");
                                    playedCenter.put(currentPlayer, emptyCard);
                                }
                                System.out.println("\n");
                            }
                        }
                        
                        // ----------------------------------------- //
                        // ----- Card played by current player ----- //
                        // ----------------------------------------- //
                        else if (playCard.equals("card")) { 
                            System.out.println("Display a card played by current player");
                        }
        
                        // ----------------------- //
                        // ----- Played Card ----- //
                        // ----------------------- //
                        else {
                            playedCard = isPlayerCard();
                            if (currentPlayer != null) {
                                // ----- Is player card ----- //
                                if (playedCard != null) {         
                                    // ----- Is valid move ----- //
                                    if (isValidMove()) {
                                        currentPlayer.playerCards.remove(playedCard); // --- Remove from player's deck
                                        center.add(playedCard);                       // --- Add to the center
                                        playedCenter.put(currentPlayer, playedCard);
            
                                        // Move to the next player's turn
                                        int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
                                        Player nextPlayer = players.get(nextPlayerIndex);
                                        currentPlayer.turn = false;
                                        nextPlayer.turn = true;
            
                                    } 
                                    else {
                                        System.out.println("Invalid move! Please play a card that follows the suit or rank of the lead card.");
                                        System.out.println("");
                                    }
                                }
                                else {
                                    if (!playCard.equals("d")) { // --- Not in player's card + not draw card
                                        System.out.println("Player doesn't have this card."); // --- Not player's card
                                        System.out.println("");
                                    }
                                }
                            }
                        }
                        
                        // ===== TESTING CODE ===== // 
                        // TEST AFTER WON GAME RESULT (CONTINUE / RESTART / EXIT)
                        // currentPlayer.playerCards.clear(); 
                    }
                }
                if (continueGame) {evaluateWinner();} // End game don't show round winner
            }
            System.out.println("*** Player" + (players.indexOf(winPlayer) + 1) + " won the game. ***\n");
            countScore();
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Score  : Player1 =  " + player1.score + " | Player2 =  " + player2.score + " | Player3 =  " + player3.score + " | Player4 = " + player4.score);
            System.out.println("---------------------------------------------------------------------");

            // ===== TESTING CODE ===== // 
            // TEST BELOW 100 SCORE LOOP AND ABOVE 100 PRINTLN
            // player1.score = 100;
            System.out.println("");
            
            lostPlayer(); // Find the highest score loser

            if (below100) {continuePlay();}

        }
    }
    

    // ========== DRAW CARD ========== //
    public static Card drawCard() {
        Random random = new Random();
        int rand = random.nextInt(99999);           // --- Random number
        int deckSize = cards.size();                // --- Deck size
        int numWithinDeck = rand % deckSize;        // --- Number within deck
        Card randomCard = cards.get(numWithinDeck); // --- Get a random card from deck

        currentPlayer.playerCards.add(randomCard);  // --- Add card into player's deck
        cards.remove(randomCard);                   // --- Remove card from the deck
        
        return randomCard;
    }

    
    // ========== EVALUATE WINNER ========== //
    public static void evaluateWinner() {
        // Card
        Card highestRankCard = center.get(0);

        // Player
        roundWinner = players.get(startTurn() - 1);

        // ========== Set highest card ========== //
        if (trickNum == 1) {
            if (center.get(0).getSuit() == leadCard.getSuit()) { // ----- Same suit
                if (highestRankCard == leadCard) {
                    highestRankCard = center.get(1);
                }
            }
        }
        System.out.println("");
        System.out.println("------------------------------");
        // ========== Get highest card ========== //
        for (Map.Entry<Player, Card> entry : playedCenter.entrySet()) {
            Player player = entry.getKey();
            Card card = entry.getValue();
            
            System.out.println("      Player " + (players.indexOf(player) + 1) + " plays " + card);

            // ---------- Dont compare own self ---------- //
            if (leadCard == card) {}
            
            // ---------- Empty card ---------- //
            else if (card.getSuit() == "nothing") {}

            // ---------- Same number no need to compare ---------- //
            else if (compareRanks(leadCard.getRank(), card.getRank()) == 0) {}

            // ---------- Same suit compare and get the highest rank ---------- //
            else if (card.getSuit().equals(leadCard.getSuit())) {                
                if (compareRanks(highestRankCard.getRank(), card.getRank()) < 0) { // ----- Card > highestRankCard
                    highestRankCard = card;
                    roundWinner = player;
                }
            }
        }

        System.out.println("------------------------------");
        System.out.println("      Highest Card: " + highestRankCard);
        System.out.println("");

        if (roundWinner != null) {
            System.out.println("*** Player " + (players.indexOf(roundWinner) + 1) + " wins Trick #" + trickNum + " ***");
            
            // ---------- Only winner active turn ---------- //
            for (Player player : players) {
                player.turn = false;
            }
            roundWinner.turn = true;
            center.clear();
            playedCenter.clear();
        } 
        else {
            System.out.println("No one won");
        }
        // trickNum ++;
    }
        

    // ========== COUNT SCORE ========== //
    public static void countScore() {
        for (Player player : players) {
            for (Card card : player.playerCards) {
                int value = 0;
                switch (card.getRank()) {
                    case "A":
                        value = 1;
                        break;
                    case "2":
                        value = 2;
                        break;
                    case "3":
                        value = 3;
                        break;
                    case "4":
                        value = 4;
                        break;
                    case "5":
                        value = 5;
                        break;
                    case "6":
                        value = 6;
                        break;
                    case "7":
                        value = 7;
                        break;
                    case "8":
                        value = 8;
                        break;
                    case "9":
                        value = 9;
                        break;
                    case "10":
                    case "J":
                    case "Q":
                    case "K":
                        value = 10;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid rank: " + card.getRank());
                }
                player.score += value;
            }
            // System.out.println("Player " + (players.indexOf(player) + 1) + " score: " + player.score);
        }
    }


    // ========== END GAME CHECK ========== //
    public static Player endGameCheck() {
        for (Player player : players) {
            if (player.playerCards.size() == 0){
                continueGame = false;
                return player;
            }
        }
        return null;
    }    
    

    // ========== LOST PLAYER ========== //
    public static void lostPlayer() { 
        int highestScore = 0;
        Player highestPlayer = null;
        for (Player player : players) {
            if (player.score > highestScore) {
                highestScore = player.score;
                highestPlayer = player;
            }
        }

        if (highestScore >= 100 && highestPlayer != null) {
            below100 = false;
            System.out.println("Player" + (players.indexOf(highestPlayer) + 1) + " has the highest score & reached 100, lost the game.\n");
        }
    }


    // ========== CONTINUE PLAY ========== //
    public static void continuePlay() {

        boolean continueInput = true;
        String input = null;

        while (continueInput) {
            System.out.println("1. Continue a new game.");
            System.out.println("2. Restart game.");
            System.out.println("3. Exit game.");
            System.out.print("> ");
            input = scan.nextLine();
            System.out.println("");


            // ----- NEW GAME ----- //
            if (input.equals("1")) {
                // ----- Clear ----- //
                for (Player player : players) {
                    player.playerCards.clear();
                    player.turn = false;
                }
                center.clear();
                playedCenter.clear();
                startCard.clear();
                cards.clear();
                continueGame = true;

                // ----- New Deck ----- //
                
                new Deck();
                distribute();

                continueInput = false;
            }
            else if (input.equals("2")) {
                System.out.println("");
                System.out.println("      ===== Start a new game =====     ");

                // ----- Clear ----- //
                for (Player player : players) {
                    player.playerCards.clear();
                    player.turn = false;
                    player.score = 0;
                }
                center.clear();
                playedCenter.clear();
                startCard.clear();
                cards.clear();
                continueGame = true;

                // ----- New Deck ----- //
                new Deck();
                distribute();

                continueInput = false;
            }
            else if (input.equals("3")) {
                System.out.println("         ===== Exit Game =====         ");
                System.exit(0);

                continueInput = false;
            }
        }
    }
    
    public static void loadGame(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String dataLine;

            while ((dataLine = bufferedReader.readLine()) != null) {
                if (dataLine.startsWith("Trick =")) {
                    trickNum = Integer.parseInt(dataLine.split(" = ")[1]);
                } else if (dataLine.startsWith("Player")) {
                    int playerIndex = Integer.parseInt(dataLine.substring(6, 7));
                    Player player = players.get(playerIndex - 1);
                    String[] cardStrings = dataLine.split(" = ")[1].split(", ");
                    player.playerCards.clear();
                    for (String cardString : cardStrings) {
                        String suit = cardString.substring(0, 1);
                        String rank = cardString.substring(1);
                        player.playerCards.add(new Card(rank, suit));
                    }
                } else if (dataLine.startsWith("Center Card")) {
                    String[] cardStrings = dataLine.split(" = ");
                    center.clear();
                    if (cardStrings.length > 1) {
                        String[] cardString = cardStrings[1].split(", ");
                        for (String cards : cardString) {
                            String suit = cards.substring(0, 1);
                            String rank = cards.substring(1);
                            Card card = new Card(rank, suit);
                            center.add(card);
                        }
                    }
                } else if (dataLine.startsWith("Map")) {
                    String[] maps = dataLine.split(" = ");
                    playedCenter.clear();
                    if (maps.length > 1) {
                        String[] map = maps[1].split(" \\| ");
                        for (String play : map) {
                            String[] playData = play.trim().split(", ");
                            int intPlayer = Integer.parseInt(playData[0].trim());
                                            
                            String cardString = playData[1];
                            String suit = cardString.substring(0, 1);
                            String rank = cardString.substring(1);
                            Card card = new Card(rank, suit);

                            playedCenter.put(players.get(intPlayer), card);
                        }
                    }
                } else if (dataLine.startsWith("Deck")) {
                    String[] cardStrings = dataLine.split(" = ")[1].split(", ");
                    cards.clear();
                    for (String cardString : cardStrings) {
                        String suit = cardString.substring(0, 1);
                        String rank = cardString.substring(1);
                        cards.add(new Card(rank, suit));
                    }
                } else if (dataLine.startsWith("Score")) {
                    int playerIndex = Integer.parseInt(dataLine.split("Score ")[1].split(" = ")[0]);
                    int score = Integer.parseInt(dataLine.split(" = ")[1]);
                    players.get(playerIndex - 1).score = score;
                }else if (dataLine.startsWith("Current Player")) {
                    int playerIndex = Integer.parseInt(dataLine.split(" = Player ")[1]);
                    // Set the turn to the current player
                    for (Player player : players) {
                        player.turn = false;
                    }
                    currentPlayer = players.get((playerIndex));
                    currentPlayer.turn = true;
                }
            }
            bufferedReader.close();
            System.out.println("Game loaded successfully.");

        }  catch (IOException e) {
            System.out.println("An error occurred while loading the game.");
            e.printStackTrace();
        }

        display();
    }

    public static void saveGame(String fileName) {

        try (FileWriter fileWriter = new FileWriter(fileName);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // save trick number
            bufferedWriter.write("Trick = " + trickNum);
            bufferedWriter.newLine();

            // save Player's hands 
            for (Player player : players) {
                bufferedWriter.write("Player" + (players.indexOf(player) +1)+ " Cards = ");
                for (Card card : player.playerCards) {
                    bufferedWriter.write(card.toString() + ", ");
                }
                bufferedWriter.newLine();
            }

            // save center
            bufferedWriter.write("Center Card = ");
            if (!center.isEmpty()) {
                for (Card card : center) {
                    bufferedWriter.write(card + ", ");
                }
            }
            bufferedWriter.newLine();

            // save playedCenter
            bufferedWriter.write("Map = ");
            if (!playedCenter.isEmpty()) {
                for (Map.Entry<Player, Card> entry : playedCenter.entrySet()) {
                    Player player = entry.getKey();
                    Card card = entry.getValue();
                    bufferedWriter.write(players.indexOf(player) + ", " + card.toString() + " | ");
                    if(center == null)
                    center = null; 
                }
            }
            bufferedWriter.newLine();

            // save deck
            bufferedWriter.write("Deck = ");
            for (Card card : cards) {
                bufferedWriter.write(card.toString() + ", ");
            }
            bufferedWriter.newLine();

            // save scores
            for (Player player : players) {
                bufferedWriter.write("Score " + (players.indexOf(player) +1) + " = " + player.score);
                bufferedWriter.newLine();
            }
                                
            // now play player
            bufferedWriter.write("Current Player = Player " + (players.indexOf(currentPlayer)));
            bufferedWriter.newLine();

            System.out.println(fileName + " is saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
        display();
    }

}
