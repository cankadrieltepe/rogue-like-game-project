package listeners;

import domain.Game;
import domain.util.Coordinate;

public interface GameListener {

    public abstract void onGameEvent(Game game);

    public abstract void onHighlightEvent(Coordinate coordinate);

    public abstract void onGameWin();

    public abstract void onGameLose();

}
