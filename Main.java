import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public final int GRIDWIDTH = 11;
    public final int GRIDHEIGHT = 11;
    public Character[][] grid;
    public Person person;
    public Objective objective;
    public Wall wall;
    public Character spaceSymbol;
    public int waitTime;
    public Character[][] tempGrid;

    public Main() {
        setup();
        // runs update once to make sure the person hasn't spawned on the objective
        update();
        ArrayList<String> bp = bestPath(new ArrayList<String>(), person.position.x, person.position.y, new ArrayList<String>());
        movement(bp);
    }
    public void setup() { // sets up all the fields
        person = new Person(4, 6, 'p');
        objective = new Objective(5, 4, 'o');
        Character wallSymbol = '@';
        wall = new Wall(wallSymbol, spaceSymbol, GRIDWIDTH, GRIDHEIGHT);
        makeMaze();
        spaceSymbol = '-';

        waitTime = 500;

        grid = new Character[GRIDHEIGHT][GRIDWIDTH];
        tempGrid = grid;
    }
    public void makeMaze() {
        wall.generateWall(0, 0, 3, 1);
        wall.generateWall(0, 4, 2, 1);
        wall.generateWall(0, 7, 1, 3);
        
        wall.generateWall(1, 2, 4, 1);
        wall.generateWall(1, 5, 6, 1);
        
        wall.generateWall(2, 7, 2, 1);
        wall.generateWall(2, 9, 3, 1);
        
        wall.generateWall(3, 8, 7, 1);
        
        wall.generateWall(4, 1, 3, 1);
        wall.generateWall(4, 3, 4, 1);

        wall.generateWall(5, 6, 2, 1);

        wall.generateWall(6, 10, 3, 1);
        wall.generateWall(6, 4, 1, 1);

        wall.generateWall(8, 1, 1, 6);

        wall.generateWall(9, 1, 1, 1);
        wall.generateWall(9, 6, 1, 2);

        wall.generateWall(10, 3, 1, 2);
    }

    public ArrayList<String> bestPath(ArrayList<String> path, int movingX, int movingY, ArrayList<String> bp) { // finds the best path to the objective
        if(movingX == objective.position.x && movingY == objective.position.y) {
            if(path.size() < bp.size() || bp.size() <= 0) {
                return path;
            }
            return bp;
        }
        if(path.size() > bp.size() && bp.size() > 0) { // drops a path if it becomes longer than the shortest path
            return bp;
        }

        tempGrid[movingY][movingX] = 'x';

        if(movingX + 1 < GRIDWIDTH && tempGrid[movingY][movingX + 1] != 'x' && !wall.colliding(new Point(movingX + 1, movingY))) {
            ArrayList<String> s = new ArrayList<String>(path);
            s.add("right");
            bp = bestPath(s, movingX + 1, movingY, bp);
        }
        if(movingX - 1 > -1 && tempGrid[movingY][movingX - 1] != 'x' && !wall.colliding(new Point(movingX - 1, movingY))) {
            ArrayList<String> s = new ArrayList<String>(path);
            s.add("left");
            bp = bestPath(s, movingX - 1, movingY, bp);
        }
        if(movingY + 1 < GRIDHEIGHT && tempGrid[movingY + 1][movingX] != 'x' && !wall.colliding(new Point(movingX, movingY + 1))) {
            ArrayList<String> s = new ArrayList<String>(path);
            s.add("down");
            bp = bestPath(s, movingX, movingY + 1, bp);
        }
        if(movingY - 1 > -1 && tempGrid[movingY - 1][movingX] != 'x' && !wall.colliding(new Point(movingX, movingY - 1))) {
            ArrayList<String> s = new ArrayList<String>(path);
            s.add("up");
            bp = bestPath(s, movingX, movingY - 1, bp);
        }
        tempGrid[movingY][movingX] = spaceSymbol;
        return bp;
    }

    public boolean update() { // refreshes the grid, updates positions, and checks for win condition
        resetGrid();
        setWallPosition();
        if (setPersonPosition()) {
            printGrid();
            return true;
        }
        setObjectivePosition();
        printGrid();
        return false;
    }
    public void movement(ArrayList<String> bp) { // moves the person based on the calculated path
        for(String temp : bp) {
            if(temp.equals("right")) {
                person.position.x++;
            } else if(temp.equals("left")) {
                person.position.x--;
            } else if(temp.equals("down")) {
                person.position.y++;
            } else if(temp.equals("up")) {
                person.position.y--;
            } 
            
            wait(waitTime);
            update();
        }
    }   
    public boolean win() {
        return person.position.x == objective.position.x && person.position.y == objective.position.y;
    }

    // setting positions of objects on the grid
    public void setWallPosition() {
        for(int i = 0; i < wall.positions.length; i++) {
            for(int j = 0; j < wall.positions[i].length; j++) {
                if(wall.positions[i][j] == wall.symbol) {
                    grid[i][j] = wall.symbol;
                }
            }
        }
    }
    public boolean setPersonPosition() {
        if(wall.colliding(person.position)) {
            System.out.println("The person spawned in a wall. Fix it!");
            return false;
        } else if(win()) {
            grid[person.position.y][person.position.x] = person.symbol;
            System.out.println("Great job! The person made it to the objective!");
            return true;
        } else if(inBounds(person.position)) {
            grid[person.position.y][person.position.x] = person.symbol;
            return false;
        } else {
            System.out.println("The person's position is not real or is out of bounds. X: " + person.position.x + " Y: " + person.position.y);
            return false;
        }
    }
    public void setObjectivePosition() {
        if(wall.colliding(objective.position)) {
            System.out.println("The objective spawned in a wall. Fix it!");
        } else if(inBounds(objective.position)) {
            grid[objective.position.y][objective.position.x] = objective.symbol;
        } else {
            System.out.println("The objective's position is not real or is out of bounds. X: " + objective.position.x + " Y: " + objective.position.y);
        }
    }
    public boolean inBounds(Point point) {
        return point.x > -1 && point.y > -1 && point.x < GRIDWIDTH && point.y < GRIDHEIGHT;
    }

    // grid functions
    public void resetGrid() { // returns grid to a blanks state
        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(grid[i], spaceSymbol);
        }
    }
    public void printGrid() {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }
    public static void main(String[] args) {
        new Main();
    }
}