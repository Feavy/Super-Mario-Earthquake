package fr.feavy.rendering.animation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;

import fr.feavy.window.GameScreen;

public class FadeScreen implements Animation {

	private String text;
	private int fontSize;
	private Color color;

	public FadeScreen(String text, int fontSize, Color color) {
		this.text = text;
		this.fontSize = fontSize;
		this.color = color;
	}

	@Override
	public void play(GameScreen screen, Graphics2D g) {
		g.setFont(new Font("sansSerif", Font.PLAIN, fontSize));
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
		Composite noAlpha = g.getComposite();
		for (int i = 0; i < 25; i++) {
			if (text != null) {
				g.setComposite(noAlpha);
				g.setPaint(Color.white);
				g.drawString(text, screen.getWidth()/2-text.length()*(fontSize/4), screen.getHeight()/2);
			}
			g.setComposite(alpha);
			g.setPaint(color);
			g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		screen.currentAnimationTerminated();
	}

}
