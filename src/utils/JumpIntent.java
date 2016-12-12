package utils;

import components.CollisionComponent;
import components.VelocityComponent;
import core.Entity;
import javafx.geometry.Point2D;

public class JumpIntent extends MoveIntent {
	public JumpIntent(Point2D acceleration, Point2D maxVelocity) {
		super(acceleration, maxVelocity);
	}

	@Override
	public void handle(Entity entity) {
		try {
			VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
			CollisionComponent collisionComponent = (CollisionComponent) entity.getComponent(CollisionComponent.class);
			if (collisionComponent.isColliding(Direction.DOWN))
				velocityComponent.setVelocity(getVelocity(velocityComponent.getVelocity(), acceleration, maxVelocity));
		} catch (ComponentNotFoundException e) {
			throw new IllegalArgumentException();
		}
	}

	private Point2D getVelocity(Point2D velocity, Point2D acceleration, Point2D maxVelocity) {
		Point2D acceleratedVelocity = new Point2D(velocity.getX() + acceleration.getX(), acceleration.getY());
		if (Math.abs(acceleratedVelocity.getX()) > maxVelocity.getX())
			acceleratedVelocity = new Point2D(Math.copySign(maxVelocity.getX(), acceleratedVelocity.getX()),
					acceleratedVelocity.getY());
		if (Math.abs(acceleratedVelocity.getY()) > maxVelocity.getY())
			acceleratedVelocity = new Point2D(acceleratedVelocity.getX(),
					Math.copySign(maxVelocity.getY(), acceleratedVelocity.getY()));
		return acceleratedVelocity;
	}
}
