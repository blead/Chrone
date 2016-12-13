package renderables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RenderableDoorBlock extends RenderableBlock {
	private RenderableDiamond core;
	private boolean isOpen;

	public RenderableDoorBlock(double width, double height, Paint secondaryColor) {
		super(width, height, secondaryColor);
		isOpen = false;
		core = new RenderableDiamond(width, height);
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
			gc.setFill(getSecondaryColor());
		super.render(gc, x, y);
		if (!isOpen) {
			gc.setFill(getSecondaryColor());
			core.render(gc, x, y);
		}
	}
}
