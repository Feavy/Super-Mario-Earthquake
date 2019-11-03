package fr.feavy.game.levels;

import java.awt.Graphics2D;

import fr.feavy.rendering.ScreenObject;
import fr.feavy.window.GameScreen;

public class Platform extends ScreenObject{

	private int x1, x2, y1, y2, id;
	private boolean damageable, ghost;
	
	public Platform(int id, boolean damageable, int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.id = id;
		this.damageable = damageable;
		this.ghost = false;
	}
	
	public Platform(int id, boolean damageable, boolean ghost, int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.id = id;
		this.damageable = damageable;
		this.ghost = ghost;
	}
	
	public boolean isGhost(){return ghost;}
	public boolean isDdamageable(){return damageable;}
	
	public int getAbsX1(){
		return x1;
	}
	
	public int getAbsX2(){
		return x2;
	}
	
	public int getAbsY1(){
		return y1;
	}
	
	public int getAbsY2(){
		return y2;
	}
	
	public int getX1(){ return getScreen().getPX(Math.min(x1, x2)); }
	public int getX2(){ return getScreen().getPX(Math.max(x1, x2)); }
	public int getY1(){ return getScreen().getPY(Math.min(y1, y2)); }
	public int getY2(){ return getScreen().getPY(Math.max(y1, y2)); }

	public int getId(){ return id; }
	
	public void setBounds(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	
}
