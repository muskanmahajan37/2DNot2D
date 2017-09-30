package model;

import javafx.scene.paint.Color;

public class Wall {

    /**
     * X position of start
     */
    public double x;

    /**
     * Y position of start
     */
    public double y;

    /**
     * Angle the wall extends from starting point
     */
    public double theta;

    /**
     * Distance the wall extends into space
     */
    public double length;

    public Wall(double x, double y, double theta, double length) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.length = length;
    }

    public Color getColorAtDist(double dist) {
        if (((int)dist*7) % 2 == 0) {
            return Color.BLUEVIOLET;
        } else {
            return Color.CRIMSON;
        }
    }
}
