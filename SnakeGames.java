package snake.games; // Keep this package if you are using an IDE like NetBeans

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Main class for the Snake Game.
 * Sets up the JFrame and adds the GamePanel.
 */
public class SnakeGames extends JFrame {

    public SnakeGames() {
        // Create and set up the game panel
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel); // Add the game panel to the frame

        // Set frame properties
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        this.setResizable(false); // Prevent resizing
        this.pack(); // Sizes the frame so that all its contents are at or above their preferred sizes
        this.setLocationRelativeTo(null); // Center the window on the screen
        this.setVisible(true); // Make the frame visible
    }

    /**
     * Main method to start the Snake Game.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Run the game on the Event Dispatch Thread (EDT) for Swing applications
        SwingUtilities.invokeLater(() -> {
            new SnakeGames();
        });
    }
}

/**
 * GamePanel handles all the game logic and drawing.
 * It extends JPanel and implements ActionListener for game updates
 * and KeyListener for user input.
 */
class GamePanel extends JPanel implements ActionListener {

    // --- Game Constants ---
    private final int SCREEN_WIDTH = 600; // Width of the game screen
    private final int SCREEN_HEIGHT = 600; // Height of the game screen
    private final int UNIT_SIZE = 25; // Size of each game unit (snake body part, food)
    private final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // Total number of game units
    private final int DELAY = 100; // Delay in milliseconds for game updates (controls snake speed)

    // --- Game State Variables ---
    private final int x[] = new int[GAME_UNITS]; // X coordinates of snake body parts
    private final int y[] = new int[GAME_UNITS]; // Y coordinates of snake body parts
    private int bodyParts = 6; // Initial number of snake body parts
    private int applesEaten; // Score: number of apples eaten
    private int appleX; // X coordinate of the food
    private int appleY; // Y coordinate of the food
    private char direction = 'R'; // Initial direction of the snake (R=Right, L=Left, U=Up, D=Down)
    private boolean running = false; // Is the game running?
    private Timer timer; // Timer for the game loop
    private Random random; // Random number generator for food placement

    private JButton retryButton; // Button to retry the game

    /**
     * Constructor for the GamePanel.
     * Initializes game components and starts the game.
     */
    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Set panel size
        this.setBackground(Color.BLACK); // Set background color
        this.setFocusable(true); // Make panel focusable to receive key events
        this.addKeyListener(new MyKeyAdapter()); // Add key listener for user input

        // Initialize and configure the retry button
        retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Ink Free", Font.BOLD, 30));
        retryButton.setBackground(Color.DARK_GRAY);
        retryButton.setForeground(Color.WHITE);
        retryButton.setFocusable(false); // Prevents the button from stealing focus from the panel
        retryButton.addActionListener(e -> restartGame()); // Add action listener for retry

        // Use GridBagLayout for centering the button
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        // Position the button in a new row (gridy=1) to place it below the drawn "Game Over" text.
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        // Add top padding to create space between the text and the button.
        gbc.insets = new Insets(40, 0, 10, 0);
        this.add(retryButton, gbc);
        retryButton.setVisible(false); // Hide button initially

        startGame(); // Start the game
    }

    /**
     * Initializes the game variables and starts the game timer.
     */
    public void startGame() {
        // Reset snake position and length
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R'; // Reset initial direction
        newApple(); // Generate the first food item

        running = true; // Set game state to running
        if (timer != null) {
            timer.stop(); // Stop existing timer if any
        }
        timer = new Timer(DELAY, this); // Create a new timer with the specified delay and this panel as listener
        timer.start(); // Start the timer

        retryButton.setVisible(false); // Ensure button is hidden when game starts
        this.requestFocusInWindow(); // Request focus back to the panel for key events
    }

    /**
     * Restarts the game when the retry button is pressed.
     */
    private void restartGame() {
        startGame();
    }

    /**
     * Overrides the paintComponent method to draw game elements.
     * This method is called automatically when the component needs to be redrawn.
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call superclass method
        draw(g); // Call custom draw method
    }

    /**
     * Custom draw method to render game objects.
     * @param g The Graphics object.
     */
    public void draw(Graphics g) {
        if (running) {
            // Draw grid lines (optional, good for debugging)
            // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            //     g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // Vertical lines
            //     g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // Horizontal lines
            // }

            // Draw food
            g.setColor(Color.RED); // Set color to red for food
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // Draw a filled oval for the food

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) { // Snake's head
                    g.setColor(Color.GREEN); // Head color
                } else { // Snake's body
                    g.setColor(new Color(45, 180, 0)); // Body color
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // Draw a filled rectangle for each body part
            }

            // Draw score
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g); // If game is not running, draw game over screen
        }
    }

    /**
     * Generates a new apple (food) at a random valid location.
     */
    public void newApple() {
        // Randomly place apple within the game screen, aligning to the grid
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    /**
     * Moves the snake based on its current direction.
     * Shifts all body parts, then updates the head's position.
     */
    public void move() {
        // Shift all body parts by one position to the previous part's location
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Update the head's position based on the current direction
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE; // Move head up
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE; // Move head down
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE; // Move head left
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; // Move head right
                break;
        }
    }

    /**
     * Checks if the snake has eaten the food.
     * If so, increases score, body parts, and generates new food.
     */
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) { // If head collides with apple
            bodyParts++; // Increase snake length
            applesEaten++; // Increase score
            newApple(); // Generate a new apple
        }
    }

    /**
     * Checks for collisions:
     * - Head collides with body.
     * - Head collides with walls.
     * If a collision occurs, sets running to false.
     */
    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) { // If head's coordinates match any body part's coordinates
                running = false; // Game over
                break;
            }
        }
        // Check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (x[0] >= SCREEN_WIDTH) {
            running = false;
        }
        // Check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop(); // Stop the game timer if game is over
            retryButton.setVisible(true); // Show the retry button
        }
    }

    /**
     * Draws the "Game Over" screen.
     * @param g The Graphics object.
     */
    public void gameOver(Graphics g) {
        // Final Score text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        // Position score text well above the center to make space for "Game Over" and button
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, SCREEN_HEIGHT / 2 - 100);

        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        // Position game over text above the center, but below the score, creating a logical flow
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2 - 20);

    }

    /**
     * This method is called repeatedly by the Timer.
     * It updates the game state and redraws the panel.
     * @param e The ActionEvent generated by the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move(); // Move the snake
            checkApple(); // Check for food collision
            checkCollisions(); // Check for wall or self-collision
        }
        repaint(); // Request the panel to repaint itself (calls paintComponent)
    }

    /**
     * Inner class to handle keyboard input.
     * Extends KeyAdapter for convenience.
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') { // Prevent turning back on itself
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') { // Prevent turning back on itself
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') { // Prevent turning back on itself
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') { // Prevent turning back on itself
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
