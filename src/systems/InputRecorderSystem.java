/*
 * @author Thad Benjaponpitak
 */
package systems;

import components.InputRecorderComponent;
import core.EntityManager;
import core.InputManager;
import entities.Entity;
import utils.ComponentNotFoundException;

public class InputRecorderSystem extends EntitySystem {
	public InputRecorderSystem() {
		super(12);
	}

	@Override
	public void update(double deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				InputRecorderComponent inputRecorderComponent = (InputRecorderComponent) entity
						.getComponent(InputRecorderComponent.class);
				if (inputRecorderComponent.getDuration() > 0) {
					inputRecorderComponent.addPressedRecord(InputManager.getInstance().getPressed());
					inputRecorderComponent.addTriggeredRecord(InputManager.getInstance().getTriggered());
					inputRecorderComponent.decreaseDuration();
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
