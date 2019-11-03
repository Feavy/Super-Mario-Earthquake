package fr.feavy.rendering;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import fr.feavy.event.events.Event;
import fr.feavy.event.events.ReachLevelPortalEvent;
import fr.feavy.event.listeners.EventListener;
import fr.feavy.image.GameImage;
import fr.feavy.image.Sprite;
import fr.feavy.utils.Game;
import fr.feavy.window.GameScreen;
import fr.feavy.window.Main;

public class MarioWorldMap extends ScreenObject implements EventListener {

	private Sprite marioWorldMapSprite;

	private SpriteAnimationRunnable marioAnimationRunnable;

	private int yOffset = 0;
	private int xOffset = 0;

	private int pendingXOffset = 0;
	private int pendingYOffset = 0;

	private int pendingDirection = 0;

	static final int LEFT = 37, UP = 38, RIGHT = 39, DOWN = 40;

	private int facingDirection = DOWN;

	private Point location;

	private double xPositionOffset = 0;
	private double yPositionOffset = 0;

	private boolean animation = false;
	private boolean newAnimation = false;
	
	private BufferedImage marioWorldMap;

	public MarioWorldMap(Point position) {
		super();
		marioAnimationRunnable = new SpriteAnimationRunnable();
		Main.addEventListener(this);
		this.location = position;
	}

	@Override
	public void unLoad() {
		super.unLoad();
		this.marioWorldMap = null;
		this.marioWorldMapSprite = null;
	}
	
	@Override
	public void load() {
		super.load();
		this.marioWorldMap = GameImage.createImage(GameImage.MARIO_WORLD_MAP);
		this.marioWorldMapSprite = new Sprite(marioWorldMap, true, 32, 16, 0, 3);
		new Thread(marioAnimationRunnable).start();
	}
	
	@Override
	public void draw(Graphics2D g) {

		int y = getIntAxeY(location.y);
		int x = getIntAxeX(location.x, y);
		if (xOffset != 0) {
			x += xPositionOffset;
			xPositionOffset += getPX((double)xOffset)*Math.abs(getPX((double)xOffset));
			if (x-getIntAxeX(location.x + xOffset, y) <= 4 && x-getIntAxeX(location.x + xOffset, y) >= -4) {
				xPositionOffset = 0;
				location.x += xOffset;
				callEvent(new ReachLevelPortalEvent(facingDirection, location));
				if (pendingDirection != 0) {
					xOffset = pendingXOffset;
					yOffset = pendingYOffset;
					facingDirection = pendingDirection;
				}
			}
		} else if (yOffset != 0) {
			y = getScreen().getAxeY((int) (y + yPositionOffset));
			x = getScreen().getIntAxeX(location.x, y);
			yPositionOffset += getPY((double)yOffset)*Math.abs(getPY((double)yOffset));
			if (y-getIntAxeY(location.y + yOffset) <= 4 && y-getIntAxeY(location.y + yOffset) >= -4) {
				yPositionOffset = 0;
				location.y += yOffset;
				callEvent(new ReachLevelPortalEvent(facingDirection, location));
				if (pendingDirection != 0) {
					xOffset = pendingXOffset;
					yOffset = pendingYOffset;
					facingDirection = pendingDirection;
				}
			}
		} else {
			if (animation) {
				newAnimation = false;
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						if (animation && !newAnimation) {
							marioWorldMapSprite.setDefaultImage();
							animation = false;
						}
					}
				}, 400l);
			}
		}

		g.drawImage(marioWorldMapSprite.getCurrentImage(), x + getScreen().getPX(10),
				y - getScreen().getPY(42), getScreen().getPX(32),
				getScreen().getPY(64), null);
	}

	public boolean isMoving(){ return (xOffset != 0 || yOffset != 0); }
	
	public Point getLocation(){	return location; }

	public void move(int direction){

		newAnimation = true;
		
		if (xOffset == 0 && yOffset == 0) {

			animation = true;

			facingDirection = direction;
			pendingDirection = 0;

			xOffset = 0;
			yOffset = 0;

			xOffset = Game.getXOffset(direction);
			yOffset = Game.getYOffset(direction);

		} else {
			pendingDirection = direction;

			pendingXOffset =  Game.getXOffset(direction);
			pendingYOffset = Game.getYOffset(direction);
			
		}
	}
	
	public void stopMoving(){
		pendingXOffset = 0;
		pendingYOffset = 0;
		pendingDirection = 40;
	}
	
	@Override
	public void callEvent(Event e) {
		Main.callEvent(e);
	}
	
	@Override
	public void onEvent(Event e) {

	}

	private class SpriteAnimationRunnable implements Runnable {
		
		@Override
		public void run() {
			while (loaded()) {
				if (animation)
					marioWorldMapSprite.nextImage(facingDirection - 37);
				try {
					Thread.sleep(120);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void setPosition(Point point) {
		this.location = point;
	}

}
