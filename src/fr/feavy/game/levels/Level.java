package fr.feavy.game.levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.feavy.event.events.Event;
import fr.feavy.event.events.KeyPressedEvent;
import fr.feavy.event.events.KeyReleasedEvent;
import fr.feavy.event.events.ShowTextBubbleEvent;
import fr.feavy.event.listeners.EventListener;
import fr.feavy.image.GameImage;
import fr.feavy.rendering.Character;
import fr.feavy.rendering.Mario;
import fr.feavy.rendering.ScreenObject;
import fr.feavy.rendering.object.LevelObject;
import fr.feavy.sounds.GameSounds;
import fr.feavy.sounds.MusicRunnable;
import fr.feavy.utils.Game;
import fr.feavy.window.GameScreen;
import fr.feavy.window.Main;

public abstract class Level extends ScreenObject implements EventListener{

	private int id;
	private boolean done;
	
	private boolean showHitBoxes = false;
	
	private int backgroundId, foregroundId;
	private Image background, foreground;
	
	private List<Character> characters;
	private List<LevelObject> levelObjects;
	
	private Mario mario;
	
	private String musicName;
	
	protected Level(int id, int backgroundID){
		super();
		this.musicName = GameSounds.getLevelMusic(id);
		this.showHitBoxes = false;
		this.id = id;
		this.done = false;
		this.backgroundId = backgroundID;
		this.foregroundId = id;
	}

	public void start(){
		GameSounds.playMusic();
		if(Game.isFirstLevel){
			Game.isFirstLevel = false;
			List<String> colressDialog = new ArrayList<String>();
			colressDialog.add("Vous voilà entré dans un niveau.");
			colressDialog.add("Comme je vous l'ai dit, chaque niveau");
			colressDialog.add("renferme un parchemin unique : vous");
			colressDialog.add("pourrez l'obtenir après avoir");
			colressDialog.add("réaliser une tâche bien définie.");
			colressDialog.add(" ");
			colressDialog.add("Mémoriser bien les informations de");
			colressDialog.add("chaque parchemin car elles vous seront");
			colressDialog.add("utiles pour protéger "+Game.cityName+" plus tard.");
			colressDialog.add("Appuyez sur les flèches [gauche, haut,");
			colressDialog.add("droite] pour vous déplacer et ainsi pouvoir");
			colressDialog.add("récupérer le parchemin. Bonne chance !");
			callEvent(new ShowTextBubbleEvent("colressFirstDialog", colressDialog, true));
		}
	}
	
	@Override
	public void load(){
		super.load();
		Main.addEventListener(this);
		GameSounds.setMusic(musicName);
		this.levelObjects = new ArrayList<LevelObject>();
		this.characters = new LinkedList<Character>();
		
		LevelObject levelPlatform = new LevelObject(0,0,null) {
			
			@Override
			public void draw(Graphics2D g) {
				// TODO Auto-generated method stub
				
			}
		};
		levelPlatform.addPlatform(new Platform(0, false, 0, 0, 10, getScreen().getHeight()));
		levelPlatform.addPlatform(new Platform(1, false, getPX2(getScreen().getWidth()), 0, getPX2(getScreen().getWidth())-10, getScreen().getHeight()));
		addObject(levelPlatform);
		
		this.background = GameImage.getBackground(backgroundId);
		this.foreground = GameImage.getLevel(foregroundId);
		
		for(Character c : characters)
			c.load();
	}
	
	public void stopMusic(){
		GameSounds.stopMusic();
	}
	
	@Override
	public void unLoad() {
		super.unLoad();
		GameSounds.stopMusic();
		mario.unLoad();
		Main.removeEventListener(this);
		for(Character c : characters)
			c.unLoad();
		this.levelObjects = null;
		characters = null;
		mario = null;
		background = null;
		foreground = null;
	}
	
	public void addObject(LevelObject obj){levelObjects.add(obj);}
	
	public LevelObject[] getObjects(){ return levelObjects.toArray(new LevelObject[levelObjects.size()]); }
	
	public Mario getMario(){return mario;}
	
	public void setMario(Mario mario){
		characters.add(mario);
		mario.load();
		this.mario = mario;
	}
	
	public void setCharacter(Character character){
		character.load();
		characters.add(character);
	}
	
	public List<Character> getCharacters(){return characters;}
	
	public void done() {done = true;}
	
	public boolean isDone(){ return done; }
	
	public int getId(){ return id; }
	
	public void drawRect(Graphics2D g, int x1, int y1, int x2, int y2){
		g.drawRect(x1, y1, x2-x1, y2-y1);
		g.setColor(Color.GREEN);
		g.drawLine(x2, y2, x2, y2);
		g.setColor(Color.RED);
	}
	
	public void draw(Graphics2D g){
		g.drawImage(background, 0, 0, getScreen().getWidth(), getScreen().getHeight(), null);
		g.drawImage(foreground, 0, 0, getScreen().getWidth(), getScreen().getHeight(), null);
		
		
		/**DEBUG**/
		
		g.setColor(Color.RED);
		for(LevelObject o : getObjects()){
			o.draw(g);
			if(showHitBoxes){
				for(Platform p : o.getPlatforms()){
					drawRect(g, p.getX1(), p.getY1(), p.getX2(), p.getY2());
				}
			}
		}
		g.setColor(Color.BLACK);
		for(Character c : characters){
			c.draw(g);
			if(showHitBoxes){
				for(Platform p : c.getPlatforms()){
					drawRect(g, p.getX1(), p.getY1(), p.getX2(), p.getY2());
				}
			}
		}
		
	}

	@Override
	public void callEvent(Event e) {
		Main.callEvent(e);
	}

	@Override
	public void onEvent(Event e) {
		if(!loaded())
			return;
		if(e.getID().equals("keyPressedEvent")){
			KeyPressedEvent kpe = (KeyPressedEvent)e;
			int key = kpe.getKeyCode();
			if(Game.isArrow(key)){
				mario.startMoving(key);
			}else if(key == 72)
				showHitBoxes = !showHitBoxes;
		}else if(e.getID().equals("keyReleasedEvent")){
			KeyReleasedEvent kre = (KeyReleasedEvent)e;
			int key = kre.getKeyCode();
			mario.stopMoving(key);
		}
	}

	public void removeCharacter(Character character) {
		characters.remove(character);
	}

	public void unDone() {
		done = false;
	}

}
