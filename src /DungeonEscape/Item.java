package DungeonEscape;

public class Item {
    public enum ItemType {KEY, HEALTH_POTION, SWORD}
    
    private ItemType type;
    private int x, y;
    
    public Item(ItemType t, int x, int y) {
        this.type = t;
        this.x = x;
        this.y = y;
    }
    
    public ItemType getType() {
        return type;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public char getTypeChar() {
        switch(type) {
            case KEY: return 'K';
            case HEALTH_POTION: return 'H';
            case SWORD: return 'S';
            default: return '?';
        }
    }
}