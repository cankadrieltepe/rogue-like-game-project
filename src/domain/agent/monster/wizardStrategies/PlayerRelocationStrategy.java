package domain.agent.monster.wizardStrategies;

import domain.Game;
import domain.agent.monster.Wizard;
import domain.level.CollisionChecker;
import domain.util.Coordinate;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.Serializable;
import java.util.Random;
public class PlayerRelocationStrategy implements WizardBehaviorStrategy, Serializable {

    @Override
    public void execute(Wizard wizard) {
        System.out.println("Wizard: The hero is struggling. Moving the player to a random valid location.");

        CollisionChecker collisionChecker = Game.getInstance().getDungeon().getCollisionChecker();
        Coordinate randomLocation;

        do {
            // Generate random coordinates within the bounds
            int x = new Random().nextInt(16);  // 0 to 15
            int y = new Random().nextInt(16);  // 0 to 15
            randomLocation = new Coordinate(x, y);
        } while (!collisionChecker.checkTileEmpty(randomLocation));  // Keep generating until we find an empty location
        // Set the player’s new location
        Game.getInstance().getPlayer().setLocation(randomLocation);
        Game.getInstance().playSound("src/assets/teleport.wav");


        System.out.println("Player teleported to: " + randomLocation.getX() + ", " + randomLocation.getY());

        wizard.disappear();
    }
}
