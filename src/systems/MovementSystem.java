package systems;

import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import utils.ComponentNotFoundException;

public class MovementSystem extends EntitySystem {
	@Override
	public void update(float deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
				positionComponent.setPosition(positionComponent.getPosition().add(velocityComponent.getVelocity()));
				/* TODO: .multiply(deltaTime * UNIT_PER_TIME); */
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
