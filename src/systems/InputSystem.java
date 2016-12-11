package systems;

import components.InputComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import core.InputManager;
import javafx.scene.input.KeyCode;
import utils.ComponentNotFoundException;
import utils.Intent;

public class InputSystem extends EntitySystem {
	public InputSystem() {
		super(12);
	}

	@Override
	public void update(double deltaTime) {
		// entity-modifying intents
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				InputComponent inputComponent = (InputComponent) entity.getComponent(InputComponent.class);
				for (KeyCode keyCode : InputManager.getInstance().getPressed()) {
					Intent intent = inputComponent.getPressedIntent(keyCode);
					if (intent != null)
						intent.handle(entity);
				}
				for (KeyCode keyCode : InputManager.getInstance().getTriggered()) {
					Intent intent = inputComponent.getTriggeredIntent(keyCode);
					if (intent != null)
						intent.handle(entity);
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
		// global Intents
		for (KeyCode keyCode : InputManager.getInstance().getPressed()) {
			Intent intent = InputManager.getInstance().getPressedIntent(keyCode);
			if (intent != null)
				intent.handle(null);
		}
		for (KeyCode keyCode : InputManager.getInstance().getTriggered()) {
			Intent intent = InputManager.getInstance().getTriggeredIntent(keyCode);
			if (intent != null)
				intent.handle(null);
		}
	}
}
