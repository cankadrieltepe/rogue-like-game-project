package domain.agent.monster.wizardStrategies;
import domain.agent.monster.Wizard;
import domain.level.Dungeon;
import domain.level.Hall;
import domain.objects.ObjectType;
import domain.util.Coordinate;
import org.junit.jupiter.api.BeforeEach;

import domain.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import domain.level.GridDesign;

class WizardBehaviorStrategyTest {
    private Wizard wizard;
    private Game game;
    int testRow;
    int testColumn;
    @BeforeEach
    public void setUp() {
        game = Game.getInstance();
        wizard = new Wizard();
        testRow = Game.random.nextInt(2, 14);
        testColumn = Game.random.nextInt(2, 14);

        // Initialize the grid designs for the dungeon
        GridDesign[] gridDesigns = new GridDesign[4];
        for (int i = 0; i < 4; i++) {
            gridDesigns[i] = new GridDesign(16, 16, 2);

            // Place the first set of objects for rune locations
            gridDesigns[i].placeObject(testRow, testColumn, ObjectType.CHEST_CLOSED);
            gridDesigns[i].placeObject(15 - testRow, 15 - testColumn, ObjectType.CHEST_CLOSED);

            // Place additional objects at various positions to populate runeLocations
            for (int x = 2; x < 14; x += 3) {
                for (int y = 2; y < 14; y += 3) {
                    gridDesigns[i].placeObject(x, y, ObjectType.CHEST_CLOSED);
                }
            }

            //  add some more variety
            gridDesigns[i].placeObject(3, 5, ObjectType.CHEST_CLOSED);
            gridDesigns[i].placeObject(12, 10, ObjectType.CHEST_CLOSED);
        }

        // Initialize the game with the grid designs
        game.initPlayMode(gridDesigns);
        // Ensure that the current hall has valid rune locations
        Hall currentHall = game.getDungeon().getCurrentHall();
        currentHall.setNewRuneLocation();
        System.out.println("Initial Rune Location: " + currentHall.getRuneLocation());

    }
    @Test
    void testRuneRelocationStrategy() throws InterruptedException {
        WizardBehaviorStrategy behavior = new RuneRelocationStrategy();
        Coordinate initialRuneLocation = game.getDungeon().getCurrentHall().getRuneLocation();

        System.out.println("Initial Rune Location: " + initialRuneLocation);

        game.getDungeon().getCurrentHall().getTimer().reset();
        Thread.sleep(3600);
        behavior.execute(wizard);

        Coordinate newRuneLocation = game.getDungeon().getCurrentHall().getRuneLocation();
        System.out.println("New Rune Location: " + newRuneLocation);

        // Verify that the rune's location has changed
        assertNotEquals(initialRuneLocation, newRuneLocation);
    }

    @Test
    void testPlayerRelocationStrategy() {

        WizardBehaviorStrategy behavior = new PlayerRelocationStrategy();
        Coordinate initialPlayerLocation = game.getPlayer().getLocation();

        // Simulate < 30% time remaining

        game.getDungeon().getCurrentHall().getTimer().increaseTimeRemaining(-85);
        behavior.execute(wizard);

        // Verify that the player was teleported
        assertNotEquals(initialPlayerLocation, game.getPlayer().getLocation());
        assertFalse(game.getAgents().contains(wizard));
    }



    @Test
    void testIdleStrategy() throws InterruptedException {
        WizardBehaviorStrategy behavior = new IdleStrategy();
        Coordinate wizardLocation = wizard.getLocation();

        // Simulate 50% time remaining
        game.getDungeon().getCurrentHall().getTimer().increaseTimeRemaining(-50);
        behavior.execute(wizard);
        Thread.sleep(2100);  // Wait 2.1 seconds

        // Verify that the wizard stays idle but disappears after 2 seconds
        assertEquals(wizardLocation, wizard.getLocation());
        assertFalse(game.getAgents().contains(wizard));
    }
}
