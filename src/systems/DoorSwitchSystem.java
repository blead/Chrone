package systems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import components.CollisionComponent;
import components.ContactComponent;
import components.DoorComponent;
import components.JumpableSurfaceComponent;
import components.RenderComponent;
import components.SwitchComponent;
import core.AudioManager;
import core.EntityManager;
import entities.DoorBlock;
import entities.Entity;
import renderables.RenderableDoorBlock;
import renderables.RenderableSwitchBlock;
import utils.ComponentNotFoundException;
import utils.Direction;

public class DoorSwitchSystem extends EntitySystem {
	public DoorSwitchSystem() {
		super(26);
	}

	@Override
	public void update(double deltaTime) {
		List<Entity> entities = EntityManager.getInstance().getEntities();
		Set<Character> codes = getCodes(entities);
		setDoors(entities, codes);
	}

	private Set<Character> getCodes(List<Entity> entities) {
		Set<Character> codes = new HashSet<>();
		for (Entity entity : entities) {
			try {
				ContactComponent contactComponent = (ContactComponent) entity.getComponent(ContactComponent.class);
				SwitchComponent switchComponent = (SwitchComponent) entity.getComponent(SwitchComponent.class);
				RenderComponent renderComponent = (RenderComponent) entity.getComponent(RenderComponent.class);
				RenderableSwitchBlock switchBlock = (RenderableSwitchBlock) renderComponent.getShape();
				if (contactComponent.isContact()) {
					codes.add(switchComponent.getCode());
					switchBlock.setActive(true);
					if (contactComponent.isContact(Direction.UP))
						switchBlock.setDirection(Direction.UP);
					else if (contactComponent.isContact(Direction.DOWN))
						switchBlock.setDirection(Direction.DOWN);
					else if (contactComponent.isContact(Direction.LEFT))
						switchBlock.setDirection(Direction.LEFT);
					else if (contactComponent.isContact(Direction.RIGHT))
						switchBlock.setDirection(Direction.RIGHT);
					else
						throw new UnsupportedOperationException();
				} else {
					switchBlock.setActive(false);
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
		return codes;
	}

	private void setDoors(List<Entity> entities, Set<Character> codes) {
		for (Entity entity : entities) {
			try {
				DoorComponent doorComponent = (DoorComponent) entity.getComponent(DoorComponent.class);
				RenderComponent renderComponent = (RenderComponent) entity.getComponent(RenderComponent.class);
				RenderableDoorBlock doorBlock = (RenderableDoorBlock) renderComponent.getShape();
				if (codes.contains(doorComponent.getCode())) {
					// open
					if (!doorComponent.isOpen()) {
						AudioManager.getInstance().remove(AudioManager.DOOR_CLOSE);
						AudioManager.getInstance().uniquePlay(AudioManager.DOOR_OPEN);
						doorComponent.setOpen(true);
						entity.remove(CollisionComponent.class);
						entity.remove(JumpableSurfaceComponent.class);
						renderComponent.setAlpha(DoorBlock.ALPHA);
						doorBlock.setOpen(true);
					}
				} else {
					// close
					if (doorComponent.isOpen()) {
						AudioManager.getInstance().remove(AudioManager.DOOR_OPEN);
						AudioManager.getInstance().uniquePlay(AudioManager.DOOR_CLOSE);
						doorComponent.setOpen(false);
						entity.add(new CollisionComponent(doorComponent.getCollisionShape()),
								new JumpableSurfaceComponent());
						renderComponent.setAlpha(1);
						doorBlock.setOpen(false);
					}
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
