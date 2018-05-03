package fr.feavy.event.events;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

public class ShowTextBubbleEvent extends Event{

	private List<String> message;
	private Point startPoint;
	private String dialogId;
	private Image picture;
	private boolean isHelpMessage;
	private boolean popup;
	
	public ShowTextBubbleEvent(String dialogId, List<String> message, Image picture, Point startPoint) {
		super("showTextBubbleEvent");
		this.message = message;
		this.startPoint = startPoint;
		this.dialogId = dialogId;
		this.picture = picture;
		this.isHelpMessage = false;
	}
	
	public ShowTextBubbleEvent(String dialogId, List<String> message, boolean popup) {
		super("showTextBubbleEvent");
		this.message = message;
		this.dialogId = dialogId;
		this.isHelpMessage = true;
		this.popup = popup;
	}

	public boolean isPopup(){return popup;}
	public boolean isHelpMessage(){return isHelpMessage;}
	public Image getPicture(){return picture;}
	public String getDialogId(){return dialogId;}
	public Point getStartPoint(){return startPoint;}
	public List<String> getMessage(){return message;}
	
}
