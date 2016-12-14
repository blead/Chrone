/*
 * @author Thad Benjaponpitak
 */
package components;

import javafx.scene.shape.Shape;

public class DoorComponent extends CodeComponent {
	private Shape collisionShape;
	private boolean isOpen;

	public DoorComponent(Shape collisionShape, char code) {
		super(code);
		this.collisionShape = collisionShape;
		isOpen = false;
	}

	public Shape getCollisionShape() {
		return collisionShape;
	}

	public void setCollisionShape(Shape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
