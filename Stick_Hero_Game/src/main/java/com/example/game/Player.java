package com.example.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {
    private static final double WIDTH = 80;
    private static final double HEIGHT = 80;

    private double X_Coordinate;
    private double Y_Coordinate;

    private Image Ninja = new Image("file:Ninja.png");
    private boolean isFlipped = false;

    public Player(double x, double y) {
        this.X_Coordinate = x;
        this.Y_Coordinate = y - HEIGHT;
    }

    public void generate(GraphicsContext gc) {
        if (isFlipped) {
            gc.save();
            gc.translate(X_Coordinate, Y_Coordinate + HEIGHT);
            gc.scale(1, -1);
            gc.drawImage(Ninja, -WIDTH / 2, -60, WIDTH, HEIGHT);
            gc.restore();
        }
        else {
            gc.drawImage(Ninja, X_Coordinate - WIDTH / 2, Y_Coordinate, WIDTH, HEIGHT);
        }
    }

    public double getX() {
        return X_Coordinate;
    }

    public void setX(double x) {
        this.X_Coordinate = x;
    }

    public double getY() {
        return Y_Coordinate;
    }

    public void setY(double y) {
        this.Y_Coordinate = y;
    }

    public void flip() {
        // Toggle the flip state
        isFlipped = !isFlipped;
    }

    public boolean getFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }
}
