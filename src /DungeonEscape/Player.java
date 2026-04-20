package DungeonEscape;
public class Player {
    private int x, y, health = 100, maxHealth = 100;
    private int attack = 10, defense = 3;
    private int monstersDefeated = 0;
    private boolean hasKey = false;
    private boolean hasSword = false;  // Added this field
    
    public void takeDamage(int d) { health = Math.max(0, health - d); }
    public void heal(int a) { health = Math.min(maxHealth, health + a); }
    
    public int getX() { return x; } 
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; } 
    public void setY(int y) { this.y = y; }
    
    public int getHealth() { return health; } 
    public void setHealth(int h) { health = h; }
    
    public int getMaxHealth() { return maxHealth; }
    
    public int getAttack() { return attack; }
    public void setAttack(int a) { attack = a; }
    
    public int getDefense() { return defense; }
    public void setDefense(int d) { defense = d; }
    
    public boolean hasKey() { return hasKey; } 
    public void setHasKey(boolean k) { hasKey = k; }
    
    // Added these methods for sword
    public boolean hasSword() { return hasSword; }
    public void setHasSword(boolean s) { hasSword = s; }
    
    public int getMonstersDefeated() { return monstersDefeated; }
    public void setMonstersDefeated(int m) { monstersDefeated = m; }
}