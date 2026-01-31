package domain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

/**Initializes texture HashMap and reads all files in assets folder
 * 
 */

public class Textures {
	
	private static HashMap<String, BufferedImage> sprites;

	
	
	
	public static void createSprites() {
	
	sprites = new HashMap<String, BufferedImage>();
	
	File folder = new File("src/assets");
	File folder2 = new File("src/assets/build_mode_assets");
	
	for (File file : folder.listFiles()) {
		
		try {
			sprites.put(file.getName().replaceAll(".png", ""), ImageIO.read(file));
		} catch (IOException e) {

		}
	}

	for (File file : folder2.listFiles()) {
		try {
			sprites.put(file.getName().substring(2).replaceAll("\\.png$", ""), ImageIO.read(file));
		} catch (IOException e) {
			System.err.println("[Utils][Textures]: Exception reading "+file.getName());
		}
	}

	}
	
	public static BufferedImage getSprite(String name) {
		BufferedImage sprite = sprites.get(name);
		if(sprite != null) return sprite;
		else return sprites.get("error");
	}

	public static JLabel createImageLabels(String fileName, int x, int y, int width, int height) {
		ImageIcon imageIcon = new ImageIcon("src/assets/" + fileName +".png");
		Image image1 = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon resizedImageIcon = new ImageIcon(image1);
		JLabel imageLabel = new JLabel(resizedImageIcon);

		imageLabel.setBounds(x, y, width, height);
		imageLabel.setName(fileName);
		return imageLabel;
	}
}
