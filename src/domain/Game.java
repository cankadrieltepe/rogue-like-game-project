package domain;
/**GAME CLASS OVERVIEW
 * Game class represents our adventure game. It manages several things:
 * An instance of player
 * Monster agents that are controlled by the system
 * The collectibles that we call enchantments
 * The 4 dungeon halls that the player has to go through
 * It also contains the game loop which is responsible for the updating the state of the game.
 * It follows a singleton pattern.
 */
/**Representation Invariant
 * It ensures several things:
 * That the player instance is not null
 * The list of agents has the player as their first element
 * The dungeon must have at least one hall loaded
 * The pause state and game's state must be consistent with each other
 * The executor service must not be shut down unless the game is over.
 */
// Abstraction Function (AF):
//   AF(r) = The game instance G where:
//      - G.player = the player instance with properties:
//         - health = r.player.getHealth()
//         - location = r.player.getLocation()
//      - G.agents = list of all active agents (including player and monsters):
//         - G.agents[0] = r.player (the player must always be the first agent)
//         - G.agents[1..n] = monsters agents
//      - G.enchantments = list of collectible enchantments available in the current hall:
//         - Each enchantment `e` in `r.enchantments` has a `remainingFrame` indicating duration.
//      - G.dungeon = the dungeon `r.dungeon` containing a sequence of halls:
//         - r.dungeon.getCurrentHall() = the active hall with:
//           - remainingTime = the remaining time in the current hall.
//           - layout = grid layout for the hall (`GridDesign`)
//      - G.paused = true if the game is paused (no state updates occur).
//      - G.listeners = the list of subscribed listeners for game events.
//      - G.executor = the background thread pool managing game loop.


