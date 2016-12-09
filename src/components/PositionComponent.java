package components;

import core.Component;
import javafx.geometry.Point2D;

public class PositionComponent extends Component {
	private Point2D position;

	public PositionComponent() {
		position = Point2D.ZERO;
	}

	public PositionComponent(Point2D position) {
		this.position = position;
	}

	public PositionComponent(double x, double y) {
		position = new Point2D(x, y);
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}
}
