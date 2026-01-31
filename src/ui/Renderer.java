package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import domain.Textures;

public class Renderer {
	
	
	private int scale;
	
	
	public Renderer() {
		this.scale = 3;
	}
	
	
	
	public void renderEmptyHall(Graphics g) {
		
		BufferedImage sprite = Textures.getSprite("floor_plain");
		
		
		
	}

}
