package ui;

import domain.Game;
import domain.agent.Player;
import domain.collectables.EnchantmentType;
import domain.level.CountDownTimer;
import domain.util.Coordinate;
import listeners.GameListener;
import listeners.PlayerListener;
import listeners.TimerListener;
import ui.Swing.Panels.GamePanel;
import ui.Swing.Panels.HallPanelHolder;
import ui.Swing.Panels.PlayModeMenu;

import javax.swing.*;
import java.awt.*;

public class PlayModePage extends Page implements PlayerListener, GameListener, TimerListener {

    private HallPanelHolder panelHolder;

    private JPanel objectChooserPanel;

    private PlayModeMenu buttonPanel;

    private JLabel timerLabel;

    public PlayModePage(boolean isNewGame) {
        super();
        initUI();

        this.subscribe(Game.getInstance());
        this.subscribe(Game.getInstance().getPlayer());
        if(isNewGame){
            Game.getInstance().startGame();
        }
        else {
            Game.getInstance().continueGame();
        }
    }

    @Override
    protected void initUI() {

        this.setBackground(new Color(66, 40, 53));

        this.panelHolder = new HallPanelHolder(new GamePanel());
        this.add(panelHolder);

        this.objectChooserPanel = new JPanel();
        this.objectChooserPanel.setPreferredSize(new Dimension(250, 750));
        objectChooserPanel.setLayout(new BorderLayout());
        this.add(objectChooserPanel, BorderLayout.EAST);

        JPanel wrapperPanel = new JPanel(new GridBagLayout()); // Centers its child
        wrapperPanel.setBackground(this.objectChooserPanel.getBackground()); // Match background
        this.objectChooserPanel.add(wrapperPanel, BorderLayout.CENTER);

        this.buttonPanel = new PlayModeMenu();

        wrapperPanel.add(buttonPanel);

        wrapperPanel.setBackground(new Color(66, 40, 53));

        timerLabel = new JLabel("Seconds: " +
                Game.getInstance().getDungeon().getCurrentHall().getTimer().getInitialTimeRemaining());
        timerLabel.setBounds(40, 400, 200, 20);
        timerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 22));
        timerLabel.setForeground(new Color(255, 255, 255));
        this.buttonPanel.add(timerLabel);


        this.buttonPanel.setLayout(null);

        //subscribe to all halls timers.
        for (int i = 0; i < 4; i++) {
            CountDownTimer timer = Game.getInstance().getDungeon().getHalls()[i].getTimer();
            this.subscribe(timer);
        }
        SwingUtilities.invokeLater(panelHolder.getExternalPanel()::requestFocusInWindow);
    }

    private void updateTimer(CountDownTimer timer) {
        timerLabel.setText("Seconds: " + timer.getTimeRemaining());
        this.buttonPanel.revalidate();
        this.buttonPanel.repaint();
    }

    public void subscribe (Player p) {
        p.addListener(this);
    }

    private void subscribe(Game game) {
        game.addListener(this);
    }

    private void subscribe(CountDownTimer timer) {
        timer.addListener(this);
    }

    @Override
    public void onHealthEvent(int num) {
        this.buttonPanel.updateLives(num);
    }


    @Override
    public void onTimerEvent(CountDownTimer timer) {
        updateTimer(timer);

        if (timer.getTimeRemaining() <= 0) {
            Game.getInstance().loseGame();
        }
    }

    @Override
    public void onRuneEvent(boolean hasRune) {
        this.buttonPanel.updateRune(hasRune);
        this.panelHolder.setDoorOpen(hasRune);
    }

    @Override
    public void onCollectEnch(EnchantmentType type) {
        this.buttonPanel.updateEnchCount(type, 1);
    }

    @Override
    public void onRemoveEnch(EnchantmentType type) {
        this.buttonPanel.updateEnchCount(type, -1);
    }

    @Override
    public void onHallChange(int currentHall) {
        this.panelHolder.setCurrentHall(currentHall);
    }

    @Override
    public void onGameEvent(Game game) {
        this.buttonPanel.updatePause(game.isPaused());
    }

    @Override
    public void onHighlightEvent(Coordinate coordinate) {}

    @Override
    public void onGameWin() {
        PageManager.getInstance().showWinGamePage();
    }

    @Override
    public void onGameLose() {
        PageManager.getInstance().showGameOverPage();
    }
}
