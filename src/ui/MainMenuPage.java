
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPage extends Page implements ActionListener{
	
    private JButton startButton;
    private JButton helpButton;
    private JButton loadButton;
    private ImageIcon icon;
    private HelpScreen helpScreen;

    MainMenuPage() {
        // Add the background image
    	
    	super();
    	
    	initUI();

        helpScreen = new HelpScreen();

    }
    
    protected void initUI() {
    	
    	
    	icon = new ImageIcon("src/assets/LOGO.png");
        JLabel backgroundLabel = new JLabel(icon);
        backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundLabel.setVerticalAlignment(JLabel.TOP);
        backgroundLabel.setBackground(Color.black);
        backgroundLabel.setOpaque(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); // Buttons centered with some space
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Gongster",Font.BOLD,25));
        helpButton = new JButton("Help");
        helpButton.setFont(new Font("Gongster",Font.BOLD,25));
        loadButton = new JButton("Load Game");
        loadButton.setFont(new Font("Gongster",Font.BOLD,25));
        startButton.setBackground(new Color(101, 67, 33));
        helpButton.setBackground(new Color(101, 67, 33));
        loadButton.setBackground(new Color(101, 67, 33));
        startButton.setForeground(Color.BLACK);
        helpButton.setForeground(Color.black);
        loadButton.setForeground(Color.BLACK);
        buttonPanel.add(startButton);
        buttonPanel.add(helpButton);
        buttonPanel.add(loadButton);
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setOpaque(true);
        // Create the main frame
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 800));
        this.add(backgroundLabel, BorderLayout.NORTH); // Add image at the top
        this.add(buttonPanel, BorderLayout.CENTER); // Add buttons at the bottom

        startButton.addActionListener(this);
        helpButton.addActionListener(this);
        loadButton.addActionListener(this);
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton) {
            //open build mode
        	
            PageManager.getInstance().showBuildingModePage();
        }

        if(e.getSource() == helpButton){
            //open view help
            helpScreen.setVisible(true);

        }

        if(e.getSource() == loadButton) {
            PageManager.getInstance().showGameSelectionPage();
        }
    }
}

