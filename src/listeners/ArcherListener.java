package listeners;
import domain.agent.monster.Archer;
import domain.agent.monster.Monster;

public interface ArcherListener {

    public abstract void onArrowActivationEvent(Archer archer);

    public abstract void onArrowDeactivationEvent(Archer archer);

    public abstract void onNewGameEvent();

    public abstract void onGameOverEvent();
}