package fr.feavy.rendering;

import java.awt.image.BufferedImage;

import fr.feavy.image.Sprite;
import fr.feavy.sounds.GameSounds;
import fr.feavy.utils.Game;

public class Goumba extends Character{

	public Goumba(BufferedImage goumbaSprite, int x, int y) {
		super(new Sprite(goumbaSprite, false, 30, 25, 0, 0), Character.WHEN_MOVING, 0.75, x, y, 1, 0, 2);
	}

	@Override
	public void blockedByPlatform() {
		super.blockedByPlatform();
		startMoving(Game.getOpositeDirection(getFacingDirection()));
	}
	
	
	@Override
	public void platformContact(Character actor, int id) {
		if(actor instanceof Mario){
			((Mario)actor).onHit();
		}
		super.platformContact(actor, id);
	}
	
	@Override
	public void platformPressed(Character actor, int id) {
		super.platformPressed(actor, id);
		if(actor instanceof Mario){
			GameSounds.playSound(GameSounds.STOMP);
			((Mario)actor).jump(false);
		}
		destroy();
	}
	
	@Override
	public void onHit() {
		// TODO Auto-generated method stub
		
	}
	
}
