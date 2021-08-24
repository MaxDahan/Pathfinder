import java.awt.Point;
import java.util.Arrays;

public class Wall {
    public Character[][] positions;
    public Character symbol;

    public Wall(Character symbol, Character spaceSymbol, int gridWidth, int gridHeight) {
        this.symbol = symbol;
        positions = new Character[gridHeight][gridWidth];
        for (int i = 0; i < positions.length; i++) {
            Arrays.fill(positions[i], spaceSymbol);
        }
    }
    
    public void generateWall(int x, int y, int width, int height) { // generates a list of positions corresponding to an x, y, width, and height
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                positions[j + y][i + x] = symbol;
            }
        }
    }

    public boolean colliding(Point point) {
        if(positions[point.y][point.x] == symbol) {
            return true;
        }
        
        return false;
    }
}