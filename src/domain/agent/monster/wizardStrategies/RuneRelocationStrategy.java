package domain.agent.monster.wizardStrategies;

import domain.Game;
import domain.agent.monster.Wizard;
import domain.agent.monster.wizardStrategies.WizardBehaviorStrategy;
import domain.level.CountDownTimer;
import domain.util.Coordinate;

import java.io.Serializable;

public class RuneRelocationStrategy implements WizardBehaviorStrategy, Serializable {
    private long lastTeleportTime = System.currentTimeMillis();
    @Override
    public void execute(Wizard wizard) {
        CountDownTimer timer = Game.getInstance().getDungeon().getCurrentHall().getTimer();
        float currentTimeRemaining = timer.getTimeRemaining();
        long currentSystemTime = System.currentTimeMillis();
        if (currentSystemTime - lastTeleportTime >= 3000) {  // 3000 ms = 3 seconds
            Coordinate currentRuneLocation = Game.getInstance().getDungeon().getCurrentHall().getRuneLocation();
            Coordinate newRuneLocation;

            do {
                newRuneLocation=Game.getInstance().getDungeon().getCurrentHall().setNewRuneLocationstat();
            } while (newRuneLocation.equals(currentRuneLocation));  // Ensure it's different from the current location
            Game.getInstance().getDungeon().getCurrentHall().setSpecificRuneLocation(newRuneLocation);
            System.out.println("Rune teleported to: (" + newRuneLocation.getX() + ", " + newRuneLocation.getY() + ")");
            lastTeleportTime = currentSystemTime;
        }
    }
}
