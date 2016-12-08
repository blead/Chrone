package components;

import core.Component;
import javafx.geometry.Point2D;

public class VelocityComponent implements Component {
	private Point2D velocity;

	public VelocityComponent() {
		velocity = Point2D.ZERO;
	}

	public VelocityComponent(double x, double y) {
		velocity = new Point2D(x, y);
	}

	public VelocityComponent(Point2D velocity) {
		this.velocity = velocity;
	}

	public Point2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}
}
