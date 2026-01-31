package domain.collectables;

import domain.Game;
import domain.factories.EnchantmentFactory;
import domain.level.CollisionChecker;
import domain.util.Coordinate;

import java.io.Serializable;

public class Enchantment implements Serializable {

    private static final int ENCHANTMENT_DURATION_FRAME = 20 * 6; //how many times update is called before the ench is removed.

    private final EnchantmentType type;
    private int remainingFrame;
    private Coordinate location;

    public Enchantment(EnchantmentType type) {
        this.type = type;
        this.remainingFrame = ENCHANTMENT_DURATION_FRAME;

        Coordinate c = new Coordinate(Game.random.nextInt(16),Game.random.nextInt(16));
        while (!Game.getInstance().getDungeon().getCollisionChecker().checkTileEmpty(c)) //ensure spawned on empty tile.
        {
            c = new Coordinate(Game.random.nextInt(16),Game.random.nextInt(16));
        }
        this.location = c;
    }

    public void decreaseRemainingFrame()
    {
        remainingFrame--;
        //deletion logic, should send a message to UI, not really sure on which design pattern to use
        //so for now going to send a message to the gameFactory, which has UI elements as listeners.
        if (remainingFrame <= 0)
        {
            Game.getInstance().getEnchantments().remove(this);
            EnchantmentFactory.getInstance().notifyRemoval(this);
        }
    }

    public void setLocation(Coordinate location){
        this.location = location;
    }

    public Coordinate getLocation() {return location;}

    public EnchantmentType getType() {
        return type;
    }

    public float getRemainingTime() {
        return remainingFrame;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingFrame = remainingTime;
    }

    public void useEnchantment()
    {
        //switch case for each type of enchantment.
        switch (type)
        {
            case EnchantmentType.Life -> Game.getInstance().getPlayer().increaseHealth();
            //add other enchantment types here.
        }

    }
}
