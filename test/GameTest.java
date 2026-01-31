import domain.Game;
import domain.agent.Agent;
import domain.agent.Player;
import domain.factories.EnchantmentFactory;
import domain.factories.MonsterFactory;
import domain.level.Dungeon;
import domain.level.GridDesign;
import domain.objects.ObjectType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    /**
     * singletonTest: Test whether there is only 1 game instance at any time
     * Expect: When we call the getInstance method without ending the game
     *         we should get the same game
     */
    @Test
    void singletonTest() {
        Game game1 = Game.getInstance();
        Game game2 = Game.getInstance();
        assertSame(game1, game2);
    }

    /**
     * startGameTest: Test whether game properly starts with everything
     * Expect: When the startGame called on the game object, game loop's
     *         executor should be running, factories should be running, having a
     *         current creation task.
     */
    @Test
    void startGameTest() {
        Game game = Game.getInstance();
        game.startGame();

        //game loop should be running
        assertFalse(game.getExecutor().isTerminated());
        assertFalse(MonsterFactory.getInstance().getSchedule().isTerminated());
        assertFalse(EnchantmentFactory.getInstance().getSchedule().isTerminated());
    }

    /**
     * pauseAndResumeGameTest: Test whether game pauses and resumes
     * Expect: Before the pause game should be properly running
     *         after calling the pause method timer should stop with some remaining time
     *         monster and enchantment factory also stops their creation.
     *         After resume everything should run normally
     */
    @Test
    void pauseAndResumeGameTest() {

        Game game = Game.getInstance();
        GridDesign[] gridDesigns = new GridDesign[4];
        for(int i = 0; i < 4; i++) {
            gridDesigns[i] = new GridDesign( 16, 16, 2);
            gridDesigns[i].placeObject(5, 5, ObjectType.CHEST_CLOSED);
        }
        game.initPlayMode(gridDesigns);

        //We need to start to pause or resume the game
        game.startGame();

        assertFalse(game.isPaused());

        // Pause the game
        game.togglePause();
        //All the task's should be terminated
        assertTrue(MonsterFactory.getInstance().getSchedule().isTerminated());
        assertTrue(EnchantmentFactory.getInstance().getSchedule().isTerminated());
        assertTrue(game.getDungeon().getCurrentHall().getTimer().isTerminated());

        // Resume the game
        //All the task should be rerunning
        game.togglePause();
        assertFalse(MonsterFactory.getInstance().getSchedule().isTerminated());
        assertFalse(EnchantmentFactory.getInstance().getSchedule().isTerminated());
        assertFalse(game.getDungeon().getCurrentHall().getTimer().isTerminated());
    }

    /**
     * endGameTest: Test whether game properly ends
     * Expect: When the end game method is called
     *         every operation and object related with the
     *         current game has to be gone and instance is null.
     *         So if we cal getInstance right after the endGame method
     *         the game we got has to differ from the previous game
     *         in every way
     */
    @Test
    void endGameTest() {
        Game prevGame = Game.getInstance();
        GridDesign[] gridDesigns = new GridDesign[4];
        for(int i = 0; i < 4; i++) {
            gridDesigns[i] = new GridDesign( 16, 16, 2);
            gridDesigns[i].placeObject(5, 5, ObjectType.CHEST_CLOSED);
        }
        prevGame.initPlayMode(gridDesigns);
        Player prevPlayer = prevGame.getPlayer();
        List<Agent> prevAgents = prevGame.getAgents();
        Dungeon prevDungeon = prevGame.getDungeon();

        //To end the game we must finish it first
        prevGame.startGame();
        //End the game
        prevGame.endGame();

        //Create new game
        Game newGame = Game.getInstance();
        Player newPlayer = newGame.getPlayer();
        List<Agent> newAgents = newGame.getAgents();
        Dungeon newDungeon = newGame.getDungeon();

        //If the game properly ended that these
        //Assertions has to be true, ie no games
        //Share common game instance
        //player dungeon or agents list
        assertNotSame(prevGame,newGame);
        assertNotSame(prevAgents,newAgents);
        assertNotSame(prevDungeon,newDungeon);
        assertNotSame(prevPlayer,newPlayer);
    }
}