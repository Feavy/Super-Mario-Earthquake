package fr.feavy.event.listeners;

import fr.feavy.event.events.Event;
import fr.feavy.window.Main;

public interface EventListener {

	public void callEvent(Event e);
	
	public void onEvent(Event e);
	
}
