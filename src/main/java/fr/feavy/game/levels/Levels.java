package fr.feavy.game.levels;

public class Levels {

	private static Level[] levels = new Level[4];
	
	public static boolean initialize(){
		levels[0] = new Level1();
		levels[1] = new Level2();
		levels[2] = new Level3();
		levels[3] = new Level4();
		return true;
	}
	
	public static Level getLevel(int id){
		return levels[id];
	}
	
}
