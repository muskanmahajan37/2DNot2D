import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Equation;
import model.Player;
import model.ViewLine;
import model.Wall;

public class GameView extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        Canvas canvas = new Canvas();

        GraphicsContext g = canvas.getGraphicsContext2D();


        Player player = new Player(2, 4, 0);
        Wall wall = new Wall(10, -500, Math.PI / 2, 1000);

        int angle = -10;
        int deltaAngle = 1;
        int iterations = 20;

        int sectionWidth = 600 / iterations;

        g.setFill(Color.IVORY);
        g.fillRect(0, 0, 10, 40);
        for (int drawPixel = 0; drawPixel < iterations; drawPixel++) {
            angle += deltaAngle;

            ViewLine v = player.viewLine(angle);
            Equation e = new Equation(v, wall);

            g.setFill(wall.getColorAtDist(e.distanceAlongWall()));
            g.fillRect(drawPixel * sectionWidth, 90, sectionWidth, 20 );
        }


        Pane root = new Pane(canvas);
        primaryStage.setScene(new Scene(root, 600, 200));
        primaryStage.show();


    }
}