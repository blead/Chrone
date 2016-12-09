package systems;

import java.util.List;

import components.CollisionComponent;
import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import utils.Collision;
import utils.ComponentNotFoundException;
import utils.Direction;

public class CollisionSystem extends EntitySystem {
	@Override
	public void update(float deltaTime) {
		List<Entity> entities = EntityManager.getInstance().getEntities();
		for (Entity entity : entities) {
			try {
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
				CollisionComponent collisionComponent = (CollisionComponent) entity
						.getComponent(CollisionComponent.class);
				Rectangle collisionBox = getCollisionBox(positionComponent.getPosition(),
						collisionComponent.getCollisionShape()),
						broadPhaseArea = getBroadPhaseArea(collisionBox, velocityComponent.getVelocity());
				Collision collision = Collision.NONE;
				do {
					for (Entity other : entities) {
						// only test against static entities
						if (other.contains(VelocityComponent.class))
							continue;
						try {
							PositionComponent otherPositionComponent = (PositionComponent) other
									.getComponent(PositionComponent.class);
							CollisionComponent otherCollisionComponent = (CollisionComponent) other
									.getComponent(CollisionComponent.class);
							Rectangle otherCollisionBox = getCollisionBox(otherPositionComponent.getPosition(),
									otherCollisionComponent.getCollisionShape());
							// only test inside broadPhaseArea
							if (doIntersect(broadPhaseArea, otherCollisionBox)) {
								// find earliest collision
								collision = Collision.min(collision,
										getCollision(collisionBox, otherCollisionBox, velocityComponent.getVelocity()));
							}
						} catch (ComponentNotFoundException e) {
							continue;
						}
					}
					positionComponent.setPosition(getResolvedPosition(positionComponent.getPosition(),
							velocityComponent.getVelocity(), collision));
					// slide off with remaining velocity
					velocityComponent.setVelocity(getSlidingVelocity(velocityComponent.getVelocity(), collision));
					// repeat until there is no collision
				} while (collision.getTime() < 1);
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}

	private Rectangle getCollisionBox(Point2D position, Shape collisionShape) {
		Bounds collisionBounds = collisionShape.getBoundsInParent();
		return new Rectangle(position.getX(), position.getY(), collisionBounds.getWidth(), collisionBounds.getHeight());
	}

	private Rectangle getBroadPhaseArea(Rectangle collisionBox, Point2D velocity) {
		Rectangle broadPhaseArea = new Rectangle();
		broadPhaseArea.setX(velocity.getX() > 0 ? collisionBox.getX() : collisionBox.getX() + velocity.getX());
		broadPhaseArea.setY(velocity.getY() > 0 ? collisionBox.getY() : collisionBox.getY() + velocity.getY());
		broadPhaseArea.setWidth(collisionBox.getWidth() + Math.abs(velocity.getX()));
		broadPhaseArea.setHeight(collisionBox.getHeight() + Math.abs(velocity.getY()));
		return broadPhaseArea;
	}

	private boolean doIntersect(Rectangle a, Rectangle b) {
		return a.getX() < b.getX() + b.getWidth() && a.getX() + a.getWidth() > b.getX()
				&& a.getY() < b.getY() + b.getHeight() && a.getY() + a.getHeight() > b.getY();
	}

	private Collision getCollision(Rectangle collisionBox, Rectangle otherCollisionBox, Point2D velocity) {
		// swept collision detection
		Point2D[] distances = getDistances(collisionBox, otherCollisionBox, velocity),
				times = getTimes(distances, velocity);
		double entryTime = Math.max(times[0].getX(), times[0].getY()),
				exitTime = Math.min(times[1].getX(), times[1].getY());

		if (entryTime > exitTime || times[0].getX() < 0 && times[0].getY() < 0
				|| times[0].getX() > 1 && times[0].getY() > 1) {
			return Collision.NONE;
		} else {
			Direction direction;
			if (times[0].getX() > times[0].getY()) {
				direction = Direction.fromPoint2D(new Point2D(-distances[0].getX(), 0));
			} else {
				direction = Direction.fromPoint2D(new Point2D(0, -distances[0].getY()));
			}
			return new Collision(direction, entryTime);
		}
	}

	private Point2D getResolvedPosition(Point2D position, Point2D velocity, Collision collision) {
		return position.add(velocity.multiply(collision.getTime()));
	}

	private Point2D getSlidingVelocity(Point2D velocity, Collision collision) {
		double slidingVelocity = velocity.dotProduct(collision.getDirection().getDy(), collision.getDirection().getDx())
				* (1 - collision.getTime());
		return new Point2D(slidingVelocity * collision.getDirection().getDy(),
				slidingVelocity * collision.getDirection().getDx());
	}

	private Point2D[] getDistances(Rectangle a, Rectangle b, Point2D velocity) {
		// [0]: entry distance, [1]: exit distance
		double leftToRight = Math.abs(b.getX() - (a.getX() + a.getWidth())),
				rightToLeft = Math.abs((b.getX() + b.getWidth()) - a.getX()),
				topToBottom = Math.abs(b.getY() - (a.getY() + a.getHeight())),
				bottomToTop = Math.abs((b.getY() + b.getHeight()) - a.getY());
		return new Point2D[] {
				new Point2D(Math.copySign(Math.min(leftToRight, rightToLeft), velocity.getX()),
						Math.copySign(Math.min(topToBottom, bottomToTop), velocity.getY())),
				new Point2D(Math.copySign(Math.max(leftToRight, rightToLeft), velocity.getY()),
						Math.copySign(Math.max(topToBottom, bottomToTop), velocity.getY())) };
	}

	private Point2D[] getTimes(Point2D[] distances, Point2D velocity) {
		// [0]: entry time, [1]: exit time
		double entryTimeX, exitTimeX, entryTimeY, exitTimeY;
		try {
			entryTimeX = distances[0].getX() / velocity.getX();
			exitTimeX = distances[1].getX() / velocity.getX();
		} catch (ArithmeticException e) {
			entryTimeX = Double.NEGATIVE_INFINITY;
			exitTimeX = Double.POSITIVE_INFINITY;
		}
		try {
			entryTimeY = distances[0].getY() / velocity.getY();
			exitTimeY = distances[1].getY() / velocity.getY();
		} catch (ArithmeticException e) {
			entryTimeY = Double.NEGATIVE_INFINITY;
			exitTimeY = Double.POSITIVE_INFINITY;
		}
		return new Point2D[] { new Point2D(entryTimeX, entryTimeY), new Point2D(exitTimeX, exitTimeY) };
	}
}
