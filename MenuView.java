import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Level;
import model.Wall;

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
        Level l = new Level();
        l.walls.add(new Wall(10, 0, Math.PI / 3, 100));

        l.playerstartx = 0;
        l.playerstarty = 0;
        l.playerstarttheta = 0;


        l.exitX = 10;
        l.exitY = 0;
        l.exitRadius = 1;

        return Collections.singletonList(l);
    }
}
