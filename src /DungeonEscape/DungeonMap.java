package DungeonEscape;

import java.util.Random;

public class DungeonMap {
    private char[][] grid;
    private Random random = new Random();
    
    public DungeonMap(int width, int height) {
        grid = new char[width][height];
        generateOpenWorld();
    }
    
    public void generateOpenWorld() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = '.'; // All cells are open
            }
        }
    }
    
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
    
    public char getCell(int x, int y) {
        return isValidPosition(x, y) ? grid[x][y] : '#';
    }
    
    public void setCell(int x, int y, char c) {
        if (isValidPosition(x, y)) {
            grid[x][y] = c;
        }
    }
    
    public int getRandomX() {
        return random.nextInt(grid.length);
    }
    
    public int getRandomY() {
        return random.nextInt(grid[0].length);
    }
    
    public char[][] getGrid() {
        return grid;
    }
}