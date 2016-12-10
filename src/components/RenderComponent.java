package components;

import core.Component;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import utils.Renderable;

public class RenderComponent extends Component {
	private Renderable shape;
	private Paint color;
	private Point2D offset;

	public RenderComponent(Renderable shape, Paint color) {
		this(shape, color, 0, 0);
	}

	public RenderComponent(Renderable shape, Paint color, Point2D offset) {
		this(shape, color, offset.getX(), offset.getY());
	}

	public RenderComponent(Renderable shape, Paint color, double offsetX, double offsetY) {
		this.shape = shape;
		this.color = color;
		offset = new Point2D(offsetX, offsetY);
	}

	public Renderable getShape() {
		return shape;
	}

	public void setShape(Renderable shape) {
		this.shape = shape;
	}

	public Paint getColor() {
		return color;
	}

	public void setColor(Paint color) {
		this.color = color;
	}

	public Point2D getOffset() {
		return offset;
	}

	public void setOffset(Point2D offset) {
		this.offset = offset;
	}
}
