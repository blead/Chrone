package systems;

import components.PositionComponent;
import components.RenderComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import core.LevelManager;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import main.Main;
import utils.ComponentNotFoundException;

public class RenderSystem extends EntitySystem {
	public RenderSystem() {
		super(Integer.MAX_VALUE);
	}

	@Override
	public void update(double deltaTime) {
		GraphicsContext bg = Main.getInstance().getBackground().getGraphicsContext2D(),
				gc = Main.getInstance().getGame().getGraphicsContext2D();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				bg.drawImage(LevelManager.getInstance().getBackground(), 0, 0);
				gc.clearRect(0, 0, LevelManager.getInstance().getLevel().getWidth(),
						LevelManager.getInstance().getLevel().getHeight());
			}
		});
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				RenderComponent renderComponent = (RenderComponent) entity.getComponent(RenderComponent.class);
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				Point2D drawingPosition = positionComponent.getPosition().add(renderComponent.getOffset());
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						gc.setFill(renderComponent.getColor());
						gc.setGlobalAlpha(renderComponent.getAlpha());
						renderComponent.getShape().render(gc, drawingPosition.getX(), drawingPosition.getY());
					}
				});
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
