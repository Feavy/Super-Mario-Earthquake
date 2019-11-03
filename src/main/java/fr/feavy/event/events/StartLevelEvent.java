package fr.feavy.event.events;

public class StartLevelEvent extends Event{

	private int lvlID;
	
	public StartLevelEvent(Integer lvlID) {
		super("startLevelEvent");
		this.lvlID = lvlID;
	}

	public int getLevelID(){ return lvlID; }
	
}
