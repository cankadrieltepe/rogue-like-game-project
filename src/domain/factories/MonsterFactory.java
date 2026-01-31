package domain.factories;

import domain.Game;
import domain.agent.Agent;
import domain.agent.monster.Archer;
import domain.agent.monster.Fighter;
import domain.agent.monster.Monster;
import domain.agent.monster.Wizard;
import domain.collectables.Enchantment;
import listeners.EnchantmentListener;
import listeners.FactoryListener;
import ui.Graphics.AgentGrapichs.ArcherGraphics;
import ui.Graphics.AgentGrapichs.FighterGraphics;
import ui.Graphics.AgentGrapichs.MobilMonsterGraphics;
import ui.Graphics.AgentGrapichs.WizardGraphics;
import ui.Graphics.ArrowGraphics;
import ui.Graphics.EnchantmentGraphics;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// A class which creates random monsters
// every 8 seconds
public class MonsterFactory {

    private ScheduledExecutorService schedule;
    private final List<FactoryListener> listeners;
    private static MonsterFactory instance;
    private MonsterCreationTask currentTask;
    private long stopTime;
    private long lastCreation;
    private long passedTime;

    public static MonsterFactory getInstance() {
        if (instance == null) {
            instance = new MonsterFactory();
        }
        return instance;
    }

    private MonsterFactory() {
        //Adds a reference to the monster list in the game
        listeners = new LinkedList<>();
    }

    public void nextHall() {
       publishNextHallEvent();
       ArrowGraphics.getInstance(36).nextHall();

       schedule.shutdownNow();
       schedule = Executors.newSingleThreadScheduledExecutor();
       currentTask = new MonsterCreationTask();
       schedule.scheduleAtFixedRate(currentTask, 50, 8000, TimeUnit.MILLISECONDS);
    }

    public void newGame() {
        publishNewGameEvent();

        schedule = Executors.newSingleThreadScheduledExecutor();
        currentTask = new MonsterCreationTask();
        schedule.scheduleAtFixedRate(currentTask, 50, 8000, TimeUnit.MILLISECONDS);        // repeat with period of 8
    }

    public void gameOver() {
        publishGameOverEvent();

        currentTask.cancel();
        schedule.close();
    }

    public void pauseCreation() {
        currentTask.cancel();
        schedule.close();
        stopTime = System.currentTimeMillis();
        passedTime = stopTime - lastCreation;
    }

    public void resumeCreation () {
        long dt = 8000 - passedTime;
        lastCreation = System.currentTimeMillis() - passedTime;
        System.out.println(dt);
        currentTask = new MonsterCreationTask();

        schedule = Executors.newSingleThreadScheduledExecutor();
        schedule.scheduleAtFixedRate(currentTask, dt > 0? dt: 50 , 8000, TimeUnit.MILLISECONDS);
    }

    public void addListener(FactoryListener fl) {
        listeners.add(fl);
    }

    public void continueGame(List<Agent> agents, Long passedTime) {
        for (FactoryListener fl: listeners)
            fl.onNewGameEvent();
        for(Agent agent : agents) {
            if(agent instanceof Monster) {
                for (FactoryListener fl: listeners)
                    fl.onCreationEvent((Monster) agent);
            }
        }
        this.passedTime = passedTime;
    }

    public void publishCreationEvent(Monster monster) {
        for (FactoryListener fl: listeners)
            fl.onCreationEvent(monster);
        Game.getInstance().playSound("src/assets/monster-spawn.wav");

    }

    public void publishNextHallEvent() {
        for (FactoryListener fl: listeners)
            fl.onDeletionEvent();
    }

    public void publishNewGameEvent() {
        for (FactoryListener fl: listeners)
            fl.onNewGameEvent();
    }

    public void publishGameOverEvent() {
        for (FactoryListener fl: listeners)
            fl.onGameOverEvent();
    }

    public long getPassedTime() {
        return passedTime;
    }

    private class MonsterCreationTask extends TimerTask {
        @Override
        public void run() {

            int monster = Game.random.nextInt(3);
            Monster w = switch (monster) {
                case 0 -> new Archer();
                case 1 -> new Fighter();
                case 2 -> new Wizard();
                default -> null;
            };

            Game.getInstance().getAgents().add(w);
            publishCreationEvent(w);
            lastCreation = System.currentTimeMillis();
        }
    }

    public ScheduledExecutorService getSchedule(){
        return schedule;
    }
}