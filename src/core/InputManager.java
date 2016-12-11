package core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyCode;

public class InputManager {
	private static InputManager instance = null;
	private HashSet<KeyCode> pressed, triggered;

	private InputManager() {
		pressed = new HashSet<>();
		triggered = new HashSet<>();
	}

	public static InputManager getInstance() {
		if (instance == null)
			instance = new InputManager();
		return instance;
	}

	synchronized public Set<KeyCode> getPressed() {
		return Collections.unmodifiableSet(pressed);
	}

	public boolean isPressed(KeyCode keyCode) {
		return pressed.contains(keyCode);
	}

	synchronized public void setPressed(KeyCode keyCode, boolean isPressed) {
		if (isPressed) {
			if (pressed.add(keyCode)) {
				triggered.add(keyCode);
			} else {
				triggered.remove(keyCode);
			}
		} else {
			pressed.remove(keyCode);
		}
	}

	synchronized public Set<KeyCode> getTriggered() {
		return Collections.unmodifiableSet(triggered);
	}

	public boolean isTriggered(KeyCode keyCode) {
		return triggered.contains(keyCode);
	}
}
