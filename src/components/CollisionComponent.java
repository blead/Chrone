package components;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.shape.Shape;
import utils.Direction;

public class CollisionComponent extends Component {
	private Shape collisionShape;
	private Set<Direction> isColliding;

	public CollisionComponent(Shape collisionShape) {
		this.collisionShape = collisionShape;
		isColliding = new HashSet<>();
	}

	public Shape getCollisionShape() {
		return collisionShape;
	}

	public void setCollisionShape(Shape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public boolean isColliding(Direction direction) {
		return isColliding.contains(direction);
	}

	public void setColliding(Direction direction) {
		isColliding.add(direction);
	}

	public void clearColliding() {
		isColliding.clear();
	}
}
