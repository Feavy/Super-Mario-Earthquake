package fr.feavy.rendering;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import fr.feavy.game.levels.Platform;
import fr.feavy.window.GameScreen;

public abstract class ScreenObject {

	private boolean loaded;
	
	public ScreenObject(){
		this.loaded = false;
	}
	
	public void load(){loaded = true;}
	
	public void unLoad(){loaded = false;}
	
	public abstract void draw(Graphics2D g);
	
	protected GameScreen getScreen(){ return GameScreen.instance; }
	
	public boolean loaded(){return loaded;}
	
	protected int getIntAxeX(int x, int y){ return getScreen().getIntAxeX(x, y); }
	
	protected int getIntAxeY(int y){ return getScreen().getIntAxeY(y); }
	
	protected int getPX(int nb){ return getScreen().getPX(nb); }
	
	protected double getPX(double xOffset){ return getScreen().getPX(xOffset); }
	
	protected int getPY(int nb){ return getScreen().getPY(nb); }
	
	protected double getPY(double nb){ return getScreen().getPY(nb); }
	
	protected int getPX2(int nb){ return getScreen().getPX2(nb); }
	
	protected int getPY2(int nb){ return getScreen().getPY2(nb); }
	
}
