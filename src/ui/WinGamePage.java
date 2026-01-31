package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class WinGamePage extends Page implements ActionListener {

    private JButton playAgainButton;
    private JButton exitButton;
    private ImageIcon icon;

    WinGamePage() {
        super();
        initUI();
    }

    protected void initUI() {
        icon = new ImageIcon("src/assets/LOGO.png");
        JLabel backgroundLabel = new JLabel(icon);
        backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundLabel.setVerticalAlignment(JLabel.TOP);
        backgroundLabel.setBackground(Color.black);
        backgroundLabel.setOpaque(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Gongster", Font.BOLD, 25));
        exitButton = new JButton("Main Menu");
        exitButton.setFont(new Font("Gongster", Font.BOLD, 25));
        playAgainButton.setBackground(new Color(101, 67, 33));
        exitButton.setBackground(new Color(101, 67, 33));
        playAgainButton.setForeground(Color.BLACK);
        exitButton.setForeground(Color.BLACK);
        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setOpaque(true);

// Create the message label with custom font and color
        JLabel messageLabel = new JLabel("You won the game!");
        messageLabel.setFont(new Font("Gongster", Font.BOLD, 25));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBackground(Color.BLACK);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setOpaque(true);

// Add the message to a panel with padding to move it higher
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        messagePanel.setBackground(Color.BLACK);
        messagePanel.add(messageLabel);

// Set the layout and add components
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 800));
        this.add(backgroundLabel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
        this.add(messagePanel, BorderLayout.SOUTH);

// Add action listeners
        playAgainButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playAgainButton) {
            PageManager.getInstance().showBuildingModePage();
        }

        if (e.getSource() == exitButton) {
            PageManager.getInstance().showMainMenuPage();
        }
    }
}
