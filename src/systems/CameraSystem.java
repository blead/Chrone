package systems;

import java.util.PriorityQueue;
import java.util.Queue;

import components.CameraComponent;
import components.PositionComponent;
import components.RenderComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import core.LevelManager;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import main.Main;
import utils.ComponentNotFoundException;
import utils.PrioritizedPoint2D;
import utils.Renderable;

public class CameraSystem extends EntitySystem {
	public CameraSystem() {
		super(50);
	}

	@Override
	public void update(double deltaTime) {
		Queue<PrioritizedPoint2D> anchorPositions = new PriorityQueue<>();
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				RenderComponent renderComponent = (RenderComponent) entity.getComponent(RenderComponent.class);
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				CameraComponent cameraComponent = (CameraComponent) entity.getComponent(CameraComponent.class);
				anchorPositions.add(new PrioritizedPoint2D(getAnchorPosition(renderComponent.getShape(),
						renderComponent.getOffset(), positionComponent.getPosition()), cameraComponent.getPriority()));
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
		Point2D viewportPosition = getAdjustedViewportPosition(
				getViewportPosition(anchorPositions, Main.WIDTH, Main.HEIGHT), Main.WIDTH, Main.HEIGHT,
				LevelManager.getInstance().getLevel().getWidth(), LevelManager.getInstance().getLevel().getHeight());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getInstance().getGameRoot().setTranslateX(-viewportPosition.getX());
				Main.getInstance().getGameRoot().setTranslateY(-viewportPosition.getY());
			}
		});
	}

	private Point2D getAnchorPosition(Renderable shape, Point2D offset, Point2D position) {
		return position.add(offset).add(shape.getWidth() / 2, shape.getHeight() / 2);
	}

	private Point2D getViewportPosition(Queue<PrioritizedPoint2D> anchorPositions, double viewportWidth,
			double viewportHeight) {
		if (anchorPositions.size() > 0) {
			Point2D minPosition = new Point2D(Double.MAX_VALUE, Double.MAX_VALUE),
					maxPosition = new Point2D(Double.MIN_VALUE, Double.MIN_VALUE);
			while (!anchorPositions.isEmpty()) {
				Point2D newPosition = anchorPositions.poll().getPoint2D(),
						newMinPosition = new Point2D(Math.min(minPosition.getX(), newPosition.getX()),
								Math.min(minPosition.getY(), newPosition.getY())),
						newMaxPosition = new Point2D(Math.max(maxPosition.getX(), newPosition.getX()),
								Math.max(maxPosition.getY(), newPosition.getY()));
				if (newMaxPosition.getX() - newMinPosition.getX() < viewportWidth
						&& newMaxPosition.getY() - newMinPosition.getY() < viewportHeight) {
					minPosition = newMinPosition;
					maxPosition = newMaxPosition;
				} else {
					break;
				}
			}
			Point2D offset = new Point2D((viewportWidth - (maxPosition.getX() - minPosition.getX())) / 2,
					(viewportHeight - (maxPosition.getY() - minPosition.getY())) / 2);
			return minPosition.subtract(offset);
		} else {
			return Point2D.ZERO;
		}
	}

	private Point2D getAdjustedViewportPosition(Point2D viewportPosition, double viewportWidth, double viewportHeight,
			double levelWidth, double levelHeight) {
		if (viewportWidth > levelWidth) {
			viewportPosition = viewportPosition.subtract((viewportWidth - levelWidth) / 2, 0);
		} else {
			if (viewportPosition.getX() < 0)
				viewportPosition = viewportPosition.subtract(viewportPosition.getX(), 0);
			if (viewportPosition.getX() + viewportWidth > levelWidth)
				viewportPosition = viewportPosition.subtract(viewportPosition.getX() + viewportWidth - levelWidth, 0);
		}
		if (viewportHeight > levelHeight) {
			viewportPosition = viewportPosition.subtract(0, (viewportHeight - levelHeight) / 2);
		} else {
			if (viewportPosition.getY() < 0)
				viewportPosition = viewportPosition.subtract(0, viewportPosition.getY());
			if (viewportPosition.getY() + viewportHeight > levelHeight)
				viewportPosition = viewportPosition.subtract(0, viewportPosition.getY() + viewportHeight - levelHeight);
		}
		return viewportPosition;
	}
}
