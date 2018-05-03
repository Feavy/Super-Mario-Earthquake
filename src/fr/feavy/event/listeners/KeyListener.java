package fr.feavy.event.listeners;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import fr.feavy.event.events.Event;
import fr.feavy.event.events.KeyPressedEvent;
import fr.feavy.event.events.KeyReleasedEvent;
import fr.feavy.utils.Game;
import fr.feavy.window.Main;

public class KeyListener implements EventListener, java.awt.event.KeyListener{

	private List<Integer> currentKeyPressed = new ArrayList<Integer>();

	private int cheatPositon = 0;
	private final int[] cheatCode = {Game.UP, Game.UP, Game.DOWN, Game.DOWN, Game.LEFT, Game.RIGHT, Game.LEFT, Game.RIGHT, 66, 65};
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == cheatCode[cheatPositon]){
			cheatPositon++;
			if(cheatPositon == cheatCode.length){
				callEvent(new Event("gameTerminatedEvent"));
				cheatPositon = 0;
			}
		}else
			cheatPositon = 0;
		if(!currentKeyPressed.contains(key)){
			currentKeyPressed.add(key);
			callEvent(new KeyPressedEvent(key));
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		currentKeyPressed.remove((Integer)e.getKeyCode());
		callEvent(new KeyReleasedEvent(e.getKeyCode()));
	}

	@Override
	public void onEvent(Event e) {
	}

	@Override
	public void callEvent(Event e) {
		Main.callEvent(e);
	}

}
