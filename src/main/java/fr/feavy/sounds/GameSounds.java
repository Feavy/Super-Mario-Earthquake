package fr.feavy.sounds;

import jaco.mp3.player.MP3Player;

public class GameSounds {

	public static final String BUTTON = "button";
	public static final String INTRODUCTION = "introduction";
	public static final String FADE = "fade";
	public static final String WORLDMAP_TIC = "worldmap_tic";
	public static final String TEXT = "text";
	public static final String JUMP = "jump";
	public static final String SPIKES = "spikes";
	public static final String STOMP = "stomp";
	public static final String DISCOVERY = "discovery";
	public static final String EARTHQUAKE = "earthquake";
	public static final String WORLDMAP_MUSIC = "worldmap_music";
	public static final String LEVEL0 = "lvl0_music";
	public static final String LEVEL1 = "lvl1_music";
	public static final String LEVEL2 = "lvl2_music";
	public static final String LASER = "laser";
	public static final String BOSS_MUSIC = "boss_music";
	public static final String END_MUSIC = "end_music";
	public static final String VICTORY = "victory";

	private static MusicRunnable currentMusic;
	
	public static String getLevelMusic(int levelID){
		return "lvl"+levelID+"_music";
	}
	
	public static void playSound(String soundName) {
		MP3Player player = new MP3Player(GameSounds.class.getResource("/sounds/"+soundName+".mp3"));
		player.play();
	}
	public static boolean isMusic() {
		return currentMusic != null;
	}

	public static boolean isMusicCurrentlyRunning() {
		return currentMusic.isCurrentlyRunning();
	}

	public static void setMusic(String musicName) {
		currentMusic = new MusicRunnable(musicName);
	}

	public static void playMusic() {
		new Thread(currentMusic).start();
	}

	public static void stopMusic() {
		currentMusic.stop();
		currentMusic = null;
	}

}
