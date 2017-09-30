package model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public List<Wall> walls = new ArrayList<>();

    public double playerstartx;
    public double playerstarty;
    public double playerstarttheta;


    public double exitX;
    public double exitY;
    public double exitRadius;

    public Level scale(double scale) {
        for (Wall w: walls) {
            w.x = w.x * scale;
            w.y = w.y * scale;
            w.length = w.length * scale;
        }
        playerstartx = playerstartx * scale;
        playerstarty = playerstarty * scale;
        exitX = exitX * scale;
        exitY = exitY * scale;
        exitRadius = exitRadius * scale;
        return this;
    }
}
