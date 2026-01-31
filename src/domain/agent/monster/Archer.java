package domain.agent.monster;

import domain.Game;
import domain.entities.Arrow;
import domain.entities.Projectile;
import domain.util.Coordinate;
import domain.util.Direction;
import listeners.ArcherListener;
import listeners.FactoryListener;
import ui.Graphics.ArrowGraphics;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Archer extends Monster {
    private LinkedList<ArcherListener> listeners;
    private static final int ATTACK_RANGE = 4;
    private static final int MOVE_FRAME_LIMIT = 20;
    private static final int ATTACK_FRAME_LIMIT = 60;

    private int moveFrame;
    private int attackFrame;
    public Projectile arrow;



    public Archer() {
        super();
        setType("archer");
        listeners = new LinkedList<>();
        moveFrame = 0;
        attackFrame = ATTACK_FRAME_LIMIT;

        ArrowGraphics.getInstance(36).subscribe(this);

    }

    public void addListener(ArcherListener al) {
        listeners.add(al);
    }

    public void prepareSaveGame() {
        this.listeners = null;
    }

    public void recreateGame() {
        this.listeners = new LinkedList<>();
        ArrowGraphics.getInstance(36).subscribe(this);
    }

    public void publishArrowActivationEvent() {
        for (ArcherListener al : listeners)
            al.onArrowActivationEvent(this);
    }

    /*public void publishArrowDeactivationEvent() {
        for (ArcherListener al : listeners)
            al.onArrowDeactivationEvent(this);
    }*/


    // move method of the archer
    public void move() {

        shootArrow();

        //Move after each 0.33 seconds
        if (++moveFrame <= MOVE_FRAME_LIMIT) return;

        moveFrame = 0;

        Direction prev = getDirection();
        setDirection(DIRECTIONS[Game.random.nextInt(4)]);

        if (Game.getInstance().getDungeon().getCollisionChecker().validMove(this)) {
            switch (getDirection()) {
                case UP -> getLocation().setY(getLocation().getY() + 1);
                case DOWN -> getLocation().setY(getLocation().getY() - 1);
                case RIGHT -> getLocation().setX(getLocation().getX() + 1);
                case LEFT -> getLocation().setX(getLocation().getX() - 1);
            }
        } else {
            setDirection(prev);
        }
    }

    private void shootArrow() {

        if (arrow == null || !arrow.alive) {
            arrow = new Arrow(this); // Initialize the arrow only when needed
            }

        Coordinate archerloc = this.getLocation();
        Coordinate playerloc = Game.getInstance().getPlayer().getLocation();
        Coordinate initialarrowloc = new Coordinate(archerloc.getX(), archerloc.getY());

        if (arrow.alive) {arrow.update();}

        if (attackFrame >= ATTACK_FRAME_LIMIT) {
            int dx;
            int dy;

            if (Coordinate.euclideanDistance(archerloc, playerloc) <= ATTACK_RANGE &&
                    !Game.getInstance().getPlayer().isInvisible()) {

                dx = playerloc.getX() - archerloc.getX();
                dy = playerloc.getY() - archerloc.getY();

                int gcd = gcd(Math.abs(dx), Math.abs(dy));
                dx /= gcd;
                dy /= gcd;

                arrow.set(initialarrowloc, dx, dy, true, this);
                publishArrowActivationEvent();
                Game.getInstance().getPlayer().reduceHealth();
                attackFrame = 0;
            } else { // if player is not found in the attack range */

                int[] directions = {-1, 0, 1};

                do {
                    dx = directions[Game.random.nextInt(3)];
                    dy = directions[Game.random.nextInt(3)];
                } while (dx == 0 && dy == 0);

                arrow.set(initialarrowloc, dx, dy, true, this);
                publishArrowActivationEvent();
                Game.getInstance().playSound("src/assets/arrow-sound.wav");

                attackFrame = 0;
            }
        }
        attackFrame++;}

        private int gcd (int a, int b){
            return b == 0 ? a : gcd(b, a % b);
        }

    }

