package utils;

import components.VelocityComponent;
import core.Entity;
import javafx.geometry.Point2D;

public class MoveIntent implements Intent {
	private Point2D acceleration, maxVelocity;

	public MoveIntent(Point2D acceleration, Point2D maxVelocity) {
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;
	}

	@Override
	public void handle(Entity entity) {
		try {
			VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
			Point2D velocity = velocityComponent.getVelocity().add(acceleration);
			if (Math.abs(velocity.getX()) > maxVelocity.getX())
				velocity = new Point2D(Math.copySign(maxVelocity.getX(), velocity.getX()), velocity.getY());
			if (Math.abs(velocity.getY()) > maxVelocity.getY())
				velocity = new Point2D(velocity.getX(), Math.copySign(maxVelocity.getY(), velocity.getY()));
			velocityComponent.setVelocity(velocity);
		} catch (ComponentNotFoundException e) {
			throw new IllegalArgumentException();
		}
	}
}
