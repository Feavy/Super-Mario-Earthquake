package fr.feavy.game.levels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import fr.feavy.event.events.EarthquakeAnimationTerminated;
import fr.feavy.event.events.EndLevelEvent;
import fr.feavy.event.events.Event;
import fr.feavy.event.events.QuizzQuestionAnsweredEvent;
import fr.feavy.event.events.ShowTextBubbleEvent;
import fr.feavy.event.events.StartAnimationEvent;
import fr.feavy.event.events.StartQuizzEvent;
import fr.feavy.event.events.TextBubbleTerminatedEvent;
import fr.feavy.image.GameImage;
import fr.feavy.image.Sprite;
import fr.feavy.rendering.Character;
import fr.feavy.rendering.HUD;
import fr.feavy.rendering.Mario;
import fr.feavy.rendering.animation.FadeScreen;
import fr.feavy.rendering.object.Laser;
import fr.feavy.rendering.object.LevelObject;
import fr.feavy.sounds.GameSounds;
import fr.feavy.sounds.MusicRunnable;
import fr.feavy.utils.Game;
import fr.feavy.utils.Question;
import fr.feavy.window.GameScreen;

public class Level4 extends Level {

	private BufferedImage bowserSprite;
	private Character bowser;
	private LevelObject levelPlatforms;
	private Laser laser;
	private List<Question> quizz = new LinkedList<Question>();

	private int yOffset = -1;
	private int yDecalage = 0;

	private boolean animation = false;
	private boolean falling = false;
	private boolean endAnimation = false;
	
	private int textDecalage = 0;

	private int death, magni;
	
	private boolean quizzTerminated;
	
	private Random r = new Random();
	
	private int lastTranslation = 0;
	
	private Level instance;
	
	public Level4() {
		super(3, 2);
	}

	@Override
	public void load() {
		super.load();
		this.instance = this;
		quizzTerminated = false;
		falling = false;
		levelPlatforms = new LevelObject(0, 0, null) {

			@Override
			public void draw(Graphics2D g) {
				// TODO Auto-generated method stub

			}
		};
		levelPlatforms.addPlatform(new Platform(0, false, 0, 544, 789, 544));
		levelPlatforms.addPlatform(new Platform(0, false, 498, 500, 531, 500));
		this.bowserSprite = GameImage.createImage(GameImage.BOWSER_SPRITE);
		bowser = new Character(new Sprite(bowserSprite, true, 110, 83, 0, 0), Character.ALWAYS, 1, 520, 250, 0, 0, 5) {

			@Override
			public void draw(Graphics2D g) {
				if (yDecalage % 2 == 0)
					setDecalage(-yDecalage);
				if (!animation) {
					yDecalage += yOffset;
					if (yDecalage >= 60) {
						yOffset = -1;
					} else if (yDecalage <= 0) {
						yOffset = 1;
					}
				} else {
					if(yDecalage < 500 && !falling)
						yDecalage++;
					else
						falling = true;
					if(falling && yDecalage > -50)
						yDecalage -=20;
					else if(yDecalage <= -50){
						getMario().jump(false);
						if(Game.inhabitantAmount - death <= 0){
							Game.inhabitantAmount = 0;
							callEvent(new EndLevelEvent(instance, false));
						}else
							Game.inhabitantAmount-=death;
						falling = false;
						animation = false;
					}
				}
				super.draw(g);
			}

			@Override
			public void onHit() {
				// TODO Auto-generated method stub

			}
		};
		this.laser = new Laser(-110,360,GameImage.createImage(GameImage.LASER));
		laser.load();
		createQuizz();
		bowser.setLevel(this);
		bowser.setAnimationSpeedTime(80);
		super.setCharacter(bowser);
		super.addObject(levelPlatforms);
	}

	@Override
	public void start() {
		super.start();
		List<String> bowserDialog = new LinkedList<String>();
		bowserDialog.add("Mouahahaha ! Je ne vais faire qu'une");
		bowserDialog.add("bouchée de toi... et de toute la ville !!!");
		callEvent(new ShowTextBubbleEvent("bowserLvl4", bowserDialog, GameImage.createImage(GameImage.BOWSER), new Point(602, 253)));
	}
	
