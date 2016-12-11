package core;

import java.util.HashSet;

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

	public boolean isPressed(KeyCode keyCode) {
		return pressed.contains(keyCode);
	}

	public void setPressed(KeyCode keyCode, boolean isPressed) {
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

	public boolean isTriggered(KeyCode keyCode) {
		return triggered.contains(keyCode);
	}
}
