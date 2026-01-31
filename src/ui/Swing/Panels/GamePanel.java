package ui.Swing.Panels;

import controllers.KeyHandler;

import javax.swing.*;

import domain.Game;
import domain.Textures;

import listeners.GameListener;

import domain.level.GridDesign;
import domain.level.Hall;
import domain.level.Tile;
import domain.util.Coordinate;
import ui.Graphics.AgentGrapichs.*;
import ui.Graphics.ArrowGraphics;
import listeners.GameListener;
import ui.Graphics.AgentGrapichs.ArcherGraphics;
import ui.Graphics.AgentGrapichs.FighterGraphics;
import ui.Graphics.AgentGrapichs.PlayerGraphics;
import ui.Graphics.AgentGrapichs.WizardGraphics;
import ui.Graphics.EnchantmentGraphics;
import ui.Graphics.SwordGraphics;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// In the next generations of the game KeyListener will
// be replaced by KeyBindings

// Also after receiving the horizontal and vertical squares, width heights
// and the bounds have to be final

// GameSettingsPanel will take GamePanel and will change
// FPS,ETC
public class GamePanel extends JPanel implements MouseListener, GameListener {

    // Screen settings each ca
    private final int baseTileSize = 36; // Objects will be 64x64
    //private double scale = 1.4;

    private int scalingFactor = 1; // Going to be an input for different resolutions etc

    private int horizontalSquares = 16; // how many squares in the x direction
    private int width = horizontalSquares * (scalingFactor * baseTileSize); // horizontal pixels

    private int verticalSquares = 16; // how many squares in the y direction
    private int height = verticalSquares * (scalingFactor * baseTileSize); // vertical pixels

    private Coordinate highlightCoordinate;

    KeyHandler keyHandler = new KeyHandler();

    private final PlayerGraphics playerGraphics;
    private final FighterGraphics fighterGraphics;
    private final ArcherGraphics archerGraphics;
    private final WizardGraphics wizardGraphics;
    private final EnchantmentGraphics enchantmentGraphics;
    private final ArrowGraphics arrowGraphics;
    private final SwordGraphics swordGraphics;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Game game;


