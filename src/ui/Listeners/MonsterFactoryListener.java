package ui.Listeners;

import domain.agent.monster.Monster;
import domain.factories.MonsterFactory;

public interface MonsterFactoryListener {

    public abstract void onCreationEvent(MonsterFactory factory, Monster monster);

}
