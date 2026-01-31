package domain.util;

import java.io.Serializable;

public class Coordinate implements Serializable {

    private int x; // x coordinate
    private int y; // y coordinate

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate location) {
        this.x = location.x;
        this.y = location.y;
    }

    //Set the location of the coordinate
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate c = (Coordinate) obj;
            return this.x == c.x && this.y == c.y;
        }
        return false;
    }

    public static double euclideanDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x,2) + Math.pow(c1.y - c2.y,2));
    }
    public static int manhattanDistance (Coordinate c1, Coordinate c2) {
        return Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
    }

    public int getX() {return x;}
    public void setX(int x) {this.x = x;}

    public int getY() {return y;}
    public void setY(int y) {this.y = y;}
}
