package domain.level;
import domain.Game;
import listeners.TimerListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//each hall has a timer that counts down from 5 * objects placed in build mode.
public class CountDownTimer {
    private ScheduledExecutorService timer;
    private final float initialTimeRemaining;
    private float currentTimeRemaining;
    private List<TimerListener> listeners;

    public CountDownTimer(int initialTime)
    {
        listeners = new LinkedList<>();
        this.initialTimeRemaining = initialTime;
        currentTimeRemaining = initialTime;
    }

    public float getTimeRemaining() {
        return currentTimeRemaining;
    }

    public float getTimeElapsed()
    {
        return initialTimeRemaining - currentTimeRemaining;
    }

    public float getInitialTimeRemaining() {
        return initialTimeRemaining;
    }

    public float getCurrentTimeRemaining() {
        return currentTimeRemaining;
    }

    public void setCurrentTimeRemaining(float currentTimeRemaining) {
        this.currentTimeRemaining = currentTimeRemaining;
    }

    public void increaseTimeRemaining(float amount)
    {
        currentTimeRemaining += amount;
        notifyListeners();
    }

    public void reset()
    {
        currentTimeRemaining = initialTimeRemaining;
        notifyListeners();
    }

    public void addListener(TimerListener listener)
    {
        listeners.add(listener);
    }

    private void notifyListeners()
    {
        System.out.println("Time remaining: " + currentTimeRemaining);
        for (TimerListener listener : listeners) {
            listener.onTimerEvent(this);
        }
    }

    public void start() //starts the timer and reduces 1 every second.
    {
        notifyListeners();
        timer = Executors.newSingleThreadScheduledExecutor();


        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            public void run() {
                currentTimeRemaining -= 1;
                notifyListeners();
                if(currentTimeRemaining <= 0)
                {
                  timer.shutdown();
                  Game.getInstance().loseGame();
                }
            }
        }, 1000, 1000,TimeUnit.MILLISECONDS);
    }

    public void pause() //pauses the timer
    {
        timer.shutdown();
    }

    public boolean isTerminated(){
        return timer.isTerminated();
    }
}
