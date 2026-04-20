package DungeonEscape;

public class Monster {
    private String type;
    private int x, y, health, attack, defense;
    
    public Monster(String t, int x, int y) {
        this.type = t;
        this.x = x;
        this.y = y;
        switch(type) {
            case "Goblin": 
                health = 30; attack = 8; defense = 3; break;
            case "Orc": 
                health = 50; attack = 12; defense = 5; break;
            case "Troll":
                health = 70; attack = 15; defense = 8; break;
            default: 
                health = 20; attack = 5; defense = 1;
        }
    }
    
    public String getType() { return type; }
    public int getX() { return x; } 
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; } 
    public void setY(int y) { this.y = y; }
    
    public int getHealth() { return health; } 
    public void setHealth(int h) { health = h; }
    
    public int getAttack() { return attack; } 
    public void setAttack(int a) { attack = a; }
    
    public int getDefense() { return defense; } 
    public void setDefense(int d) { defense = d; }
}