/*
 * @author Thad Benjaponpitak
 */
package renderables;

import javafx.scene.canvas.GraphicsContext;

public class RenderableRectangle implements Renderable {
	private double width, height;

	public RenderableRectangle(double width, double height) {
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
		gc.fillRect(x, y, width, height);
	}
}
