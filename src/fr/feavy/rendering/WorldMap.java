package fr.feavy.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fr.feavy.event.events.Event;
import fr.feavy.event.events.KeyPressedEvent;
import fr.feavy.event.events.KeyReleasedEvent;
import fr.feavy.event.events.ReachLevelPortalEvent;
import fr.feavy.event.events.ShowTextBubbleEvent;
import fr.feavy.event.events.StartAnimationEvent;
import fr.feavy.event.events.StartLevelEvent;
import fr.feavy.event.events.TextBubbleTerminatedEvent;
import fr.feavy.event.listeners.EventListener;
import fr.feavy.image.GameImage;
import fr.feavy.rendering.animation.FadeScreen;
import fr.feavy.sounds.GameSounds;
import fr.feavy.utils.Game;
import fr.feavy.window.Main;

public class WorldMap extends ScreenObject implements EventListener {

	private MarioWorldMap marioWorldMap;

	private ScreenObject[] screenObjects = new ScreenObject[1];

	private Map<Point, Integer> levels;
	
	private List<Point> path = new ArrayList<Point>();
	
	private int currentArrowPressed = 0;
	
	private Image redPortal, bluePortal, blackPortal, tree;
	
	private boolean canStartLevel;
	
	private Point lastPosition;
	
	public WorldMap(Map<Point, Integer> levels) {
		super();
		Main.addEventListener(this);
		this.levels = levels;
		lastPosition = new Point(5,9);
	}

