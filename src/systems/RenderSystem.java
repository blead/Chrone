package systems;

import components.PositionComponent;
import components.RenderComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import javafx.scene.canvas.GraphicsContext;
import utils.ComponentNotFoundException;

public class RenderSystem extends EntitySystem {
	@Override
	public void update(float deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				RenderComponent renderComponent = (RenderComponent) entity.getComponent(RenderComponent.class);
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				/* TODO: get GraphicsContext */
				GraphicsContext gc = null;
				gc.setFill(renderComponent.getColor());
				renderComponent.getShape().draw(gc, positionComponent.getPosition().getX(),
						positionComponent.getPosition().getY());
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
