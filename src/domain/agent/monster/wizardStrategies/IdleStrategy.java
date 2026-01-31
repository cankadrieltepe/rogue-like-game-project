package domain.agent.monster.wizardStrategies;

import domain.agent.monster.Wizard;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class IdleStrategy implements WizardBehaviorStrategy, Serializable {
    @Override
    public void execute(Wizard wizard) {
        System.out.println("Wizard: Indecisive... staying still for 2 seconds.");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wizard.disappear(); // Disappear after 2 seconds
            }
        }, 2000); // 2 seconds delay
    }
}
