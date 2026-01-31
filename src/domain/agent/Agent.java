package domain.agent;

import domain.Game;
import domain.entities.Entity;
import domain.util.Coordinate;
import domain.util.Direction;

import java.io.Serializable;

public abstract class Agent extends Entity implements Serializable {

    // Agents need a lot of fields from the game
    // This fields may change later
    // depending on what we actually want;
    protected static Game game; //To make access faster
    protected String type;
    protected Direction direction;
    protected Coordinate location;
    protected final Direction[] DIRECTIONS = Direction.values();


    public abstract void move();

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = new Coordinate(location.getX(), location.getY());  // Create new reference
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public static void setGame(Game game) {
        Agent.game = game;
    }

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

    public void recreateGame() {

    }

    public void prepareSaveGame() {

    }

}