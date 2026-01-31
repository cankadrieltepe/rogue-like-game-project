package ui.Graphics;

import domain.Game;
import domain.agent.Agent;
import domain.agent.monster.Archer;
import domain.agent.monster.Fighter;
import domain.agent.monster.Monster;
import domain.entities.Projectile;
import listeners.FighterListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class SwordGraphics extends EntityGraphics implements FighterListener {

    private static SwordGraphics instance;

    protected int size;
    private BufferedImage swordImage1;
    private BufferedImage swordImage2;
    private BufferedImage swordImage3;
    private BufferedImage currentImg;

    private Fighter fighter;

    private int spriteCounter;



    private SwordGraphics(int size) {
        this.size = size;
        getDefaultImages();
    }

    public static SwordGraphics getInstance(int size) {
        if (instance == null) {
            instance = new SwordGraphics(size);
        }
        return instance;}

    protected void getDefaultImages()   {
        try {
            swordImage1 = ImageIO.read(new File("src/assets/sword1.png"));
            swordImage2 = ImageIO.read(new File("src/assets/sword3.png"));
            swordImage3 = ImageIO.read(new File("src/assets/sword4.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g) {

        if (fighter != null) {

               if (fighter.swordLife <= 0) {return;}

               else if (fighter.swordLife > 10 && fighter.swordLife <= 20) {
                    currentImg = swordImage1;}
               else  {
                       currentImg = swordImage3;
                }

               fighter.swordLife -= 10;

                Graphics2D g2d = (Graphics2D) g;
                int x = fighter.getLocation().getX() * 36;
                int y = (15 - fighter.getLocation().getY()) * 36;

                // Save current transform
                //AffineTransform oldTransform = g2d.getTransform();

                // Rotate and draw the arrow image
                g2d.drawImage(currentImg, x, y, size, size, null);

                // Restore original transform
                //g2d.setTransform(oldTransform);

}
    }

    public void subscribe(Fighter fighter) {
        fighter.addListener(this);}

    @Override
    public void onFireEvent(Fighter fighter) {
        this.fighter = fighter;
    }
}
