package ui.Graphics;

import domain.Game;
import domain.agent.monster.Archer;
import domain.agent.monster.Monster;
import domain.entities.Arrow;
import domain.entities.Projectile;
import domain.util.Direction;
import listeners.ArcherListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

//ProjectileGraphics classı açılabilir

//NextHalla geçince arrows listini resetlemeli miyiz???

public class ArrowGraphics extends EntityGraphics implements ArcherListener {
    private static ArrowGraphics instance;

    protected int size;
    protected LinkedList<Projectile> arrows;
    private BufferedImage arrowImage;


    private ArrowGraphics(int size) {
        this.size = size;
        getDefaultImages();
    }

    public static ArrowGraphics getInstance(int size) {
        if (instance == null) {
            instance = new ArrowGraphics(size);
        }
        return instance;}

    protected void getDefaultImages()   {
        try {
            arrowImage = ImageIO.read(new File("src/assets/fireball_right_1.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g) {
        for (Projectile arrow: arrows) {
            if (!arrow.alive) continue;

            double angle = Math.atan2(-arrow.getDy(), arrow.getDx());
            Graphics2D g2d = (Graphics2D) g;
            int x = arrow.pos.getX() * 36;
            int y = (15 - arrow.pos.getY()) * 36;

            // Save current transform
            AffineTransform oldTransform = g2d.getTransform();

            // Rotate and draw the arrow image
            g2d.rotate(angle, x + arrowImage.getWidth() / 2, y + arrowImage.getHeight() / 2);
            g2d.drawImage(arrowImage, x, y, size, size, null);

            // Restore original transform
            g2d.setTransform(oldTransform);
        }
    }

    public void subscribe(Archer archer) {
        archer.addListener(this);}


    public void onArrowActivationEvent(Archer archer) {
            arrows.add(archer.arrow);
    }

    public void onArrowDeactivationEvent(Archer archer) {
        arrows.remove(archer.arrow);
    }

    public void onNewGameEvent() {
        this.arrows = new LinkedList<>();
    }

    public void onGameOverEvent() {
        arrows = null;
    }

    public void nextHall(){
        arrows.clear();
    }

}
