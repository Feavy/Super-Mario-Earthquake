package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.feavy.event.events.ChampignonCollectedEvent;
import fr.feavy.game.levels.Platform;
import fr.feavy.rendering.Character;
import fr.feavy.window.GameScreen;
import fr.feavy.window.Main;

public class Champignon extends LevelObject {

	private final Rectangle champignon = new Rectangle(0, 0, 68, 67);
	private int yOffset = 1;
	
	private int yDiff = 0;
	private int frameSkip = 0;
	
	private boolean collected;

	public Champignon(int x, int y, BufferedImage image) {
		super(x, y, image);
		collected = false;
		addPlatform(new Platform(0, false, true, x, y, x + 34, y + 36));
	}

	public boolean hasBeenCollected(){return collected;}
	
	@Override
	public void platformPressed(Character actor, int id) {
		super.platformPressed(actor, id);
		if(!collected){
			Main.callEvent(new ChampignonCollectedEvent());
			collected = true;
		}
		
	}
	
	@Override
	public void platformContact(Character actor, int id) {
		super.platformContact(actor, id);
		if(!collected){
			Main.callEvent(new ChampignonCollectedEvent());
			collected = true;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(collected)
			return;
		g.drawImage(getImage(), getScreen().getPX(getX()), getScreen().getPY(getY()+yDiff),
				getScreen().getPX(champignon.width / 2), getScreen().getPY(champignon.height / 2), null);
		frameSkip++;
		if(frameSkip > 5){
			yDiff+=yOffset;
			if(yDiff%10 == 0)
				yOffset*=-1;
			frameSkip = 0;
		}
	}

}
