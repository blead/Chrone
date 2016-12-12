package utils;

import javafx.scene.canvas.GraphicsContext;

public class RenderableDiamond implements Renderable {
	private double width, height;

	public RenderableDiamond(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		gc.fillPolygon(new double[] { x, x + width / 2, x + width, x + width / 2 },
				new double[] { y + height / 2, y, y + height / 2, y + height }, 4);
	}
}
