/*
 * @author Thad Benjaponpitak
 */
package renderables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RenderableGoalBlock extends RenderableBlock {
	private RenderableStar core;

	public RenderableGoalBlock(double width, double height, Paint secondaryColor) {
		super(width, height, secondaryColor);
		core = new RenderableStar(Math.min(width, height) / 2);
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		super.render(gc, x, y);
		gc.setFill(getSecondaryColor());
		core.render(gc, x + (getWidth() - core.getWidth()) / 2, y + (getHeight() - core.getHeight()) / 2);
	}
}
