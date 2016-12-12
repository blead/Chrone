package components;

import java.util.HashMap;

import intents.Intent;
import javafx.scene.input.KeyCode;

public class InputComponent extends Component {
	private HashMap<KeyCode, Intent> pressedIntents;
	private HashMap<KeyCode, Intent> triggeredIntents;

	public InputComponent() {
		pressedIntents = new HashMap<>();
		triggeredIntents = new HashMap<>();
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
