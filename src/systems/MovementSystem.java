package systems;

import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import javafx.geometry.Point2D;
import utils.ComponentNotFoundException;

public class MovementSystem extends EntitySystem {
	public MovementSystem() {
		super(22);
	}

	@Override
	public void update(double deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
				positionComponent.setPosition(
						positionComponent.getPosition().add(velocityComponent.getVelocity().multiply(deltaTime)));
				velocityComponent.setVelocity(Point2D.ZERO);
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
