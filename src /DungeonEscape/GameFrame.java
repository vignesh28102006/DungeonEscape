package DungeonEscape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {
    private GameController controller;
    private JPanel dungeonPanel;
    private JPanel combatPanel;
    private JTextArea messageArea;
    private JPanel statsPanel;
    private JLabel healthLabel;
    private JLabel attackLabel;
    private JLabel defenseLabel;
    private JLabel keysLabel;
    private JLabel monstersLabel;

    public GameFrame(GameController controller) {
        this.controller = controller;
        setupUI();
        setupStatsPanel();
        setupInput();
        setupWindowListener(); // Now this method exists
        updateDisplay();
        updatePlayerStats();
        setVisible(true);
    }

    private void setupUI() {
        setTitle("Dungeon Escape");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Dungeon Panel
        dungeonPanel = new JPanel(new GridLayout(20, 20)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 800);
            }
        };
        initializeDungeonCells();
        add(new JScrollPane(dungeonPanel), BorderLayout.CENTER);
        
        // Combat Panel
        combatPanel = new JPanel(new GridLayout(2, 2));
        JButton attackButton = new JButton("Attack");
        attackButton.addActionListener(e -> controller.playerAttack());
        combatPanel.add(attackButton);
        
        JButton fleeButton = new JButton("Flee");
        fleeButton.addActionListener(e -> controller.playerFlee());
        combatPanel.add(fleeButton);
        
        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener(e -> confirmExit()); // Now this method exists
        combatPanel.add(exitButton);
        
        combatPanel.setVisible(false);
        add(combatPanel, BorderLayout.EAST);
        
        // Message Area
        messageArea = new JTextArea(10, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        add(new JScrollPane(messageArea), BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
    }

    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }
    
    private void setupInput() {
        dungeonPanel.setFocusable(true);
        dungeonPanel.requestFocusInWindow();
        
        dungeonPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0;
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:    dy = -1; break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:  dx = -1; break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:  dy = 1; break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT: dx = 1; break;
                }
                if(dx != 0 || dy != 0) {
                    controller.movePlayer(dx, dy);
                }
            }
        });
    }
    
    public void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit Dungeon Escape?",
            "Exit Game",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void setupStatsPanel() {
        statsPanel = new JPanel(new GridLayout(5, 1));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Player Stats"));
        
        healthLabel = new JLabel("Health: 100/100");
        attackLabel = new JLabel("Attack: 10");
        defenseLabel = new JLabel("Defense: 3");
        keysLabel = new JLabel("Keys: 0");
        monstersLabel = new JLabel("Monsters Defeated: 0");
        
        statsPanel.add(healthLabel);
        statsPanel.add(attackLabel);
        statsPanel.add(defenseLabel);
        statsPanel.add(keysLabel);
        statsPanel.add(monstersLabel);
        
        add(statsPanel, BorderLayout.WEST);
    }

    private void initializeDungeonCells() {
        for (int i = 0; i < 400; i++) {
            JLabel cell = new JLabel("", SwingConstants.CENTER);
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cell.setOpaque(true);
            cell.setFont(new Font("Arial", Font.BOLD, 12));
            dungeonPanel.add(cell);
        }
    }
    
    public void updateDisplay() {
        char[][] grid = controller.getGameState().getDungeonMap().getGrid();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                JLabel cell = (JLabel) dungeonPanel.getComponent(i * 20 + j);
                char c = grid[i][j];
                cell.setText(c == '.' ? "" : String.valueOf(c));
                
                // Set colors
                if (c == 'P') {
                    cell.setBackground(Color.BLUE);
                    cell.setForeground(Color.WHITE);
                } else if (c == 'M') {
                    cell.setBackground(Color.RED);
                    cell.setForeground(Color.WHITE);
                } else if (c == 'K') {
                    cell.setBackground(Color.YELLOW);
                } else if (c == 'H') {
                    cell.setBackground(Color.GREEN);
                } else if (c == 'E') {
                    cell.setBackground(Color.ORANGE);
                } else if (c == 'S') {
                    cell.setBackground(Color.CYAN);
                } else {
                    cell.setBackground(Color.WHITE);
                }
            }
        }
        dungeonPanel.repaint();
    }
    
    public void addMessage(String message) {
        messageArea.append(message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }
    
    public void updatePlayerStats() {
        Player player = controller.getGameState().getPlayer();
        healthLabel.setText(String.format("Health: %d/%d", player.getHealth(), player.getMaxHealth()));
        attackLabel.setText("Attack: " + player.getAttack());
        defenseLabel.setText("Defense: " + player.getDefense());
        keysLabel.setText("Keys: " + (player.hasKey() ? "1" : "0"));
        monstersLabel.setText("Monsters Defeated: " + player.getMonstersDefeated());
    }
    
    public void showCombatUI() { 
        combatPanel.setVisible(true); 
        dungeonPanel.setEnabled(false);
    }
    
    public void hideCombatUI() { 
        combatPanel.setVisible(false); 
        dungeonPanel.setEnabled(true);
        dungeonPanel.requestFocusInWindow();
    }
}