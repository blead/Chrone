package renderables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RenderableDoorBlock extends RenderableRectangle {
	private RenderableRectangle block;
	private RenderableDiamond core;
	private Paint secondaryColor;
	private boolean isOpen;

	public RenderableDoorBlock(double width, double height, Paint secondaryColor) {
		super(width, height);
		this.secondaryColor = secondaryColor;
		isOpen = false;
		block = new RenderableRectangle(width, height);
		core = new RenderableDiamond(width, height);
	}

	public Paint getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Paint secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		if (isOpen)
			gc.setFill(secondaryColor);
		block.render(gc, x, y);
		if (!isOpen) {
			gc.setFill(secondaryColor);
			core.render(gc, x, y);
		}
	}
}
