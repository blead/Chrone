/*
 * @author Thad Benjaponpitak
 */
package intents;

import components.ContactComponent;
import components.VelocityComponent;
import entities.Entity;
import javafx.geometry.Point2D;
import utils.ComponentNotFoundException;
import utils.Direction;

public class JumpIntent extends MoveIntent {
	public JumpIntent(Point2D acceleration, Point2D maxVelocity) {
		super(acceleration, maxVelocity);
	}

	@Override
	public void handle(Entity entity) {
		try {
			VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
			ContactComponent contactComponent = (ContactComponent) entity.getComponent(ContactComponent.class);
			if (contactComponent.isContact(Direction.DOWN)) {
				velocityComponent.setVelocity(getVelocity(velocityComponent.getVelocity(), acceleration, maxVelocity));
				contactComponent.setContact(Direction.DOWN, false);
			}
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
