
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.*;

public class WinGameBox {
    
    static String answer;

    public static String display(ArrayList<Player> players, String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(400);

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        
        Label label = new Label();
        label.setText(message);
        layout.getChildren().add(label);

        layout.getChildren().add(new Label("-------------------------"));
        for (Player player : players) {
            layout.getChildren().add(new Label("Player " + (Boom.players.indexOf(player) + 1) + " Score : " + player.score));
        }
        layout.getChildren().add(new Label("-------------------------"));

        Button continueGameButton = new Button("Continue");
        continueGameButton.setOnAction(e -> {
            answer = "continue";
            window.close();
        });
        
        Button restartGameButton = new Button("Restart");
        restartGameButton.setOnAction(e -> {
            answer = "restart";
            window.close();
        });

        Button exitGameButtonn = new Button("Exit");
        exitGameButtonn.setOnAction(e -> {
            boolean result = ExitBox.display("Exit Game", "Are you sure you want to exit game?");
            if (result) {window.close();}
            answer = "exit";
        });

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(continueGameButton, restartGameButton, exitGameButtonn);

        layout.getChildren().add(buttons);

        Scene scene = new Scene(layout, 100, 80);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
