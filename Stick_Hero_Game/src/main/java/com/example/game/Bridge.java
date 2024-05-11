package com.example.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bridge {
    private double Height;
    private boolean RC;
    private Color color;
    private double X_Coordinate;
    private double Angle;


    public void setX(double x) {
        this.X_Coordinate = x;
    }

    public double getX() {
        return X_Coordinate;
    }

    private int b_no;

    public Bridge(double x, double height, Color color,int b_no) {
        this.X_Coordinate = x;
        this.Height = height;
        this.color = color;
        this.Angle = 0.0;
        this.RC = false;
        this.b_no=b_no;
    }

    public void generate(GraphicsContext gc) {
        gc.save();

        gc.translate(X_Coordinate + 5, StickHeroGame.HEIGHT - 100);

        gc.rotate(Angle);

        gc.setFill(color);
        gc.fillRect(-5, -Height, 10, Height);

        gc.restore();
    }

    public void update() {

        if (Angle >= 90.0) {
            Angle = 90.0;
        }

        if (!RC) {
            Angle += 0.5;
        }

    }

    public boolean isRotationComplete() {
        return RC;
    }

    public double getTopX() {
        return X_Coordinate + 5;
    }

    public double getTopY() {
        return StickHeroGame.HEIGHT - 10 - Height;
    }

    public double getHeight(){
        return Height;
    }

    public double getAngle() {
        return Angle;
    }

    public void setAngle(double angle) {
        this.Angle = angle;
    }

    public int getB_no() {
        return b_no;
    }

    public void setB_no(int b_no) {
        this.b_no = b_no;
    }
}