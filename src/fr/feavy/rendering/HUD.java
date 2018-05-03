package fr.feavy.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import fr.feavy.image.GameImage;
import fr.feavy.utils.Game;

public class HUD extends ScreenObject{

	private Image populationImage, heartImage, parchmentImage;
	
	public HUD(){
		this.populationImage = GameImage.createImage(GameImage.POPULATION); //225 x 192
		this.heartImage = GameImage.createImage(GameImage.HEART); //202 x 181
		this.parchmentImage = GameImage.createImage(GameImage.PARCHMENT); // 117 x 151
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 105, 90);
		g.drawImage(heartImage, 5, 0, 25, 25, null);
		g.drawImage(parchmentImage, 5, 30, 25, 25, null); 
		g.drawImage(populationImage, 5, 60, 25, 25, null);
		g.setFont(new Font("Impact", Font.PLAIN, 20));
		g.setColor(Color.darkGray);
		g.drawRect(-1, -1, 105, 90);
		g.drawString(Game.live+"", 35, 20);
		g.drawString(Game.getCurrentLevel()+"", 35, 50);
		g.drawString(Game.inhabitantAmount+"", 35, 80);
	}	
}
