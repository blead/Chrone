package entities;

import components.CollisionComponent;
import components.JumpableSurfaceComponent;
import components.PositionComponent;
import components.RenderComponent;
import core.Level;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import renderables.RenderableRectangle;

public class Block extends Entity {
	public static final double WIDTH = Level.TILE_SIZE;
	public static final double HEIGHT = Level.TILE_SIZE;
	public static final Paint COLOR = Color.BLACK;

	public Block() {
		this(0, 0);
	}

	public Block(Point2D position) {
		this(position.getX(), position.getY());
	}

	public Block(double positionX, double positionY) {
		add(new RenderComponent(new RenderableRectangle(Block.WIDTH, Block.HEIGHT), Block.COLOR),
				new PositionComponent(positionX, positionY),
				new CollisionComponent(new Rectangle(Block.WIDTH, Block.HEIGHT)), new JumpableSurfaceComponent());
	}
}
