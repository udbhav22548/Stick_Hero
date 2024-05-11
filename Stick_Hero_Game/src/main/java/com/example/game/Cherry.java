package com.example.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Cherry {
    private double X_Coordinate;
    private double Y_Coordinate;
    private Circle circle;

    public Cherry(double x, double y) {
        this.X_Coordinate = x;
        this.Y_Coordinate = y;
        this.circle = new Circle(x, y, 20, Color.RED);
    }

    public double getX() {
        return X_Coordinate;
    }

    public void setX(double x) {
        this.X_Coordinate = x;
    }

    public void generate(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(X_Coordinate, Y_Coordinate, 20, 30);
    }

    public Circle getCircle() {
        return circle;
    }
}