	@Override
	public void load() {
		super.load();
		this.path = new ArrayList<Point>();
		Point l1 = null;
		int lastLevelID = -1;
		for(Point location : levels.keySet()){
			if(l1 != null && lastLevelID >= 0){
				System.err.println("create");
				createPaths(l1.x, l1.y, location.x, location.y);
			}
			l1 = location;
			
			if(Game.isLevelDone(levels.get(location)))
				lastLevelID = levels.get(location);
			else
				lastLevelID = -1;
		}
		GameSounds.setMusic(GameSounds.WORLDMAP_MUSIC);
		this.canStartLevel = true;
		marioWorldMap = new MarioWorldMap(lastPosition);
		marioWorldMap.load();
		screenObjects[0] = marioWorldMap;
		this.redPortal = GameImage.createImage(GameImage.RED_PORTAL);
		this.bluePortal = GameImage.createImage(GameImage.BLUE_PORTAL);
		this.blackPortal = GameImage.createImage(GameImage.BLACK_PORTAL);
		this.tree = GameImage.createImage(GameImage.TREE);
		if(Game.isFirstWorldMap){
			new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
					GameSounds.playMusic();
					
				}
			}, 5000L);
			this.canStartLevel = false;
			List<String> msg = new LinkedList<String>();
			msg.add("Bien le bonjour, mon nom est");
			msg.add("Nikolaï, je suis un scientifique,");
			msg.add("et je vais vous expliquer les bases");//
			msg.add("de ce monde. Vous vous trouvez");
			msg.add("présentement sur la carte; c'est à");
			msg.add("partir d'ici que vous pourrez entrer");//
			msg.add("dans les niveaux.");
			msg.add("Pour se faire, déplacez-vous en");
			msg.add("appuyant sur les flèches du clavier");//
			msg.add("afin de sélectionner votre niveau");
			msg.add("et appuyer sur espace pour entrer");
			msg.add("dedans.");//
			msg.add("Dans chaque niveau se trouve un");
			msg.add("parchemin unique qu'il vous");
			msg.add("faudra récupérer.");//
			msg.add("Une fois un niveau terminé, le");
			msg.add("suivant est débloqué et ainsi de");
			msg.add("suite. C'est ainsi que vous pourrez");//
			msg.add("vous rendre jusqu'à "+Game.cityName+" pour le");
			msg.add("duel ultime contre Bowser.");
			msg.add(" ");//
			msg.add("A noter que chaque niveau peut être");
			msg.add("refait indéfiniment donc si vous");
			msg.add("avez des doutes, n'hésitez pas.");//
			msg.add("Une dernière petite chose : ");
			msg.add("vous disposez de 99 vies pour");
			msg.add("l'ensemble des niveaux (comme");
			msg.add("indiqué en haut à gauche), si");
			msg.add("vous perdez ces 99 vies, il vous");
			msg.add("faudra recommencer depuis le");
			msg.add("début. A tout de suite !");
			callEvent(new ShowTextBubbleEvent("firstWorldmapColressDialog", msg, true));
			Game.isFirstWorldMap = false;
		}else
			GameSounds.playMusic();
	}
	
	@Override
	public void unLoad() {
		super.unLoad();
		if(GameSounds.isMusic())
			GameSounds.stopMusic();
		if(marioWorldMap != null)
			marioWorldMap.unLoad();
		marioWorldMap = null;
		this.path = null;
		this.redPortal = null;
		this.blackPortal = null;
		this.blackPortal = null;
		this.tree = null;
	}
	
	@Override
	public void draw(Graphics2D g){
		
		if(!loaded())
			return;
		
		g.setColor(new Color(101, 230, 89));
		g.fillRect(0, 0, getScreen().getWidth(), getScreen().getHeight());
		
		Point l1 = null;
		
		RenderingHints drh = g.getRenderingHints();
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		for (Point location : levels.keySet()) {
			if(l1 != null){
				drawPath(l1.x, l1.y, location.x, location.y, g);
			}
			l1 = location;
		}

		for (Point location : levels.keySet())
			drawLevelDoor(location, levels.get(location), g);

		g.setRenderingHints(drh);
		
		for (ScreenObject so : screenObjects)
			so.draw(g);
		
		//g.drawImage(GameImage.TREE, 170, 330, 110/2, 134/2, null);
	
	}

	private void drawLevelDoor(Point location, Integer lvlID, Graphics g) {

		int x = location.x;
		int y = location.y;
		
		int oldX = x;
		int oldY = y;

		y = getIntAxeY(y);
		x = getIntAxeX(x, y);

		Image toDraw = (Game.isLevelDone(lvlID)) ? bluePortal
				: (Game.getCurrentLevel() == lvlID) ? redPortal : blackPortal;

		g.drawImage(toDraw, x, y, (int) (getScreen().getWidth() / 15d), (int) (getScreen().getHeight() / 18d), null);
		//g.drawString("(" + oldX + ";" + oldY + ")", x, y);
		g.setFont(new Font("sansSerif", Font.PLAIN, getPX(12)));
		g.drawString("Niveau : " +(lvlID+1), x, y);
	}

	private void createPaths(int x1, int y1, int x2, int y2){
		if(x1 == x2 || y1 == y2){
			
			if(x1 == x2){
				int min = Math.min(y1, y2);
				int max = Math.max(y1, y2);
				for(int i = min; i <= max; i++)
					path.add(new Point(x1, i));
			}else{
				int min = Math.min(x1, x2);
				int max = Math.max(x1, x2);
				for(int i = min; i <= max; i++)
					path.add(new Point(i, y1));
			}
			
		}
	}
	
	private void drawPath(int x1, int y1, int x2, int y2, Graphics2D g) {

		// Ideal : x1-x2 == 0 || y1-y2 == 0
		
		if(x1 == x2 || y1 == y2){
			
			y1 = getScreen().getIntAxeY(y1);
			x1 = getScreen().getIntAxeX(x1, y1);
	
			y2 = getScreen().getIntAxeY(y2);
			x2 = getScreen().getIntAxeX(x2, y2);
	
			g.setColor(new Color(233, 163, 93));
	
			int dX = getScreen().getPX(15);
			int dY = 0;
	
			if (y1 - y2 == 0) {
				dY = getScreen().getPX(10);
				for (int i = 0; i < getScreen().getPX(13); i++)
					g.drawLine(x1 + dX, y1 + dY + i, x2 + dX, y2 + dY + i);
			} else {
				dY = getScreen().getPX(20);
				for (int i = 0; i < dY; i++)
					g.drawLine(x1 + dX + i, y1 + dY, x2 + dX + i, y2 + dY);
			}
			g.setColor(Color.black);
		
		}

	}

	public void canStartLevel(boolean canStart){this.canStartLevel = canStart;}
	
	private boolean isPath(Point location){	return path.contains(location); }
	
	@Override
	public void callEvent(Event e) {
		System.out.println(e.getID());
		Main.callEvent(e);
	}

	@Override
	public void onEvent(Event e) {
		if(!loaded())
			return;
		if (e.getID().equals("keyPressedEvent")) {
			KeyPressedEvent kpe = (KeyPressedEvent) e;
			if (Game.isArrow(kpe.getKeyCode())) {
				
				currentArrowPressed = kpe.getKeyCode();
				
				Point p = marioWorldMap.getLocation();
				if(isPath(new Point(p.x+Game.getXOffset(currentArrowPressed), p.y+Game.getYOffset(currentArrowPressed)))){
					Integer lvlID = levels.get(new Point(p.x+Game.getXOffset(currentArrowPressed), p.y+Game.getYOffset(currentArrowPressed)));
					if(lvlID != null){
						if(Game.isLevelDone(lvlID) || Game.getCurrentLevel() == lvlID)
							marioWorldMap.move(currentArrowPressed);
					}else{
						marioWorldMap.move(currentArrowPressed);
					}
				}
				
			}else if(kpe.getKeyCode() == 32){
				if(!canStartLevel)return;
				if(!marioWorldMap.isMoving()){
					Point location = marioWorldMap.getLocation();
					for(Point l : levels.keySet()){
						if(l.x == location.x && l.y == location.y){
							canStartLevel = false;
							this.lastPosition = location;
							GameSounds.stopMusic();
							GameSounds.playSound(GameSounds.FADE);
							callEvent(new StartAnimationEvent(new FadeScreen("Niveau "+(levels.get(l)+1), 100, Color.black)));
							new Timer().schedule(new TimerTask() {
								
								@Override
								public void run() {
									callEvent(new StartLevelEvent(levels.get(l)));
								}
							}, 500);
							break;
						}
					}
				}
			}
		} else if (e.getID().equals("keyReleasedEvent")) {
			if (((KeyReleasedEvent) e).getKeyCode() == currentArrowPressed) {
				marioWorldMap.stopMoving();
				currentArrowPressed = 0;
			}
		}else if(e.getID().equals("reachLevelPortalEvent")){
			ReachLevelPortalEvent rlpe = (ReachLevelPortalEvent)e;
			int direction = rlpe.getMovingDirection();
			
			Point p = rlpe.getLocation();
			if(!isPath(new Point(p.x+Game.getXOffset(direction), p.y+Game.getYOffset(direction)))){
				GameSounds.playSound(GameSounds.WORLDMAP_TIC);
				marioWorldMap.stopMoving();
			}
			
		}else if(e.getID().equals("textBubbleTerminatedEvent")){
			if(((TextBubbleTerminatedEvent)e).getDialogId().equals("firstWorldmapColressDialog")){
				this.canStartLevel = true;
			}
		}
		
	}

	public void setPlayerPosition(Point point) {
		marioWorldMap.setPosition(point);
	}
	
}
