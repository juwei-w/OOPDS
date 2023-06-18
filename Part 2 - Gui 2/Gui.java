
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.io.File;

public class Gui extends Application {

    Stage window;
    Label welcome, playerTurn;
    Button playButton, playCard, drawCard, restart, save, load, exit;
    Scene main_menu, game;
    HBox[] player_cards;
    HBox center_card, up_button, down_button;
    ChoiceBox<Card> choiceBox;

    public static void main (String[] args) {
        new Game();
        Boom.cards = Boom.getDeck();
        Boom.players = Boom.getPlayers();
        Boom.player1 = Boom.getPlayer1();
        Boom.player2 = Boom.getPlayer2();
        Boom.player3 = Boom.getPlayer3(); 
        Boom.player4 = Boom.getPlayer4();
        Boom.trickNum = 1;

        Boom.distribute();
        Boom.startTurn();
        Boom.currentPlayer = Boom.currentPlayer();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        window = primaryStage;

        // --- Title --- //
        window.setTitle("Go Boom !");

        
        // ============================== Game Scene ============================== //

        // --- Content --- //
        playerTurn = new Label("Player " + (Boom.players.indexOf(Boom.currentPlayer) + 1) + " turn");
        choiceBox = new ChoiceBox<Card>();

        playCard = new Button("Play Card");
        playCard.setOnAction(e -> {
            getChoice(choiceBox);
        });
        
        drawCard = new Button("Draw Card");
        drawCard.setOnAction(e -> {
            System.out.println("\n");
            Boom.drawCard();
            Boom.display();
            updateChoiceBox();
            updateGameState();
        });
        
        restart = new Button("Restart");
        restart.setOnAction(e -> restartGame());
        
        save = new Button("Save Game");    
        save.setOnAction(e -> {
            String filename = SaveBox.display();
            Boom.saveGame(filename);
        });
        
        load = new Button("Load Game");    
        load.setOnAction(e -> {
            String filename = SaveBox.display();
            File file = new File(filename);
            if (file.exists()) {
                Boom.loadGame(filename);
                updateGameState();
            }
            else {
                System.out.println("File Not Found.");
            }
        });

        exit = new Button("Exit Game");
        exit.setOnAction(e -> { 
            boolean result = ExitBox.display("Exit Game", "Are you sure you want to exit game?");
            if (result) {window.close();}
        });

        // --- Layout --- //
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);

        // Player cards
        player_cards = new HBox[4];
        for (int i = 0; i < 4; i++) {
            player_cards[i] = new HBox(10);
            player_cards[i].setPadding(new Insets(10));
            player_cards[i].setAlignment(Pos.CENTER_LEFT);
            grid.add(new Label("Player " + (i + 1) + " : "), 0, i, 1, 1);
            updateOnHand(i);
            grid.add(player_cards[i], 1, i, 1, 1);
        }
        
        // Center card
        center_card = new HBox(10);
        center_card.setPadding(new Insets(10));
        center_card.setAlignment(Pos.CENTER_LEFT);
        grid.add(new Label("Center   : "), 0, 4);
        updateCenter();
        grid.add(center_card, 1, 4);

        // Player turn & message
        playerTurn.setAlignment(Pos.CENTER);
        grid.add(playerTurn, 1, 5);

        // Play or Draw 
        up_button = new HBox(10);
        up_button.setAlignment(Pos.CENTER);
        up_button.getChildren().addAll(choiceBox, playCard, drawCard);
        grid.add(up_button, 0, 6, 2, 1);

        // Restart Save Exit
        down_button = new HBox(10);
        down_button.setAlignment(Pos.CENTER);
        down_button.getChildren().addAll(restart, save, load, exit);
        grid.add(down_button, 0, 7, 2, 1);
        

        // --- Scene --- //
        Scene game = new Scene(grid, 400, 400);


        // ============================== Main Menu Scene ============================== //

        // Content
        welcome = new Label("Welcome to Go Boom !");
        playButton = new Button("Play");
        playButton.setOnAction(e -> {
            updateChoiceBox();
            window.setScene(game);
            Boom.display();
        });

        // Layout
        VBox main_layout = new VBox(10);
        main_layout.setAlignment(Pos.CENTER);
        main_layout.getChildren().addAll(welcome, playButton);

        // Scene
        Scene main_menu = new Scene(main_layout, 300, 250);


