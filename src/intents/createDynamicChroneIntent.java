package intents;

import components.InputRecorderComponent;
import components.PositionComponent;
import core.ChroneManager;
import entities.Entity;
import utils.ComponentNotFoundException;

public class CreateDynamicChroneIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		try {
			PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
			InputRecorderComponent inputRecorderComponent = (InputRecorderComponent) entity
					.getComponent(InputRecorderComponent.class);
			ChroneManager.getInstance().createDynamicChrone(positionComponent.getPosition(),
					inputRecorderComponent.getPressedRecord(), inputRecorderComponent.getTriggeredRecord());
		} catch (ComponentNotFoundException e) {
			throw new IllegalArgumentException();
		}
	}
}
