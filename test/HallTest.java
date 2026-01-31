import domain.Game;
import domain.agent.Player;
import domain.collectables.Enchantment;
import domain.collectables.EnchantmentType;
import domain.level.GridDesign;
import domain.objects.ObjectType;
import domain.util.Coordinate;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HallTest {
    int testRow;
    int testColumn;
    Game game;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();

        // setting up random locations to test
        testRow = Game.random.nextInt(2,14);
        testColumn = Game.random.nextInt(2, 14);

        // setting up the grid for play mode
        GridDesign[] gridDesigns = new GridDesign[4];
        for(int i = 0; i < 4; i++) {
            gridDesigns[i] = new GridDesign( 16, 16, 2);
            gridDesigns[i].placeObject(testRow, testColumn, ObjectType.CHEST_CLOSED);
            gridDesigns[i].placeObject(15-testRow, 15-testColumn, ObjectType.CHEST_CLOSED);
        }
        game.initPlayMode(gridDesigns);
    }

    /**
     * Test 1: handleChosenBox - choosing enchantment to add to the inventory
     * Expect: player->bag->size changes to 1
     *
     */
    @Test
    @Order(1)
    void testHandleChoseBox_enchantment() {
        // creating new enchantment
        Enchantment e = new Enchantment(EnchantmentType.Reveal);
        game.getEnchantments().add(e);

        //Testing the function with the location of the enchantment
        game.getDungeon().getCurrentHall().handleChosenBox(game.getPlayer(), e.getLocation());

        // test
        assertTrue(game.getPlayer().getBag().getCurrentSize() == 1);
    }

    /**
     * Test 2: handleChosenBox - choosing one of the objects in the hall that does not contain the rune
     * Expect: hall->tile is still the object, player->hasRune is false
     *
     */
    @Test
    @Order(2)
    void testHandleChoseBox_placedObject() {
        // checking first to see it contains an object
        Coordinate runeloc = game.getDungeon().getCurrentHall().getRuneLocation();
        testRow = 15 - runeloc.getX();
        int testColInv = 15 - runeloc.getY();

        boolean isObject = game.getDungeon().
                getCurrentHall().getGrid()[testRow][testColInv].getName().equals(ObjectType.CHEST_CLOSED.toString());
        assertTrue(isObject);



        // checking now that it is not an empty tile
        game.getPlayer().setLocation(new Coordinate(testRow, testColInv-1));
        game.getDungeon().getCurrentHall().handleChosenBox(game.getPlayer(), new Coordinate(testRow, testColInv));
        boolean stillObject = game.getDungeon().getCurrentHall().getGrid()[testRow][testColInv].getName().equals("CHEST_CLOSED");
        assertTrue(stillObject);

        // checking that the player does not contain the rune
        boolean playerHasRune = game.getPlayer().isHasRune();
        assertFalse(playerHasRune);
    }

    /**
     * Test 3: handleChosenBox - choosing the rune location to see if it correctly handles rune found logic
     * Expect: player->hasRune is true
     *
     */
    @Test
    @Order(3)
    void testHandleChoseBox_placedObjectWithRune() {
        Coordinate runeLocation = game.getDungeon().getCurrentHall().getRuneLocation();

        // setting the player near the rune
        game.getPlayer().setLocation(new Coordinate(runeLocation.getX()-1, runeLocation.getY()));
        game.getDungeon().getCurrentHall().handleChosenBox(game.getPlayer(), runeLocation);

        //checking if it contains the rune
        boolean hasRune = game.getPlayer().isHasRune();
        assertTrue(hasRune);
    }

}