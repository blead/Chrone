/*
 * @author Thad Benjaponpitak
 */
package entities;

import components.CollisionComponent;
import components.ContactComponent;
import components.JumpableSurfaceComponent;
import components.MessageComponent;
import components.PositionComponent;
import components.RenderComponent;
import components.VelocityComponent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import renderables.RenderableInfoBlock;

public class InfoBlock extends Entity {
	public static final Paint COLOR = Color.BLUE;
	public static final Paint COLOR_DISABLED = Color.WHITE;

	public InfoBlock(String message, Paint secondaryColor) {
		this(0, 0, message, secondaryColor);
	}

	public InfoBlock(double positionX, double positionY, String message, Paint secondaryColor) {
		add(new RenderComponent(new RenderableInfoBlock(Block.WIDTH, Block.HEIGHT, secondaryColor), Block.COLOR),
				new PositionComponent(positionX, positionY),
				new CollisionComponent(new Rectangle(Block.WIDTH, Block.HEIGHT)),
				new ContactComponent(VelocityComponent.class), new MessageComponent(message, false),
				new JumpableSurfaceComponent());
	}
}
