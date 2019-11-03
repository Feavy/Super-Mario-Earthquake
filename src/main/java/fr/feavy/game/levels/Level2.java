package fr.feavy.game.levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.management.timer.Timer;

import fr.feavy.event.events.EndLevelEvent;
import fr.feavy.event.events.Event;
import fr.feavy.event.events.ShowTextBubbleEvent;
import fr.feavy.event.events.TextBubbleTerminatedEvent;
import fr.feavy.image.GameImage;
import fr.feavy.image.Sprite;
import fr.feavy.rendering.Character;
import fr.feavy.rendering.Mario;
import fr.feavy.rendering.ScreenObject;
import fr.feavy.rendering.object.Champignon;
import fr.feavy.rendering.object.LevelObject;
import fr.feavy.rendering.object.Spikes;
import fr.feavy.sounds.GameSounds;
import fr.feavy.window.GameScreen;
import fr.feavy.window.Main;

public class Level2 extends Level{

	private LevelObject levelPlatforms;
	private Champignon champignon;
	
	private BufferedImage champignonImage, toadImage, toadSprite;
	
	private List<String> toadDialog1;
	private List<String> toadDialog2;
	private List<String> toadDialog3;
	
	private BufferedImage spikesImage;
	
	private boolean finished = false;
	
	private Character toad;
	
	public Level2() {
		super(1, 0);
	}
	
	@Override
	public void load() {
		super.load();
		this.spikesImage = GameImage.createImage(GameImage.SPIKES);
		this.toadImage = GameImage.createImage(GameImage.TOAD);
		this.toadSprite = GameImage.createImage(GameImage.TOAD_SPRITE);
		this.toadDialog1 = new ArrayList<String>();
		this.toadDialog2 = new ArrayList<String>();
		this.toadDialog3 = new ArrayList<String>();
		toadDialog1.add("Aïe Aïe Aïe, J'ai perdu mon");
		toadDialog1.add("champignon sur le nuage là bas.");
		toadDialog1.add("Si princesse Peach voyait ça...");
		toadDialog1.add("Pourrais-tu me le ramener ?");
		toadDialog2.add("Oui, voilà c'est ce champignon là.");
		toadDialog2.add("Viens me le rapporter s'il-te-plaît.");
		toadDialog3.add("Ah merci, tu me sauves la vie.");
		toadDialog3.add("Tiens, pour te remercier je te donne");
		toadDialog3.add("ce parchemin.");
		this.champignonImage = GameImage.createImage(GameImage.CHAMPIGNON);
		this.champignon = new Champignon(199, 100, champignonImage);
		toad = new Character(new Sprite(toadSprite, true, 29, 19, 0, 0), Character.ALWAYS, 1, 734,480, 0, 0, 5) {
			
			@Override
			public void onHit() {
				// TODO Auto-generated method stub
				
			}
		};
		levelPlatforms = new LevelObject(0,0,null) {
			
			@Override
			public void platformContact(Character actor, int id) {
				super.platformContact(actor, id);
				if(id == 7)
					toadJoined();
			}
			
			@Override
			public void platformPressed(Character actor, int id) {
				super.platformPressed(actor, id);
				if(id == 7)
					toadJoined();
			}
			
			@Override
			public void draw(Graphics2D g) {
				// TODO Auto-generated method stub
				
			}
		};
		levelPlatforms.addPlatform(new Platform(0, false, 0, 544, 789, 544));
		levelPlatforms.addPlatform(new Platform(1, false, 605, 458, 677, 458));
		levelPlatforms.addPlatform(new Platform(2, false, 365, 416, 450, 416));
		levelPlatforms.addPlatform(new Platform(3, false, 130, 373, 209, 373));
		levelPlatforms.addPlatform(new Platform(4, false, 260, 277, 332, 277));
		levelPlatforms.addPlatform(new Platform(5, false, 405, 212, 487, 212));
		levelPlatforms.addPlatform(new Platform(6, false, 174, 134, 329, 134));
		levelPlatforms.addPlatform(new Platform(7, false, true, 600, 480, 734, 550));
		toad.setLevel(this);
		super.addObject(new Spikes(227, 497, spikesImage));
		super.addObject(new Spikes(474, 497, spikesImage));
		super.setCharacter(toad);
		super.addObject(levelPlatforms);
		super.addObject(champignon);
	}
	
	@Override
	public void start() {
		super.start();
		callEvent(new ShowTextBubbleEvent("toadDialog1Level2", toadDialog1, toadImage, new Point(getPX2(toad.getMiddleX()),getPY2(toad.getStartY()))));
	}
	
	@Override
	public void onEvent(Event e) {
		super.onEvent(e);
		if(e.getID().equals("champignonCollectedEvent")){
			callEvent(new ShowTextBubbleEvent("toadDialog2Level2", toadDialog2, toadImage, new Point(getPX2(toad.getMiddleX()),getPY2(toad.getStartY()))));
		}else if(e.getID().equals("textBubbleTerminatedEvent")){
			if(((TextBubbleTerminatedEvent)e).getDialogId().equals("toadDialog3Level2"))
				callEvent(new EndLevelEvent(this, true));
		}
	}
	
	public void toadJoined(){
		if(champignon.hasBeenCollected() && !finished){
			finished = true;
			callEvent(new ShowTextBubbleEvent("toadDialog3Level2", toadDialog3, toadImage, new Point(getPX2(toad.getMiddleX()),getPY2(toad.getStartY()))));
		}
	}
	
	@Override
	public void callEvent(Event e) {
		super.callEvent(e);
		Main.callEvent(e);
	}
	
	@Override
	public void unLoad() {
		super.unLoad();
		this.spikesImage = null;
		this.finished = false;
		this.champignon = null;
		this.champignonImage = null;
		this.levelPlatforms = null;
		this.toad = null;
		this.toadSprite = null;
		this.toadImage = null;
		this.toadDialog1 = null;
		this.toadDialog2 = null;
		this.toadDialog3 = null;
	}
	
	@Override
	public void setMario(Mario mario) {
		mario.setPosition(50,200);
		mario.setLevel(this);
		super.setMario(mario);
	}
	
}
