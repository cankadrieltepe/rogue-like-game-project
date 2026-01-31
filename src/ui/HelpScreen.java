package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//we can put .gif for imageicons or .png

public class HelpScreen extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JButton btnNext, btnPrevious;
    private int currentPage = 0;
    private final String[] helpTexts = {
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Welcome to Rokue Like</h1><p>This game is about a adventurer going escaping from different dungeon halls. Use enchantments and runes to avoid from the monsters and get out of the halls.<br>You can also save the game and play later.</p></body></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Build the Game</h1><p>In building mode,manually select the type of object and its location. <br> There must be at least 6 objects in the earth hall.<br>There must be at least 9 objects in the air hall.<br> There must be at least 13 objects in the water hall. <br> There must be at least 17 objects in the fire hall. </p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Moving</h1><p>Move the character using 'w', 'a' ,'s', 'd'.</p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Runes</h1><p>You have to find the rune in the hall to open the door and go to next hall.</p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Archer Monsters</h1><p>Shoots arrows every second if the hero is within 4 squares. The hero loses a life unless they are wearing the Cloak of Protection.</p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Fighter Monsters</h1><p>Attacks when close to the hero with a dagger. Can be distracted by the luring gem.</p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Wizard Monsters</h1><p> If less than 30% of the time remains, it teleports the player and disappears;<br> if more than 70% of the time remains, it teleports the rune every 3 seconds; <br> if between 30% and 70%, it stays for 2 seconds without doing anything and disappears.</p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Extra Time</h1><p>Adds 5 seconds to the timer when collected.</p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Reveal</h1><p>Highlights a 4x4 grid area showing where the rune is hidden. Press 'R' to use.  </p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Cloak of Protection</h1><p> Protects the hero from archer attacks for 20 seconds. Press 'P' to use. </p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Luring Gem</h1><p> Distracts fighter monsters by luring them toward a direction. Press 'B' and press W/A/S/D to select the throwing direction. </p></html>",
            "<html><head><style>body { text-align: center; }</style></head><body><h1>Extra Life</h1><p> Increases hero’s lives by 1 when collected. </p></html>"
    };
    private final String[] imagePaths = {
            null,  // No image for the first 4 pages yet
            null,
            null,
            null,
            "src/assets/archer.png",
            "src/assets/fighter.png",
            "src/assets/wizard.png",
            null,  // No image for "Extra Time" yet
            "src/assets/reveal.png",
            "src/assets/cloak.png",
            "src/assets/lure.png",
            "src/assets/heart.png"
    };
    public HelpScreen() {
        setTitle("Help - Rokuelike Game");
        setSize(800, 500); // Square size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        for (int i = 0; i < helpTexts.length; i++) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(101, 67, 33));
            JLabel label = new JLabel(helpTexts[i]);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(new Font("Gongster", Font.PLAIN, 18));
            label.setForeground(Color.WHITE);


            ImageIcon imageIcon = new ImageIcon(imagePaths[i]);
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(imageLabel, BorderLayout.NORTH);

            panel.add(label, BorderLayout.CENTER);
            mainPanel.add(panel);
        }

        btnNext = new JButton("Next");
        btnPrevious = new JButton("Previous");
        btnPrevious.setEnabled(false);

        btnNext.addActionListener(e -> {
            if (currentPage < helpTexts.length - 1) {
                currentPage++;
                cardLayout.next(mainPanel);
                btnPrevious.setEnabled(true);
            }
            if (currentPage == helpTexts.length - 1) {
                btnNext.setEnabled(false);
            }
        });

        btnPrevious.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                cardLayout.previous(mainPanel);
                btnNext.setEnabled(true);
            }
            if (currentPage == 0) {
                btnPrevious.setEnabled(false);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnPrevious);
        buttonPanel.add(btnNext);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

}
