# Java Snake Game

# 🐍 Snake Game System Explanation
This is a classic Snake game implemented in Java Swing with modern features like a restart button and score tracking. Let me break down the system:

# 🎮 Game Overview
A grid-based snake game where the player controls a snake to eat food (apples), grow longer, and avoid collisions with walls and itself.


# 🏗️ Architecture

SnakeGames (JFrame)
  - Main window container
  - Sets up the game panel in a centered, non-resizable window
  - Uses SwingUtilities.invokeLater() for thread-safe GUI initialization

GamePanel (JPanel)
  - Core game logic and rendering
  - Implements ActionListener for game loop
  - Extends JPanel for drawing capabilities
  - Contains inner class MyKeyAdapter for keyboard input

# 🔧 Game Components

Visual Elements
  - Snake: Green head with darker green body segments
  - Food (Apple): Red oval
  - Grid: Invisible 25x25 pixel unit grid (600×600 screen with UNIT_SIZE=25)
  - Score Display: Top-centered red text
  - Game Over Screen: "Game Over" text with final score and retry button


# 🎯 Game Logic Flow

1. Initialization
   Constructor → startGame() → newApple() → timer.start()


2. Game Loop (Timer-driven)
   Every DELAY (100ms):
   1. move() - Update snake position
   2. checkApple() - Check food collision
   3. checkCollisions() - Check game-ending collisions
   4. repaint() - Redraw screen


3. Movement System
   - Coordinate Arrays: Store positions of all snake segments
   - Shift Algorithm: Each segment takes position of previous segment
   - Head Update: New head position based on current direction
   - Grid-based: Movement in UNIT_SIZE (25px) increments


4. Collision Detection
  - Self-collision: Head coordinates match any body segment
  - Wall collision: Head goes outside screen boundaries (0-600)
  - Food collision: Head coordinates match apple coordinates


5. Food System
  - Random Placement: Uses Random class to generate grid-aligned positions
  - Growth Mechanic: Eating food increases bodyParts and applesEaten



# ⚙️ Technical Details

Grid System
   - 24×24 grid (600/25 = 24 units per dimension)
   - Total Units: 576 possible positions (24×24)
   - Coordinate System: Top-left origin (0,0) to bottom-right (575,575)

Performance Optimizations
  - Timer-based Updates: Fixed 100ms intervals (10 FPS game logic)
  - Conditional Rendering: Only draws grid lines when debugging
  - Efficient Collision: Simple coordinate comparisons

# 🎛️ Game Parameters
  - Speed: DELAY = 100ms (adjustable for difficulty)
  - Initial Length: 6 segments
  - Screen Size: 600×600 pixels
  - Unit Size: 25 pixels (trade-off between smoothness and performance)

# 🔄 State Transitions

<img width="275" height="40" alt="image" src="https://github.com/user-attachments/assets/f88b5d59-5694-479b-bc83-d3958e335a79" />


# 📊 Scoring System
  - 1 point per apple
  - Visual feedback: Score updates in real-time
  - Final score display: Shown on game over screen


# Summary 

This implementation demonstrates clean object-oriented design, proper Swing usage, and classic game development principles in Java. It's an excellent educational example of real-time game logic with graphical user interfaces!







