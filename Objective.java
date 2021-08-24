import java.awt.Point;

public class Objective {
    public Point position;
    public Character symbol;

    public Objective(int x, int y, Character symbol) {
        setup();
        position.x = x;
        position.y = y;
        this.symbol = symbol;
    }
    public void setup() {
        position = new Point();
    }
}