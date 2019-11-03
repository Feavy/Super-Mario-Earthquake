package fr.feavy.game.levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import javax.management.timer.Timer;
import javax.sound.midi.SysexMessage;

import fr.feavy.event.events.Event;
import fr.feavy.event.events.ShowTextBubbleEvent;
import fr.feavy.image.GameImage;
import fr.feavy.image.Sprite;
import fr.feavy.rendering.Character;
import fr.feavy.rendering.Goumba;
import fr.feavy.rendering.Mario;
import fr.feavy.rendering.ScreenObject;
import fr.feavy.rendering.object.BlackRectangle;
import fr.feavy.rendering.object.Champignon;
import fr.feavy.rendering.object.LevelObject;
import fr.feavy.rendering.object.Parchment;
import fr.feavy.rendering.object.Spikes;
import fr.feavy.sounds.GameSounds;
import fr.feavy.utils.Game;
import fr.feavy.window.GameScreen;
import fr.feavy.window.Main;

public class Level3 extends Level{

	private Parchment parchment;
	private LevelObject levelPlatforms;
	private BufferedImage goumbaSprite;
	
	private BufferedImage blackRectangleImage, parchmentImage, spikesImage;
	
	private Goumba[] goumba = new Goumba[5];
	private BlackRectangle[] rectangles = new BlackRectangle[5];
	private int goumbaAmount = 5;
	private List<String> colressDialog;
	
	public Level3() {
		super(2, 1);
	}
	
	@Override
	public void start() {
		super.start();
		for(Goumba g : goumba)
			g.startMoving(39);
		callEvent(new ShowTextBubbleEvent("colress1Level3", colressDialog, true));
	}
	
	@Override
	public void load() {
		super.load();
		this.goumba = new Goumba[5];
		this.goumbaAmount = 5;
		this.colressDialog = new LinkedList<String>();
		colressDialog.add("Et bien, on dirait qu'il y a de la");
		colressDialog.add("population ici, il serait préférable de");
		colressDialog.add("faire un peu de ménage. Qu'en dites-vous ?");
		blackRectangleImage = GameImage.createImage(GameImage.BLACK_RECTANGLE);
		this.goumbaSprite = GameImage.createImage(GameImage.GOUMBA);
		this.parchmentImage = GameImage.createImage(GameImage.PARCHMENT_OBJECT);
		this.spikesImage = GameImage.createImage(GameImage.SPIKES);
		levelPlatforms = new LevelObject(0,0,null) {
			
			@Override
			public void draw(Graphics2D g) {
				// TODO Auto-generated method stub
				
			}
		};
		levelPlatforms.addPlatform(new Platform(0, false, 0, 544, 789, 544));
		levelPlatforms.addPlatform(new Platform(1, false, 50, 417, 145, 417));
		levelPlatforms.addPlatform(new Platform(2, false, 650, 88, 789, 88));
		
		this.parchment = new Parchment(this, 730, 50, parchmentImage);
		
		for(int i = 0; i < 5; i++){
			goumba[i] = new Goumba(goumbaSprite, 150+75*i, 450);
			goumba[i].setLevel(this);
			setCharacter(goumba[i]);
		}
		super.addObject(new Spikes(698, 497, spikesImage));
		super.addObject(parchment);
		super.addObject(levelPlatforms);
	}
	
	@Override
	public void removeCharacter(Character character) {
		super.removeCharacter(character);
		if(character instanceof Goumba){
			goumbaAmount--;
			if(goumbaAmount == 0){
				goumba = null;
				GameSounds.playSound(GameSounds.DISCOVERY);
				super.addObject(new BlackRectangle(187, 440, blackRectangleImage));
				for(int i = 0; i < 5; i++){
					rectangles[i] = new BlackRectangle(150+i*100, 340-i*55, blackRectangleImage);
					super.addObject(rectangles[i]);
				}
			}
		}
	}
	
	@Override
	public void unLoad() {
		super.unLoad();
		this.levelPlatforms = null;
	}
	
	@Override
	public void setMario(Mario mario) {
		mario.setPosition(50,200);
		mario.setLevel(this);
		super.setMario(mario);
	}
	
}
