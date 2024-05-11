package com.example.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Platform {
    private double X_Coordinate;
    private double Width;
    private double Height; // New variable to represent platform height

    public Platform(double x, double width, double height) {
        this.X_Coordinate = x;
        this.Width = width;
        this.Height = height;
    }

    public void generate(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillRect(X_Coordinate, StickHeroGame.HEIGHT - Height, Width, Height);
    }

    public double getX() {
        return X_Coordinate;
    }

    public int getX_int(){
        return (int)X_Coordinate;
    }

    public void setX(double x) {
        this.X_Coordinate = x;
    }

    public double getWidth() {
        return Width;
    }

    public double getHeight() {
        return Height;
    }

    public double getY() {
        return StickHeroGame.HEIGHT - Height;
    }
}
