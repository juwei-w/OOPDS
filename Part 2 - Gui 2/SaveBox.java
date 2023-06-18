
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import javafx.geometry.*;

public class SaveBox {
    
    static String filename = "";

    public static String display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Save File");
        window.setMinWidth(250);

        TextField fileName = new TextField();
        fileName.setPromptText("Enter a file name.");

        Button okButton = new Button("Ok");
        okButton.setOnAction(e -> {
            filename = fileName.getText();
            window.close();
        });
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            window.close();
        });

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(okButton, cancelButton);

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(fileName, buttons);

        Scene scene = new Scene(layout, 100, 80);
        window.setScene(scene);
        window.showAndWait();

        return filename;
    }
}
