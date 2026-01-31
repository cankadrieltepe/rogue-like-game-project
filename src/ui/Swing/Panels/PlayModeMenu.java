package ui.Swing.Panels;


import domain.Game;
import domain.Textures;
import domain.collectables.EnchantmentType;
import domain.level.CountDownTimer;
import ui.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class PlayModeMenu extends JPanel implements MouseListener {

    private boolean isPaused;

    private boolean hasRune;

    private int lives = 3;

    private int cloakCounter;

    private int lureCounter;

    private int revealCounter;

    public PlayModeMenu() {
        super();
        this.isPaused = false;
        this.setPreferredSize(new Dimension(200, 750));
        this.setBackground(new Color(107, 85, 87));
        this.setLayout(new GridLayout(0, 1));
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.displayPauseResumeButton(g);
        this.displayLives(g);
        this.displayInventory(g);
        this.displayEnchanthments(g);
        this.displayRune(g);
    }

    private void displayPauseResumeButton(Graphics g) {
        BufferedImage pauseResumeImage;
        BufferedImage exitImage = Textures.getSprite("exit");
        if(isPaused) {
            pauseResumeImage = Textures.getSprite("resume");
        }
        else {
            pauseResumeImage = Textures.getSprite("pause");
        }
        g.drawImage(pauseResumeImage, 40, 150, 32, 32, null);
        g.drawImage(exitImage, 85, 150, 32, 32, null);
    }

    public void displayLives(Graphics g) {
        BufferedImage heart = Textures.getSprite("heart");
        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, 40 + (i % 3)*45, 300 + (i/3) * 45, 32, 32, null);
        }
    }

    private void displayTimer(CountDownTimer timer) {

    }

    private void displayRune(Graphics g) {
        if (hasRune) {
            g.setFont(new Font("Serif", Font.BOLD, 22));
            g.setColor(new Color(182, 181, 181));
            g.drawString("Rune Collected!", 20, 20);
            BufferedImage runeImage = Textures.getSprite("rune");
            g.drawImage(runeImage, 85, 50, 32, 32, null);
        }
    }

    private void displayInventory(Graphics g) {
        BufferedImage inventoryImage = Textures.getSprite("inventory");
        g.drawImage(inventoryImage, 20, 480, 160, 234, null);
    }

    private void displayEnchanthments(Graphics g) {
        int order = 0;
        int xOffset = 52;
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.setColor(new Color(199, 210 , 204));
        if(cloakCounter > 0) {
            int x = xOffset + order*33;
            BufferedImage cloakImage = Textures.getSprite("cloak");
            g.drawImage(cloakImage, x, 570, 32, 32, null);
            if(cloakCounter > 1) {
                g.drawString(String.valueOf(cloakCounter), x + 25, 595);
            }
            order++;
        }
        if(lureCounter > 0) {
            int x = xOffset + order*33;
            BufferedImage lureImage = Textures.getSprite("lure");
            g.drawImage(lureImage, xOffset + 33*order, 570, 32, 32, null);
            if(lureCounter > 1) {
                g.drawString(String.valueOf(lureCounter), x + 25, 595);
            }
            order++;
        }
        if(revealCounter > 0) {
            int x = xOffset + order*33;
            BufferedImage revealImage = Textures.getSprite("reveal");
            g.drawImage(revealImage, xOffset + 33*order, 570, 32, 32, null);
            if(revealCounter > 1) {
                g.drawString(String.valueOf(revealCounter), x + 25, 595);
            }
        }
    }

    public void updatePause(boolean isPaused) {
        this.isPaused = isPaused;
        repaint();
    }

    public void updateLives(int lives) {
        this.lives = lives;
        repaint();
    }

    public void updateRune(boolean hasRune) {
        this.hasRune = hasRune;
        repaint();
    }

    public void updateEnchCount(EnchantmentType ench, int change) {
        switch (ench) {
            case Cloak:
                cloakCounter += change;
                break;

            case Luring:
                lureCounter += change;
                break;

            default:
                revealCounter += change;
                break;
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(x >= 40 && x <= 72 && y >= 150 && y <= 182) {
            Game.getInstance().togglePause();
        }
        if(x >= 85 && x <= 117 && y >= 150 && y <= 182) {
            Game.getInstance().endGame();
            PageManager.getInstance().showMainMenuPage();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
