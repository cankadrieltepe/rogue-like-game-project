package domain.agent.monster;

import domain.Game;
import domain.agent.Agent;
import domain.util.Coordinate;

import java.io.Serializable;
import java.util.Collections;
import java.util.stream.Collectors;

public abstract class Monster extends Agent implements Serializable {

    public Monster() {
        super();

        Coordinate c = new Coordinate(Game.random.nextInt(16), Game.random.nextInt(16));

        super.setLocation(c);
    }

}


