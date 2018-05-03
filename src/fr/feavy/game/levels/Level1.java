package fr.feavy.game.levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.feavy.event.events.EndLevelEvent;
import fr.feavy.event.events.Event;
import fr.feavy.event.events.ShowTextBubbleEvent;
import fr.feavy.event.events.TextBubbleTerminatedEvent;
import fr.feavy.image.GameImage;
import fr.feavy.image.Sprite;
import fr.feavy.rendering.Character;
import fr.feavy.rendering.Mario;
import fr.feavy.rendering.ScreenObject;
import fr.feavy.rendering.object.LevelObject;
import fr.feavy.rendering.object.RedButton;
import fr.feavy.rendering.object.Spikes;
import fr.feavy.rendering.object.WoodPlatform;
import fr.feavy.sounds.GameSounds;
import fr.feavy.window.GameScreen;
import fr.feavy.window.Main;

public class Level1 extends Level{

	private RedButton rb;
	private Spikes spikes;
	private WoodPlatform woodenPlatform;
	private LevelObject levelPlaforms;
	
	private BufferedImage spikesImage, woodenPlatformImage, redButtonImage;
	
	private List<String> mortonDialog;
	
	public Level1() {
		super(0, 0);
	}
	
	@Override
	public void unLoad() {
		this.rb = null;
		this.spikes = null;
		this.woodenPlatform = null;
		this.woodenPlatformImage = null;
		this.spikesImage = null;
		this.redButtonImage = null;
		this.mortonDialog = null;
		super.unLoad();
	}
	
	@Override
	public void load() {
		super.load();
		this.mortonDialog = new LinkedList<String>();
		this.spikesImage = GameImage.createImage(GameImage.SPIKES);
		this.woodenPlatformImage = GameImage.createImage(GameImage.WOODEN_PLATFORM);
		this.redButtonImage = GameImage.createImage(GameImage.RED_BUTTON);
		
		spikes = new Spikes(350, 500, spikesImage);
		woodenPlatform = new WoodPlatform(278, 0, woodenPlatformImage);
		rb = new RedButton(woodenPlatform, 650, 450, redButtonImage);
		levelPlaforms = new LevelObject(0,0,null) {
			
			@Override
			public void draw(Graphics2D g) {
				// TODO Auto-generated method stub
				
			}
		};
		levelPlaforms.addPlatform(new Platform(0, false, 0, 544, /*789*/900  , 544));
		addObject(levelPlaforms);
		addObject(woodenPlatform);
		addObject(spikes);
		addObject(rb);
		
		mortonDialog.add("Gwarh ! Qu'est-ce qui te prend de");
		mortonDialog.add("faire ça ? C'est le parchemin que tu");
		mortonDialog.add("veux ? T'aurais pu demander gentillement");
		mortonDialog.add("c'est pas la peine d'être violent comme");
		mortonDialog.add("ça. Tiens prends-le et fiche le camp !");
		
		Character morton = new Character(new Sprite(GameImage.createImage(GameImage.MORTON_SPRITE), true, 50, 44, 0, 0), Character.ALWAYS, 1, 353,-100, 0, 0, 5){
			
			@Override
			public void onHit() {
				getMario().freeze();
				jump();
				startMoving(39);
				new Timer().schedule(new TimerTask() {
					
					@Override
					public void run() {
						stopMoving(39);
						callEvent(new ShowTextBubbleEvent("mortonDialogLevel1", mortonDialog, GameImage.createImage(GameImage.MORTON), new Point(getPX2(getMiddleX())+20,getPY2(getStartY())+50)));
						
					}
				}, 300l);
			}
		};
		morton.setLevel(this);
		setCharacter(morton);
	}
	
	@Override
	public void onEvent(Event e) {
		super.onEvent(e);
		if(e.getID().equals("textBubbleTerminatedEvent")){
			if(((TextBubbleTerminatedEvent)e).getDialogId().equals("mortonDialogLevel1")){
				callEvent(new EndLevelEvent(this, true));
			}
		}
	}
	
	public void setMario(Mario mario){
		mario.setPosition(50,200);
		mario.setLevel(this);
		System.out.println("mario set");
		super.setMario(mario);
	}
	
}
