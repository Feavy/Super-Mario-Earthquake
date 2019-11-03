package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import fr.feavy.game.levels.Platform;
import fr.feavy.rendering.Character;
import fr.feavy.rendering.ScreenObject;
import fr.feavy.window.GameScreen;

public abstract class LevelObject extends ScreenObject{

	private Point location;
	private BufferedImage image;
	private List<Platform> platforms;
	
	protected LevelObject(int x, int y, BufferedImage image) {
		this.platforms = new ArrayList<Platform>();
		this.image = image;
		this.location = new Point(x,y);
	}
	
	@Override
	public void load() {
		this.platforms = new ArrayList<Platform>();
		super.load();
	}
	
	@Override
	public void unLoad() {
		super.unLoad();
		this.platforms = null;
	}
	
	public void platformContact(Character actor, int id){}
	
	public void platformPressed(Character actor, int id) {}
	
	public void addPlatform(Platform p){ 
		platforms.add(p); 
	}
	
	public Platform getPlatform(int index){return platforms.get(index);}
	
	public Platform[] getPlatforms(){ return platforms.toArray(new Platform[platforms.size()]); }
	
	protected BufferedImage getImage(){ return image; }
	
	protected void setX(int nb){ location.x = nb;}
	protected void setY(int nb){ location.y = nb;}
	
	protected int getX(){ return location.x;}
	protected int getY(){ return location.y;}
	
	public abstract void draw(Graphics2D g);

}
