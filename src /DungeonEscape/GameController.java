package DungeonEscape;

import java.util.Random;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class GameController {
    private GameState gameState;
    private GameFrame gameFrame;
    private Random random = new Random();

    public void startGame() {
        gameState = new GameState(this);
        gameFrame = new GameFrame(this);
    }

    public void movePlayer(int dx, int dy) {
        if (gameState.isInCombat()) {
            gameFrame.addMessage("You can't move during combat!");
            return;
        }

        Player player = gameState.getPlayer();
        DungeonMap map = gameState.getDungeonMap();
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        if (!map.isValidPosition(newX, newY)) {
            gameFrame.addMessage("You can't move there!");
            return;
        }

        char cell = map.getCell(newX, newY);
        switch (cell) {
            case 'M':
                startCombat(newX, newY);
                break;
            case '.':
                moveTo(newX, newY);
                break;
            case 'K':
                collectKey(newX, newY);
                break;
            case 'H':
                collectHealthPotion(newX, newY);
                break;
            case 'S':
                collectSword(newX, newY);
                break;
            case 'E':
                tryExit(newX, newY);
                break;
            default:
                gameFrame.addMessage("Blocked!");
        }

        gameState.incrementTurnCount();
        SwingUtilities.invokeLater(() -> {
            gameFrame.updateDisplay();
            gameFrame.updatePlayerStats();
        });
    }

    private void startCombat(int x, int y) {
        Monster monster = gameState.getMonsterAt(x, y);
        if (monster != null) {
            gameState.setInCombat(true);
            gameState.setCurrentCombatMonster(monster);
            gameFrame.showCombatUI();
            gameFrame.addMessage("Combat with " + monster.getType() + "!");
        }
    }

    private void moveTo(int x, int y) {
        Player player = gameState.getPlayer();
        DungeonMap map = gameState.getDungeonMap();
        
        map.setCell(player.getX(), player.getY(), '.');
        player.setX(x);
        player.setY(y);
        map.setCell(x, y, 'P');
    }

    private void tryExit(int x, int y) {
        Player player = gameState.getPlayer();
        if (player.hasKey()) {
            int choice = JOptionPane.showConfirmDialog(
                gameFrame,
                "You escaped with the key!\nScore: " + calculateScore() + "\nExit game?",
                "Victory!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (player.getMonstersDefeated() >= 5) {
            int choice = JOptionPane.showConfirmDialog(
                gameFrame,
                "You forced the exit open!\nScore: " + calculateScore() + "\nExit game?",
                "Victory!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            gameFrame.addMessage("The exit is locked. Find a key or defeat 5 monsters!");
        }
    }

    private int calculateScore() {
        Player player = gameState.getPlayer();
        return (player.getMonstersDefeated() * 100) + 
               (player.hasKey() ? 500 : 0) + 
               (player.hasSword() ? 200 : 0) +  // Fixed this line
               (player.getHealth() * 2);
    }

    private void collectKey(int x, int y) {
        gameState.getPlayer().setHasKey(true);
        removeItem(x, y, 'K');
        gameFrame.addMessage("You found a key!");
        moveTo(x, y);
    }

    private void collectHealthPotion(int x, int y) {
        Player player = gameState.getPlayer();
        player.heal(30);
        removeItem(x, y, 'H');
        gameFrame.addMessage("Health potion restored 30 HP!");
        moveTo(x, y);
    }

    private void collectSword(int x, int y) {
        Player player = gameState.getPlayer();
        player.setHasSword(true);
        player.setAttack(player.getAttack() + 5); // Increase attack power
        removeItem(x, y, 'S');
        gameFrame.addMessage("You equipped the sword (+5 attack)!");
        moveTo(x, y);
    }

    private void removeItem(int x, int y, char type) {
        gameState.getItems().removeIf(item ->
            item.getX() == x && item.getY() == y && item.getTypeChar() == type);
        gameState.getDungeonMap().setCell(x, y, '.');
    }

    public void playerAttack() {
        if (!gameState.isInCombat()) return;
        
        Monster monster = gameState.getCurrentCombatMonster();
        Player player = gameState.getPlayer();
        
        int damage = player.getAttack();
        monster.setHealth(monster.getHealth() - damage);
        
        gameFrame.addMessage("You hit the " + monster.getType() + " for " + damage + " damage!");
        
        if (monster.getHealth() <= 0) {
            gameFrame.addMessage("You defeated the " + monster.getType() + "!");
            gameState.getMonsters().remove(monster);
            gameState.setInCombat(false);
            gameState.setCurrentCombatMonster(null);
            gameFrame.hideCombatUI();
            player.setMonstersDefeated(player.getMonstersDefeated() + 1);
        } else {
            monsterAttack();
        }
        gameFrame.updatePlayerStats();
    }
    
    public void monsterAttack() {
        Monster monster = gameState.getCurrentCombatMonster();
        Player player = gameState.getPlayer();
        
        int damage = Math.max(1, monster.getAttack() - player.getDefense());
        player.takeDamage(damage);
        
        gameFrame.addMessage("The " + monster.getType() + " hits you for " + damage + " damage!");
        
        if (player.getHealth() <= 0) {
            int choice = JOptionPane.showConfirmDialog(
                gameFrame,
                "You have been defeated!\nScore: " + calculateScore() + "\nExit game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else {
                player.setHealth(player.getMaxHealth());
                gameState.setInCombat(false);
                gameFrame.hideCombatUI();
            }
        }
        gameFrame.updatePlayerStats();
    }
    
    public void playerFlee() {
        if (!gameState.isInCombat()) return;
        
        gameFrame.addMessage("You fled from combat!");
        gameState.setInCombat(false);
        gameState.setCurrentCombatMonster(null);
        gameFrame.hideCombatUI();
    }

    public GameState getGameState() { return gameState; }
    public Random getRandom() { return random; }
}