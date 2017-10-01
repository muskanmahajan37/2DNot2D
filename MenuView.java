import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Baddie;
import model.Level;
import model.Wall;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.net.URL;
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
        List<Level> levels = initLevels(); // initLevels();

        int levelNum = 1;
        for (Level l : levels) {
            Button btn = new Button();
            levelNum++;
            btn.setText("Level #" + levelNum + " -  " + l.name);
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

        List<Wall> walls = new ArrayList<Wall>();
        walls.add(new Wall(10, 0, Math.PI / 3, 100));
        Level l = new Level(walls, 0, 0, 0, 10, 0, 1, "testmap");

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

    public List<Level> initLevels() {
        WallBuilder wb = new WallBuilder();

        List<Level> LoL = new ArrayList<Level>();

        try {
            URL urlh = getClass().getResource("hmap.txt");
            System.out.println(urlh.getPath());
            List<Wall> LoW1 = wb.wallsFromFile(urlh.getPath());

            for (Wall w : LoW1) {
                w.color1 = Color.rgb((int)(Math.random() * 255),
                                     (int)(Math.random() * 255),
                                     (int)(Math.random() * 255));
            }


            List<Wall> LoW3 = new ArrayList<>(4);
            LoW3.add(new Wall(1.0, 1.0, Math.PI / 4, Math.sqrt(2)));
            LoW3.add(new Wall(2.0, 2.0, Math.PI * 7 / 4, Math.sqrt(2)));
            LoW3.add(new Wall(3.0, 1.0, Math.PI * 5 / 4, Math.sqrt(2)));
            LoW3.add(new Wall(2.0, 0.0, Math.PI * 3 / 4, Math.sqrt(2)));
            for (Wall w : LoW3) {
                w.color2 = Color.rgb((int)(Math.random() * 255),
                        (int)(Math.random() * 255),
                        (int)(Math.random() * 255));
            }

            Wall exith = new Wall(15, 0, Math.PI / 4, 3 * Math.sqrt(2));
            Level hmap = new Level(LoW1, -1, -1, 0, 16, 1, 2, "Square");
            System.out.println(LoW1);
            LoL.add(hmap);





            URL urlg = getClass().getResource("gmap.txt");
            List<Wall> LoW2 = wb.wallsFromFile(urlg.getPath());
            Wall exitg = new Wall(11, 5, 0, 1);
            for (Wall w : LoW2) {
                w.color2 = Color.rgb((int)(Math.random() * 255),
                        (int)(Math.random() * 255),
                        (int)(Math.random() * 255));
            }
            Level gmap = new Level(LoW2, 13, 8, Math.PI / 2, 11, 5, 2, "G");
            LoL.add(gmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return LoL;
    }

}
