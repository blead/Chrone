/*
 * @author Thad Benjaponpitak
 */
package components;

import javafx.scene.shape.Shape;

public class CollisionComponent extends Component {
	private Shape collisionShape;

	public CollisionComponent(Shape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public Shape getCollisionShape() {
		return collisionShape;
	}

	public void setCollisionShape(Shape collisionShape) {
		this.collisionShape = collisionShape;
	}
}
