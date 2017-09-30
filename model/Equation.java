package model;

public class Equation {

    double a1;
    double a2;

    double b1;
    double b2;

    double c1;
    double c2;


    // using cramer's rule for 2x2 matrix
    // https://chilimath.com/lessons/advanced-algebra/cramers-rule-with-two-variables/
    public Equation(ViewLine viewLine, Wall wall) {
        a1 = Math.cos(viewLine.theta);
        a2 = Math.sin(viewLine.theta);

        b1 = -Math.cos(wall.theta);
        b2 = -Math.sin(wall.theta);

        c1 = wall.x - viewLine.x;
        c2 = wall.y - viewLine.y;
    }

    public double distanceAlongViewLine() {
        return (c1 * b2 - b1 * c2) / (a1 * b2 - b1 * a2);
    }

    public double distanceAlongWall() {
        return (a1 * c2 - c1 * a2) / (a1 * b2 - b1 * a2);
    }
}
