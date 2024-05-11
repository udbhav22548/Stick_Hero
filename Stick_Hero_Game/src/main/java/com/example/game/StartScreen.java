package com.example.game;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class StartScreen extends Pane {
    public StartScreen(Runnable On_Start) {
        setPrefSize(StickHeroGame.WIDTH, StickHeroGame.HEIGHT);

        Image backgroundImage = new Image("file:background.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1000, 800, false, false, false, false)
        );
        setBackground(new javafx.scene.layout.Background(background));

        javafx.scene.text.Text gameName = new javafx.scene.text.Text("Stick Hero");
        gameName.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        gameName.setFill(Color.BLACK);
        gameName.setTextAlignment(TextAlignment.CENTER);
        gameName.setLayoutX((StickHeroGame.WIDTH - gameName.getBoundsInLocal().getWidth()) / 2);
        gameName.setLayoutY(StickHeroGame.HEIGHT / 4);

        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 24; -fx-background-color: #FF0000;");
        startButton.setOnAction(event -> On_Start.run());
        startButton.setLayoutX(gameName.getLayoutX() + 120);
        startButton.setLayoutY(StickHeroGame.HEIGHT / 2);

        getChildren().addAll(gameName, startButton);
    }
}
