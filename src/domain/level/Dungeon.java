package domain.level;

import domain.factories.MonsterFactory;
import domain.util.Coordinate;

import java.io.Serializable;
import java.util.LinkedList;

public class Dungeon implements Serializable {

    private GridDesign[] gridDesigns;
    private int currentHall;
    private CollisionChecker collisionChecker;
    private Hall[] halls;
    private Coordinate[] doorCoordinates = {
            new Coordinate(3, 0),
            new Coordinate(8, 0),
            new Coordinate(11, 0),
            new Coordinate(8, 0)
    };

    public Dungeon() {
        collisionChecker = new CollisionChecker(this);
    }

    public void loadDesigns(GridDesign[] gridDesigns) {

        halls = new Hall[4];

        halls[0] = new Hall("earth",  gridDesigns[0].getPlacedObjectCount());
        halls[0].transferGridDesign(gridDesigns[0]);

        halls[1] = new Hall("air", gridDesigns[1].getPlacedObjectCount());
        halls[1].transferGridDesign(gridDesigns[1]);

        halls[2] = new Hall("water", gridDesigns[2].getPlacedObjectCount());
        halls[2].transferGridDesign(gridDesigns[2]);

        halls[3] = new Hall("fire", gridDesigns[3].getPlacedObjectCount());
        halls[3].transferGridDesign(gridDesigns[3]);

        currentHall = 0;
    }

    public void nextHall() {
        if(currentHall == 3) {
            return;
        }currentHall++;}

    public void recreateGame() {
        this.collisionChecker = new CollisionChecker(this);
        for(int i = 0; i < 4; i++) {
            halls[i].recreateGame();
        }
    }

    public void prepareGameSave() {
        this.collisionChecker = null;
        for(int i = 0; i < 4; i++) {
            halls[i].prepareGameSave();
        }
    }

    public Hall getCurrentHall() {
        return halls[currentHall];
    }

    public GridDesign[] getGridDesigns(){
        return gridDesigns;
    }

    public void setGridDesigns(GridDesign[] gridDesigns){
        this.gridDesigns = gridDesigns;
    }

    public int getCurrentHallIndex(){
        return currentHall;
    }

    public void setCurrentHall(int currentHall){
        this.currentHall = currentHall;
    }

    public CollisionChecker getCollisionChecker(){
        return collisionChecker;
    }

    public void setCollisionChecker(CollisionChecker collisionChecker){
        this.collisionChecker = collisionChecker;
    }

    public Hall[] getHalls(){
        return halls;
    }

    public void setHalls(Hall[] halls){
        this.halls = halls;
    }

    public Coordinate getDoorCoordinate() {
        return doorCoordinates[currentHall];
    }

}
