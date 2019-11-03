package fr.feavy.event.events;

import fr.feavy.rendering.animation.Animation;

public class StartAnimationEvent extends Event{

	private Animation anim;
	
	public StartAnimationEvent(Animation a) {
		super("startAnimationEvent");
		anim = a;
	}
	
	public Animation getAnimation(){ return anim; }

}
