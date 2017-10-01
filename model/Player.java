package model;

public class Player {
    /**
     * X position
     */
    public double x;

    /**
     * Y position
     */
    public double y;

    /**
     * Angle the player is looking at
     */
    public double theta;

    public boolean left;
    public boolean right;
    public boolean up;
    public boolean down;

    /**
     * On this.update() turn in the direction
     */
    double nextTurnValue;

    public Player(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public ViewLine viewLine(double angle) {
        return new ViewLine(x, y, theta + angle);
    }

    public void update(float deltaTime) {
        this.theta += nextTurnValue * deltaTime;
        nextTurnValue = 0;

        if (up) {
            this.x += 3 * deltaTime * Math.cos(theta);
            this.y += 3 * deltaTime * Math.sin(theta);
        }

        if (down) {
            this.x += 3 * deltaTime * Math.cos(theta + Math.PI);
            this.y += 3 * deltaTime * Math.sin(theta + Math.PI);
        }

        if (right) {
            this.x += 3 * deltaTime * Math.cos(theta + 3 * Math.PI / 2);
            this.y += 3 * deltaTime * Math.sin(theta + 3 * Math.PI / 2);
        }

        if (left) {
            this.x += 3 * deltaTime * Math.cos(theta + Math.PI / 2);
            this.y += 3 * deltaTime * Math.sin(theta + Math.PI / 2);
        }

    }

    public String toString() {
        String result = "";
        result += "[Player: ";
        result += "Theta: " + this.theta + "  ";
        result += "x: " + this.x + "  ";
        result += "Y: " + this.y + "  ";
        result += "]";
        return result;
    }

    public void updateTheta(double newTheta) {
        nextTurnValue = -newTheta;
    }
}
