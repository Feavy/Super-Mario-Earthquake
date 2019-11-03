package fr.feavy.window;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.xml.validation.SchemaFactoryConfigurationError;

import fr.feavy.event.events.EndLevelEvent;
import fr.feavy.event.events.Event;
import fr.feavy.event.events.KeyPressedEvent;
import fr.feavy.event.events.QuizzQuestionAnsweredEvent;
import fr.feavy.event.events.ShowTextBubbleEvent;
import fr.feavy.event.events.StartAnimationEvent;
import fr.feavy.event.events.StartLevelEvent;
import fr.feavy.event.events.StartQuizzEvent;
import fr.feavy.event.events.TextBubbleTerminatedEvent;
import fr.feavy.event.listeners.EventListener;
import fr.feavy.event.listeners.KeyListener;
import fr.feavy.game.levels.Level;
import fr.feavy.game.levels.Levels;
import fr.feavy.image.GameImage;
import fr.feavy.rendering.HUD;
import fr.feavy.rendering.Mario;
import fr.feavy.rendering.WorldMap;
import fr.feavy.sounds.GameSounds;
import fr.feavy.sounds.MusicRunnable;
import fr.feavy.utils.Game;
import fr.feavy.utils.Question;
import javafx.embed.swing.JFXPanel;

public class GameScreen extends JFXPanel implements EventListener {

	private int startAbsX = 34;
	private int startAbsY = 45;

	private int defaultWidth = 789;
	private int defaultHeight = 590;

	private Runnable refreshingRunnable;

	private boolean animationRunning = false;

	private WorldMap worldMap;
	private Mario mario;

	private Point bubbleLocation = null;
	private List<String> message = null;
	private Image picture = null;
	private int pictureOffset = 0;

	private int currentLine = 1;
	private int currentLetter = 1;

	private boolean isHelpMessage;
	private boolean pressA = false;
	private String aText = "";

	private String currentDialogId = "";

	private int a = 0;

	public static GameScreen instance;

	private boolean levelLoading;

	private boolean isCredits = false;
	private boolean isIntroduction = true;
	private boolean isQuizz = false;

	private int cursorPosition = 0;
	private int questionIndex = 0;
	private List<Question> quizz;
	private List<String> quizzDialog = new LinkedList<String>();
	private List<String> currentScrollingText = new LinkedList<String>();
	private double scrollingTextOffset = 0;
	private int scrollingTextOffsetWait = 0;

	private BufferedImage parchmentPicture = null;
	private boolean firstParchment = true;
	
	private boolean isNextPopup = false;
	
	private HUD hud;
	
	private Random r;

	private List<int[]> circles = new LinkedList<int[]>();
	private List<Color> circlesColor = new LinkedList<Color>();
	
	private Image endBackground, thxForPlaying;
	
	public GameScreen(Map<Point, Integer> levels) {

		createIntroduction();

		setRequestFocusEnabled(true);
		
		this.r = new Random();
		this.isCredits = false;
		this.hud = new HUD();
		
		quizzDialog.add("Bowser prépare une attaque ! Vite,");
		quizzDialog.add("");
		instance = this;
		this.isQuizz = false;
		this.firstParchment = true;
		this.isIntroduction = true;
		this.levelLoading = false;
		this.bubbleLocation = null;
		this.message = null;

		setBounds(0, 0, defaultWidth, defaultHeight);
		setBackground(Color.WHITE);

		Main.addEventListener(this);

		animationRunning = false;

		refreshingRunnable = new RefreshingRunnable();
		new Thread(refreshingRunnable).start();
		this.worldMap = new WorldMap(levels);
		this.mario = new Mario(0, 0);

		addKeyListener(new KeyListener());
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("(" + e.getX() + ";" + e.getY() + ")");

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		if (isIntroduction) {
			writeIntroduction(g2d);
			return;
		}else if(isCredits){
			writeCredits(g2d);
			return;
		}

		if (parchmentPicture != null) {
			drawParchment(g2d);
		} else if (Game.currentLevel != null) {
			Game.currentLevel.draw(g2d);
		} else {
			worldMap.draw(g2d);
		}
		hud.draw(g2d);
		if (message != null) {
			drawMessage(g2d);
		}
		if (isQuizz) {
			drawQuizzAnswers(g2d);
		}

	}

