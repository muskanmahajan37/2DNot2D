import model.Equation;
import model.Player;
import model.Wall;

public class TestCramer {

    public static void main(String[] args) {
        Player player = new Player(0, 0, 0);
        Wall wall = new Wall(10, -5, Math.PI / 2, 1000);

        Equation e = new Equation(player.viewLine(0), wall);

        System.out.println(e.distanceAlongViewLine());
        System.out.println(e.distanceAlongWall());
    }
}
