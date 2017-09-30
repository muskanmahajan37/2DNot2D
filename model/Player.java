package model;

public class Player {
    /** X position */
    double x;

    /** Y position */
    double y;

    /** Angle the player is looking at */
    double theta;

    /** On this.update() move in this direction */
    String nextMoveDirection;

    public Player(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public ViewLine viewLine(double angle) {
        return new ViewLine (x, y, theta + angle);
    }

    public void update(float deltaTime) {
        if (nextMoveDirection == null)
            return;

        if (nextMoveDirection.equals("W")) {
            this.x += 3 * deltaTime * Math.cos(theta);
            this.y += 3 * deltaTime * Math.sin(theta);
        }
        else if (nextMoveDirection.equals("S")) {
            this.x += 3 * deltaTime * Math.cos(theta + Math.PI);
            this.y += 3 * deltaTime * Math.sin(theta + Math.PI);
        }
        else if (nextMoveDirection.equals("A")) {
            this.x += 3 * deltaTime * Math.cos(theta + 3 * Math.PI / 2);
            this.y += 3 * deltaTime * Math.sin(theta + 3 * Math.PI / 2);
        }
        else if (nextMoveDirection.equals("D")) {
            this.x += 3 * deltaTime * Math.cos(theta + Math.PI / 2);
            this.y += 3 * deltaTime * Math.sin(theta + Math.PI / 2);
        }

        nextMoveDirection = null;
    }

    public void updatePosition(String direction) {
        nextMoveDirection = direction;
    }
}
