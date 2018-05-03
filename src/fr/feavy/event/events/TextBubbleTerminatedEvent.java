package fr.feavy.event.events;

public class TextBubbleTerminatedEvent extends Event{

	private String dialogId;
	
	public TextBubbleTerminatedEvent(String dialogId) {
		super("textBubbleTerminatedEvent");
		this.dialogId = dialogId;
	}
	
	public String getDialogId(){return dialogId;}

}