	@Override
	public void onEvent(Event e) {
		super.onEvent(e);
		if(e.getID().equals("textBubbleTerminatedEvent")){
			if(((TextBubbleTerminatedEvent)e).getDialogId().equals("bowserLvl4")){
				List<String> colressDialog = new LinkedList<String>();
				colressDialog.add("Oh non ! Bowser se prépare à attaquer");
				colressDialog.add("la ville, je me dépêche de finir");
				colressDialog.add("mon invention.");
				colressDialog.add("Au cas où il attaquerait avant, je vous");
				colressDialog.add("poserais une question pour trouver");
				colressDialog.add("un moyen de protéger la ville.");
				colressDialog.add("Sélectionnez la bonne réponse avec");
				colressDialog.add("les flèche 'haut' et 'bas' et");
				colressDialog.add("appuyez sur A pour valider votre");
				colressDialog.add("réponse.");
				callEvent(new ShowTextBubbleEvent("colressDialogLvl4", colressDialog, true));
			}
		}
		if (e.getID().equals("quizzQuestionAnsweredEvent")) {
			QuizzQuestionAnsweredEvent qqae = (QuizzQuestionAnsweredEvent) e;
			if (!qqae.isGoodAnswer()) {
				animation = true;
				this.death = qqae.getDeathAmount();
				this.magni = qqae.getMagnitude();
			}
		}else if(e.getID().equals("textBubbleTerminatedEvent")){
			if(((TextBubbleTerminatedEvent)e).getDialogId().equals("colressDialogLvl4")){
				callEvent(new StartQuizzEvent(quizz));
			}else if(((TextBubbleTerminatedEvent)e).getDialogId().equals("quizzTerminatedDialog")){
				GameSounds.playSound(GameSounds.LASER);
				laser.fire();
			}
		}else if(e.getID().equals("laserCourseTerminatedEvent")){
			callEvent(new StartAnimationEvent(new FadeScreen(null, 0, Color.WHITE)));
			removeCharacter(bowser);
			laser.unLoad();
			quizzTerminated = true;
			stopMusic();
		}else if(e.getID().equals("animationTerminatedEvent")){
			if(quizzTerminated){
				GameSounds.setMusic(GameSounds.VICTORY);
				GameSounds.playMusic();
				new Timer().schedule(new TimerTask() {
					
					@Override
					public void run() {
						callEvent(new Event("gameTerminatedEvent"));
					}
				}, 8000l);
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(endAnimation){
			lastTranslation = r.nextInt(75)-35;
			g.translate(0, lastTranslation);
		}else if(lastTranslation != 0){
			lastTranslation += -lastTranslation/lastTranslation;
		}
		super.draw(g);
		if(laser.loaded())
			laser.draw(g);
		if(animation){
			endAnimation = false;
			g.setFont(new Font("Impact", Font.BOLD, getPX(45)));
			g.setColor(Color.black);
			g.drawString("Séisme ! Magnitude "+magni+" !", getPX(150), getPY(300-textDecalage*2));
			textDecalage++;
		}else if(textDecalage >= 200 || endAnimation){
			if(!endAnimation){
				textDecalage = 0;
				GameSounds.playSound(GameSounds.EARTHQUAKE);
			}
			endAnimation = true;
			g.setFont(new Font("Impact", Font.BOLD, getPX(45)));
			g.setColor(Color.black);
			g.drawString(death+" morts !", getPX(240), getPY(300-textDecalage));
			textDecalage++;
			if(textDecalage > 450){
				endAnimation = false;
				textDecalage = 0;
				callEvent(new EarthquakeAnimationTerminated());
			}
		}
	}

	private void createQuizz() {
		// 1
		quizz.add(new Question("L'intensité d'un séisme mesure...", 1,
				new String[] { "l'énergie libérée par le séisme.",
						"l'intensité des dégâts causés par le séisme à la surface.",
						"l'intensité des secousses ressenties." }));
		// 2
		quizz.add(new Question("L'onde P est...", 0,
				new String[] { "la première onde parvenant à la surface lors d'un séisme.",
						"responsable de beaucoup de dégâts à la surface.", "une onde transversale." }));
		// 3
		quizz.add(new Question("L'onde de Love est...", 0,
				new String[] { "une onde de surface causant la majorité des dégâts.",
						"une onde de volume causant la majorité des dégâts.",
						"une onde de surface ne causant pas de dégâts particuliers." }));
		// 4
		quizz.add(new Question("L'épicentre d'un séisme est...", 2,
				new String[] { "le point de départ de ses ondes sismiques.",
						"le milieu de la faille qui a créé le séisme.",
						"le point à la verticale du foyer, situé à la surface." }));
		// 5
		quizz.add(new Question("Pour prévoir un séisme, deux méthodes*existent :", 0,
				new String[] { "les probabilités et la recherche de signes annonciateurs.", "la chance et l'intuition.",
						"l'étude des nuages et de la vitesse de pousse des arbres." }));
		// 6
		quizz.add(new Question("Les signes annonciateurs des séismes...", 1,
				new String[] { "se produisent systématiquement.",
						"sont très difficiles à analyser et ne se manifestent pas toujours.",
						"font l'unanimité au sein de la communauté scientifique." }));
		// 7
		quizz.add(new Question("Avant un séisme, du radon peut...", 2, new String[] { "être absorbé par le sol.",
				"perturber la circulation des eaux souterraines.", "être émis des microfailles." }));
		// 8
		quizz.add(new Question("Une habitation pouvant résister aux séismes*est...", 0, new String[] {
				"scindée en plusieurs parties.", "en un seul bloc.", "constituée de grandes ouvertures." }));
		// 9
		quizz.add(new Question("Il est préférable de tapisser les murs d'une*habitation de...", 2,
				new String[] { "composite.", "plexiglas.", "géotextile." }));
		// 10
		quizz.add(new Question("Il vaut mieux construire une structure rigide*sur un sol...", 1,
				new String[] { "rigide.", "mou.", "fertile." }));
	}

	@Override
	public void setMario(Mario mario) {
		mario.setPosition(50, 200);
		mario.setLevel(this);
		super.setMario(mario);
	}

}
