package domain.agent;

import domain.Game;
import domain.agent.monster.Fighter;
import domain.collectables.Enchantment;
import domain.collectables.EnchantmentType;
import domain.util.Coordinate;
import domain.util.Direction;
import listeners.PlayerListener;
import ui.Graphics.AgentGrapichs.PlayerGraphics;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Player extends Agent implements Serializable {

    private final static int MAX_HEALTH = 5;
    private final static int INVISIBILITY_DURATION = 5;

    private List<PlayerListener> listeners;
    private int health;
    private final Bag bag;
    private boolean hasRune;
    private boolean invisible;
    private Timer timer; // This methods is for now;

    public Player() {
        listeners = new LinkedList<>();
        health = 3;
        bag = new Bag();
        hasRune = false;
        invisible = false;
        setLocation(new Coordinate(0,0));
        timer = new Timer();
        setDirection(Direction.STOP);
        PlayerGraphics.getInstance(36).setPlayer(this);
    }

    /**
     * Requires:
     *  - enchantment != null
     *
     * Modifies:
     *  - If enchantment is Life or Time, modifies player health or hall time without considering the bag.
     *  - If enchantment is Cloak, Reveal, or Lure, removes the enchantment from the bag
     *    and triggers the corresponding effect (invisibility, highlightRune, lureUsed).
     *
     * Effects:
     *  - Life: calls increaseHealth().
     *  - Time: adds 5 seconds to the current hall’s timer.
     *  - Cloak: calls gainInvisibility().
     *  - Reveal: calls highlightRune() in the current hall.
     *  - Lure: calls lureUsed(...) on all Fighter agents.
     *  - If the enchantment was removed from the bag, notifies PlayerListeners
     *    via onRemoveEnch(enchantmentType).
     */
    public void useEnchantment(EnchantmentType enchantment) {
        System.out.println(enchantment);
        if(enchantment == EnchantmentType.Life || enchantment == EnchantmentType.Time) {
            if (enchantment == EnchantmentType.Life) {increaseHealth();}
            else {Game.getInstance().getDungeon().getCurrentHall().getTimer().increaseTimeRemaining(5);}
        }
        else {
            if (bag.containsEnchantment(enchantment)) {
                bag.removeEnchantment(enchantment);
                if (enchantment == EnchantmentType.Cloak) {
                    gainInvisibility();
                    for (PlayerListener pl : listeners)
                        pl.onRemoveEnch(enchantment);
                }
                else if (enchantment== EnchantmentType.Reveal) {
                    Game.getInstance().getDungeon().getCurrentHall().higlightRune();
                    for (PlayerListener pl : listeners)
                        pl.onRemoveEnch(enchantment);
                }
                else {

                    Coordinate c = switch (Game.getInstance().getKeyHandler().runeThrowDirection) {
                        case UP -> new Coordinate(location.getX(),location.getY() + 1);
                        case DOWN -> new Coordinate(location.getX(),location.getY() - 1);
                        case LEFT -> new Coordinate(location.getX() - 1,location.getY());
                        case RIGHT -> new Coordinate(location.getX() + 1,location.getY());
                        default -> null;
                    };

                    if(Game.getInstance().getDungeon().getCollisionChecker().checkTileEmpty(c)){
                        for (Agent m : Game.getInstance().getAgents()) {
                            if (m instanceof Fighter) {
                                ((Fighter) m).lureUsed(c);
                            }
                        }
                        for (PlayerListener pl : listeners)
                            pl.onRemoveEnch(enchantment);
                    }
                    bag.addEnchantment(enchantment);
                }
            }
        }

    }

    public void collectEnchantment(Enchantment enchantment) {
        if (enchantment.getType() == EnchantmentType.Life || enchantment.getType() == EnchantmentType.Time) {
            useEnchantment(enchantment.getType());
            return;
        }
        bag.addEnchantment(enchantment.getType());

        for (PlayerListener pl : listeners)
            pl.onCollectEnch(enchantment.getType());
        Game.getInstance().playSound("src/assets/collect.wav");
    }

    /**
     * @requires Game, Game->keyHandler, Game -> Dungeon, Dungeon -> CollisionChecker
     *           location has to be non-null and valid.
     *           Direction enum must contain UP, Down, Left, Right values.
     * @modifies Modifies the direction of the character
     *           Modifies the location of the character
     *           Modifies the game state by changing the current hall
     * @effects Changes the character's direction based on the keyboard input
     *          Moves in the character to the direction if the move is valid.
     *          If the character has rune, and reaches the door then transitions to next
     *          hall and updates the location
     */
    public void move () {

        if (Game.getInstance().getKeyHandler().goUp)
            setDirection(Direction.UP);
        else if (Game.getInstance().getKeyHandler().goDown)
            setDirection(Direction.DOWN);
        else if (Game.getInstance().getKeyHandler().goRight)
            setDirection(Direction.RIGHT);
        else if (Game.getInstance().getKeyHandler().goLeft)
            setDirection(Direction.LEFT);
        else
            setDirection(Direction.STOP);

        Direction currDirection = getDirection();

        if (Game.getInstance().getDungeon().getCollisionChecker().validMove(this)) {

            switch (currDirection) {
                case UP -> getLocation().setY(getLocation().getY() + 1);
                case DOWN -> getLocation().setY(getLocation().getY() - 1);
                case RIGHT -> getLocation().setX(getLocation().getX() + 1);
                case LEFT -> getLocation().setX(getLocation().getX() - 1);
            }
        }

        Coordinate doorCoordinate = Game.getInstance().getDungeon().getDoorCoordinate();

        // for getting to the next Hall
        if (currDirection == Direction.DOWN && hasRune && location.equals(doorCoordinate)) {
            this.location.setLocation(0, 0);
            this.setHasRune(false);
            Game.getInstance().nextHall();
        }
    }

    public void addListener(PlayerListener pl) {
        listeners.add(pl);
    }

    public void publishEvent(int num) {
        for (PlayerListener pl : listeners)
            pl.onHealthEvent(num);
    }

    public void gainInvisibility() {
        invisible = true;
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
               invisible = false;
            }
        }, INVISIBILITY_DURATION * 1000);
    }

    public void increaseHealth() {
        if (health < MAX_HEALTH) {
            health++;
            publishEvent(health);
        }
    }

    public void reduceHealth() {
        if(!invisible){
            health--;
            publishEvent(health);
        }
    }

    public void setHasRune(boolean hasRune) {
        this.hasRune = hasRune;
        for (PlayerListener pl : listeners)
            pl.onRuneEvent(hasRune);
    }

    public void prepareGameSave() {
        this.listeners = null;
        this.timer = null;
    }

    public void recreateGame() {
        this.listeners = new LinkedList<>();
        this.timer = new Timer();
    }

    public void restartEvent() {
        game = Game.getInstance();
        for (PlayerListener pl : listeners) {
            pl.onHealthEvent(health);
            pl.onRuneEvent(hasRune);
            pl.onHallChange(game.getDungeon().getCurrentHallIndex());
            for (Map.Entry<EnchantmentType, Integer> entry : this.bag.getEnchantmentCounter().entrySet()) {
                EnchantmentType enchantmentType = entry.getKey();
                int count = entry.getValue();

                for (int i = 0; i < count; i++) {
                    pl.onCollectEnch(enchantmentType);
                }
            }
        }
    }

    public boolean isInvisible() {
        return invisible;
    }

    public int getHealth(){
        return health;
    }

    public Bag getBag(){
        return bag;
    }

    public boolean isHasRune() {
        return hasRune;
    }
}