	private void drawQuizzAnswers(Graphics2D g2d) {
		if (message == null) {
			quizzDialog.remove(1);
			quizzDialog.add(quizz.get(questionIndex).getQuestion());// 9 || 5
			callEvent(new ShowTextBubbleEvent("quizz", quizzDialog, isNextPopup));
		}
		g2d.setFont(new Font("sansSerif", Font.PLAIN, getPX(11)));
		if (pictureOffset >= 140) {
			for (int i = 0; i < 3; i++) {
				g2d.setColor(Color.WHITE);
				g2d.fillRect(getPX(85), getPY(217 + 85 * i), getPX(350), getPY(50));
				g2d.setColor(Color.BLACK);
				g2d.drawRect(getPX(85), getPY(217 + 85 * i), getPX(350), getPY(50));
				g2d.drawString((i + 1) + ") " + quizz.get(questionIndex).getAnswers()[i], getPX(90),
						getPY(247 + 85 * i));
			}
			Polygon p = new Polygon(new int[]{getPX(65), getPX(80), getPX(65)}, new int[]{getPY(234+85*cursorPosition), getPY(244+85*cursorPosition), getPY(254+85*cursorPosition)}, 3);
			g2d.setColor(Color.WHITE);
			g2d.fillPolygon(p);
			g2d.setColor(Color.BLACK);
			g2d.drawPolygon(p);
		}
	}

