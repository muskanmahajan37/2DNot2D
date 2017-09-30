import com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter;
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

import java.io.FileNotFoundException;
import java.net.URL;
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
            game.createCanvas(initLevels().get(0));
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

        List<Wall> walls = new ArrayList<Wall>();
        walls.add(new Wall(10, 0, Math.PI / 3, 100));
        Level l = new Level(walls, 0, 0, 0, 10, 0, 1, "testmap");

        levels.add(l);

        return levels;
    }

    public List<Level> initLevels(){
        WallBuilder wb = new WallBuilder();

        List<Level> LoL = new ArrayList<Level>();

        try {
            URL urlh = getClass().getResource("hmap.txt");

            System.out.println(urlh.getPath());
            List<Wall> LoW1 = wb.wallsFromFile(urlh.getPath());
            Wall exith = new Wall(15, 0, Math.PI / 4, 3 * Math.sqrt(2));
            Level hmap = new Level(LoW1,  1, 7, 0, 16, 1, 2, "H");
            LoL.add(hmap);

            URL urlg = getClass().getResource("gmap.txt");
            List<Wall> LoW2 = wb.wallsFromFile(urlg.getPath());
            Wall exitg = new Wall(11, 5, 0, 1);
            Level gmap = new Level(LoW2,  13, 8, Math.PI / 2, 11, 5, 2, "G");
            LoL.add(gmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return LoL;
    }

}
