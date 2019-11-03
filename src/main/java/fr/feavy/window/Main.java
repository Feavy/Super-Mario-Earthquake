package fr.feavy.window;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.feavy.event.events.Event;
import fr.feavy.event.listeners.EventListener;
import fr.feavy.event.listeners.KeyListener;
import fr.feavy.game.levels.Level;
import fr.feavy.game.levels.Level1;
import fr.feavy.game.levels.Level2;
import fr.feavy.game.levels.Level3;
import fr.feavy.game.levels.Level4;
import fr.feavy.game.levels.Levels;
import fr.feavy.image.GameImage;
import fr.feavy.sounds.GameSounds;
import fr.feavy.utils.Game;

public class Main extends JFrame {

	private static GameScreen gameContentPane;
	private static FirstContentPane firstContentPane;
	private static Main instance;

	private static List<EventListener> eventListeners = new ArrayList<EventListener>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		for(String s : ge.getAvailableFontFamilyNames())
//			System.out.println(s);
		
		instance = this;
		Levels.initialize();
		
		setTitle("TPE Parasismique - Quentin ***** & Louis ***** 1re S°2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 805, 629);
		setLocationRelativeTo(null);
		setResizable(true);
		createFirstPanel();
		URL iconURL = getClass().getResource("/icon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());
	}
	
	public static void removeEventListener(EventListener el){
		System.out.println("remove event listener : "+el);
		eventListeners.remove(el);
	}
	
	public static void addEventListener(EventListener el) {
		System.out.println("add event listener : "+el);
		eventListeners.add(el);
	}

	public static void callEvent(Event e) {
		try{
			for (EventListener el : eventListeners)
				el.onEvent(e);
		}catch(Exception ex){}
	}

	public void startButtonPressed() {
		GameSounds.playSound(GameSounds.BUTTON);
		firstContentPane = null;
		Map<Point, Integer> levels = new LinkedHashMap<Point, Integer>();
		levels.put(new Point(5, 9), 0);
		levels.put(new Point(5, 6), 1);
		levels.put(new Point(11, 6), 2);
		levels.put(new Point(11, 2), 3);
//		for(Level l : levels.values())
//			l.done();
//		Game.setCurrentLevel(4);
		createGamePanel(levels);
		
	}
	
	private void createFirstPanel(){
		firstContentPane = new FirstContentPane(this);
		firstContentPane.setBorder(new EmptyBorder(5,5,5,5));
		BufferedImage img = GameImage.createImage(GameImage.EARTHQUAKE);
		firstContentPane.setBackgroundImage(img);
		firstContentPane.setLayout(null);
		setContentPane(firstContentPane);
		
	}
	
	private void createGamePanel(Map<Point, Integer> levels) {
		gameContentPane = new GameScreen(levels);
		gameContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		gameContentPane.setLayout(null);
		//gameContentPane.add(new webview);
		setContentPane(gameContentPane);
	}

}
