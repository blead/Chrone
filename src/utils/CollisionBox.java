package utils;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class CollisionBox extends Rectangle {
	public CollisionBox(Point2D position, Shape collisionShape) {
		this(position.getX(), position.getY(), collisionShape.getBoundsInParent().getWidth(),
				collisionShape.getBoundsInParent().getHeight());
	}

	public CollisionBox(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	public boolean intersects(Rectangle other) {
		return intersectsX(other) && intersectsY(other);
	}

	public boolean intersectsX(Rectangle other) {
		return getX() < other.getX() + other.getWidth() && getX() + getWidth() > other.getX();
	}

	public boolean intersectsY(Rectangle other) {
		return getY() < other.getY() + other.getHeight() && getY() + getHeight() > other.getY();
	}

	public CollisionBox getBroadPhaseArea(Point2D velocity) {
		double x = velocity.getX() > 0 ? getX() : getX() + velocity.getX(),
				y = velocity.getY() > 0 ? getY() : getY() + velocity.getY(),
				width = getWidth() + Math.abs(velocity.getX()), height = getHeight() + Math.abs(velocity.getY());
		return new CollisionBox(x, y, width, height);
	}

	public Direction getContactDirectionX(Rectangle other) {
		if (getX() + getWidth() == other.getX())
			return Direction.RIGHT;
		if (other.getX() + other.getWidth() == getX())
			return Direction.LEFT;
		return Direction.NONE;
	}

	public Direction getContactDirectionY(Rectangle other) {
		if (getY() + getHeight() == other.getY())
			return Direction.DOWN;
		if (other.getY() + other.getHeight() == getY())
			return Direction.UP;
		return Direction.NONE;
	}
}
