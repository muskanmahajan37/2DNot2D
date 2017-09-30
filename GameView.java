import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameView extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("2DNot2D");

        Canvas canvas = new Canvas(600, 200);

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.fillRect(10, 10, 10, 40);

        Pane root = new Pane(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}