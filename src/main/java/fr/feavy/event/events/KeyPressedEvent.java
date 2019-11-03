package fr.feavy.event.events;

public class KeyPressedEvent extends Event{

	private int keyCode;
	
	public KeyPressedEvent(int keyCode) {
		super("keyPressedEvent");
		this.keyCode = keyCode;
	}
	
	public int getKeyCode(){ return keyCode; }

}
