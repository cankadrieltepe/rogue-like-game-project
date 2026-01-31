package ui.Graphics.AgentGrapichs;

import domain.agent.monster.Monster;
import domain.agent.monster.Wizard;
import domain.factories.MonsterFactory;
import listeners.FactoryListener;
import ui.Graphics.EntityGraphics;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

public class WizardGraphics extends EntityGraphics implements FactoryListener, Serializable {

    private static WizardGraphics instance;
    protected int size;
    protected BufferedImage img;
    private LinkedList<Wizard> wizards;

    private WizardGraphics(int size) {
        this.size = size;
        wizards = new LinkedList<>();
        getDefaultImages();
        this.subscribe(MonsterFactory.getInstance());
    }

    public static WizardGraphics getInstance(int size) {
        if (instance == null) {
            instance = new WizardGraphics(size);
        }
        return instance;
    }

    protected void getDefaultImages()   {
        try {
            img = ImageIO.read(new File("src/assets/wizard4x.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g) {
        for (Wizard w: wizards) {
            g.drawImage(img, w.getLocation().getX()*36, (15 - w.getLocation().getY())*36, size, size,null);
        }
    }

    public void onCreationEvent(Monster monster) {
        if (monster instanceof Wizard)
            wizards.add((Wizard) monster);
    }

    public void onDeletionEvent() {
        this.wizards.clear();
    }

    @Override
    public void onNewGameEvent(){
        wizards = new LinkedList<>();
    }

    @Override
    public void onGameOverEvent(){
        wizards = null;
    }

    public void onDeletionEventOne(Wizard wizard) {
        wizards.remove(wizard);  // Remove the specific wizard from the list
    }

    public void subscribe(MonsterFactory mf) {
        mf.addListener(this);
    }

}