import controllers.KeyHandler;
import domain.agent.Agent;
import domain.agent.Player;
import domain.agent.monster.Monster;
import domain.collectables.Enchantment;
import domain.factories.EnchantmentFactory;
import domain.factories.MonsterFactory;
import domain.level.Dungeon;
import domain.level.GridDesign;
import domain.util.Coordinate;
import listeners.GameListener;
import ui.Graphics.AgentGrapichs.PlayerGraphics;
import ui.Graphics.ArrowGraphics;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game implements Serializable {

    private long passedTimeEnch;
    private long passedTimeMonster;
    private boolean gameRestarted = false;

    private ExecutorService executor;
    private List<GameListener> listeners;
    public final static SecureRandom random = new SecureRandom();
    private static Game instance;
    private Player player;

    private volatile boolean paused;
    private KeyHandler keyHandler; // this field is for now

    private List<Agent> agents; // Holds set of agent monsters + players, removing and creating this may take some time
    private List<Enchantment> enchantments;
    private Dungeon dungeon;


    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Game() {
        executor =  Executors.newSingleThreadExecutor();
        player = new Player();

        dungeon = new Dungeon();
        listeners = new LinkedList<>();
        enchantments = new LinkedList<>();

        agents = new LinkedList<>();
        agents.add(player);
        paused = false;
        assert repOk();
    }
    public boolean repOk() {
        if (player == null) return false;
        if(agents.isEmpty()|| agents.get(0) !=player)return false;
        if(dungeon == null) return false; // ||dungeon.getCurrentHall()==null
        if (paused&& !executor.isShutdown()) return false;
        return true;
    }

    public void saveGame() {
        executor = null;

        listeners = null;

        keyHandler = null;

        this.passedTimeEnch = EnchantmentFactory.getInstance().getPassedTime();

        this.passedTimeMonster = MonsterFactory.getInstance().getPassedTime();

        for(Agent agent : agents) {
            agent.prepareSaveGame();
        }
        dungeon.prepareGameSave();
        player.prepareGameSave();
        GameSaveLoader.saveGame();
        System.exit(0);
    }

    private void loadGame() {
        executor = Executors.newSingleThreadExecutor();
        for(Agent agent : agents) {
            agent.recreateGame();
        }
        gameRestarted = true;
        this.listeners = new LinkedList<>();
        this.player.recreateGame();
        PlayerGraphics.getInstance(36).setPlayer(this.player);
        this.dungeon.recreateGame();
    }

    public void continueGame() {
        EnchantmentFactory.getInstance().continueGame(enchantments, passedTimeEnch);
        MonsterFactory.getInstance().continueGame(agents, passedTimeMonster);
        ArrowGraphics.getInstance(36).onNewGameEvent();
        executor.execute(new Update());
    }

    public void startGame () {
        MonsterFactory.getInstance().newGame();
        EnchantmentFactory.getInstance().newGame();

        ArrowGraphics.getInstance(36).onNewGameEvent();
        //Executor runs the method instead of threads
        executor.execute(new Update());
    }

    // a method which will terminate everything in the game
    public void endGame() {
        //really important

        //NORMALLY GAME LOOP HAS TO TERMINATTE ITSELF
        dungeon.getCurrentHall().getTimer().pause();
        executor.shutdown();

        MonsterFactory.getInstance().gameOver();
        EnchantmentFactory.getInstance().gameOver();
        ArrowGraphics.getInstance(36).onGameOverEvent();

        instance = null;
    }
    public void playSound(String path) {
        try {
            File audioFile = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // A method which will be used for the time passage of the game.
    public void update(){
        // really important fact is that player has to be the
        // first element of the given list
        for (Agent m : agents) {
            m.move();
        }

        for (Enchantment e : enchantments) {
            e.decreaseRemainingFrame();
        }
    }

    public void publishGameEvent() {
        for (GameListener gl : listeners)
            gl.onGameEvent(this);
    }

    public void initPlayMode(GridDesign[] gridDesigns) {
        dungeon.loadDesigns(gridDesigns);
        dungeon.getCurrentHall().getTimer().start();
    }

    public static void initLoadedGame(Game game) {
        instance = game;
        game.loadGame();
    }

    public void nextHall() {
        dungeon.getCurrentHall().getTimer().pause(); //stop the timer of the previous hall.
        // should just end at this point
        if(dungeon.getCurrentHallIndex() == 3) {
            winGame();
            return;
        }

        dungeon.nextHall();
        dungeon.getCurrentHall().getTimer().start();
        //clears the agent problem
        agents.clear();
        agents.add(player);

        enchantments = new LinkedList<>();

        MonsterFactory.getInstance().nextHall();
        EnchantmentFactory.getInstance().nextHall();
    }

    public synchronized void togglePause() {
        paused = !paused;

        if (paused)
            pauseGame();
        else
            resumeGame();

        publishGameEvent(); // Notify listeners
    }

    public void pauseGame() {
        dungeon.getCurrentHall().getTimer().pause();
        MonsterFactory.getInstance().pauseCreation();
        EnchantmentFactory.getInstance().pauseCreation();
    }

    public void resumeGame() {
        dungeon.getCurrentHall().getTimer().start(); //start = resume
        MonsterFactory.getInstance().resumeCreation();
        EnchantmentFactory.getInstance().resumeCreation();
    }

    public void handleChosenBox(Player player, Coordinate c1) {
        dungeon.getCurrentHall().handleChosenBox(player,c1);
    }

    public void openHall() {}
    public void createVictoryScreen() {}
    public boolean isTimeExpired () {return false;}
    public boolean isPlayerDead() {return true;}
    public Object getObject() {return null;}



    private class Update implements Runnable {
        @Override
        public void run() {
            if(gameRestarted) {
                player.restartEvent();
                gameRestarted = false;
            }
            double currentTime;
            double frameInterval = (double) 1000000000 / 24; // 1 billion nano second is equal to 1 secon, 1/FPS = diff between per frame
            double diff = 0; // represents the time passed between two consecutive frames
            double lastTime = System.nanoTime();

            //To break the loop, assign a boolen which becomes false
            //When ESC is pressed or when game is over
            //Handle in update method.
            //To restart the game just set boolen true and reexecuce
            while (player.getHealth() > 0) {
                if (!paused) {
                    currentTime = System.nanoTime();
                    diff += (currentTime - lastTime)/frameInterval;
                    lastTime = System.nanoTime();

                    if (diff >= 1) {
                        update();
                        publishGameEvent();
                        diff = 0;
                    }
                }
            }
            loseGame();
        }
    }

    public synchronized void removeMonster(Monster monster) {
        if (agents.contains(monster)) {
            agents.remove(monster);
            System.out.println("Monster removed: " + monster.getClass().getSimpleName());
        } else {
            System.out.println("Monster not found!");
        }
    }

    private void winGame() {
        endGame();
        for (GameListener gl : listeners)
            gl.onGameWin();
    }

    public void loseGame() {
        endGame();
        for (GameListener gl : listeners)
            gl.onGameLose();
    }

    public boolean isPaused() {
        return paused;
    }

    public void addListener(GameListener gl) {
        listeners.add(gl);
    }

    public List<Enchantment> getEnchantments() {
        return enchantments;
    }

    public Player getPlayer() {
        return player;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<GameListener> getListeners() {
        return listeners;
    }

    public Dungeon getDungeon(){
        return dungeon;
    }
    public ExecutorService getExecutor(){
        return executor;
    }
}
