package ui.Graphics.AgentGrapichs;

import domain.agent.monster.Archer;
import domain.agent.monster.Monster;
import domain.factories.MonsterFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ArcherGraphics extends MobilMonsterGraphics {

    private static ArcherGraphics instance;

    private ArcherGraphics(int size) {
        super(size);
        getDefaultImages();
        this.subscribe(MonsterFactory.getInstance());
    }

    public static ArcherGraphics getInstance(int size) {
        if (instance == null) {
            instance = new ArcherGraphics(size);
        }
        return instance;
    }

    protected void getDefaultImages()   {
        try {
            leftPic = ImageIO.read(new File("src/assets/archer4xLeft.png"));
            rightPic = ImageIO.read(new File("src/assets/archer4xRight.png"));
            currentImg = rightPic;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreationEvent(Monster monster) {
        if (monster instanceof Archer)
            monsters.add(monster);
    }

}
