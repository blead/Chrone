package systems;

import components.PositionComponent;
import core.EntityManager;
import core.LevelManager;
import core.ToastManager;
import entities.Entity;
import entities.Player;
import utils.ComponentNotFoundException;

public class GarbageCollectionSystem extends EntitySystem {
	public static final double HEIGHT_LIMIT = 1.5;

	public GarbageCollectionSystem() {
		super(60);
	}

	@Override
	public void update(double deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				if (positionComponent.getPosition().getY() > LevelManager.getInstance().getLevel().getHeight()
						* GarbageCollectionSystem.HEIGHT_LIMIT) {
					EntityManager.getInstance().remove(entity);
					if (entity instanceof Player)
						ToastManager.getInstance().show("Press R to restart level");
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
