package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import intents.Intent;
import javafx.scene.input.KeyCode;

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
		synchronized (pressed) {
			return new HashSet<>(pressed);
		}
	}

	public boolean isPressed(KeyCode keyCode) {
		synchronized (pressed) {
			return pressed.contains(keyCode);
		}
	}

	public void setPressed(KeyCode keyCode, boolean isPressed) {
		if (isPressed) {
			boolean isTriggered = false;
			synchronized (pressed) {
				isTriggered = pressed.add(keyCode);
			}
			if (isTriggered) {
				synchronized (triggered) {
					triggered.add(keyCode);
				}
			}
		} else {
			synchronized (pressed) {
				pressed.remove(keyCode);
			}
		}
	}

	public Set<KeyCode> getTriggered() {
		synchronized (triggered) {
			return new HashSet<>(triggered);
		}
	}

	public boolean isTriggered(KeyCode keyCode) {
		synchronized (triggered) {
			return triggered.contains(keyCode);
		}
	}

	public void clearTriggered() {
		synchronized (triggered) {
			triggered.clear();
		}
	}

	public Intent getPressedIntent(KeyCode keyCode) {
		synchronized (pressedIntents) {
			return pressedIntents.get(keyCode);
		}
	}

	public void setPressedIntent(KeyCode keyCode, Intent intent) {
		synchronized (pressedIntents) {
			pressedIntents.put(keyCode, intent);
		}
	}

	public Intent getTriggeredIntent(KeyCode keyCode) {
		synchronized (triggeredIntents) {
			return triggeredIntents.get(keyCode);
		}
	}

	public void setTriggeredIntent(KeyCode keyCode, Intent intent) {
		synchronized (triggeredIntents) {
			triggeredIntents.put(keyCode, intent);
		}
	}
}
