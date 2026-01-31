package ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Pause extends JFrame implements ActionListener{

	 JButton resumeButton;
	 JButton helpButton;
	 public Pause(){
		 	ImageIcon icon=new ImageIcon("pause.png");
	        JLabel backgroundLabel = new JLabel();
	        
	        backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
	        backgroundLabel.setVerticalAlignment(JLabel.TOP);
	        backgroundLabel.setText("Pause Screen");
	        backgroundLabel.setFont(new Font("Gongster",Font.BOLD,100));
	        backgroundLabel.setBackground(new Color(101, 67, 33));
	        backgroundLabel.setOpaque(true);
	        
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));	     
	        resumeButton = new JButton();
	        resumeButton.setIcon(icon);
	        resumeButton.setBorderPainted(false);
	        //resumeButton.setContentAreaFilled(false);
	        resumeButton.setAlignmentX(CENTER_ALIGNMENT);

	       // resumeButton.setFocusPainted(false);
	        helpButton = new JButton("Help");
	        helpButton.setFont(new Font("Gongster",Font.BOLD,50));
	        resumeButton.setBackground(new Color(101, 67, 33));
	        helpButton.setBackground(new Color(101, 67, 33));
	        resumeButton.setForeground(Color.WHITE);
	        helpButton.setForeground(Color.WHITE);
	        helpButton.setAlignmentX(CENTER_ALIGNMENT);
	        
	        buttonPanel.add(resumeButton);
	        buttonPanel.add(Box.createVerticalStrut(20));
	        buttonPanel.add(helpButton);
	        buttonPanel.setBackground(new Color(101, 67, 33));
	        buttonPanel.setOpaque(true);
	        // Create the main frame
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setSize(1000, 800); // Adjust size as needed
	        this.setLayout(new BorderLayout());
	        this.add(backgroundLabel, BorderLayout.NORTH); // Add image at the top
	        this.add(buttonPanel, BorderLayout.CENTER); // Add buttons at the bottom

	        this.setVisible(true);
	     
	    }
	 @Override
	 public void actionPerformed(ActionEvent e) {
   	  if(e.getSource()==resumeButton) {
   		  //open build mode
   	  }if(e.getSource()==helpButton){
   		  //open view help
   	  }
     }


}
