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
import javafx.scene.canvas.Canvas;
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
		Point2D viewportPosition = getViewportPosition(anchorPositions, Main.WIDTH, Main.HEIGHT,
				LevelManager.getInstance().getLevel().getWidth(), LevelManager.getInstance().getLevel().getHeight());
		Canvas background = Main.getInstance().getBackground(), game = Main.getInstance().getGame();
		if (viewportPosition != null) {
			Point2D backgroundTranslate = getBackgroundTranslate(viewportPosition, Main.WIDTH, Main.HEIGHT,
					background.getWidth(), background.getHeight());
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					background.setTranslateX(backgroundTranslate.getX());
					background.setTranslateY(backgroundTranslate.getY());
					game.setTranslateX(-viewportPosition.getX());
					game.setTranslateY(-viewportPosition.getY());
				}
			});
		}
	}

	private Point2D getAnchorPosition(Renderable shape, Point2D offset, Point2D position) {
		return position.add(offset).add(shape.getWidth() / 2, shape.getHeight() / 2);
	}

	private Point2D getViewportPosition(Queue<PrioritizedPoint2D> anchorPositions, double viewportWidth,
			double viewportHeight, double levelWidth, double levelHeight) {
		Point2D viewportPosition;
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
						&& newMaxPosition.getY() - newMinPosition.getY() < viewportHeight && newMaxPosition.getX() >= 0
						&& newMinPosition.getX() <= levelWidth && newMaxPosition.getY() >= 0
						&& newMinPosition.getY() <= levelHeight) {
					minPosition = newMinPosition;
					maxPosition = newMaxPosition;
				} else {
					break;
				}
			}
			if (minPosition.getX() != Double.MAX_VALUE && minPosition.getY() != Double.MAX_VALUE
					&& maxPosition.getX() != Double.MIN_VALUE && maxPosition.getY() != Double.MIN_VALUE) {
				Point2D offset = new Point2D((viewportWidth - (maxPosition.getX() - minPosition.getX())) / 2,
						(viewportHeight - (maxPosition.getY() - minPosition.getY())) / 2);
				viewportPosition = minPosition.subtract(offset);
				if (viewportWidth > levelWidth) {
					viewportPosition = viewportPosition.subtract((viewportWidth - levelWidth) / 2, 0);
				} else {
					if (viewportPosition.getX() < 0)
						viewportPosition = viewportPosition.subtract(viewportPosition.getX(), 0);
					if (viewportPosition.getX() + viewportWidth > levelWidth)
						viewportPosition = viewportPosition
								.subtract(viewportPosition.getX() + viewportWidth - levelWidth, 0);
				}
				if (viewportHeight > levelHeight) {
					viewportPosition = viewportPosition.subtract(0, (viewportHeight - levelHeight) / 2);
				} else {
					if (viewportPosition.getY() < 0)
						viewportPosition = viewportPosition.subtract(0, viewportPosition.getY());
					if (viewportPosition.getY() + viewportHeight > levelHeight)
						viewportPosition = viewportPosition.subtract(0,
								viewportPosition.getY() + viewportHeight - levelHeight);
				}
				return viewportPosition;
			}
		}
		return null;
	}

	private Point2D getBackgroundTranslate(Point2D viewportPosition, double viewportWidth, double viewportHeight,
			double backgroundWidth, double backgroundHeight) {
		Point2D offset = new Point2D(backgroundWidth - viewportWidth, backgroundHeight - viewportHeight).multiply(0.5),
				ratio = new Point2D(viewportWidth / backgroundWidth, viewportHeight / backgroundHeight);
		double translateX, translateY;
		if (ratio.getX() >= 1)
			translateX = viewportPosition.getX() - offset.getX();
		else
			translateX = -ratio.getX() * viewportPosition.getX() - offset.getX();
		if (ratio.getY() >= 1)
			translateY = viewportPosition.getY() - offset.getY();
		else
			translateY = -ratio.getY() * viewportPosition.getY() - offset.getY();
		return new Point2D(translateX, translateY);
	}
}