    public GamePanel() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); //Instead of drawing one by one, draw the imagine in the background first, then draw the entire image later
        this.addKeyListener(keyHandler); // Key listener to handle key presses
        this.setFocusable(true); // All eyes on me
        addMouseListener(this);

        playerGraphics = PlayerGraphics.getInstance(36);
        fighterGraphics = FighterGraphics.getInstance(36);
        archerGraphics = ArcherGraphics.getInstance(36);
        wizardGraphics = WizardGraphics.getInstance(36);
        enchantmentGraphics = EnchantmentGraphics.getInstance(36);
        arrowGraphics = ArrowGraphics.getInstance(36);
        swordGraphics = SwordGraphics.getInstance(36);

        game = Game.getInstance();

        game.setKeyHandler(keyHandler);
        subscribe(game);
    }

    /***
     public GamePanel(int scalingFactor, int horizontalSquares, int verticalSquares, int FPS) {
     this(scalingFactor,horizontalSquares,verticalSquares);
     this.FPS = FPS;
     } ***/

    /***
     public GamePanel(int scalingFactor, int horizontalSquares, int verticalSquares) {
     this.scalingFactor = scalingFactor;
     this.horizontalSquares = horizontalSquares;
     this.verticalSquares = verticalSquares;

     this.setPreferredSize(new Dimension(width, height));
     this.setBackground(Color.BLACK);
     this.setDoubleBuffered(true); //Instead of drawing one by one, draw the imagine in the background first, then draw the entire image later
     this.addKeyListener(keyHandler); // Key listener to handle key presses
     this.setFocusable(true); // All eyes on me
     playerGraphics = new PlayerGraphics(baseTileSize, game.getPlayer());
     //ighterGraphics = new FighterGraphics(baseTileSize, 16,horizontalBound,verticalBound);
     //archerGraphics = new ArcherGraphics(baseTileSize, 16,horizontalBound,verticalBound);
     //wizardGraphics = new WizardGraphics(baseTileSize);
     } ***/

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (!game.isPaused()) {
        // draw empty hall
        this.initEmptyHall(g);
        //draw objects from build mode
        drawObjects(g);
        playerGraphics.draw(g2);
        fighterGraphics.draw(g2);
        archerGraphics.draw(g2);
        arrowGraphics.draw(g2);
        swordGraphics.draw(g2);
        wizardGraphics.draw(g2);
        enchantmentGraphics.draw(g2);

        this.highlightRune(g);

        } else {
            drawPauseScreen(g2);
        }
        g2.dispose();
    }

    // for drawing empty hall
    public void initEmptyHall(Graphics g) {
        this.setOpaque(false);
    }

    //Pause screen
    private void drawPauseScreen(Graphics2D g2) {
        // background
        g2.setColor(new Color(101, 67, 33));
        g2.fillRect(0, 0, width, height);

        // pause text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Gongster", Font.BOLD, 80));
        String pauseText = "Game Paused";
        int textWidth = g2.getFontMetrics().stringWidth(pauseText);
        g2.drawString(pauseText, (width - textWidth) / 2, height / 3);

        g2.setFont(new Font("Gongster", Font.BOLD, 50));
        String resumeText = "Press ESC to Resume";
        textWidth = g2.getFontMetrics().stringWidth(resumeText);
        g2.drawString(resumeText, (width - textWidth) / 2, height / 2);

        String helpText = "Press H for Help";
        textWidth = g2.getFontMetrics().stringWidth(helpText);
        g2.drawString(helpText, (width - textWidth) / 2, (height / 2) + 60);


        g2.setFont(new Font("Gongster", Font.BOLD, 35));

        String saveButtonText = "Save Game and Exit";
        textWidth = g2.getFontMetrics().stringWidth(saveButtonText);

        g2.setColor(new Color(200, 200, 200));

        g2.setStroke(new BasicStroke(2));

        g2.drawRoundRect((width-textWidth)/2-10, height/2 + 213, 355, 50, 10, 10);
        g2.setColor(new Color(101/2, 67/2, 16));
        g2.fillRoundRect((width-textWidth)/2-10, height/2 + 213, 355, 50, 10, 10);

        g2.setColor(Color.WHITE);
        System.out.println((width-textWidth)/2-10);
        /*
        if(hoverSaveLoad) {
        }
         */
        g2.drawString(saveButtonText, (width-textWidth)/2, (height/2) + 250);
    }

    private void highlightRune(Graphics g) {
        if(highlightCoordinate != null) {
            g.setColor(new Color(255, 255, 255, 64));
            g.fillRect(highlightCoordinate.getX() * baseTileSize, highlightCoordinate.getY() * baseTileSize, baseTileSize*4, baseTileSize*4);
        }
    }

    // for drawing hall object from build mode
    private void drawObjects(Graphics g) {
        Tile[][] grid = game.getDungeon().getCurrentHall().getGrid();
        for (int row = 0; row < grid.length; row++) {
            // grid size needs to change
            int verticalSize = 16;
            for (int col = 0; col < verticalSize; col++) {
                Tile gridObject = grid[row][col];
                if (gridObject != null && !gridObject.getName().equals("aa")) {
                    String objName = gridObject.getName().toLowerCase();
                    BufferedImage objectSprite = Textures.getSprite(objName);
                    int h = 36;
                    int w = 36;

                    // different size for column
                    if(objName.equals("column")) {
                        h = 56;
                        w = 28;
                    }

                    if(objName.equals("chest_closed") || objName.equals("chest_full_gold") || objName.equals("chest_full")) {
                        h = 26;
                        w = 26;
                    }

                    //int scaledWidth = (int) (w * scale);
                    //int scaledHeight = (int) (h * scale);


                    int offsetX = (baseTileSize - w) / 2;
                    int offsetY = (baseTileSize - h) / 2;
                    g.drawImage(objectSprite, row * baseTileSize + offsetX, (verticalSize - col - 1) * baseTileSize + offsetY, w, h, null);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!game.isPaused()) {
            int x = e.getX() / baseTileSize;
            int y = 15 - (e.getY() / baseTileSize);
            System.out.println("clicked at x: " + x + " y: " + y);
            Coordinate chosenC = new Coordinate(x, y);
            game.handleChosenBox(game.getPlayer(), chosenC);

        }
        else {
            int x = e.getX();
            int y = e.getY();
            if(x >= 207 && x <=  207 + 355 && y >= height/2 + 213 && y <= height/2 + 263) {
                Game.getInstance().saveGame();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void onGameEvent(Game game) {
        repaint();
    }

    @Override
    public void onHighlightEvent(Coordinate coordinate) {
        this.highlightCoordinate = coordinate;
         // wait 2 seconds
        scheduler.schedule(() -> {
            this.highlightCoordinate = null;
        }, 2, TimeUnit.SECONDS);
    }

    @Override
    public void onGameWin() {}

    @Override
    public void onGameLose() {}

    private void subscribe(Game game) {
        game.addListener(this);
    }
}
