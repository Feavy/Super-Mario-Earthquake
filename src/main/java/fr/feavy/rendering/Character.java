package fr.feavy.rendering;

import java.awt.Graphics2D;
import java.awt.Point;

import fr.feavy.event.events.Event;
import fr.feavy.event.listeners.EventListener;
import fr.feavy.game.levels.Level;
import fr.feavy.game.levels.Platform;
import fr.feavy.image.Sprite;
import fr.feavy.rendering.object.LevelObject;
import fr.feavy.utils.Game;
import fr.feavy.window.Main;

public abstract class Character extends LevelObject implements EventListener{

	private Point location;
	private Point defaultLocation;
	private Sprite sprite;

	private AnimationRunnable anim;

	private int xOffset, yOffset;

	private final int LEFT = 37, UP = 38, RIGHT = 39, DOWN = 40;

	private int lookingDirection;

	private int xVelocity, yVelocity = 0;
	
	private int xVelocityDif = 0;

	private Level level;

	private boolean isJumping = false;
	private boolean isMoving = false;
	
	private int width, height;
	
	private int live = 3;
	
	private boolean frozen = false;
	
	private Platform platform;
	
	private int rightRow, leftRow;
	
	private int maxXVelocity;
	
	public static int WHEN_MOVING = 0;
	public static int ALWAYS = 1;
	
	private int animationId;
	
	private int animationSleepTime;
	
	private int yDecalage = 0;
	
	public Character(Sprite sprite, int animationId, double sizeMultiplier, int x, int y, int movingRightRow, int movingLeftRow, int maxXVelocity) {
		
		super(x,y,null);
		
		this.animationSleepTime = 100+animationId*100;
		this.animationId = animationId;
		this.maxXVelocity = maxXVelocity;
		this.rightRow = movingRightRow;
		this.leftRow = movingLeftRow;
		
		this.frozen = false;
		
		Main.addEventListener(this);
		
		this.sprite = sprite;
		
		this.location = new Point(x, y);
		anim = new AnimationRunnable();
		
		xOffset = 0;
		yOffset = 1;
		
		yVelocity = 0;
		xVelocity = 0;
		xVelocityDif = 0;
		
		lookingDirection = LEFT;
		isJumping = false;
		isMoving = false;
		
		this.width = (int) (sprite.getWidth()*2*sizeMultiplier);
		this.height = (int) (sprite.getHeight()*2*sizeMultiplier);
		
	}
	
	public void setAnimationSpeedTime(int time){animationSleepTime = time;}
	
	public void load() {
		super.load();
		platform = new Platform(0, false, getStartX(), getStartY(), getEndX(), getEndY()+5);
		super.addPlatform(platform);
		new Thread(anim).start();
	}

	public void freeze(){
		stopMoving(39);
		frozen = true;
	}
	
	public void unFreeze(){frozen = false;}
	
	public void setPosition(int x, int y){
		this.location = new Point(x, y);
		this.defaultLocation = new Point(x, y);
	}
	
	public void setLevel(Level parent){
		this.level = parent;
	}
	
	private int getStartX(){
		return getPX(location.x);
	}
	
	public int getMiddleX(){
		return (int) ((getStartX()+getEndX())/2d);
	}
	
	private int getEndX(){
		return getPX(location.x+width);
	}
	
	public int getStartY(){
		return getPY(location.y);
	}
	
	private int getEndY(){
		return getPY(location.y+height);
	}
	
	public Level getLevel(){return level;}
	
	public abstract void onHit();
	
	public void resetPosition(){
		setPosition(defaultLocation.x, defaultLocation.y);
		yOffset = 1;
		yVelocity = 0;
	}
	
	public void jump(){
		yOffset = -1;
		yVelocity = 15;
		isJumping = true;
	}
	
	public void startMoving(int direction) {
		if(frozen)
			return;
		if (direction == UP) {
			if (isJumping)
				return;
			jump();
		} else if (direction != DOWN) {
			isMoving = true;
			lookingDirection = direction;
			xVelocity = (isJumping) ? 3 : 2;
			xOffset = Game.getXOffset(direction);
			xVelocityDif = 1;
		}

	}

	public void stopMoving(int direction) {
		if (direction == 37 || direction == 39) {
			xVelocityDif = -1;
		}
	}

