package entities;

import components.PositionComponent;
import components.RenderComponent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import main.Main;
import renderables.RenderableText;

public class TextUserInterface extends Entity {
	public static final Paint COLOR = Color.WHITE;

	public TextUserInterface(String text, double positionX, double positionY) {
		this(text, Font.getDefault(), positionX, positionY);
	}

	public TextUserInterface(String text, Font font, double positionX, double positionY) {
		RenderableText renderableText = new RenderableText(text, font);
		add(new RenderComponent(renderableText, TextUserInterface.COLOR,
				(Main.WIDTH - (positionX + renderableText.getWidth())) / 2,
				(Main.HEIGHT - (positionY + renderableText.getHeight())) / 2),
				new PositionComponent(positionX, positionY));
	}
}
