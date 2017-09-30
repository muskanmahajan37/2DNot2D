package model;

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

}
