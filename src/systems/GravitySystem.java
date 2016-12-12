package systems;

import components.GravityComponent;
import components.VelocityComponent;
import core.EntityManager;
import core.LevelManager;
import entities.Entity;
import utils.ComponentNotFoundException;

public class GravitySystem extends EntitySystem {
	public GravitySystem() {
		super(10);
	}

	@Override
	public void update(double deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
				GravityComponent gravityComponent = (GravityComponent) entity.getComponent(GravityComponent.class);
				velocityComponent.setVelocity(
						velocityComponent.getVelocity().add(LevelManager.getInstance().getLevel().getGravity()));
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}

}
