/*
 * @author Thad Benjaponpitak
 */
package entities;

import components.CollisionComponent;
import components.ContactComponent;
import components.JumpableSurfaceComponent;
import components.PositionComponent;
import components.RenderComponent;
import components.SwitchComponent;
import components.VelocityComponent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import renderables.RenderableSwitchBlock;

public class SwitchBlock extends Entity {
	public SwitchBlock(Paint codeColor, char code) {
		this(0, 0, codeColor, code);
	}

	public SwitchBlock(double positionX, double positionY, Paint codeColor, char code) {
		add(new RenderComponent(new RenderableSwitchBlock(Block.WIDTH, Block.HEIGHT, codeColor), Block.COLOR),
				new PositionComponent(positionX, positionY),
				new CollisionComponent(new Rectangle(Block.WIDTH, Block.HEIGHT)),
				new ContactComponent(VelocityComponent.class), new SwitchComponent(code),
				new JumpableSurfaceComponent());
	}
}
