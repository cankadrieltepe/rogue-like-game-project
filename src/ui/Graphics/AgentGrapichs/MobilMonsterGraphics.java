package ui.Graphics.AgentGrapichs;

import domain.agent.monster.Monster;
import domain.factories.MonsterFactory;
import domain.util.Direction;
import listeners.FactoryListener;
import ui.Graphics.EntityGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public abstract class MobilMonsterGraphics extends EntityGraphics implements FactoryListener {

    protected  BufferedImage rightPic, leftPic;
    protected int size;
    protected BufferedImage currentImg;
    protected LinkedList<Monster> monsters;

    public MobilMonsterGraphics(int size) {
        this.size = size;
    }

    public void draw(Graphics g) {
        for (Monster monster: monsters) {
            if (monster.getDirection() == Direction.LEFT) {
                currentImg = leftPic;
            } else if (monster.getDirection() == Direction.RIGHT) {
                currentImg = rightPic;
            }
            g.drawImage(currentImg, monster.getLocation().getX()*36,(15 - monster.getLocation().getY())*36 , size, size,null);
        }
    }

    @Override
    public void onNewGameEvent(){
        this.monsters = new LinkedList<>();
    }

    @Override
    public void onGameOverEvent(){
        this.monsters = null;
    }

    public void onDeletionEvent() {
        this.monsters.clear();
    }

    public void subscribe(MonsterFactory mf) {
        mf.addListener(this);
    }
}