	public boolean testPlatform(LevelObject obj){
		for(Platform p : obj.getPlatforms()){
			if (getEndX() >= p.getX1() && getStartX() <= p.getX2()) {
				if ((getEndY()) - p.getY1() > 0 && (getEndY()) - p.getY1() < getPY(20) && yVelocity > 5 ) {
					if(p.isDdamageable())
						onHit();
					if(!p.isGhost()){
						location.y = p.getPY2(p.getY1() - getPY(height));
						isJumping = false;
						yVelocity = 10;
					}
					obj.platformPressed(this, p.getId());
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean testPlatformSides(LevelObject obj){
		for(Platform p : obj.getPlatforms()){
			if(getEndY() < p.getY2() && getEndY() > p.getY1()){
				if(getEndX() - p.getX1() >= 0 && getEndX() - p.getX1() < 20 && xOffset > 0){
					if(!p.isGhost()){
						location.x = getPX2(p.getX1() - getPX(width)-1);
						xVelocity = 0;
						xOffset = 0;
						isMoving = false;
					}
					obj.platformContact(this, p.getId());
					return true;
				}else if(getStartX()-p.getX2() <= 0 && getStartX()-p.getX2() > -20 && xOffset < 0){
					if(!p.isGhost()){
						location.x = getPX2(p.getX2()+1);
						xVelocity = 0;
						xOffset = 0;
						isMoving = false;
					}
					obj.platformContact(this, p.getId());
					return true;
				}
			}
		}
		return false;
	}
	
	public void setDecalage(int offset){
		yDecalage = offset;
	}
	
	public void setLocation(Point newLocation){
		this.location = newLocation;
	}
	
	public int getFacingDirection(){return lookingDirection;}
	
	public void refreshPlatform(){
		platform.setBounds(getPX2(getStartX())+20, getPY2(getStartY())+20, getPX2(getEndX())-20, getPY2(getEndY())+5);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(!loaded())
			return;
		g.drawImage(sprite.getCurrentImage(), getPX(location.x), getPY(location.y+yDecalage), getPX(width), getPY(height), null);
		
		location.x += xVelocity*xOffset;
		location.y += yVelocity*yOffset;

		if(yVelocity <= 15)
			yVelocity += yOffset;

		if(xVelocity < maxXVelocity && xVelocityDif > 0 && !isJumping){
			xVelocity += xVelocityDif;
		}
		if(xVelocityDif < 0){
			xVelocity += xVelocityDif;
		}
		if(xVelocity <= 0){
			xOffset = 0;
			xVelocityDif = 0;
			isMoving = false;
		}
		
		if (yVelocity == 0) {
			if (yOffset < 0) {
				yVelocity = 1;
				yOffset = 1;
			}
		}
		
		if (yOffset > 0){
			boolean onPlatform = false;
			for (LevelObject obj : level.getObjects()) {
				onPlatform = testPlatform(obj);
				if(onPlatform)
					break;
			}
			if(!onPlatform){
				for (LevelObject obj : level.getCharacters()) {
					onPlatform = testPlatform(obj);
					if(onPlatform)
						break;
				}
			}
			if(!onPlatform)
				isJumping = true;
		}
		
		if(isMoving){
			for (LevelObject obj : level.getObjects()) {
				testPlatformSides(obj);
				if(!isMoving)
					break;
			}
			if(isMoving){
				for (LevelObject obj : level.getCharacters()) {
					testPlatformSides(obj);
					if(!isMoving)
						break;
					
				}
			}
			if(!isMoving)
				blockedByPlatform();
		}
		refreshPlatform();
	}

	public void destroy(){
		level.removeCharacter(this);
	}
	
	public void blockedByPlatform(){
		
	}
	
	private class AnimationRunnable implements Runnable {
		@Override
		public void run() {
			int row = 0;
			while (loaded()) {
				try {
					if(isMoving && animationId == WHEN_MOVING || animationId == ALWAYS){
						if(lookingDirection == RIGHT){
							sprite.nextImage(rightRow);
							row = rightRow;
						}else{
							sprite.nextImage(leftRow);
							row = leftRow;
						}
					}else if(animationId == ALWAYS){
						sprite.nextImage(row);
					}
					Thread.sleep(animationSleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void callEvent(Event e) {
		Main.callEvent(e);
	}

	@Override
	public void onEvent(Event e) {
		// TODO Auto-generated method stub
		
	}

}
