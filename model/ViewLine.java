package model;

public class ViewLine {
    /** X position of start */
    double x;

    /** Y position of start */
    double y;

    /** Angle the view extends from starting point */
    double theta;


    public ViewLine(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }
}
