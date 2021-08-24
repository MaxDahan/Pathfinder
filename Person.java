import java.awt.Point;

public class Person {
    public Point position;
    public Character symbol;

    public Person(int x, int y, Character symbol) {
        setup();
        position.x = x;
        position.y = y;
        this.symbol = symbol;
    }
    public void setup() {
        position = new Point();
    }
}
