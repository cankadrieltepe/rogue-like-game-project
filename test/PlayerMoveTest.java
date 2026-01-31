import controllers.KeyHandler;
import domain.Game;
import domain.agent.Player;
import domain.agent.monster.Wizard;
import domain.level.GridDesign;
import domain.objects.ObjectType;
import domain.util.Coordinate;
import domain.util.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMoveTest{
    int testRow;
    int testColumn;
    Game game;
    Player player;
    KeyHandler keyHandler;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        player = game.getPlayer();
        keyHandler = new KeyHandler();
        game.setKeyHandler(keyHandler);

        Wizard w = new Wizard();
        w.setLocation(new Coordinate(10,2));
        game.getAgents().add(w);

        game.startGame();

        // setting up random locations to test
        testRow = 5;
        testColumn = 5;

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
     * keyboardInputTest: Test whether player updates its direction based on key input
     * Expect: player's initial direction is start
     *         when go down player's direction is down
     *         hold for all keys
     */
    @Test
    void keyboardInputTest(){

        //Init player
        assertEquals(Direction.STOP, player.getDirection());

        keyHandler.goDown = true;
        player.move();
        //player has to recognize this key stroke
        assertEquals(Direction.DOWN, player.getDirection());
        keyHandler.goDown = false;

        keyHandler.goUp = true;
        player.move();
        //player has to recognize this key stroke
        assertEquals(Direction.UP, player.getDirection());
        keyHandler.goUp = false;

        keyHandler.goLeft = true;
        player.move();
        //player has to recognize this key stroke
        assertEquals(Direction.LEFT, player.getDirection());
        keyHandler.goLeft = false;

        keyHandler.goRight = true;
        player.move();
        //player has to recognize this key stroke
        assertEquals(Direction.RIGHT, player.getDirection());
        keyHandler.goRight = false;
    }

    /**
     * updateLocationTest: check whether player correctly moves in to the directon
     * Expect: if there is nothing to collide then player has to move in to the direction
     *
     *
     */
    @Test
    void moveInDirectionTest() {
        //from the setup we know that there is a chest in (5,10)

        //there is nothing close to (1,1) so agent can freely move
        player.setLocation(new Coordinate(1,1));

        //player has to go up by 1
        keyHandler.goUp = true;
        player.move();
        keyHandler.goUp = false;
        assertEquals(new Coordinate(1,2), player.getLocation());

        //player has to go down by 1
        keyHandler.goDown = true;
        player.move();
        keyHandler.goDown = false;
        assertEquals(new Coordinate(1,1), player.getLocation());

        //player has to go left by 1
        keyHandler.goLeft = true;
        player.move();
        keyHandler.goLeft = false;
        assertEquals(new Coordinate(0,1), player.getLocation());

        //player has to go right by 1
        keyHandler.goRight = true;
        player.move();
        keyHandler.goRight = false;
        assertEquals(new Coordinate(1,1), player.getLocation());

        //there is a box in the (5,10) player cant move in right direction
        player.setLocation(new Coordinate(4,10));
        keyHandler.goRight = true;
        player.move();
        keyHandler.goRight = false;
        assertEquals(new Coordinate(4,10),player.getLocation());

        //there is a box in the (5,10) player cant move in down direction
        player.setLocation(new Coordinate(5,11));
        keyHandler.goDown = true;
        player.move();
        keyHandler.goDown = false;
        assertEquals(new Coordinate(5,11),player.getLocation());

        //there is a box in the (5,10) player cant move in up direction
        player.setLocation(new Coordinate(5,9));
        keyHandler.goUp = true;
        player.move();
        keyHandler.goUp = false;
        assertEquals(new Coordinate(5,9),player.getLocation());

        //there is a box in the (5,10) player cant move in left direction
        player.setLocation(new Coordinate(6,10));
        keyHandler.goLeft = true;
        player.move();
        keyHandler.goLeft = false;
        assertEquals(new Coordinate(6,10),player.getLocation());

        //from the setup there is a wizard at (10,2) so player shouldt move
        player.setLocation(new Coordinate(9,2));
        keyHandler.goRight = true;
        player.move();
        keyHandler.goRight = false;
        assertEquals(new Coordinate(9,2),player.getLocation());
    }
    /**
     * moveToNextHallTest: check whether player can pass to the next hall
     * Expect: if player is adjacent to door and directed to down and has rune
     *         player pass to the next hall
     *
     */
    @Test
    void moveToNextHallTest() {
        //door location is at (3,0), given from the design
        Coordinate doorLoc = new Coordinate(3, 0);
        player.setLocation(doorLoc);


        assertFalse(player.isHasRune());
        keyHandler.goDown = true;
        player.move();
        keyHandler.goDown = false;
        //Since player has no rune player should pass through the door, even the direction is down
        assertEquals(0,game.getDungeon().getCurrentHallIndex());

        keyHandler.goLeft = true;
        player.move();
        keyHandler.goLeft = false;
        //Since player has no rune player should pass through the door, even the direction is down
        assertEquals(0,game.getDungeon().getCurrentHallIndex());

        // no need to show non-runed version for other directions,

        player.setHasRune(true);
        keyHandler.goRight = true;
        player.move();
        keyHandler.goRight = false;
        //Player has rune but direction is different
        assertEquals(0,game.getDungeon().getCurrentHallIndex());


        keyHandler.goDown = true;
        player.move();
        keyHandler.goDown = false;
        //Player now can pass
        assertEquals(1,game.getDungeon().getCurrentHallIndex());
    }
}