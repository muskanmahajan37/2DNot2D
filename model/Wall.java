package model;

import javafx.scene.paint.Color;

public class Wall {

    /** how wide each color thing on the wall is */
    static double colorBeamWidth = 1;

    /** X position of start */
    double x;

    /** Y position of start */
    double y;

    /** Angle the wall extends from starting point */
    double theta;

    /** Distance the wall extends into space */
    double length;

    public Wall(double x, double y, double theta, double length) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.length = length;
    }

    /** Color of the band dist units along the wall */
    public Color getColorAtDist(double dist) {
        if (dist % (2 * colorBeamWidth) < colorBeamWidth) {
            return Color.RED;
        } else {
            return Color.BLUE;
        }
    }
}
