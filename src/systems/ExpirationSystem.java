package systems;

import components.ExpirationComponent;
import core.AudioManager;
import core.EntityManager;
import entities.DynamicChrone;
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
				if (expirationComponent.getExpirationTime() <= 0) {
					EntityManager.getInstance().remove(entity);
					if (entity instanceof DynamicChrone) {
						AudioManager.getInstance().uniquePlay(AudioManager.CHRONE_REMOVE);
						AudioManager.getInstance().remove(AudioManager.CHRONE_REMOVE);
					}
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
