package fr.feavy.event.events;

public class KeyReleasedEvent extends Event{

	private int keyCode;
	
	public KeyReleasedEvent(int keyCode) {
		super("keyReleasedEvent");
		this.keyCode = keyCode;
	}
	
	public int getKeyCode(){ return keyCode; }

}
