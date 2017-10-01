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
//        List<Level> levels = levels();
        List<Level> levels = initLevels();

        int levelNum = 0;
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

        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall(10, 0, Math.PI / 2, 100));
        walls.get(0).color1 = Color.MAROON;
        walls.add(new Wall(0, 0, Math.PI / 2, 100));
        walls.get(1).color1 = Color.AQUAMARINE;
        Level l = new Level(walls, 0, -1, 0, 10, 0, 1, "testmap");

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

        List<Level> LoL = new ArrayList<>();

        // These vars should be set by each level that uses them
        List<Wall> LoW;
        Level level;
        URL url;
        Wall exitWall;

        // Exit walls should be GREEN, GREEN

        // use level.scale(factor) to scale completely scale a level

        // use level.randomizeWallColor1() to randomize the color of each wall's stripe 1


//LEVEL 1 - EASY SQUARE
        url = getClass().getResource("AJLevel1.txt");
        LoW = wb.wallsFromFile(url);

        level = new Level(LoW, 1, 1, 0, 6, 5, 1.3, "Easy Square");
        level.randomizeWallColor1();
        level.setWallDensity(3);

        exitWall = new Wall(5, 5, -Math.PI / 4, 8);
        exitWall.color1 = Color.GREEN;
        exitWall.color2 = Color.GREEN;
        LoW.add(exitWall);
        LoL.add(level);


//LEVEL 2 - TINY L


        Wall wall0 = new Wall(0, 0, Math.PI / 2, 60);
        wall0.color2 = Color.BLUE;

        Wall wall1 = new Wall(0, 60, 0, 50);
        wall1.color2 = Color.DEEPPINK;

        Wall wall2 = new Wall(50, 60, 3 * Math.PI / 2, 20);
        wall2.color2 = Color.BROWN;

        Wall wall3 = new Wall(20, 40, 0, 30);
        wall3.color2 = Color.YELLOW;

        Wall wall4 = new Wall(20, 0, Math.PI / 2, 40);
        wall4.color2 = Color.DARKGREY;

        Wall wall5 = new Wall(0, 0, 0, 20);
        wall5.color2 = Color.PURPLE;

        Wall exit = new Wall(45, 40, 1, 5 * Math.sqrt(2));
        exit.color1 = Color.GREEN;
        exit.color2 = Color.GREEN;

        LoW = new ArrayList<>();
        LoW.add(wall0);
        LoW.add(wall1);
        LoW.add(wall2);
        LoW.add(wall3);
        LoW.add(wall4);
        LoW.add(wall5);
        LoW.add(exit);
        level = new Level(LoW, 2, 4, 0,
                50, 40, 10, "Tiny L");
        level.scale(0.4);
        level.setWallDensity(1.8);
        LoL.add(level);


//LEVEL 3 - G
        url = getClass().getResource("gmap.txt");
        LoW = wb.wallsFromFile(url);
        level = new Level(LoW, 14, 1, Math.PI, 10.5, 3.5, 1.3, "G");
//            level.randomizeWallColor1();
        level.setWallDensity(3.6);
        for (Wall w : LoW)
            w.color1 = Color.PALEVIOLETRED;
        exitWall = new Wall(11, 3, -5 * Math.PI / 4, 2);
        exitWall.color1 = Color.GREEN;
        exitWall.color2 = Color.GREEN;
        LoW.add(exitWall);
        LoL.add(level);


//LEVEL 4 - THE I
        Wall wallI0 = new Wall(0, 0, Math.PI / 2, 40);
        wallI0.color2 = Color.BLUE;
        Wall wallI1 = new Wall(0, 40, 0, 25);
        wallI1.color2 = Color.DEEPPINK;
        Wall wallI2 = new Wall(25, 25, Math.PI / 2, 15);
        wallI2.color2 = Color.YELLOW;
        Wall wallI3 = new Wall(25, 0, Math.PI / 2, 15);
        wallI3.color2 = Color.NAVY;
        Wall wallI4 = new Wall(0, 0, 0, 25);
        wallI4.color2 = Color.FUCHSIA;
        Wall wallI5 = new Wall(25, 15, 0, 15);
        wallI5.color2 = Color.LIME;
        Wall wallI6 = new Wall(25, 25, 0, 15);
        wallI6.color2 = Color.MAROON;
        Wall wallI7 = new Wall(40, 25, Math.PI / 2, 15);
        wallI7.color2 = Color.ORCHID;
        Wall wallI8 = new Wall(40, 0, Math.PI / 2, 15);
        wallI8.color2 = Color.ROYALBLUE;
        Wall wallI9 = new Wall(40, 40, 0, 25);
        wallI9.color2 = Color.DARKVIOLET;
        Wall wallI10 = new Wall(40, 0, 0, 25);
        wallI10.color2 = Color.LIGHTGRAY;
        Wall wallI11 = new Wall(65, 0, Math.PI / 2, 40);
        wallI11.color2 = Color.GOLDENROD;
        Wall wallIExit = new Wall(40, 35, Math.PI / 4, 5 * Math.sqrt(2));
        wallIExit.color1 = Color.GREEN;
        wallIExit.color2 = Color.GREEN;
        LoW = new ArrayList<>();
        LoW.add(wallI0);
        LoW.add(wallI1);
        LoW.add(wallI2);
        LoW.add(wallI3);
        LoW.add(wallI4);
        LoW.add(wallI5);
        LoW.add(wallI6);
        LoW.add(wallI7);
        LoW.add(wallI8);
        LoW.add(wallI9);
        LoW.add(wallI10);
        LoW.add(wallI11);
        LoW.add(wallIExit);
        LoL.add(new Level(LoW, 2, 4, Math.PI,
                40, 40, 10, "The I"));


        // Level 5
        url = getClass().getResource("AJLevel2.txt");
        LoW = wb.wallsFromFile(url);
        level = new Level(LoW, 1, 1, 0, 1, 9, 1, "Baby Hell");
        level.setWallDensity(3.6);
