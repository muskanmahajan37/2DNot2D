import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.awt.*;
import java.util.List;

public class GameView {
    public Canvas canvas;
    public boolean win = false;

    private Player player;
    private Stage primaryStage;
    private boolean ignoreMouseEvent = false;
    private Level level;

    private long timeOfWin = 0;
    private Runnable onQuitFunc = null;

    public  GameView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void createCanvas(Level level) {
        this.level = level;

        AnimationTimer loop = new AnimationTimer() {
            private long before = System.currentTimeMillis();
            private float deltaTime;

            @Override
            public void handle(long l) {
                deltaTime = (System.currentTimeMillis() - before) / 1000F;

                double beforeX = player.x;
                double beforeY = player.y;
                double beforeTheta = player.theta;

                if (!win)
                    player.update(deltaTime);

                if (Math.abs(player.x - level.exitX) + Math.abs(player.y - level.exitY) < level.exitRadius) {
                    win = true;
                    timeOfWin = System.currentTimeMillis();
                }

                if (beforeX != player.x || beforeY != player.y || beforeTheta != player.theta)
                    draw();

                before = before + (long) (deltaTime * 1000);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        canvas = new Canvas(600, 200);
        canvas.setOnMouseEntered(event -> canvas.setCursor(Cursor.NONE));
        canvas.setOnMouseExited(event -> canvas.setCursor(Cursor.DEFAULT));

        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    player.updatePosition("W");
                    break;
                case S:
                    player.updatePosition("S");
                    break;
                case A:
                    player.updatePosition("A");
                    break;
                case D:
                    player.updatePosition("D");
                    break;
                case Q:
                    onQuitFunc.run();
                    break;
                case R:

            }
        });

        canvas.setOnMouseMoved(event -> {
            //System.out.println(event.getSceneX() + "   " + event.getX() + "   " + event.getScreenX());
            if (ignoreMouseEvent) {
                ignoreMouseEvent = false;
                return;
            }

            double mouseDeltaX = 0;
            mouseDeltaX += Math.round(event.getScreenX() - (primaryStage.getX() + (primaryStage.getWidth() / 2.0)));
            double newTheta = mouseDeltaX / 10;
            player.updateTheta(newTheta);

            ignoreMouseEvent = true;
            try {
                Robot robot = new Robot();
                robot.mouseMove((int) (primaryStage.getX() + (primaryStage.getWidth() / 2.0)),
                        (int) (primaryStage.getY() + (primaryStage.getHeight() / 2.0)));
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });

        draw();
        loop.start();
    }

    public Color colorAtViewLine(List<Wall> walls, ViewLine viewLine) {

        Wall nearestWall = null;
        double distAlongWall = Double.POSITIVE_INFINITY;
        double currentClosestDist = Double.POSITIVE_INFINITY;

        for (Wall w : walls) {

            Equation e = new Equation(viewLine, w);

//            System.out.println(viewLine.x + " " + viewLine.y + " " + viewLine.theta);
//            System.out.println(w.x + " " + w.y + " " + w.theta);

            double wallDist = e.distanceAlongWall();
            double viewDist = e.distanceAlongViewLine();

            if (viewDist < 0)
                continue;

            if (wallDist < 0)
                continue;

            if (wallDist > w.length)
                continue;

            if (viewDist <= 0.1)
                player = new Player(2, 4, 0);

            if (viewDist < currentClosestDist) {
                nearestWall = w;
                currentClosestDist = viewDist;
                distAlongWall = wallDist;
            }
        } // End for loop

        if (nearestWall == null) {
            return null;
        } else {
            return nearestWall.getColorAtDist(distAlongWall);
        }
    }

    private void draw() {

        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < 201; i++) {
            Color c = colorAtViewLine(level.walls, player.viewLine((-i + 100) * Math.PI / 400));

            if (c != null) {
                g.setFill(c);
            } else {
                g.setFill(Color.BLACK);
            }
            g.fillRect(i * 3, 50, 3, 20);

//            System.out.println("Drawing at: "+ i + "\n\n");


        }

        if (win) {
            g.setFill(Color.WHITE);
            g.setFont(new Font("Roman", 100));
            g.fillText("YOU WIN", 50, 100);
        }
    }

    public void onQuit(Runnable value) {
        onQuitFunc = value;
    }
}