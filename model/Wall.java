package model;

import javafx.scene.paint.Color;

public class Wall {

    /**
     * how wide each color thing on the wall is
     */
    public static double colorBeamWidth = 1;

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

    /**
     * Copy constructor.
     */
    public Wall(Wall aWall) {
        this(aWall.x, aWall.y, aWall.theta, aWall.length);
    }

    public Color getColorAtDist(double dist) {
        if (((int)dist) % 2 == 0) {
            return Color.BLUEVIOLET;
        } else {
            return Color.CRIMSON;
        }
    }
}
