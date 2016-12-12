package entities;

import components.CameraComponent;
import components.CollisionComponent;
import components.GravityComponent;
import components.PositionComponent;
import components.VelocityComponent;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public abstract class Chrone extends Entity {
	public static final double ALPHA = 0.5;

	public Chrone() {
		this(0, 0, 0, 0);
	}

	public Chrone(Point2D position, Point2D velocity) {
		this(position.getX(), position.getY(), velocity.getX(), velocity.getY());

	}

	public Chrone(double positionX, double positionY, double velocityX, double velocityY) {
		add(new PositionComponent(positionX, positionY), new VelocityComponent(velocityX, velocityY),
				new GravityComponent(), new CollisionComponent(new Rectangle(Player.WIDTH, Player.HEIGHT)),
				new CameraComponent(2));
	}
}
