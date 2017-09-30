package model;

public class ViewLine {
    /**
     * X position of start
     */
    public double x;

    /**
     * Y position of start
     */
    public double y;

    /**
     * Angle the view extends from starting point
     */
    public double theta;


    public ViewLine(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }
}
