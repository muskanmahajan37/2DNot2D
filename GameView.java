import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.awt.*;
import java.util.List;


public class GameView {
    public Canvas canvas;
    public boolean win = false;
    public Level level;
    public int deaths;


    private Player player;
    private Stage primaryStage;
    private boolean ignoreMouseEvent = false;
    private Runnable onQuitFunc = null;

    GameView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void createCanvas(Level level) {
        this.win = false;
        this.deaths = 0;
        this.level = level;

        this.level = level;
        this.player = new Player(level.playerstartx, level.playerstarty, level.playerstarttheta);
        AnimationTimer loop = new AnimationTimer() {
            private long before = System.currentTimeMillis();
            private float deltaTime;

            @Override
            public void handle(long l) {
                deltaTime = (System.currentTimeMillis() - before) / 1000F;

                if (!win) {
                    player.update(deltaTime);
                    if (Math.random() > 0.76)
                        //System.out.println(player);
                    for (Baddie b : level.baddies) {
                        b.updateBaddie(deltaTime);
                    }
                }

                if (Math.abs(player.x - level.exitX) + Math.abs(player.y - level.exitY) < level.exitRadius) {
                    win = true;
                }

                draw();

                before = before + (long) (deltaTime * 1000);
            }
        };

        canvas = new Canvas(600, 200);
        canvas.setOnMouseEntered(event -> canvas.setCursor(Cursor.NONE));
        canvas.setOnMouseExited(event -> canvas.setCursor(Cursor.DEFAULT));


        canvas.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case W:
                    player.up = false;
                    break;
                case S:
                    player.down = false;
                    break;
                case A:
                    player.left = false;
                    break;
                case D:
                    player.right = false;
                    break;
            }

        });
        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    player.up = true;
                    break;
                case S:
                    player.down = true;
                    break;
                case A:
                    player.left = true;
                    break;
                case D:
                    player.right = true;
                    break;
                case Q:
                    loop.stop();
                    onQuitFunc.run();
                    break;
                case R:
                    player = new Player(level.playerstartx, level.playerstarty, level.playerstarttheta);
                    break;
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

        loop.start();
    }

    private Color colorAtViewLine(List<Wall> walls, ViewLine viewLine) {

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

            if (viewDist <= 0.1) {
                deaths += 1;
                player = new Player(level.playerstartx, level.playerstarty, level.playerstarttheta);
            }


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

        g.setFill(Color.DARKRED);
        g.setFont(new Font("Roman", 16));
        g.fillText("DEATHS: " + deaths, 50, 160);

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