        window.setScene(main_menu);
        window.show();
    }

    // ============================== GUI Functions ============================== //

    public void newGame(String command) {
        // ----- Clear ----- //
        if (command == "continue") {
            for (Player player : Boom.players) {
                player.playerCards.clear();
                player.turn = false;
            }
        }
        else if (command == "restart") {
            for (Player player : Boom.players) {
                player.playerCards.clear();
                player.turn = false;
                player.score = 0;
            } 
            System.out.println("\n      ===== Start a new game =====     ");
        }

        Boom.center.clear();
        Boom.playedCenter.clear();
        Boom.startCard.clear();
        Boom.continueGame = true;

        // ----- New Deck ----- //
        Boom.cards.clear();
        new Deck();
        Boom.distribute();

        // ----- Player turn ----- //
        Boom.startTurn();
        Boom.currentPlayer = Boom.currentPlayer();
    }

    public void restartGame() {
        // ----- Clear ----- //
        for (Player player : Boom.players) {
            player.playerCards.clear();
            player.turn = false;
            player.score = 0;
        }
        Boom.center.clear();
        Boom.playedCenter.clear();
        Boom.startCard.clear();
        Boom.continueGame = true;

        // ----- New Deck ----- //
        Boom.cards.clear();
        new Deck();
        Boom.distribute();

        // ----- Player turn ----- //
        Boom.startTurn();
        Boom.currentPlayer = Boom.currentPlayer();

        System.out.println("\n\n      ===== Start a new game =====     ");

        Boom.display();
        updateGameState();
    }

    public void updatePlayerTurn() {
        playerTurn.setText("Player " + (Boom.players.indexOf(Boom.currentPlayer) + 1) + " turn");
    }

    public void updateOnHand(int playerIndex) {
            Player player = Boom.players.get(playerIndex);
            for (Card card : player.playerCards) {
                HBox player_card = player_cards[playerIndex];
                Label cardLabel = new Label(card.toString());
                player_card.getChildren().add(cardLabel);
            }
    }

    public void updateCenter() {
        center_card.getChildren().clear();
        for (Card card : Boom.center) {
            Label cardLabel = new Label(card.toString());
            center_card.getChildren().add(cardLabel);
        }
    }

    public void updateGameState() {
        // Clears
        for (HBox player : player_cards) {
            player.getChildren().clear();
        }

        // Updates
        for (int i = 0; i < 4; i++) {
            updateOnHand(i);
        }
        updatePlayerTurn();
        updateCenter();
        updateChoiceBox();
    }

    public void updateChoiceBox() {
        choiceBox.getItems().clear();
        for (Card card : Boom.currentPlayer.playerCards) {
            choiceBox.getItems().add(card);
        }
        choiceBox.setValue(Boom.currentPlayer.playerCards.get(0));
    }

    public void getChoice(ChoiceBox<Card> choiceBox) {
        Card card = choiceBox.getValue();
        System.out.print(card + "\n");
        Boom.playCard = card.toString();
        Boom.playedCard = Boom.isPlayerCard();
        if (Boom.playedCard != null) {         
            // ----- Is valid move ----- //
            if (Boom.isValidMove()) {
                Boom.currentPlayer.playerCards.remove(card); // --- Remove from player's deck
                Boom.center.add(card);                       // --- Add to the center
                Boom.playedCenter.put(Boom.currentPlayer, card);
                // Boom.currentPlayer.playerCards.clear();

                // --- Continue Game --- //
                if (Boom.currentPlayer.playerCards.isEmpty()) {
                    System.out.println("");
                    System.out.println("*** Player" + (Boom.players.indexOf(Boom.currentPlayer) + 1) + " won the game. ***\n");
                    Boom.countScore();
                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Score  : Player1 =  " + Boom.player1.score + " | Player2 =  " + Boom.player2.score + " | Player3 =  " + Boom.player3.score + " | Player4 = " + Boom.player4.score);
                    System.out.println("---------------------------------------------------------------------");
                    
                    String answer = WinGameBox.display(Boom.players, "Won Game !", "Player " + (Boom.players.indexOf(Boom.currentPlayer) + 1) + " won the game ! ! !");
                    if (answer == "continue") {
                        newGame(answer);
                    }
                    else if (answer == "restart") {
                        newGame(answer);
                    }
                    else if (answer == "exit") {
                        System.out.println("         ===== Exit Game =====         \n");
                        window.close();
                    }
                }
                // --- Move to next player --- //
                else {
                    int nextPlayerIndex = (Boom.players.indexOf(Boom.currentPlayer) + 1) % Boom.players.size();
                    Player nextPlayer = Boom.players.get(nextPlayerIndex);
                    Boom.currentPlayer.turn = false;
                    nextPlayer.turn = true;
                    Boom.currentPlayer = nextPlayer;
                }
            } 
            else {
                System.out.println("");
                System.out.println("Invalid move! Please play a card that follows the suit or rank of the lead card.");
                MessageBox.display("Invalid Move !", "Invalid move! Please play a card that follows the suit or rank of the lead card.");
            }
        }

        if (Boom.playedCenter.size() == 4) {
            Boom.evaluateWinner(); 
            
            // Winner turn
            Boom.currentPlayer = Boom.roundWinner;
            Boom.center.clear();
            Boom.playedCenter.clear();
            MessageBox.display("Round Winner", "Player " + (Boom.players.indexOf(Boom.roundWinner) + 1) + " wins Trick #" + Boom.trickNum);
            Boom.trickNum++;
        }

        if (!Boom.currentPlayer.playerCards.isEmpty()) {
            Boom.display();
            updateGameState();
        }
    }
}
