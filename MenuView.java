import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Level;
import model.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuView extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        primaryStage.setResizable(false);
        primaryStage.setTitle("2DNot2D?");

        StackPane root = new StackPane();

        ScrollPane scrollPane = new ScrollPane();
        VBox scrollPaneChild = new VBox();
        scrollPane.setContent(scrollPaneChild);

        Label info = new Label("1st person 2D game\n\n" +
                "wasd + moust to move\n" +
                "Press 'q' to exit a level.\n" +
                "You die if you touch any wall.\n" +
                "You win by getting to the end (the green wall)\n" +
                "Good luck!\n\n" +
                "You have died 0 times so far.\n ");

        scrollPaneChild.getChildren().add(info);

        GameView game = new GameView(primaryStage);

        List<Button> buttons = new ArrayList<>();
        List<Level> levels = levels();

        int levelNum = 0;
        for (Level l : levels) {
            Button btn = new Button();
            levelNum++;
            btn.setText("Level #" + levelNum + " -  " + l);
            btn.setOnAction(event -> {
                game.createCanvas(l);
                root.getChildren().remove(scrollPane);
                root.getChildren().add(game.canvas);
                game.canvas.requestFocus();
            });
            buttons.add(btn);
            scrollPaneChild.getChildren().add(btn);
        }

        AtomicInteger totalDeaths = new AtomicInteger();
        game.onQuit(() -> {
            root.getChildren().remove(game.canvas);
            root.getChildren().add(scrollPane);
            if (game.win) {
                int index = levels.indexOf(game.level);
                buttons.get(index).setStyle("-fx-background-color: moccasin");
            }

            totalDeaths.addAndGet(game.deaths);
            String times;
            if (totalDeaths.get() == 1)
                times = "time";
            else
                times = "times";
            info.setText(info.getText().replaceFirst("You have died (\\d*) times? so far",
                    "You have died " + totalDeaths.get() + " " + times + " so far"));
        });

        root.getChildren().add(scrollPane);
        primaryStage.setScene(new Scene(root, 600, 200));
        primaryStage.show();
    }


    private List<Level> levels() {

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

        return levels;
    }
}
