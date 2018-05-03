package fr.feavy.event.events;

public class Event {

	private String id;
	
	public Event(String eventID){
		id = eventID;
	}
	
	public String getID(){ return id; }
	
}
