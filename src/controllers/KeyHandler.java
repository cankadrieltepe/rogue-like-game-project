package controllers;

import domain.Game;
import domain.collectables.Enchantment;
import domain.collectables.EnchantmentType;
import domain.util.Direction;
import ui.HelpScreen;
import ui.Swing.Panels.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;


// One change could be the latter pressed button will get the lead
public class KeyHandler implements KeyListener {
    private HelpScreen helpScreen;

    // These are public for now, later we can change them to private or protected
    public boolean goUp, goDown, goLeft, goRight, revealUsed, lureUsed, protectionUsed, options;
    public Direction runeThrowDirection; // U,D,L,R representing up,down,left,right
    public KeyHandler() {
        helpScreen = new HelpScreen(); // Initialize the HelpScreen JFrame

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //For any key pressed

        int key = e.getKeyCode(); // Keys are actually integer coded

        //This lets multiple keystrokes to be recorded at the same time
        if (key == KeyEvent.VK_UP) goUp = true;
        if (key == KeyEvent.VK_DOWN) goDown = true;
        if (key == KeyEvent.VK_LEFT) goLeft = true;
        if (key == KeyEvent.VK_RIGHT) goRight = true;
        if (key == KeyEvent.VK_ESCAPE) options = true;
        if (key == KeyEvent.VK_ESCAPE) Game.getInstance().togglePause();

        if (Game.getInstance().isPaused() && (key == KeyEvent.VK_H)) {
            if (!helpScreen.isVisible()) {
                helpScreen.setVisible(true);
            }
        }

        if (key == KeyEvent.VK_R && !revealUsed) {
            Game.getInstance().getPlayer().useEnchantment(EnchantmentType.Reveal);
            revealUsed = true;
        }
        if (key == KeyEvent.VK_P && !protectionUsed) {
            Game.getInstance().getPlayer().useEnchantment(EnchantmentType.Cloak);
            protectionUsed = true;
        }
        if (key == KeyEvent.VK_B) {
            lureUsed = true;
        }

        if (key == KeyEvent.VK_W && lureUsed) {
            runeThrowDirection = Direction.UP;
            Game.getInstance().getPlayer().useEnchantment(EnchantmentType.Luring);
        }

        if (key == KeyEvent.VK_A && lureUsed) {
            runeThrowDirection = Direction.LEFT;
            Game.getInstance().getPlayer().useEnchantment(EnchantmentType.Luring);
        }

        if (key == KeyEvent.VK_S && lureUsed) {
            runeThrowDirection = Direction.DOWN;
            Game.getInstance().getPlayer().useEnchantment(EnchantmentType.Luring);
        }

        if (key == KeyEvent.VK_D && lureUsed) {
            runeThrowDirection = Direction.RIGHT;
            Game.getInstance().getPlayer().useEnchantment(EnchantmentType.Luring);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        //For any key releasing

        int key = e.getKeyCode(); // Keys are actually integer coded

        if (key == KeyEvent.VK_UP) goUp = false;
        if (key == KeyEvent.VK_DOWN) goDown = false;
        if (key == KeyEvent.VK_LEFT) goLeft = false;
        if (key == KeyEvent.VK_RIGHT) goRight = false;

        if (key == KeyEvent.VK_ESCAPE) options = false;

        if (key == KeyEvent.VK_R) revealUsed = false;
        if (key == KeyEvent.VK_P) protectionUsed = false;

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_W||key == KeyEvent.VK_S||key == KeyEvent.VK_D) {
            lureUsed = false;
        }
    }
}



