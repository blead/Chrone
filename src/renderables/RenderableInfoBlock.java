/*
 * @author Thad Benjaponpitak
 */
package renderables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RenderableInfoBlock extends RenderableBlock {
	private RenderableRectangle stem;
	private RenderableCircle tittle;
	private double totalHeight;

	public RenderableInfoBlock(double width, double height, Paint secondaryColor) {
		super(width, height, secondaryColor);
		stem = new RenderableRectangle(width / 6, height / 3);
		tittle = new RenderableCircle(width / 12);
		totalHeight = stem.getHeight() + tittle.getHeight() * 2;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		super.render(gc, x, y);
		gc.setFill(getSecondaryColor());
		stem.render(gc, x + (getWidth() - stem.getWidth()) / 2,
				y + (getHeight() - totalHeight) / 2 + tittle.getHeight() * 2);
		tittle.render(gc, x + (getWidth() - stem.getWidth()) / 2, y + (getHeight() - totalHeight) / 2);
	}
}
