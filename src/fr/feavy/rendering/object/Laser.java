package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.feavy.event.events.Event;
import fr.feavy.window.Main;

public class Laser extends LevelObject{

	private final Rectangle laser = new Rectangle(0, 0, 226, 41);
	private int yOffset = 1;
	private boolean fire = false;
	
	public Laser(int x, int y, BufferedImage image) {
		super(x, y, image);
		this.fire = false;
	}

	public void fire(){fire = true;}	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getImage(), getScreen().getPX(getX()), getScreen().getPY(getY()+yOffset),
				getScreen().getPX(laser.width / 2), getScreen().getPY(laser.height / 2), null);
		yOffset*=-1;
		if(fire){
			setX(getX()+8);
			if(getX() >= 450){
				fire = false;
				Main.callEvent(new Event("laserCourseTerminatedEvent"));
			}
		}
	}

}
