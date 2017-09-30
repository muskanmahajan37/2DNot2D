import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Baddie;
import model.Level;
import model.Wall;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuView extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        primaryStage.setResizable(false);
        primaryStage.setTitle("Hello World!");

        StackPane root = new StackPane();

        ScrollPane scrollPane = new ScrollPane();
        VBox scrollPaneChild = new VBox();
        scrollPane.setContent(scrollPaneChild);

        GameView game = new GameView(primaryStage);
        game.onQuit(() -> {
            root.getChildren().remove(game.canvas);
            root.getChildren().add(scrollPane);
            if (game.win) {

            }
        });

        Button btn = new Button();
        btn.setText("Level One");
        btn.setOnAction(event -> {
            game.createCanvas(levels().get(0));
            root.getChildren().remove(scrollPane);
            root.getChildren().add(game.canvas);
            game.canvas.requestFocus();

        });
        scrollPaneChild.getChildren().add(btn);
        root.getChildren().add(scrollPane);
        primaryStage.setScene(new Scene(root, 600, 200));
        primaryStage.show();
    }


    public List<Level> levels() {

        List<Level> levels = new ArrayList<>();

        Level l = new Level();
        l.walls.add(new Wall(10, 0, Math.PI / 3, 100));
        l.playerstartx = 0;
        l.playerstarty = 0;
        l.playerstarttheta = 0;
        l.exitX = 10;
        l.exitY = 0;
        l.exitRadius = 1;
        levels.add(l);

        Baddie baddie = new Baddie(7, 0, 1);
        List<Point2D> points = new ArrayList<>(3);
        points.add(new Point2D.Double(7, 0));
        points.add(new Point2D.Double(7, 4));
        points.add(new Point2D.Double(3, 2));
        baddie.setPatrol(points, 1);

        l.addBaddie(baddie);



        return levels;
    }
}
