package fr.feavy.rendering.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.feavy.game.levels.Platform;
import fr.feavy.image.GameImage;
import fr.feavy.rendering.Character;
import fr.feavy.window.GameScreen;

public class RedButton extends LevelObject {

	private final Rectangle button = new Rectangle(0, 0, 114, 47);
	private final Rectangle bar = new Rectangle(0, 47, 18, 101);
	private final Rectangle base = new Rectangle(0, 148, 170, 85);
	
	private int buttonY;
	private boolean buttonPressed;
	
	private int times;
	
	private WoodPlatform woodenPlatform;

	public RedButton(WoodPlatform woodPlatform, int x, int y, BufferedImage image) {
		super(x, y, image);
		addPlatform(new Platform(0, false, x + 30, y + 68, x + 85, y + 100));
		addPlatform(new Platform(1, false, x + 45, y+10, x + 70, y+69));
		buttonY = y;
		buttonPressed = false;
		times = 0;
		this.woodenPlatform = woodPlatform;
	}
	
	@Override
	public void platformPressed(Character actor, int platformID) {
		super.platformPressed(actor, platformID);
		if(platformID == 1){
			buttonPressed = true;
		}
	}

	@Override
	public void draw(Graphics2D g) {
			
		if(times > 45){
			buttonPressed = false;
		}
		
		if(buttonPressed){
			getPlatform(1).setBounds(getPlatform(1).getAbsX1(), getPlatform(1).getAbsY1()+1, getPlatform(1).getAbsX2(), getPlatform(1).getAbsY2());
			buttonY++;
			times++;
			woodenPlatform.moveLeft();
		}
		
		g.drawImage(getImage().getSubimage(base.x, base.y, base.width, base.height),
				getScreen().getPX(getX() + 15), getScreen().getPY(getY() + 60),
				getScreen().getPX(base.width / 2), getScreen().getPY(base.height / 2), null);
		g.drawImage(getImage().getSubimage(bar.x, bar.y, bar.width, bar.height), getScreen().getPX(getX() + 53),
				getScreen().getPY(buttonY+20), getScreen().getPX(bar.width / 2),
				getScreen().getPY(bar.height / 2-times), null);
		g.drawImage(getImage().getSubimage(button.x, button.y, button.width, button.height),
				getScreen().getPX(getX() + 30), getScreen().getPY(buttonY), getScreen().getPX(button.width / 2),
				getScreen().getPY(button.height / 2), null);
	}

}
