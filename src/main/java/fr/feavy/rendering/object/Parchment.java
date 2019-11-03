package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.feavy.event.events.ChampignonCollectedEvent;
import fr.feavy.event.events.EndLevelEvent;
import fr.feavy.game.levels.Level;
import fr.feavy.game.levels.Platform;
import fr.feavy.rendering.Character;
import fr.feavy.window.Main;

public class Parchment extends LevelObject{
	
	private final Rectangle parchment = new Rectangle(0, 0, 216, 178);
	private int yOffset = 1;
	
	private int yDiff = 0;
	private int frameSkip = 0;
	
	private boolean collected;
	
	private Level level;

	public Parchment(Level parent, int x, int y, BufferedImage image) {
		super(x, y, image);
		this.level = parent;
		collected = false;
		addPlatform(new Platform(0, false, true, x, y, x + 45, y + 39));
	}

	public boolean hasBeenCollected(){return collected;}
	
	@Override
	public void platformPressed(Character actor, int id) {
		super.platformPressed(actor, id);
		if(!collected){
			collected = true;
			Main.callEvent(new EndLevelEvent(level, true));
		}
		
	}
	
	@Override
	public void platformContact(Character actor, int id) {
		super.platformContact(actor, id);
		if(!collected){
			collected = true;
			Main.callEvent(new EndLevelEvent(level, true));
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(collected)
			return;
		g.drawImage(getImage(), getScreen().getPX(getX()), getScreen().getPY(getY()+yDiff),
				getScreen().getPX(parchment.width / 4), getScreen().getPY(parchment.height / 4), null);
		frameSkip++;
		if(frameSkip > 5){
			yDiff+=yOffset;
			if(yDiff%10 == 0)
				yOffset*=-1;
			frameSkip = 0;
		}
	}
}
