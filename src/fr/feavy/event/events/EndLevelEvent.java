package fr.feavy.event.events;

import fr.feavy.game.levels.Level;

public class EndLevelEvent extends Event{

	private boolean win;
	private Level level;
	
	public EndLevelEvent(Level l, boolean win) {
		super("endLevelEvent");
		this.win = win;
		this.level = l;
	}

	public Level getLevel(){return level;}
	public boolean win(){ return win; }
	
}
