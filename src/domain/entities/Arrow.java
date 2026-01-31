package domain.entities;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import domain.Game;
import domain.agent.monster.Archer;
import domain.util.*;

public class Arrow extends Projectile {


    public Coordinate arrowPos;
    public Direction randomDirection;

    public int dx;
    public int dy;
    public int speed;
    public int life;
    private final int maxLife;
    protected final Direction[] DIRECTIONS = Direction.values();

    public BufferedImage image;


    public Arrow(Archer archer) {
        speed = 5;
        maxLife = 80;
        life = maxLife;
        alive = false;
    }


 }
