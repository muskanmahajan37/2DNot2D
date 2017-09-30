package model;

public class Player {
    /** X position */
    double x;

    /** Y position */
    double y;

    /** Angle the player is looking at */
    double theta;

    public Player(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public ViewLine viewLine(double angle) {
        return new ViewLine (x, y, theta + angle);
    }

}
