package components;

import java.util.HashMap;

import core.Component;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;
import utils.Intent;

public class InputComponent extends Component {
	private HashMap<KeyCode, Intent> intents;

	@SafeVarargs
	public InputComponent(Pair<KeyCode, Intent>... intentPairs) {
		intents = new HashMap<>();
		for (Pair<KeyCode, Intent> intentPair : intentPairs) {
			intents.put(intentPair.getKey(), intentPair.getValue());
		}
	}

	public Intent getIntent(KeyCode keyCode) {
		return intents.get(keyCode);
	}

	public void setIntent(KeyCode keyCode, Intent intent) {
		intents.put(keyCode, intent);
	}
}
