package components;

import javafx.geometry.Point2D;

public class VelocityComponent extends Component {
	private Point2D velocity;

	public VelocityComponent() {
		this(0, 0);
	}

	public VelocityComponent(Point2D velocity) {
		this(velocity.getX(), velocity.getY());
	}

	public VelocityComponent(double x, double y) {
		velocity = new Point2D(x, y);
	}

	public Point2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}
}
