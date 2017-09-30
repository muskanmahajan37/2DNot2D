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

    public void updatePosition(String direction) {
        if (direction.equals("W")) {
            this.y += 1;
        }
        else if (direction.equals("S")) {
            this.y -= 1;
        }
        else if (direction.equals("A")) {
            this.x -= 1;
        }
        else if (direction.equals("D")) {
            this.x += 1;
        }
    }

    public void updateTheta(double newTheta) {
        this.theta = newTheta;
    }
}
