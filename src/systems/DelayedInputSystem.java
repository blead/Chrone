package systems;

import components.DelayedInputComponent;
import components.InputRecordComponent;
import core.EntityManager;
import entities.Entity;
import intents.Intent;
import javafx.scene.input.KeyCode;
import utils.ComponentNotFoundException;

public class DelayedInputSystem extends EntitySystem {
	public DelayedInputSystem() {
		super(16);
	}

	@Override
	public void update(double deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				InputRecordComponent inputRecordComponent = (InputRecordComponent) entity
						.getComponent(InputRecordComponent.class);
				DelayedInputComponent delayedInputComponent = (DelayedInputComponent) entity
						.getComponent(DelayedInputComponent.class);
				if (inputRecordComponent.isAvailable()) {
					for (KeyCode keyCode : inputRecordComponent.getPressed()) {
						Intent intent = delayedInputComponent.getPressedIntent(keyCode);
						if (intent != null)
							intent.handle(entity);
					}
					for (KeyCode keyCode : inputRecordComponent.getTriggered()) {
						Intent intent = delayedInputComponent.getTriggeredIntent(keyCode);
						if (intent != null)
							intent.handle(entity);
					}
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
