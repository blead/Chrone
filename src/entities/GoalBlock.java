/*
 * @author Thad Benjaponpitak
 */
package entities;

import components.CollisionComponent;
import components.ContactComponent;
import components.GoalComponent;
import components.JumpableSurfaceComponent;
import components.MessageComponent;
import components.PositionComponent;
import components.RenderComponent;
import components.VelocityComponent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import renderables.RenderableGoalBlock;

public class GoalBlock extends Entity {
	public static final Paint COLOR = Color.WHITE;
	public static final Paint ACTIVE_COLOR = Color.YELLOW;

	public GoalBlock(Paint goalColor) {
		this(0, 0, goalColor);
	}

	public GoalBlock(double positionX, double positionY, Paint goalColor) {
		add(new RenderComponent(new RenderableGoalBlock(Block.WIDTH, Block.HEIGHT, goalColor), Block.COLOR),
				new PositionComponent(positionX, positionY),
				new CollisionComponent(new Rectangle(Block.WIDTH, Block.HEIGHT)),
				new ContactComponent(VelocityComponent.class), new GoalComponent(),
				new MessageComponent("CONGRATULATIONS", true), new JumpableSurfaceComponent());
	}
}
