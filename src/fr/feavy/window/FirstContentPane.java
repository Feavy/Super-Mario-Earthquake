package fr.feavy.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import fr.feavy.image.GameImage;
import javafx.embed.swing.JFXPanel;

public class FirstContentPane extends JFXPanel{

	private BufferedImage background, title;
	private JButton startButton;
	private JLabel bottom;
	
	private int frames = 0;
	
	private Runnable buttonRunnable;
	
	private boolean running = true;
	
	public FirstContentPane(Main main){
		bottom = new JLabel("Groupe : Louis ***** & Quentin *****     Lycée *****    Année 2016-2017", SwingConstants.CENTER);
		bottom.setForeground(Color.WHITE);
		bottom.setFont(new Font("sansSerif", Font.BOLD, 15));
		buttonRunnable = new Runnable() {

			
			@Override
			public void run() {
				while(running){
					invertColor(startButton);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
		add(bottom);
		startButton = new JButton("START");
		startButton.setFont(new Font("sansSerif", Font.BOLD, 49));
		startButton.setForeground(Color.white);
		startButton.setBackground(Color.darkGray);
		startButton.setBorderPainted(false);
		startButton.setFocusable(false);
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				running = false;
				main.startButtonPressed();
			}
		});
		add(startButton);
		new Thread(buttonRunnable).start();
		this.title = GameImage.createImage(GameImage.TITLE);
	}
	
	public void invertColor(JButton button){
		Color back = button.getBackground();
		button.setBackground(button.getForeground());
		button.setForeground(back);
	}
	
	public void setBackgroundImage(BufferedImage img) {
		this.background = img;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		startButton.setBounds(width/2-100, height/2-40, 200, 80);
		bottom.setBounds(x+x/4, 7*height/8, width-x/4, height/8);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(title, 0, getHeight()/20, getWidth(), getHeight()/3, null);
	}

}
