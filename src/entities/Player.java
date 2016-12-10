package entities;

import components.CameraComponent;
import components.CollisionComponent;
import components.GravityComponent;
import components.PositionComponent;
import components.RenderComponent;
import components.VelocityComponent;
import core.Entity;
import core.Level;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import utils.RenderableRectangle;

public class Player extends Entity {
	public static final double WIDTH = Level.TILE_SIZE / 2;
	public static final double HEIGHT = Level.TILE_SIZE / 2;
	public static final Paint COLOR = Color.BLUE;

	public Player() {
		this(0, 0, 0, 0);
	}

	public Player(Point2D position, Point2D velocity) {
		this(position.getX(), position.getY(), velocity.getX(), velocity.getY());

	}

	public Player(double positionX, double positionY, double velocityX, double velocityY) {
		add(new RenderComponent(new RenderableRectangle(Player.WIDTH, Player.HEIGHT), Player.COLOR),
				new PositionComponent(positionX, positionY), new VelocityComponent(velocityX, velocityY),
				new GravityComponent(), new CollisionComponent(new Rectangle(Player.WIDTH, Player.HEIGHT)),
				new CameraComponent());
	}
}
