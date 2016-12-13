package renderables;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import utils.Direction;

public class RenderableSwitchBlock extends RenderableRectangle {
	private RenderableRectangle block, edgeX, edgeY;
	private RenderableCircle core;
	private Paint secondaryColor;
	private Set<Direction> directions;
	private boolean isActive;

	public RenderableSwitchBlock(double width, double height, Paint secondaryColor) {
		super(width, height);
		this.secondaryColor = secondaryColor;
		directions = new HashSet<>();
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

	public boolean isDirectionActive(Direction direction) {
		return directions.contains(direction);
	}

	public void setDirection(Direction direction) {
		setDirection(direction, true);
	}

	public void setDirection(Direction direction, boolean isActive) {
		if (isActive)
			directions.add(direction);
		else
			directions.remove(direction);
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
			if (isDirectionActive(Direction.UP))
				edgeY.render(gc, x, y);
			if (isDirectionActive(Direction.DOWN))
				edgeY.render(gc, x, y + getHeight() - edgeY.getHeight());
			if (isDirectionActive(Direction.LEFT))
				edgeX.render(gc, x, y);
			if (isDirectionActive(Direction.RIGHT))
				edgeX.render(gc, x + getWidth() - edgeX.getWidth(), y);
		}
	}
}
