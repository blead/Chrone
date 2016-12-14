/*
 * @author Thad Benjaponpitak
 */
package renderables;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RenderableText implements Renderable {
	private String text;
	private Font font;
	private double width, height;

	public RenderableText(String text, Font font) {
		this.text = text;
		this.font = font;
		Point2D textSize = getTextSize(text, font);
		width = textSize.getX();
		height = textSize.getY();
	}

	private Point2D getTextSize(String string, Font font) {
		Text text = new Text(string);
		text.setFont(font);
		Bounds bounds = text.getBoundsInLocal();
		Rectangle rectangle = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		bounds = Shape.intersect(text, rectangle).getBoundsInLocal();
		return new Point2D(bounds.getWidth(), bounds.getHeight());
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		gc.setFont(font);
		gc.fillText(text, x, y);
	}
}