/*
 * @author Thad Benjaponpitak
 */
package renderables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class RenderableBlock extends RenderableRectangle {
	private RenderableRectangle block;
	private Paint secondaryColor;

	public RenderableBlock(double width, double height, Paint secondaryColor) {
		super(width, height);
		this.secondaryColor = secondaryColor;
		block = new RenderableRectangle(width, height);
	}

	public Paint getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Paint secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		block.render(gc, x, y);
	}
}
