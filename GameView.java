import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Equation;
import model.Player;
import model.ViewLine;
import model.Wall;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameView extends Application {
    private Canvas canvas;
    private Player player;
    private int startX;
    private int startY;
    private int startT;
    private List<Wall> myWalls;
    public boolean ignoreMouseEvent = false;

    double exitX = 50;
    double exitY = 40;
    boolean win = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("2DNot2D");

        canvas = new Canvas(600, 200);

//        player = new Player(2, 4, 0); //Math.PI * 1 / 5);
        player = new Player(40, 40, 0);

        startX = 2;
        startY = 4;
        startT = 0;

        Wall wall0 = new Wall(0, 0, Math.PI/2, 60);
        wall0.color2 = Color.BLUE;

        Wall wall1 = new Wall(0, 60, 0, 50);
        wall1.color2 = Color.DEEPPINK;

        Wall wall2 = new Wall(50, 60, 3*Math.PI/2, 20);
        wall2.color2 = Color.GREEN;

        Wall wall3 = new Wall(20, 40, 0, 30);
        wall3.color2 = Color.YELLOW;

        Wall wall4 = new Wall(20, 0, Math.PI/2, 40);
        wall4.color2 = Color.DARKGREY;

        Wall wall5 = new Wall(0, 0, 0, 20);
        wall5.color2 = Color.PURPLE;

        Wall exit = new Wall(45, 40, 1, 5 * Math.sqrt(2));
        exit.color1 = Color.DEEPSKYBLUE;
        exit.color2 = Color.DEEPSKYBLUE;

        myWalls = new ArrayList<>();
        myWalls.add(wall0);
        myWalls.add(wall1);
        myWalls.add(wall2);
        myWalls.add(wall3);
        myWalls.add(wall4);
        myWalls.add(wall5);
        myWalls.add(exit);


        AnimationTimer loop = new AnimationTimer() {
            private long before = System.currentTimeMillis();
            private float deltaTime;

            @Override
            public void handle(long l) {
                deltaTime = (System.currentTimeMillis() - before) / 1000F;

                double tmpx = player.x;
                double tmpy = player.y;
                double tmpA = player.theta;

                if (!win)
                    player.update(deltaTime);

                if (Math.abs(player.x - exitX) + Math.abs(player.y - exitY) < 5)
                    win = true;

                if (tmpx != player.x || tmpy != player.y || tmpA != player.theta)
                    draw();

                before = before + (long) (deltaTime * 1000);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Pane root = new Pane(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                primaryStage.getScene().setCursor(Cursor.NONE);
            }
        });
        primaryStage.getScene().setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
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
                }
            }
        });

        primaryStage.getScene().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
            }
        });

        primaryStage.show();

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
            Color c = colorAtViewLine(myWalls, player.viewLine((-i + 100) * Math.PI / 400));

            if (c != null) {
                g.setFill(c);
            } else {
                g.setFill(Color.BLACK);
            }
            g.fillRect(i*3 , 50, 3, 20);

//            System.out.println("Drawing at: "+ i + "\n\n");


        }

        if (win) {
            g.setFill(Color.WHITE);
            g.setFont(new Font("Roman", 100));
            g.fillText("YOU WIN", 50, 100);
        }
    }
}