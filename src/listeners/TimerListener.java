package listeners;

import domain.level.CountDownTimer;

public interface TimerListener {
    public abstract void onTimerEvent(CountDownTimer timer);
}
