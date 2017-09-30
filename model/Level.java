package model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public List<Wall> walls = new ArrayList<>();

    public double playerstartx;
    public double playerstarty;
    public double playerstarttheta;


    public double exitX = 50;
    public double exitY = 40;
    public double exitRadius = 10;
}
