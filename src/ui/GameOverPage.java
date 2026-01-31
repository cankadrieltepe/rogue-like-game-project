package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPage extends Page implements ActionListener {

    private JButton retryButton;
    private JButton exitButton;
    private ImageIcon icon;

    GameOverPage() {
        super();
        initUI();
    }

    protected void initUI() {
        // Add background image
        icon = new ImageIcon("src/assets/LOGO.png");
        JLabel backgroundLabel = new JLabel(icon);
        backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundLabel.setVerticalAlignment(JLabel.TOP);
        backgroundLabel.setBackground(Color.black);
        backgroundLabel.setOpaque(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); // Buttons centered with space
        retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Gongster", Font.BOLD, 25));
        exitButton = new JButton("Main Menu");
        exitButton.setFont(new Font("Gongster", Font.BOLD, 25));
        retryButton.setBackground(new Color(101, 67, 33));
        exitButton.setBackground(new Color(101, 67, 33));
        retryButton.setForeground(Color.BLACK);
        exitButton.setForeground(Color.BLACK);
        buttonPanel.add(retryButton);
        buttonPanel.add(exitButton);
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setOpaque(true);

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 800));
        this.add(backgroundLabel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);

        retryButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == retryButton) {
            PageManager.getInstance().showBuildingModePage();
        }

        if (e.getSource() == exitButton) {
            PageManager.getInstance().showMainMenuPage();
        }
    }
}