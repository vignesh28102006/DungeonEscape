package DungeonEscape;
import java.util.*;

public class GameState {
    private Player player = new Player();
    private DungeonMap dungeonMap = new DungeonMap(20, 20); // Larger map
    private List<Monster> monsters = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private boolean isNight = false;
    private int level = 1, score = 0, turnCount = 0;
    private boolean inCombat = false;
    private Monster currentCombatMonster;
    private GameController controller;

    public GameState(GameController c) {
        this.controller = c;
        initializeLevel();
    }
    
    private void initializeLevel() {
        dungeonMap.generateOpenWorld();
        placePlayer();
        placeExit();
        placeMonsters();
        placeItems();
    }

    private void placeMonsters() {
        for (int i = 0; i < 5 + level; i++) { // More monsters
            int x, y;
            do {
                x = dungeonMap.getRandomX();
                y = dungeonMap.getRandomY();
            } while (dungeonMap.getCell(x, y) != '.');

            String[] types = {"Goblin", "Orc", "Troll"};
            String type = types[controller.getRandom().nextInt(types.length)];
            monsters.add(new Monster(type, x, y));
            dungeonMap.setCell(x, y, 'M');
        }
    }

    private void placePlayer() {
        int x, y;
        do {
            x = dungeonMap.getRandomX();
            y = dungeonMap.getRandomY();
        } while (dungeonMap.getCell(x, y) != '.');
        player.setX(x);
        player.setY(y);
        dungeonMap.setCell(x, y, 'P');
    }
    
    private void placeExit() {
        int x, y;
        do {
            x = dungeonMap.getRandomX();
            y = dungeonMap.getRandomY();
        } while (dungeonMap.getCell(x, y) != '.');
        dungeonMap.setCell(x, y, 'E');
    }

    private void placeItems() {
        // Place 3-5 keys
        for (int i = 0; i < 3 + controller.getRandom().nextInt(3); i++) {
            placeRandomItem(Item.ItemType.KEY, 'K');
        }
        // Place health potions
        for (int i = 0; i < 2 + controller.getRandom().nextInt(2); i++) {
            placeRandomItem(Item.ItemType.HEALTH_POTION, 'H');
        }
        // Place swords
        placeRandomItem(Item.ItemType.SWORD, 'S');
    }

    private void placeRandomItem(Item.ItemType type, char symbol) {
        int x, y;
        do {
            x = dungeonMap.getRandomX();
            y = dungeonMap.getRandomY();
        } while (dungeonMap.getCell(x, y) != '.');
        
        items.add(new Item(type, x, y));
        dungeonMap.setCell(x, y, symbol);
    }

    public void incrementTurnCount() {
        turnCount++;
        isNight = turnCount % 20 == 0;
    }
    
    public Monster getMonsterAt(int x, int y) {
        return monsters.stream()
                     .filter(m -> m.getX() == x && m.getY() == y)
                     .findFirst()
                     .orElse(null);
    }

    public Player getPlayer() { return player; }
    public DungeonMap getDungeonMap() { return dungeonMap; }
    public List<Monster> getMonsters() { return monsters; }
    public List<Item> getItems() { return items; }
    public boolean isNight() { return isNight; }
    public int getLevel() { return level; }
    public int getScore() { return score; }
    public int getTurnCount() { return turnCount; }
    public boolean isInCombat() { return inCombat; }
    public Monster getCurrentCombatMonster() { return currentCombatMonster; }

    public void setInCombat(boolean b) { inCombat = b; }
    public void setCurrentCombatMonster(Monster m) { currentCombatMonster = m; }
}