	private void drawParchment(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.drawImage(parchmentPicture, getPX(60), 0, getWidth() - getPX(120), getHeight(), null);
		if (message == null) {
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("sansSerif", Font.PLAIN, getPX(15)));
			g2d.drawString("Appuyez sur A pour terminer", getPX(300), getHeight() - getPY(30));
			pressA = true;
			aText = "parchmentTerminer";
		}
	}

	private void drawMessage(Graphics2D g2d) {
		if (isHelpMessage) {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.drawImage(picture, getPX(630), getPY(460 - pictureOffset), getPX(634 / 4), getPY(1539 / 4), null);
			if (pictureOffset < 140) {
				pictureOffset += 3;
				return;
			}
		}
		Polygon p = new Polygon(new int[] { getPX(600), getPX(661), getPX(bubbleLocation.x) },
				new int[] { getPY(160), getPY(150), getPY(bubbleLocation.y) }, 3);
		// TODO à optimiser
		g2d.setColor(Color.black);
		g2d.drawPolygon(p);
		g2d.drawOval(60, 0, getWidth() - 120, getPY(200));
		g2d.setColor(Color.WHITE);
		g2d.fillOval(60 + 1, 1, getWidth() - 121, getPY(199));
		g2d.fillPolygon(p);
		g2d.setColor(Color.black);
		if (!isHelpMessage) {
			g2d.drawImage(picture, getPX(114), getPY(7), getPX(75), getPY(75), null);
			g2d.drawRect(getPX(114), getPY(7), getPX(75), getPY(75));
		}
		if (pressA) {
			if (a <= 30) {
				g2d.setFont(new Font("SansSerif", Font.PLAIN, getPX(10)));
				g2d.drawString("Appuyez sur A pour " + aText, getPX(330-aText.length()), getPY(183));
			} else if (a >= 60) {
				a = 0;
			}
			a++;
		}
		g2d.setFont(new Font("SansSerif", Font.ITALIC, getPX(25)));
		int line;
		for (line = 0; line < currentLine; line++) {
			if (line + 1 == currentLine)
				g2d.drawString(getLetters(message.get(line), currentLetter), getPX(210), getPY(50 + 40 * line));
			else
				g2d.drawString(getLetters(message.get(line), message.get(line).length()), getPX(210),
						getPY(50 + 40 * line));
		}
		if (currentLetter == message.get(line - 1).length() && currentLine < message.size() && currentLine < 3) {
			currentLetter = 1;
			currentLine++;
			if(currentLetter%2 == 0)
				GameSounds.playSound(GameSounds.TEXT);
		} else if (currentLetter < message.get(line - 1).length()) {
			currentLetter++;
			if(currentLetter%2 == 0)
				GameSounds.playSound(GameSounds.TEXT);
		}
		if (currentLine >= 3 && message.size() - 3 > 0 && currentLetter >= message.get(line - 1).length()) {
			pressA = true;
			aText = "continuer";
		} else {
			pressA = false;
		}
		if (currentLine == message.size() && currentLetter >= message.get(line - 1).length()) {
			pressA = true;
			aText = "terminer";
		}
		if(isQuizz)
			aText = "valider votre réponse";
	}

	public void addSpaces(int count){
		for(int i = 0; i < count; i++)
			currentScrollingText.add("");	
	}
	
	private void createIntroduction() {
		GameSounds.setMusic(GameSounds.INTRODUCTION);
		scrollingTextOffset = 0;
		scrollingTextOffsetWait = 0;
		currentScrollingText.add("    Message des créateurs : Bienvenue dans notre jeu vidéo,");
		currentScrollingText.add("                          n'oubliez pas d'activer le son");
		currentScrollingText.add("  Note : le jeu fonctionne mieux dans la résolution par défaut.");
		addSpaces(15);
		currentScrollingText.add("C'est terrible ! Bowser vient de mettre au point une machine");
		currentScrollingText.add("capable  de créer  des  séismes  effroyables et il s'apprête à");
		currentScrollingText.add("l'expérimenter  sur  notre pauvre ville.  Heureusement, notre");
		currentScrollingText.add("scientifique  local   Nikolaï  travaille  déjà   sur  l'affaire  mais");
		currentScrollingText.add("nous  avons   besoin  d'une  personne  compétente,  comme");
		currentScrollingText.add("vous, pour protéger la population pendant qu'il travaille.");
		currentScrollingText.add("Mais  ne  vous  inquiétez  pas,  un  ancien  sage  du  nom de");
		currentScrollingText.add("Wilfred avait déjà prévu un désastre de ce genre il");
		currentScrollingText.add("y  a  fort  longtemps,  il  a  donc  réparti   trois   parchemins");
		currentScrollingText.add("autour  de  la  ville  afin  la  protéger  de  tout  problème  de");
		currentScrollingText.add("séisme. Recoltez  ces parchemins car  ils  vous  donneront");
		currentScrollingText.add("les clés pour protéger au mieux les habitants.");
		currentScrollingText.add("");
		currentScrollingText.add("Mais faites vite, Bowser est déjà en route...");
		currentScrollingText.add("");
		currentScrollingText.add("La France vous regarde !");
	}

	private void createCredits(){
		scrollingTextOffset = 0;
		scrollingTextOffsetWait = 0;
		this.endBackground = GameImage.getBackground(0);
		this.thxForPlaying = GameImage.createImage(GameImage.THANKS);
		currentScrollingText = new LinkedList<String>();
		currentScrollingText.add("Et c'est ainsi que les plans de Bowser partirent en fumée.");
		currentScrollingText.add("Les quelques dommages causés à la ville furent réparés.");
		currentScrollingText.add("Et Mario, après cette petite escale, reparti en direction");
		currentScrollingText.add("du royaume Champignon...");
		currentScrollingText.add("");
		currentScrollingText.add("Cette histoire apprit beaucoup à Nikolaï et Mario :");
		currentScrollingText.add("ils comprirent qu'il existe beaucoup de moyens");
		currentScrollingText.add("susceptibles de prot�ger les populations contre les");
		currentScrollingText.add("séismes comme, la cr�ation de bâtiments étudiés pour");
		currentScrollingText.add("y résister, qui utiliseraient notamment des amortisseurs");
		currentScrollingText.add("à frottement, et dont les murs seraient tapissés de");
		currentScrollingText.add("géotextile... Parallèlement, nous pouvons imaginer la");
		currentScrollingText.add("création d'un \"super-détecteur\" qui rassemblerait un");
		currentScrollingText.add("détecteur pour chaque signe pr�curseur des séismes et qui");
		currentScrollingText.add("seraient placés à des endroits stratégiques, là où les");
		currentScrollingText.add("études de probabilités auraient détecté les plus gros risques.");
		currentScrollingText.add("On pense notament aux dosimètres à radon ou bien à des");
		currentScrollingText.add("échelles dans les nappes phréatiques. Chaque détection");
		currentScrollingText.add("pourrait alors être référencée dans une base de données afin");
		currentScrollingText.add("que des mesures soient prises si nécessaire.");
		currentScrollingText.add("");
		currentScrollingText.add("Mais il faut garder à l'esprit que les séismes sont des");
		currentScrollingText.add("phénomènes naturels, et donc inévitable.");
		currentScrollingText.add("Il ne sera jamais possible de les annuler.");
		currentScrollingText.add("");
		currentScrollingText.add("");
		currentScrollingText.add("");
		currentScrollingText.add("");
		currentScrollingText.add("50*              Credits");
		currentScrollingText.add("");
		currentScrollingText.add("");
		currentScrollingText.add("Développeur : Quentin *****");
		currentScrollingText.add("Scénario et idées : Louis *****");
		currentScrollingText.add("Graphiste : Quentin *****");
		currentScrollingText.add("Graphiste (Mario) : A.J NItro & CyberWolf.");
		currentScrollingText.add("Graphiste (Goumba) : T3hTeeks");
		currentScrollingText.add("Musique : Frank Nora");
		currentScrollingText.add("Musique : TeknoAXE");
		currentScrollingText.add("Script : Quentin *****");
		currentScrollingText.add("");
		currentScrollingText.add("30*   Ressources/Assets");
		currentScrollingText.add("");
		currentScrollingText.add("Nintendo (Bowser, Toad, Musiques)");
		currentScrollingText.add("Spike Chunsoft");
		currentScrollingText.add("The Pok�mon Company");
		currentScrollingText.add("Spike Chunsoft");
		currentScrollingText.add("CNN.com");
		currentScrollingText.add("Square Enix");
		currentScrollingText.add("Lindsey Stirling & Peter Hollens");
		currentScrollingText.add("");
		currentScrollingText.add("30*   Statistiques");
		currentScrollingText.add("");
		currentScrollingText.add("Taille du jeu : 29,7 Mo");
		currentScrollingText.add("Nombre de classes : 49");
		currentScrollingText.add("Nombre de méthodes : 284");
		currentScrollingText.add("Nombre de lignes : 3390");
		currentScrollingText.add("");
		currentScrollingText.add("");
	}
	
	private void writeCredits(Graphics2D g2d){
		writeScrollingText(g2d, currentScrollingText, 2950, 0.5, new int[]{958,670});
		AlphaComposite alpha;
		if(scrollingTextOffset > 2500){
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) ((scrollingTextOffset-2500)/500));
			g2d.setComposite(alpha);
			g2d.drawImage(endBackground, 0, 0, getWidth(), getHeight(), null);
			if(scrollingTextOffset > 2750){
				alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) ((scrollingTextOffset-2750)/250f));
				g2d.setComposite(alpha);
				g2d.drawImage(thxForPlaying, 0, getHeight()/2-getHeight()/8, getWidth(), getHeight()/4, null);
			}
		}
		if(scrollingTextOffsetWait >= 0)return;
		int last = 0;
		while(circles.size() < 30){
			//x, y, width, fallingSpeed
			circles.add(new int[]{last, -200-last, r.nextInt(10)+15, r.nextInt(4)+1});
			circlesColor.add(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			last+=25;
		}
		int[] data;
		for(int i = 0; i < circles.size(); i++){
			data = circles.get(i);
			g2d.setColor(circlesColor.get(i));
			alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, data[3]/6f);
			g2d.setComposite(alpha);
			g2d.fillOval(getPX(data[0]), getPY(data[1]), getPX(data[2]), getPX(data[2]));
			circles.get(i)[1] += data[3];
		}
		for(int i = 0; i < circles.size(); i++){
			if(circles.get(i)[1] > getHeight()){
				circles.set(i, new int[]{circles.get(i)[0], -200, r.nextInt(10)+15, r.nextInt(4)+1});
				circlesColor.set(i, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			}
		}
	}
	
	private void writeIntroduction(Graphics2D g2d) {
		double speed = 0.25;
		if(scrollingTextOffset < 660)
			speed = 1;
		writeScrollingText(g2d, currentScrollingText, 1350, speed, new int[]{330,200});
		if(scrollingTextOffset < 630)return;
		if(!GameSounds.isMusicCurrentlyRunning())
			GameSounds.playMusic();
	}

	private void writeScrollingText(Graphics2D g2d, List<String> text, int maxOffset, double scrollingSpeed, int[] waitingTime){
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		if(scrollingTextOffset < 200){
			AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (scrollingTextOffset/200));
			g2d.setComposite(alpha);
		}
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, getPX(20)));
		for (int i = 0; i < text.size(); i++) {
			if(text.get(i).contains("*")){
				g2d.setFont(new Font("Arial", Font.BOLD, getPX(Integer.parseInt(text.get(i).split("\\*")[0]))));
				g2d.drawString(text.get(i).split("\\*")[1], getPX(100), getPY((int) (620 + 40 * i - scrollingTextOffset)));
				g2d.setFont(new Font("Arial", Font.BOLD, getPX(20)));
				continue;
			}
			g2d.drawString(text.get(i), getPX(100), getPY((int) (620 + 40 * i - scrollingTextOffset))); // ->																							// 50
		}
		//System.out.println(scrollingTextOffset);
		if(scrollingTextOffset == waitingTime[0]){
			scrollingTextOffsetWait++;
			if(scrollingTextOffsetWait >= waitingTime[1]){
				scrollingTextOffset++;
				scrollingTextOffsetWait = -1;
			}
			return;
		}
		if (scrollingTextOffset < maxOffset){
			scrollingTextOffset += scrollingSpeed;
		}
		else if (!pressA){
			pressA = true;
			grabFocus();
		}else {
			
			g2d.setFont(new Font("Arial", Font.PLAIN, getPX(10)));
			g2d.drawString("Appuyez sur A pour commencer", getPX(300), getPY(575));
		}
	}
	
	/**
	 * Get proportionality x
	 * 
	 * @param nb
	 *            default value
	 * @return
	 */
	public int getPX(int nb) {
		return getWidth() * nb / defaultWidth;
	}
	
	public double getPX(double xOffset) {
		return getWidth() * xOffset / defaultWidth;
	}

	public int getPX2(int nb) {
		return defaultWidth * nb / getWidth();
	}

	/**
	 * Get proportionality y
	 * 
	 * @param nb
	 *            default value
	 * @return
	 */
	public int getPY(int nb) {
		return getHeight() * nb / defaultHeight;
	}
	
	public double getPY(double nb) {
		return getHeight() * nb / defaultHeight;
	}

	public int getPY2(int nb) {
		return defaultHeight * nb / getHeight();
	}

	public int getIntAxeX(int x, int y) {
		return (int) ((y - getConstant()) / getCoefDirecteur()) + getDeltaX() * x;
	}

	public int getIntAxeY(int y) {
		return 10 + y * getDeltaY();
	}

	public int getAxeX(int x, int y) {
		return (int) (((y - getConstant()) / getCoefDirecteur())) + x;
	}

	public int getAxeY(int y) {
		return y;
	}

	public int getDeltaX() {
		return getPX(70);
	}

	public int getDeltaY() {
		return (int) (getHeight() / 11.8d);
	}

	public double getCoefDirecteur() {
		Point a = new Point(getDeltaX(), 0);
		Point b = new Point(getDeltaX() * -4, getHeight());

		double d = (a.getY() - b.getY()) / (a.getX() - b.getX());

		return d;

	}

	private double getConstant() {
		Point a = new Point(getDeltaX(), 0);

		double i = a.x * getCoefDirecteur();

		return (a.y / i);
	}

	public int getWidth() {
		return (int) getBounds().getWidth();
	}

	public int getHeight() {
		return (int) getBounds().getHeight();
	}

	@Override
	public void repaint() {
		if (!animationRunning)
			super.repaint();
	}

	private String getLetters(String line, int letterAmount) {
		return line.substring(0, letterAmount);
	}

	private class RefreshingRunnable implements Runnable {

		private boolean activated = true;

		public RefreshingRunnable() {
			activated = true;
		}

		@Override
		public void run() {
			while (activated) {
				repaint();
				try {
					Thread.sleep(1000 / 60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		public void stop() {
			activated = false;
		}

	}

	public void currentAnimationTerminated() {
		if (levelLoading) {
			levelLoading = false;
			Game.currentLevel.start();
		}
		animationRunning = false;
		callEvent(new Event("animationTerminatedEvent"));
	}

	public void setCurrentLevel(Level l) {
		levelLoading = true;
		Game.currentLevel = l;
	}

	@Override
	public void callEvent(Event e) {
		Main.callEvent(e);
	}

	@Override
	public void onEvent(Event e) {
		if (e.getID().equals("startAnimationEvent")) {
			animationRunning = true;
			GameScreen instance = this;
			new Thread(new Runnable() {

				@Override
				public void run() {
					Graphics2D g = (Graphics2D) getGraphics();

					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

					((StartAnimationEvent) e).getAnimation().play(instance, g);
				}
			}).start();
		} else if (e.getID().equals("startLevelEvent")) {
			StartLevelEvent sle = (StartLevelEvent) e;
			worldMap.unLoad();
			System.err.println("startLevelEvent");
			Level level = Levels.getLevel(sle.getLevelID());
			level.load();
			level.setMario(mario);
			setCurrentLevel(level);
		} else if (e.getID().equals("endLevelEvent")) {
			Level l = ((EndLevelEvent)e).getLevel();
			if(((EndLevelEvent)e).win()){
				 l.done();
					if(Game.getCurrentLevel() == l.getId())
						Game.setCurrentLevel(Game.getCurrentLevel() + 1);
			}else{
				Game.setCurrentLevel(0);
			}
			Game.currentLevel.unLoad();
			Game.currentLevel = null;
			worldMap.load();
			if (((EndLevelEvent) e).win()) {
				l.done();
				this.parchmentPicture = GameImage.getParchment(l.getId());
				if (firstParchment) {
					List<String> colressDialog = new LinkedList<String>();
					colressDialog.add("Ah ! Je vois que vous avez reçu");
					colressDialog.add("votre premier parchemin, félicitation !");
					colressDialog.add("N'hésitez pas à redimensionner la fenêtre");
					colressDialog.add("pour mieux voir ce qu'il contient.");
					callEvent(new ShowTextBubbleEvent("colressFirstParchmentDialog", colressDialog, true));
					firstParchment = false;
				}
			} else {
				worldMap.setPlayerPosition(new Point(5,9));
				Game.live = 99;
			}
		} else if (e.getID().equals("showTextBubbleEvent")) {
			ShowTextBubbleEvent stbe = (ShowTextBubbleEvent) e;
			this.currentLetter = 1;
			this.currentLine = 1;
			this.currentDialogId = stbe.getDialogId();
			mario.freeze();
			this.isHelpMessage = stbe.isHelpMessage();
			this.pressA = false;
			message = new LinkedList<String>();
			for (String line : stbe.getMessage()) {
				if (line.contains("*")) {
					for (String subLine : line.split("\\*"))
						message.add(subLine);
				} else {
					message.add(line);
				}
			}
			if (!isHelpMessage) {
				this.picture = stbe.getPicture();
				this.bubbleLocation = stbe.getStartPoint();
			} else {
				this.pictureOffset = stbe.isPopup()?0:140;
				this.bubbleLocation = new Point(720, 300);
				this.picture = GameImage.createImage(GameImage.COLRESS);
			}
		} else if (e.getID().equals("startQuizzEvent")) {
			StartQuizzEvent sqe = (StartQuizzEvent) e;
			this.questionIndex = 0;
			this.quizz = sqe.getQuestions();
			this.isNextPopup = false;
			this.isQuizz = true;
		} else if (e.getID().equals("keyPressedEvent")) {
			KeyPressedEvent kpe = (KeyPressedEvent)e;
			if (kpe.getKeyCode() == 65 && pressA && !isQuizz) {
				if (isIntroduction) {
					GameSounds.stopMusic();
					try{
						worldMap.load();
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					isIntroduction = false;
					pressA = false;
					return;
				}
				if (aText.equals("continuer")) {
					this.currentLetter = 1;
					this.currentLine = 1;
					this.pressA = false;
					message.remove(2);
					message.remove(1);
					message.remove(0);
				} else if (aText.equals("terminer")) {
					this.picture = null;
					this.message = null;
					mario.unFreeze();
					System.out.println("terminated, id = "+currentDialogId);
					callEvent(new TextBubbleTerminatedEvent(currentDialogId));
				} else if (aText.equals("parchmentTerminer")) {
					this.parchmentPicture = null;
					this.pressA = false;
				}
				pressA = false;
			}else if(isQuizz){
				if(kpe.getKeyCode() == 38 && cursorPosition > 0)
					cursorPosition--;
				else if(kpe.getKeyCode() == 40 && cursorPosition < 2)
					cursorPosition++;
				else if(kpe.getKeyCode() == 65 && pressA){
					boolean isGoodAnswer = quizz.get(questionIndex).getGoodIndex() == cursorPosition;
					if(!isGoodAnswer){
						Random r = new Random();
						int magni = r.nextInt(3)+7;
						int deathAmount = (r.nextInt(3000)+2000)*magni;
						System.out.println("Magnitude: "+magni+"\nMorts: "+deathAmount);
						callEvent(new QuizzQuestionAnsweredEvent(questionIndex, magni, deathAmount));
					}else{
						callEvent(new QuizzQuestionAnsweredEvent(questionIndex));
						List<String> msg = new LinkedList<String>();
						msg.add("Oui ! Félicitation vous avez empêché");
						msg.add("un séisme. Continuez comme ça.");
						callEvent(new ShowTextBubbleEvent("colressQuizzSuccess", msg, false));
					}
				}
			}
		} else if(e.getID().equals("quizzQuestionAnsweredEvent")){
			System.out.println("answered");
			this.questionIndex++;
			this.message = null;
			this.isQuizz = false;
		}else if(e.getID().equals("earthquakeAnimationTerminated")){
			List<String> msg = new LinkedList<String>();
			msg.add("Aïe, ce n'était pas ça...");
			callEvent(new ShowTextBubbleEvent("colressQuizzError", msg, true));
		}else if(e.getID().equals("textBubbleTerminatedEvent")){
			if(((TextBubbleTerminatedEvent)e).getDialogId().contains("colressQuizz")){
				if(questionIndex >= quizz.size()){
					List<String> colressDialog = new ArrayList<String>();
					colressDialog.add("C'est bon, j'ai terminé le giga-laser,");
					colressDialog.add("J'espère qu'il suffira à faire partir");
					colressDialog.add("Bowser... Attention, c'est parti !");
					callEvent(new ShowTextBubbleEvent("quizzTerminatedDialog", colressDialog, true));
				}else{
					this.isNextPopup = false;
					this.isQuizz = true;
				}
			}
		}else if(e.getID().equals("gameTerminatedEvent")){
			try{
			if(Game.currentLevel != null)
				Game.currentLevel.unLoad();
			Game.currentLevel = null;
			if(worldMap != null)
				worldMap.unLoad();
			worldMap = null;
			createCredits();
			isCredits = true;
			new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
					GameSounds.playSound(GameSounds.END_MUSIC);
				}
			}, 21250);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
