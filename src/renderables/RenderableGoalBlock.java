package renderables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RenderableGoalBlock extends RenderableRectangle {
	private RenderableRectangle block;
	private RenderableStar core;
	private Paint secondaryColor;

	public RenderableGoalBlock(double width, double height, Paint secondaryColor) {
		super(width, height);
		this.secondaryColor = secondaryColor;
		block = new RenderableRectangle(width, height);
		core = new RenderableStar(Math.min(width, height) / 2);
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
		gc.setFill(secondaryColor);
		core.render(gc, x + (getWidth() - core.getWidth()) / 2, y + (getHeight() - core.getHeight()) / 2);
	}
}
