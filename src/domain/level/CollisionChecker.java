package domain.level;

import domain.Game;
import domain.agent.Agent;
import domain.collectables.Enchantment;
import domain.util.Coordinate;

import java.io.Serializable;

// A helper class for the dungeon
// at this point
public class CollisionChecker {

    private final int ROW_NUMBER = 16;
    private final int COL_NUMBER = 16;

    private Dungeon dungeon;

    public CollisionChecker(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public boolean validMove(Agent agent) {
        return isInBoundary(agent) &&
                !checkTileCollisions(agent) &&
                !checkAgentCollisions(agent) &&
                !checkEnchantmentCollisions(agent);
    }

    // Checks whether movement is in the boundary
    // returns true if the movement is in the boundary
    public boolean isInBoundary (Agent agent) {
        return switch (agent.getDirection()) {
            case UP -> agent.getLocation().getY() + 1 < ROW_NUMBER;
            case DOWN -> agent.getLocation().getY() - 1 >= 0;
            case LEFT -> agent.getLocation().getX() - 1 >= 0;
            case RIGHT -> agent.getLocation().getX() + 1 < COL_NUMBER;
            case STOP -> true;
        };
    }

    // A really primitive checker for the tiles
    // with the located object
    public boolean checkTileCollisions (Agent agent) {
        Tile tile;
        return switch (agent.getDirection()) {
            case STOP -> false;
            case UP -> {
                tile = dungeon.getCurrentHall().getGrid()[agent.getLocation().getX()][agent.getLocation().getY() + 1];
                yield tile.isCollisable();
            }
            case DOWN -> {
                tile = dungeon.getCurrentHall().getGrid()[agent.getLocation().getX()][agent.getLocation().getY() - 1];
                yield tile.isCollisable();
            }
            case LEFT -> {
                tile = dungeon.getCurrentHall().getGrid()[agent.getLocation().getX() - 1][agent.getLocation().getY()];
                yield tile.isCollisable();
            }
            case RIGHT -> {
                tile = dungeon.getCurrentHall().getGrid()[agent.getLocation().getX() + 1][agent.getLocation().getY()];
                yield tile.isCollisable();
            }
        };
    }

    public boolean checkEnchantmentCollisions(Agent agent) {
        Coordinate c;
        switch (agent.getDirection()) {
            case STOP -> c = new Coordinate(agent.getLocation().getX(),agent.getLocation().getY());
            case UP -> c = new Coordinate(agent.getLocation().getX(),agent.getLocation().getY() + 1);
            case DOWN -> c = new Coordinate(agent.getLocation().getX(),agent.getLocation().getY() - 1);
            case RIGHT -> c = new Coordinate(agent.getLocation().getX() + 1, agent.getLocation().getY());
            case LEFT -> c = new Coordinate(agent.getLocation().getX() - 1, agent.getLocation().getY());
            default -> c = agent.getLocation();
        }
        for (Enchantment enchantment : Game.getInstance().getEnchantments()) {
            if (enchantment.getLocation().equals(c)) {
                return true;
            }
        }
        return false;
    }


    // The target list contains all the agents
    public boolean checkAgentCollisions(Agent agent) {
        Tile tile;
        Coordinate c;

        switch (agent.getDirection()) {
            case STOP -> c = new Coordinate(agent.getLocation().getX(),agent.getLocation().getY());
            case UP -> c = new Coordinate(agent.getLocation().getX(),agent.getLocation().getY() + 1);
            case DOWN -> c = new Coordinate(agent.getLocation().getX(),agent.getLocation().getY() - 1);
            case RIGHT -> c = new Coordinate(agent.getLocation().getX() + 1, agent.getLocation().getY());
            case LEFT -> c = new Coordinate(agent.getLocation().getX() - 1, agent.getLocation().getY());
            default -> c = agent.getLocation();
        }

        for (Agent target : Game.getInstance().getAgents()) {
            if (!target.equals(agent) && target.getLocation().equals(c)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTileEmpty(Coordinate c)
    {

        if (dungeon.getCurrentHall().getGrid()[c.getX()][c.getY()].isCollisable()) {
            return false;
        }

        for (Agent target :Game.getInstance().getAgents()) {
            if (target.getLocation().equals(c)) {
                return false;
            }
        }

        for (Enchantment enchantment : Game.getInstance().getEnchantments()) {
            if (enchantment.getLocation().equals(c)) {
                return false;
            }
        }

        return true;
    }

}
