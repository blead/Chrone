/*
 * @author Thad Benjaponpitak
 */
package systems;

import java.util.List;

import components.CollisionComponent;
import components.ContactComponent;
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
				ContactComponent contactComponent = (ContactComponent) entity.getComponent(ContactComponent.class);
				contactComponent.clearContacts();
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
		for (Entity entity : entities) {
			try {
				PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
				CollisionComponent collisionComponent = (CollisionComponent) entity
						.getComponent(CollisionComponent.class);
				ContactComponent contactComponent = (ContactComponent) entity.getComponent(ContactComponent.class);
				CollisionBox collisionBox = new CollisionBox(positionComponent.getPosition(),
						collisionComponent.getCollisionShape());
				for (Entity other : entities) {
					if (entity.equals(other) || !other.contains(contactComponent.getTargetComponent()))
						continue;
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
								contactComponent.setContact(contactDirectionX);
							}
						}
						if (collisionBox.intersectsX(otherCollisionBox)) {
							Direction contactDirectionY = collisionBox.getContactDirectionY(otherCollisionBox);
							if (contactDirectionY != Direction.NONE) {
								contactComponent.setContact(contactDirectionY);
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
