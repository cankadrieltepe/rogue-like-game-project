package ui.Swing.Panels;


import domain.Textures;
import ui.Graphics.TileSetImageGetter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

// Wrapper for hall panel
// Offsets will be replaced with constants for better readabilty

public class HallPanelHolder extends JPanel {
    public final int tileNumber = 16;
    private JPanel externalPanel;
    private boolean isDoorOpen = false;
    private int currentHall = 0;

    public HallPanelHolder(JPanel externalPanel) {
        this.setPreferredSize(new Dimension(800, 830));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setLayout(null);

        externalPanel.setLocation(140, 164);
        externalPanel.setSize(externalPanel.getPreferredSize());

        externalPanel.setFocusable(true);
        externalPanel.requestFocusInWindow();
        externalPanel.setOpaque(false);

        this.add(externalPanel);

        this.externalPanel = externalPanel;
        // draw walls
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHallStructure(g);
    }

    private void drawHallStructure(Graphics g) {

        BufferedImage floor = TileSetImageGetter.getInstance().getFloorImage();
        g.drawImage(floor,0, 0,850, 850, null);

        BufferedImage hall;

        g.setColor(new Color(234, 239, 239));
        g.setFont(new Font("Gongster",Font.BOLD,40));

        switch (currentHall) {
            case 0:
                hall = Textures.getSprite("hearth");
                g.setColor(new Color(170, 101, 65));
                g.drawString("Hall Of Earth", 202, 77);
                g.setColor(new Color(18, 18, 16));
                g.drawString("Hall Of Earth", 199, 75);
                break;
            case 1:
                hall = Textures.getSprite("hwater");
                g.setColor(new Color(135, 187, 197));
                g.drawString("Hall Of Water", 202, 77);
                g.setColor(new Color(30, 128, 237));
                g.drawString("Hall Of Water", 201, 75);
                break;
            case 2:
                hall = Textures.getSprite("hfire");
                g.setColor(new Color(170, 101, 65));
                g.drawString("Hall Of Fire", 202, 77);
                g.setColor(new Color(18, 18, 16));
                g.drawString("Hall Of Fire", 199, 75);
                break;
            default:
                hall = Textures.getSprite("hair");
                g.setColor(new Color(189, 150, 75));
                g.drawString("Hall Of Air", 202, 77);
                g.setColor(new Color(227, 225, 225));
                g.drawString("Hall Of Air", 199, 75);
                break;
        }

        g.drawImage(hall, 119, 110, 618, 685, null);

        if(isDoorOpen) {
            BufferedImage doorOpen = Textures.getSprite("dooropen");
            this.drawDoorOpen(g, doorOpen);
        } else {
            BufferedImage door = Textures.getSprite("door");
            this.drawDoor(g, door);
        }


    }


    private void drawDoor(Graphics g, BufferedImage door){
        switch (currentHall) {
            case 1:
                g.drawImage(door, 370,724, 130, 52, null);
                break;
            case 2:
                g.drawImage(door, 480,724, 130, 52, null);
                break;
            case 3:
                g.drawImage(door, 370,724, 130, 52, null);
                break;
            default:
                g.drawImage(door, 180,724, 130, 52, null);
                break;
        }
    }

    private void drawDoorOpen(Graphics g, BufferedImage doorOpen) {
        switch (currentHall) {
            case 1:
                g.drawImage(doorOpen, 370,721, 130, 55, null);
                break;
            case 2:
                g.drawImage(doorOpen, 480,721, 130, 55, null);
                break;
            case 3:
                g.drawImage(doorOpen, 370,721, 130, 55, null);
                break;
            default:
                g.drawImage(doorOpen, 190,721, 130, 55, null);
                break;
        }
    }

    public void setDoorOpen(boolean isDoorOpen) {
        if(this.isDoorOpen && !isDoorOpen)
            currentHall++;
        this.isDoorOpen = isDoorOpen;
        this.repaint();
    }

    public JPanel getExternalPanel() {
        return externalPanel;
    }

    public GamePanel getGamePanel() {
        return (GamePanel) this.externalPanel;
    }

    public void setCurrentHall(int currentHall) {
        this.currentHall = currentHall;
        this.repaint();
    }

}
