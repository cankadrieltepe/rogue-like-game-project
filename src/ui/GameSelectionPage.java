
package ui;

import domain.GameSaveLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameSelectionPage extends Page implements ActionListener{

    private ArrayList<JButton> gameList;
    private ImageIcon icon;
    JPanel buttonPanel;

    GameSelectionPage(int size) {
        // Add the background image
        super();
        gameList = new ArrayList<JButton>();
        initUI();

        createGameButtons(size);
    }

    protected void initUI() {
        icon = new ImageIcon("src/assets/LOGO.png");
        JLabel backgroundLabel = new JLabel(icon);
        backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundLabel.setVerticalAlignment(JLabel.TOP);
        backgroundLabel.setBackground(Color.black);
        backgroundLabel.setOpaque(true);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); // Buttons centered with some space

        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setOpaque(true);

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 800));
        this.add(backgroundLabel, BorderLayout.NORTH); // Add image at the top
        this.add(buttonPanel, BorderLayout.CENTER); // Add buttons at the bottom


    }

    private void createGameButtons(int size) {
        for (int i = 0; i < size; i++) {
            JButton button = new JButton("Saved Game" + (i + 1));
            button.setFont(new Font("Gongster", Font.BOLD, 25));
            button.setBackground(new Color(101, 67, 33));
            button.setForeground(Color.BLACK);
            gameList.add(button);
            buttonPanel.add(button);
            button.addActionListener(this);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < gameList.size(); i++) {
            if (e.getSource() == gameList.get(i)) {
                GameSaveLoader.getInstance().loadGame(i);
                PageManager.getInstance().continuePlayModePage();
            }
        }
    }
}

