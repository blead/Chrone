package systems;

import java.util.List;

import components.CollisionComponent;
import components.PositionComponent;
import core.EntityManager;
import entities.Entity;
import utils.CollisionBox;
import utils.ComponentNotFoundException;
import utils.Direction;

public class ContactSystem extends EntitySystem {
	public ContactSystem() {
		super(24);
	}

	@Override
	public void update(double deltaTime) {
		List<Entity> entities = EntityManager.getInstance().getEntities();
		for (Entity entity : entities) {
			try {
				CollisionComponent collisionComponent = (CollisionComponent) entity
						.getComponent(CollisionComponent.class);
				collisionComponent.clearColliding();
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
		for (int i = 0; i < entities.size() - 1; i++) {
			Entity entity = entities.get(i);
			try {
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				CollisionComponent collisionComponent = (CollisionComponent) entity
						.getComponent(CollisionComponent.class);
				CollisionBox collisionBox = new CollisionBox(positionComponent.getPosition(),
						collisionComponent.getCollisionShape());
				for (int j = i + 1; j < entities.size(); j++) {
					Entity other = entities.get(j);
					try {
						PositionComponent otherPositionComponent = (PositionComponent) other
								.getComponent(PositionComponent.class);
						CollisionComponent otherCollisionComponent = (CollisionComponent) other
								.getComponent(CollisionComponent.class);
						CollisionBox otherCollisionBox = new CollisionBox(otherPositionComponent.getPosition(),
								otherCollisionComponent.getCollisionShape());
						// check for contacts
						if (collisionBox.intersectsY(otherCollisionBox)) {
							Direction contactDirectionX = collisionBox.getContactDirectionX(otherCollisionBox);
							if (contactDirectionX != Direction.NONE) {
								collisionComponent.setColliding(contactDirectionX);
								otherCollisionComponent.setColliding(contactDirectionX.rotate180());
							}
						}
						if (collisionBox.intersectsX(otherCollisionBox)) {
							Direction contactDirectionY = collisionBox.getContactDirectionY(otherCollisionBox);
							if (contactDirectionY != Direction.NONE) {
								collisionComponent.setColliding(contactDirectionY);
								otherCollisionComponent.setColliding(contactDirectionY.rotate180());
							}
						}
					} catch (ComponentNotFoundException e) {
						continue;
					}
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
