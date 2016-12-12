package systems;

import components.ExpirationComponent;
import core.EntityManager;
import entities.Entity;
import utils.ComponentNotFoundException;

public class ExpirationSystem extends EntitySystem {
	public ExpirationSystem() {
		super(40);
	}

	@Override
	public void update(double deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				ExpirationComponent expirationComponent = (ExpirationComponent) entity
						.getComponent(ExpirationComponent.class);
				expirationComponent.decreaseExpirationTime(deltaTime);
				if (expirationComponent.getExpirationTime() <= 0)
					EntityManager.getInstance().remove(entity);
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
