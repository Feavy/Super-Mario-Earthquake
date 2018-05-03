package fr.feavy.rendering;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.feavy.event.events.EndLevelEvent;
import fr.feavy.event.events.StartAnimationEvent;
import fr.feavy.image.GameImage;
import fr.feavy.image.Sprite;
import fr.feavy.rendering.animation.FadeScreen;
import fr.feavy.sounds.GameSounds;
import fr.feavy.utils.Game;
import fr.feavy.window.GameScreen;
import fr.feavy.window.Main;

public class Mario extends Character{
	
	public Mario(int x, int y) {
		super(new Sprite(GameImage.createImage(GameImage.MARIO_WORLD_MAP), true, 32, 16, 0, 2), Character.WHEN_MOVING, 1, x, y, 2, 0, 5);
	}

	@Override
	public void platformContact(Character actor, int id) {
		super.platformContact(actor, id);
		onHit();
	}
	
	@Override
	public void load() {
		super.load();
	}
	
	@Override
	public void jump() {
		jump(true);
	}
	
	public void jump(boolean playSound) {
		super.jump();
		if(playSound)
			GameSounds.playSound(GameSounds.JUMP);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(!loaded())
			return;
		super.draw(g);
		g.setColor(Color.black);
	}

	@Override
	public void onHit() {
		Game.live--;
		if(Game.live == 0){
			callEvent(new StartAnimationEvent(new FadeScreen("Vous avez perdu", 50, Color.BLACK)));
			callEvent(new EndLevelEvent(getLevel(), false));
		}else{
			Main.callEvent(new StartAnimationEvent(new FadeScreen(null, 0, Color.DARK_GRAY)));
			super.resetPosition();
		}
	}
	
}
