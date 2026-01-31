package listeners;

import domain.agent.monster.Fighter;

public interface FighterListener {

    public abstract void onFireEvent(Fighter fighter);
}
