package fr.feavy.event.events;

import java.awt.Point;

public class ReachLevelPortalEvent extends Event{

	private int movingDirection;
	private Point location;
	
	public ReachLevelPortalEvent(int movingDirection, Point location) {
		super("reachLevelPortalEvent");
		this.movingDirection = movingDirection;
		this.location = location;
	}

	public int getMovingDirection(){ return movingDirection; }
	public Point getLocation(){ return location; }
	
}
