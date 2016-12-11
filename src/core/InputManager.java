package core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.input.KeyCode;
import utils.Intent;

public class InputManager {
	private static InputManager instance = null;
	private Set<KeyCode> pressed, triggered;
	private Map<KeyCode, Intent> pressedIntents, triggeredIntents;

	private InputManager() {
		pressed = new HashSet<>();
		triggered = new HashSet<>();
		pressedIntents = new HashMap<>();
		triggeredIntents = new HashMap<>();
	}

	public static InputManager getInstance() {
		if (instance == null)
			instance = new InputManager();
		return instance;
	}

	public Set<KeyCode> getPressed() {
		return Collections.unmodifiableSet(pressed);
	}

	public boolean isPressed(KeyCode keyCode) {
		return pressed.contains(keyCode);
	}

	public void setPressed(KeyCode keyCode, boolean isPressed) {
		if (isPressed) {
			if (pressed.add(keyCode)) {
				triggered.add(keyCode);
			}
		} else {
			pressed.remove(keyCode);
		}
	}

	public Set<KeyCode> getTriggered() {
		return Collections.unmodifiableSet(triggered);
	}

	public boolean isTriggered(KeyCode keyCode) {
		return triggered.contains(keyCode);
	}

	public void clearTriggered() {
		triggered.clear();
	}

	public Intent getPressedIntent(KeyCode keyCode) {
		return pressedIntents.get(keyCode);
	}

	public void setPressedIntent(KeyCode keyCode, Intent intent) {
		pressedIntents.put(keyCode, intent);
	}

	public Intent getTriggeredIntent(KeyCode keyCode) {
		return triggeredIntents.get(keyCode);
	}

	public void setTriggeredIntent(KeyCode keyCode, Intent intent) {
		triggeredIntents.put(keyCode, intent);
	}
}
