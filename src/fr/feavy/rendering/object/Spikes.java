package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.feavy.game.levels.Platform;
import fr.feavy.image.GameImage;
import fr.feavy.rendering.Character;
import fr.feavy.rendering.Mario;
import fr.feavy.sounds.GameSounds;

public class Spikes extends LevelObject{

	private final Rectangle spikes = new Rectangle(0,0,177,107);
	
	public Spikes(int x, int y, BufferedImage image) {
		super(x, y, image);
		addPlatform(new Platform(0, true, x+10, y+5, x + 75, y + 50));
	}
	
	@Override
	public void platformPressed(Character actor, int platformID) {
		super.platformPressed(actor, platformID);
		GameSounds.playSound(GameSounds.SPIKES);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getImage(),
				getScreen().getPX(getX()), getScreen().getPY(getY()),
				getScreen().getPX(spikes.width / 2), getScreen().getPY(spikes.height / 2), null);
	}

}
