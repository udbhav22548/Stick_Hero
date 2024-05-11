package com.example.game;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class EndScreen extends Pane {
    public EndScreen(int score,int cherriesCollected,int highestScore, Runnable onRestartCallback) {
        setPrefSize(StickHeroGame.WIDTH, StickHeroGame.HEIGHT);

        // Create text for end screen
        javafx.scene.text.Text endText = new javafx.scene.text.Text("Game Over\nScore: " + score+"\nCherries Collected: " + cherriesCollected+"\nHighest Score: " + highestScore);
        endText.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        endText.setFill(Color.RED);
        endText.setTextAlignment(TextAlignment.CENTER);
        endText.setLayoutX((StickHeroGame.WIDTH - endText.getBoundsInLocal().getWidth()) / 2);
        endText.setLayoutY(StickHeroGame.HEIGHT / 3);
        Button restartButton = new Button("Restart Game");
        restartButton.setStyle("-fx-font-size: 24; -fx-background-color: #00FF00;");
        restartButton.setOnAction(event -> onRestartCallback.run());
        restartButton.setLayoutX((StickHeroGame.WIDTH - restartButton.getWidth()) / 2);
        restartButton.setLayoutY(2 * StickHeroGame.HEIGHT / 3);

        getChildren().addAll(endText, restartButton);
    }
}
