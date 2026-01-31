package domain.level;

import domain.objects.ObjectType;

import java.io.Serializable;

public class GridDesign implements Serializable {
    private ObjectType[][]  grid;
    private int minObjectLimit; //min num of objects needed to be placed in the hall during build mode.
    private int placedObjectCount = 0;

    public GridDesign(int rows, int cols, int minObjectLimit) {
        grid = new ObjectType[rows][cols];
        this.minObjectLimit = minObjectLimit;
    }

    public boolean placeObject(int row, int col, ObjectType type) {
        if(isValid(row,col) && grid[row][col] == null) {
            grid[row][col] = type;
            if(type != ObjectType.COLUMN) placedObjectCount++;
            return  true;
        }
        return false;
    }

    public boolean removeObject(int row, int col) {
        if(isValid(row,col) && grid[row][col] != null) {
            grid[row][col] = null;
            placedObjectCount--;
            return true;
        }
        return false;
    }

    public boolean isObjectPresent (int row, int col){
        return grid[row][col]!=null;
    }


    public boolean isPlacementComplete()
    {
        //returns true for now for testing purposes
        return placedObjectCount >= minObjectLimit;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    public int getMinObjectLimit() {return minObjectLimit;}
    public int getPlacedObjectCount() {return placedObjectCount;}

    public ObjectType[][] getGrid() {
        return grid;
    }
}
