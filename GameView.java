import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    private List<Wall> myWalls;
    public boolean ignoreMouseEvent = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("2DNot2D");

        canvas = new Canvas(600, 200);

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.fillRect(10, 10, 10, 40);


        player = new Player(0, 0, Math.PI * 1 / 5);
        Wall wall = new Wall(10, -500, Math.PI / 2, 1000);

        myWalls = new ArrayList<>();
        myWalls.add(wall);





        Pane root = new Pane(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        player.updatePosition("W");
                        draw();
                        break;
                    case S:
                        player.updatePosition("S");
                        draw();
                        break;
                    case A:
                        player.updatePosition("A");
                        draw();
                        break;
                    case D:
                        player.updatePosition("D");
                        draw();
                        break;
                }
            }
        });

        primaryStage.getScene().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //System.out.println(event.getSceneX() + "   " + event.getX() + "   " + event.getScreenX());
                if(ignoreMouseEvent) {
                    ignoreMouseEvent = false;
                    return;
                }

                double mouseDeltaX = 0;
                mouseDeltaX += Math.round(event.getScreenX() - (primaryStage.getX() + (primaryStage.getWidth() / 2.0)));
                double newTheta = mouseDeltaX;
                player.updateTheta(newTheta);
                draw();
                System.out.println(mouseDeltaX);

                ignoreMouseEvent = true;
                try {
                    Robot robot = new Robot();
                    robot.mouseMove((int) (primaryStage.getX() + (primaryStage.getWidth() / 2.0)),
                            (int) (primaryStage.getY() + (primaryStage.getHeight() / 2.0)));
                }
                catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.show();

        draw();
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

            if (viewDist < currentClosestDist) {
                nearestWall = w;
                currentClosestDist = viewDist;
                distAlongWall = wallDist;
            }
        } // End for loop

        return nearestWall.getColorAtDist(distAlongWall);
    }

    private void draw() {
        for (int i = 0; i < 100; i++) {
            Color c = colorAtViewLine(myWalls, player.viewLine((i - 50) * Math.PI / 400));

            canvas.getGraphicsContext2D().setFill(c);
            canvas.getGraphicsContext2D().fillRect(i * 3, 40, 3, 20);

//            System.out.println("Drawing at: "+ i + "\n\n");
        }
    }
}