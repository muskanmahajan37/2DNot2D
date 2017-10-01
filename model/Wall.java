package model;

import javafx.scene.control.SpinnerValueFactory;
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

    public Color color1 = Color.CRIMSON;
    public Color color2 = Color.BLUEVIOLET;

    public Wall(double x, double y, double theta, double length) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.length = length;
    }

    /**
     * Copy constructor.
     */
    public Wall(Wall aWall) {
        this(aWall.x, aWall.y, aWall.theta, aWall.length);
    }

    /**
     * Increase to make the stripes smaller, to make more stripes in the same amount of length
     */
    public double density = 1;

    public Color getColorAtDist(double dist) {
        if ((dist * density) % 2 < 1) {
            return color1;
        } else {
            return color2;
        }
    }

    public String toString() {
        String result = "";
        result += "[Wall: ";
        result += "X: " + Double.toString(x) + "  ";
        result += "Y: " + Double.toString(y) + "  ";
        result += "Theta: " + Double.toString(theta) + "  ";
        result += "length: " + Double.toString(length);
        result += "]\n";
        return result;
    }
}
