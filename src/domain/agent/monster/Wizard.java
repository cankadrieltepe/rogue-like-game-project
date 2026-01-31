package domain.agent.monster;
import domain.Game;
import domain.agent.monster.wizardStrategies.WizardBehaviorStrategy;
import domain.agent.monster.wizardStrategies.RuneRelocationStrategy;
import domain.agent.monster.wizardStrategies.PlayerRelocationStrategy;
import domain.agent.monster.wizardStrategies.IdleStrategy;

import domain.util.Coordinate;
import domain.level.CountDownTimer;
import ui.Graphics.AgentGrapichs.WizardGraphics;

public class Wizard extends Monster{
    private WizardBehaviorStrategy currentBehavior;

    public Wizard() {
        setType("wizard");
        System.out.println("Wizard created.");
    }


    @Override
    public void move() {
        CountDownTimer timer = Game.getInstance().getDungeon().getCurrentHall().getTimer();

        double timeRemaining = timer.getTimeRemaining();
        double totalTime = timer.getInitialTimeRemaining();
        double timeRemainingPercentage = (timeRemaining / totalTime) * 100;

        if (timeRemainingPercentage < 30) {
            setBehavior(new PlayerRelocationStrategy());
        } else if (timeRemainingPercentage > 70) {
            if (currentBehavior instanceof RuneRelocationStrategy) {
            }else {
                setBehavior(new RuneRelocationStrategy());
            }
        } else {
            setBehavior(new IdleStrategy());
        }
        currentBehavior.execute(this);
    }

    public void setBehavior(WizardBehaviorStrategy behavior) {
        this.currentBehavior = behavior;
    }

    public void  disappear() {
        System.out.println("Wizard disappeared!");
        WizardGraphics.getInstance(36).onDeletionEventOne(this);
        Game.getInstance().removeMonster(this);
        //!!!!!!!!!!!!!I did a remove method to game class pls check !!!!!!!!!!11
    }



}
