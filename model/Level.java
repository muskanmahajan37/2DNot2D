package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Level {

    public List<Wall> walls = new ArrayList<>();
    public List<Baddie> baddies = new ArrayList<>();

    public String name;
    public double playerstartx;
    public double playerstarty;
    public double playerstarttheta;


    public double exitX;
    public double exitY;
    public double exitRadius;

    public Level(List<Wall> walls, double playerstartX, double playerstarty, double playerstarttheta, double exitX,
                 double exitY, double exitRadius, String name) {
        this.walls = walls;
        this.playerstartx = playerstartX;
        this.playerstarty = playerstarty;
        this.playerstarttheta = playerstarttheta;
        this.exitX = exitX;
        this.exitY = exitY;
        this.exitRadius = exitRadius;
        this.name = name;
    }


    public Level scale(double scale) {
        for (Wall w : walls) {
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

    public Level setWallDensity(double density) {
        for (Wall w : walls)
            w.density = density;
        return this;
    }

    public Level randomizeWallColor1() {
        for (Wall w : this.walls) {
            w.color1 = Color.rgb(
                    (int) (Math.random() * 255),
                    (int) (Math.random() * 255),
                    (int) (Math.random() * 255));
        }
        return this;
    }

    public void addBaddie(Baddie toAdd) {
        baddies.add(toAdd);
        walls.add(toAdd.wallVert);
        walls.add(toAdd.wallHoriz);
    }

}
