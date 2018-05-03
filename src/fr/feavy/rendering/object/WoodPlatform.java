package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.feavy.game.levels.Platform;
import fr.feavy.image.GameImage;
import fr.feavy.rendering.Character;

public class WoodPlatform extends LevelObject {

	private final Rectangle platform = new Rectangle(0, 0, 449, 43);

	private int xDecalage = 0;
	
	private Platform p;
	
	public WoodPlatform(int x, int y, BufferedImage image) {
		super(x, y, image);
		p = new Platform(0, false, x, y, x + 250, y + 20);
		addPlatform(p);
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getImage(),
				getScreen().getPX(getX()), getScreen().getPY(getY()),
				getScreen().getPX(platform.width - 199-xDecalage), getScreen().getPY(platform.height/2), null);
	}

	public void moveLeft() {
		xDecalage+=5;
		p.setBounds(p.getAbsX1()-5, p.getAbsY1(), p.getAbsX2()-5, p.getAbsY2());
	}

}
