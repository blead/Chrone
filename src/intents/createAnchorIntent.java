package intents;

import components.PositionComponent;
import core.ChroneManager;
import entities.Entity;
import utils.ComponentNotFoundException;

public class createAnchorIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		try {
			PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
			ChroneManager.getInstance().createAnchor(positionComponent.getPosition());
		} catch (ComponentNotFoundException e) {
			throw new IllegalArgumentException();
		}
	}
}
