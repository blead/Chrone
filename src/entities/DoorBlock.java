/*
 * @author Thad Benjaponpitak
 */
package entities;

import components.CollisionComponent;
import components.DoorComponent;
import components.JumpableSurfaceComponent;
import components.PositionComponent;
import components.RenderComponent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import renderables.RenderableDoorBlock;

public class DoorBlock extends Entity {
	public static final double ALPHA = 0.2;

	public DoorBlock(Paint codeColor, char code) {
		this(0, 0, codeColor, code);
	}

	public DoorBlock(double positionX, double positionY, Paint codeColor, char code) {
		add(new RenderComponent(new RenderableDoorBlock(Block.WIDTH, Block.HEIGHT, codeColor), Block.COLOR),
				new PositionComponent(positionX, positionY),
				new CollisionComponent(new Rectangle(Block.WIDTH, Block.HEIGHT)),
				new DoorComponent(new Rectangle(Block.WIDTH, Block.HEIGHT), code), new JumpableSurfaceComponent());
	}
}
