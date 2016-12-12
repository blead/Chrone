package systems;

import java.util.List;

import components.CollisionComponent;
import components.PositionComponent;
import components.VelocityComponent;
import core.EntityManager;
import entities.Entity;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import utils.Collision;
import utils.CollisionBox;
import utils.ComponentNotFoundException;
import utils.Direction;

public class CollisionSystem extends EntitySystem {
	public CollisionSystem() {
		super(20);
	}

	@Override
	public void update(double deltaTime) {
		List<Entity> entities = EntityManager.getInstance().getEntities();
		for (Entity entity : entities) {
			try {
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
				CollisionComponent collisionComponent = (CollisionComponent) entity
						.getComponent(CollisionComponent.class);
				Point2D interpolatedVelocity = velocityComponent.getVelocity().multiply(deltaTime);
				CollisionBox collisionBox = new CollisionBox(positionComponent.getPosition(),
						collisionComponent.getCollisionShape());
				Collision collision;
				do {
					CollisionBox broadPhaseArea = collisionBox.getBroadPhaseArea(interpolatedVelocity);
					collision = Collision.NONE;
					for (Entity other : entities) {
						// only test against static entities
						if (other.contains(VelocityComponent.class))
							continue;
						try {
							PositionComponent otherPositionComponent = (PositionComponent) other
									.getComponent(PositionComponent.class);
							CollisionComponent otherCollisionComponent = (CollisionComponent) other
									.getComponent(CollisionComponent.class);
							CollisionBox otherCollisionBox = new CollisionBox(otherPositionComponent.getPosition(),
									otherCollisionComponent.getCollisionShape());
							// only test inside broadPhaseArea
							if (broadPhaseArea.intersects(otherCollisionBox)) {
								// find earliest collision
								collision = Collision.min(collision,
										getCollision(collisionBox, otherCollisionBox, interpolatedVelocity));
							}
						} catch (ComponentNotFoundException e) {
							continue;
						}
					}
					// adjust velocity to the collisions
					Point2D resolvedVelocity = getResolvedVelocity(interpolatedVelocity, collision);
					velocityComponent.setVelocity(resolvedVelocity);
					// repeat until there is no collision
					interpolatedVelocity = resolvedVelocity;
				} while (collision.getTime() < 1);
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}

	private Collision getCollision(Rectangle collisionBox, Rectangle otherCollisionBox, Point2D velocity) {
		// swept axis-aligned bounding box collision detection
		Point2D[] distances = getDistances(collisionBox, otherCollisionBox, velocity),
				times = getTimes(distances, velocity);
		double entryTime = Math.max(times[0].getX(), times[0].getY()),
				exitTime = Math.min(times[1].getX(), times[1].getY());
		if (entryTime > exitTime || (times[0].getX() < 0 && times[0].getY() < 0))
			return Collision.NONE;
		if (times[0].getX() == 0 && times[0].getY() == 0)
			return Collision.CORNER;
		Direction direction;
		if (times[0].getX() > times[0].getY()) {
			if (distances[0].getX() != 0)
				direction = Direction.fromPoint2D(new Point2D(distances[0].getX(), 0));
			else
				direction = Direction.RIGHT;
		} else {
			if (distances[0].getY() != 0)
				direction = Direction.fromPoint2D(new Point2D(0, distances[0].getY()));
			else
				direction = Direction.DOWN;
		}
		return new Collision(direction, entryTime);
	}

	private Point2D getResolvedVelocity(Point2D velocity, Collision collision) {
		double resolvedVelocityX = collision.getDirection().getDx() == 0 ? velocity.getX()
				: velocity.getX() * collision.getTime();
		double resolvedVelocityY = collision.getDirection().getDy() == 0 ? velocity.getY()
				: velocity.getY() * collision.getTime();
		return new Point2D(resolvedVelocityX, resolvedVelocityY);
	}

	private Point2D[] getDistances(Rectangle a, Rectangle b, Point2D velocity) {
		// [0]: entry distance, [1]: exit distance
		double entryDistanceX, entryDistanceY, exitDistanceX, exitDistanceY;
		if (velocity.getX() > 0) {
			entryDistanceX = b.getX() - (a.getX() + a.getWidth());
			exitDistanceX = (b.getX() + b.getWidth()) - a.getX();
		} else {
			entryDistanceX = (b.getX() + b.getWidth()) - a.getX();
			exitDistanceX = b.getX() - (a.getX() + a.getWidth());
		}
		if (velocity.getY() > 0) {
			entryDistanceY = b.getY() - (a.getY() + a.getHeight());
			exitDistanceY = (b.getY() + b.getHeight()) - a.getY();
		} else {
			entryDistanceY = (b.getY() + b.getHeight()) - a.getY();
			exitDistanceY = b.getY() - (a.getY() + a.getHeight());
		}
		return new Point2D[] { new Point2D(entryDistanceX, entryDistanceY), new Point2D(exitDistanceX, exitDistanceY) };
	}

	private Point2D[] getTimes(Point2D[] distances, Point2D velocity) {
		// [0]: entry time, [1]: exit time
		double entryTimeX, exitTimeX, entryTimeY, exitTimeY;
		try {
			if (velocity.getX() == 0)
				throw new ArithmeticException();
			entryTimeX = distances[0].getX() / velocity.getX();
			exitTimeX = distances[1].getX() / velocity.getX();
		} catch (ArithmeticException e) {
			entryTimeX = Double.NEGATIVE_INFINITY;
			exitTimeX = Double.POSITIVE_INFINITY;
		}
		try {
			if (velocity.getY() == 0)
				throw new ArithmeticException();
			entryTimeY = distances[0].getY() / velocity.getY();
			exitTimeY = distances[1].getY() / velocity.getY();
		} catch (ArithmeticException e) {
			entryTimeY = Double.NEGATIVE_INFINITY;
			exitTimeY = Double.POSITIVE_INFINITY;
		}
		if (entryTimeX > 1)
			entryTimeX = Double.NEGATIVE_INFINITY;
		if (entryTimeY > 1)
			entryTimeY = Double.NEGATIVE_INFINITY;
		return new Point2D[] { new Point2D(entryTimeX, entryTimeY), new Point2D(exitTimeX, exitTimeY) };
	}
}
