package fr.feavy.image;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Sprite{

	private BufferedImage spriteBank;
	private Image currentImage;
	private int rows = 0, cols = 0;
	
	private int currentColumn = 0;
	private int lastRow = -1;
	
	private int spriteHeight = 0, spriteWidth = 0;
	
	public final int DOWN = 0, UP = 1, RIGHT = 2, LEFT = 3;
	
	private int defaultRow, defaultColumn;
	
	int offset = 1;
	
	private boolean loop;
	
	public Sprite(BufferedImage image, boolean loop, int spriteHeight, int spriteWidth, int defaultRow, int defaultColumn) {
		this.spriteBank = image;
		this.loop = loop;
		this.offset = 1;
		rows = image.getHeight()/spriteHeight;
		cols = image.getWidth()/spriteWidth;
		this.spriteHeight = spriteHeight;
		this.spriteWidth = spriteWidth;
		currentColumn = 0;
		lastRow = -1;
		this.defaultRow = defaultRow;
		this.defaultColumn = defaultColumn;
		currentImage = spriteBank.getSubimage(spriteWidth*defaultRow, spriteHeight*defaultColumn, spriteWidth, spriteHeight);
	}
	
	public int getWidth(){return spriteWidth;}
	public int getHeight(){return spriteHeight;}
	
	public void setDefaultImage() {
		currentImage = spriteBank.getSubimage(spriteWidth*defaultRow, spriteHeight*defaultColumn, spriteWidth, spriteHeight);// TODO Auto-generated method stub
	}
	
	public void nextImage(int row){
		
		//System.out.println("currentColumn: "+currentColumn);
		
		if(row != lastRow){
			offset = 1;
			currentColumn = 0;
			lastRow = row;
		}
		
		currentImage = spriteBank.getSubimage(currentColumn*spriteWidth, row*spriteHeight, spriteWidth, spriteHeight);
		currentColumn+=offset;
		
		if(currentColumn >= cols)
			if(loop)
				currentColumn = 0;
			else{
				offset = -1;
				currentColumn--;
			}
		else if(currentColumn <= 0 && !loop)
			offset = 1;
		
	}
	
	public Image getCurrentImage(){ return currentImage; }
	
}
