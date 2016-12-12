package renderables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import utils.Direction;

public class RenderableSwitchBlock extends RenderableRectangle {
	private RenderableRectangle block, edgeX, edgeY;
	private RenderableCircle core;
	private Paint secondaryColor;
	private Direction direction;
	private boolean isActive;

	public RenderableSwitchBlock(double width, double height, Paint secondaryColor) {
		super(width, height);
		this.secondaryColor = secondaryColor;
		direction = Direction.NONE;
		isActive = false;
		block = new RenderableRectangle(width, height);
		edgeX = new RenderableRectangle(width / 8, height);
		edgeY = new RenderableRectangle(width, height / 8);
		core = new RenderableCircle(Math.min(width, height) / 8);
	}

	public Paint getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Paint secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		block.render(gc, x, y);
		gc.setFill(secondaryColor);
		core.render(gc, x + (getWidth() - core.getWidth()) / 2, y + (getHeight() - core.getHeight()) / 2);
		if (isActive) {
			switch (direction) {
			case UP:
				edgeY.render(gc, x, y);
				break;
			case DOWN:
				edgeY.render(gc, x, y + getHeight() - edgeY.getHeight());
				break;
			case LEFT:
				edgeX.render(gc, x, y);
				break;
			case RIGHT:
				edgeX.render(gc, x + getWidth() - edgeX.getWidth(), y);
				break;
			default:
				// nothing
			}
		}
	}
}
