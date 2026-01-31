package domain.entities;

import domain.Game;
import domain.agent.Agent;
import domain.agent.monster.Archer;
import domain.util.Coordinate;
import domain.util.Direction;

import java.io.Serializable;

//user Archer olarak seçildi
// Entity -> Projectile -> Arrow
// Entity -> Agent -> Monster -> Archer
// şeklinde hiyerarşiler oluşturabiliriz o zaman değişebilir.

public class Projectile extends Entity implements Serializable {

    public Agent user;
    public Coordinate pos;
    public Direction randomDirection;
    public int dx;
    public int dy;
    public boolean alive;
    public final int speed = 1;
    private final int maxLife = 200;
    public int life;

    public Projectile() {}

    public void set(Coordinate pos, int dx, int dy, boolean alive, Agent user) {
        this.pos = pos;
        this.dx = dx;
        this.dy = dy;
        this.alive = alive;
        this.user = user;
        this.life = maxLife;
    }

    public void update() {
        //alive'ı burda false yap, deactivation event publishle

        pos.setX(pos.getX() + dx*speed);
        pos.setY(pos.getY() + dy*speed);

        life--;

        if (life <= 0) {
            alive = false;
            //user.publishArrowDeactivationEvent();
        }
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void checkCollision() {
        //check collision and determine isAlive
    }
}
