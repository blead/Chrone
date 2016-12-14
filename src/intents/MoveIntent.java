/*
 * @author Thad Benjaponpitak
 */
package intents;

import components.VelocityComponent;
import entities.Entity;
import javafx.geometry.Point2D;
import utils.ComponentNotFoundException;

public class MoveIntent implements Intent {
	protected Point2D acceleration, maxVelocity;

	public MoveIntent(Point2D acceleration, Point2D maxVelocity) {
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;
	}

	@Override
	public void handle(Entity entity) {
		try {
			VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
			velocityComponent.setVelocity(getVelocity(velocityComponent.getVelocity(), acceleration, maxVelocity));
		} catch (ComponentNotFoundException e) {
			throw new IllegalArgumentException();
		}
	}

	private Point2D getVelocity(Point2D velocity, Point2D acceleration, Point2D maxVelocity) {
		Point2D acceleratedVelocity = velocity.add(acceleration);
		if (Math.abs(acceleratedVelocity.getX()) > maxVelocity.getX())
			acceleratedVelocity = new Point2D(Math.copySign(maxVelocity.getX(), acceleratedVelocity.getX()),
					acceleratedVelocity.getY());
		if (Math.abs(acceleratedVelocity.getY()) > maxVelocity.getY())
			acceleratedVelocity = new Point2D(acceleratedVelocity.getX(),
					Math.copySign(maxVelocity.getY(), acceleratedVelocity.getY()));
		return acceleratedVelocity;
	}
}
