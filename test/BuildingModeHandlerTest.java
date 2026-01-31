
import controllers.BuildingModeHandler;
import domain.objects.ObjectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class BuildingModeHandlerTest {
    private BuildingModeHandler buildingModeHandler;
    @BeforeEach
    void setUp() {
        buildingModeHandler = BuildingModeHandler.recreateBuildingModeHandler();
        buildingModeHandler.setSelectedObject(ObjectType.CHEST_CLOSED);
    }
    /**
     * Test 1: placeObjectAt - valid placement
     * Expect: returns true and object is actually placed
     */
    @Test
    public void testPlaceObjectAt_validPlacement() {
        int row = 0;
        int col = 0;
        boolean result = buildingModeHandler.placeObjectAt(row, col, 0);
        // Object should be placed successfully at (0,0),
        assertTrue( result);
        //  Grid should report object present at (0,0).
        assertTrue(buildingModeHandler.isObjectPresent(row, col, 0));
    }
    /**
     * Test 2: placeObjectAt - no selected object
     * Expect: returns false and no object is placed
     */
    @Test
    public void testPlaceObjectAt_noSelectedObject() {
        // Set selected object to null
        buildingModeHandler.setSelectedObject(null);
        int row = 1;
        int col = 1;
        boolean result = buildingModeHandler.placeObjectAt(row, col, 0);
        // Placement should fail because no object is selected,
        assertFalse( result);
        // Grid should not report an object present at (1,1)
        assertFalse(buildingModeHandler.isObjectPresent(row, col, 0));
    }
    /**
     * Test 3: placeObjectAt - invalid (out-of-bounds) indices
     * Expect: returns false and no object is placed
     *
     */
    @Test
    public void testPlaceObjectAt_outOfBounds() {
        int row = 100;
        int col = 100;
        boolean result = buildingModeHandler.placeObjectAt(row, col, 0);
        // Placing object out-of-bounds should return false.,
        assertFalse(result);
    }
    /**
     * Test 4: placeObjectAt - invalid (already item placed) indices
     * Expect: returns false and object should not be changed
     *
     */
    @Test
    public void testPlaceObjectAt_alreadyExists() {
        int row = 1;
        int col = 1;
        boolean result = buildingModeHandler.placeObjectAt(row, col, 0);
        // Place object at an empty place should return true,
        assertTrue(result);
        buildingModeHandler.setSelectedObject(ObjectType.CHEST_FULL_GOLD);
        boolean result2 = buildingModeHandler.placeObjectAt(row, col, 0);
        // Placing an object which is not empty should return false.
        assertFalse(result2);
    }
}