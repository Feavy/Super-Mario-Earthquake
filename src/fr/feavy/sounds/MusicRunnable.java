package fr.feavy.sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicRunnable implements Runnable {

	private String soundName;
	private MediaPlayer currentMedia;
	private boolean running = true, loop = true, isCurrentlyRunning;

	public MusicRunnable(String soundName) {
		this.soundName = soundName;
		this.running = true;
		this.isCurrentlyRunning = false;
	}

	public void disableLoop() {
		this.loop = false;
	}

	@Override
	public void run() {
		isCurrentlyRunning = true;
		running = true;
		Media sound = new Media(GameSounds.class.getResource("/sounds/" + soundName + ".mp3").toString());
		currentMedia = new MediaPlayer(sound);
		currentMedia.setVolume(0.75d);
		currentMedia.setOnReady(new Runnable() {

			@Override
			public void run() {
				currentMedia.play();

			}
		});
		currentMedia.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				if (running){
					System.out.println("restart");
					currentMedia.seek(Duration.ZERO);
					currentMedia.play();
				}else {
					currentMedia = null;
				}
			}
		});
		System.out.println("music terminated");
	}

	public boolean isCurrentlyRunning() {
		return isCurrentlyRunning;
	}

	public void stop() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 5; i++) {
					currentMedia.setVolume(0.75f - i / 10f);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				isCurrentlyRunning = false;
				running = false;
				currentMedia.stop();
			}
		}).start();
	}

}
