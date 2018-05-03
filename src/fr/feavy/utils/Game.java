package fr.feavy.utils;

import fr.feavy.game.levels.Level;

public class Game {

	public static int LEFT= 37, UP = 38, RIGHT = 39, DOWN = 40;
	private static int currentLevelID = 0;
	
	public static boolean isFirstLevel = true;
	public static boolean isFirstWorldMap = true;
	
	public static String cityName = "la ville";
	public static int live = 99;
	public static int levelAmount = 3;
	public static int inhabitantAmount = 150000;
	public static Level currentLevel;
	
	public static int getXOffset(int key){
		if(key != 37 && key != 39)
			return 0;
		else
			return (key == 37) ? -1 : 1;
	}
	
	public static int getYOffset(int key){
		if(key != 38 && key != 40)
			return 0;
		else
			return (key == 38) ? -1 : 1;
	}
	
	public static int getOpositeDirection(int direction){
		return direction <= 38 ? direction+2 : direction-2;
	}
	
	public static boolean isLevelDone(int levelID){return levelID < currentLevelID;}
	
	public static void setCurrentLevel(int level){currentLevelID = level;}
	
	public static int getCurrentLevel(){return currentLevelID; }
	
	public static boolean isArrow(int keyCode) { return (keyCode >= 37 && keyCode <= 40); }
	
}
