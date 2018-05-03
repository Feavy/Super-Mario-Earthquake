package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.feavy.game.levels.Platform;
import fr.feavy.image.GameImage;
import fr.feavy.rendering.Character;

public class BlackRectangle extends LevelObject {

	private final Rectangle platform = new Rectangle(0, 0, 240, 87);
	
	private Platform p;
	
	public BlackRectangle(int x, int y, BufferedImage image) {
		super(x, y, image);
		p = new Platform(0, false, x+15, y+15, x + 110, y+20);
		addPlatform(p);
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getImage(),
				getScreen().getPX(getX()), getScreen().getPY(getY()),
				getScreen().getPX(platform.width/2), getScreen().getPY(platform.height/2), null);
	}

}
