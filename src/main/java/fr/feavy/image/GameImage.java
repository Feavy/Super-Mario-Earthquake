package fr.feavy.image;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameImage {

	public static final String TITLE = "title";
	public static String EARTHQUAKE = "earthquake";
	public static String RED_PORTAL = "red spot";
	public static String BLUE_PORTAL = "blue spot";
	public static String BLACK_PORTAL = "black spot";
	public static String WORLDMAP_BACKGROUND = "worldmap_background";
	public static String TREE = "tree";
	public static String MORTON = "morton";
	public static String BOWSER = "bowser";
	public static String SPIKES = "objects/spikes";
	public static String RED_BUTTON = "objects/red_button";
	public static String MARIO_WORLD_MAP = "mario_worldmap";
	public static String MORTON_SPRITE = "morton_sprite";
	public static String WOODEN_PLATFORM = "objects/wood_platform";
	public static String BLACK_RECTANGLE = "objects/black_rectangle";
	public static String CHAMPIGNON = "objects/champignon";
	public static String PARCHMENT_OBJECT = "objects/parchment";
	public static String LASER = "objects/laser";
	public static String TOAD_SPRITE = "toad_sprite";
	public static String BOWSER_SPRITE = "bowser_sprite";
	public static String TOAD = "toad";
	public static String GOUMBA = "goumba_sprite";
	public static String COLRESS = "colress";
	
	public static String THANKS = "thanks";
	
	public static String POPULATION = "hud/population";
	public static String HEART = "hud/heart";
	public static String PARCHMENT = "hud/parchment";
	
	public static BufferedImage createImage(String imagePath){
		try {
			if(imagePath.equals("bg0"))
				return ImageIO.read(GameImage.class.getResource("/"+imagePath+".jpg"));
			else
				return ImageIO.read(GameImage.class.getResource("/"+imagePath+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage getBackground(int backgroundId){
		return createImage("backgrounds/bg"+backgroundId);
	}
	
	public static BufferedImage getLevel(int levelId){
		return createImage("levels/lvl"+levelId);
	}

	public static BufferedImage getParchment(int parchmentId) {
		return createImage("parchments/parch"+parchmentId);
	}
	
}
