package utils;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class RenderableRectangle implements Renderable {
	private double width, height;

	public RenderableRectangle(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void draw(GraphicsContext gc,double x,double y) {
		gc.fillRect(x, y, width, height);
	}
}