//          for (Wall w : LoW)
//            w.color1 = Color.PALEVIOLETRED;
        level.randomizeWallColor1();
        exitWall = new Wall(1, 9, 0, 1);
        exitWall.color1 = Color.GREEN;
        exitWall.color2 = Color.GREEN;
        LoW.add(exitWall);
        LoL.add(level);

        // Level 6
        url = getClass().getResource("AJLevel3.txt");
        LoW = wb.wallsFromFile(url);
        level = new Level(LoW, 1, 1, 0, 14, 13, .8, "Queue");
        level.randomizeWallColor1();
        level.setWallDensity(3.6);
        for (Wall w : LoW)
          w.color1 = Color.PALEVIOLETRED;
        exitWall = new Wall(14, 13, 0, 1);
        exitWall.color1 = Color.GREEN;
        exitWall.color2 = Color.GREEN;
        LoW.add(exitWall);
        LoL.add(level);

        // Level 7
        url = getClass().getResource("AJLevel4.txt");
        LoW = wb.wallsFromFile(url);
        level = new Level(LoW, 1, 1, 0, 0, 7.5, 1, "Blades");
        level.setWallDensity(3.6);
//          for (Wall w : LoW)
//            w.color1 = Color.PALEVIOLETRED;
        level.randomizeWallColor1();
        exitWall = new Wall(0, 7.5, -1 * Math.PI / 4, 2);
        exitWall.color1 = Color.GREEN;
        exitWall.color2 = Color.GREEN;
        LoW.add(exitWall);
        LoL.add(level);


        //Level 8 Baddie boys
        url = getClass().getResource("BaddieBoyz");
        LoW = wb.wallsFromFile(url);
        level = new Level(LoW, 1, 1, 0, 24, 6, 2.5, "Baddie Intro");
        level.setWallDensity(2);
//          for (Wall w : LoW)
//            w.color1 = Color.PALEVIOLETRED;
        level.randomizeWallColor1();
        exitWall = new Wall(24, 6, -1 * Math.PI / 4, 2);
        exitWall.color1 = Color.GREEN;
        exitWall.color2 = Color.GREEN;

        // Make baddies
        Baddie baddie = new Baddie(3, 1.5, 1);
        List<Point2D> baddiePoints = new ArrayList<>(3);
        baddiePoints.add(new Point2D.Double(6, 2));
        baddiePoints.add(new Point2D.Double(3, 5.5));
        baddiePoints.add(new Point2D.Double(3, 1.5));
        baddie.setPatrol(baddiePoints, 3);
        level.addBaddie(baddie);
        //
        baddie = new Baddie(9, 1.5, 1);
        baddiePoints = new ArrayList<>(3);
        baddiePoints.add(new Point2D.Double(9, 4));
        baddiePoints.add(new Point2D.Double(12, 5.5));
        baddiePoints.add(new Point2D.Double(9, 1.5));
        baddie.setPatrol(baddiePoints, 3);
        level.addBaddie(baddie);
        //
        baddie = new Baddie(14, 0.5, 1);
        baddiePoints = new ArrayList<>(6);
        baddiePoints.add(new Point2D.Double(16, 3));
        baddiePoints.add(new Point2D.Double(14, 5.5));
        baddiePoints.add(new Point2D.Double(16, 3));
        baddiePoints.add(new Point2D.Double(14, 0.5));
        baddiePoints.add(new Point2D.Double(16, 3));
        baddiePoints.add(new Point2D.Double(14, 0.5));
        baddie.setPatrol(baddiePoints, 3);
        level.addBaddie(baddie);
        //
        baddie = new Baddie(2, 3, 2);
        baddiePoints = new ArrayList<>(2);
        baddiePoints.add(new Point2D.Double(23, 3));
        baddiePoints.add(new Point2D.Double(2, 3));
        baddie.setPatrol(baddiePoints, 7);
        level.addBaddie(baddie);


        LoW.add(exitWall);
        LoL.add(level);


// Level 666 Hell
        url = getClass().getResource("Hell.txt");
        LoW = wb.wallsFromFile(url);
        Level hmap = new Level(LoW, 31, 27, 0, 16, 1, 2, "Hell");
        hmap.scale(1);
        hmap.setWallDensity(4);
        System.out.println(LoW);
        LoL.add(hmap);

        for (Wall w : LoW) {
          w.color1 = Color.rgb(
                  (int) (Math.random() * 255),
                  (int) (Math.random() * 255),
                  (int) (Math.random() * 255));
        }

        return LoL;
    }

}
