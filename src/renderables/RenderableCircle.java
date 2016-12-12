package renderables;

import javafx.scene.canvas.GraphicsContext;

public class RenderableCircle implements Renderable {
	private double radius;

	public RenderableCircle(double radius) {
		this.radius = radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public double getWidth() {
		return radius * 2;
	}

	@Override
	public double getHeight() {
		return radius * 2;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		gc.fillOval(x, y, radius * 2, radius * 2);
	}

}