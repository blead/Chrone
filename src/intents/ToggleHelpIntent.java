package intents;

import java.util.ArrayList;
import java.util.List;

import core.ToastManager;
import entities.Entity;

public class ToggleHelpIntent implements Intent {
	public static final String HELP;
	private static boolean isActive;

	static {
		isActive = false;
		List<String> helpList = new ArrayList<>();
		helpList.add("Controls:");
		helpList.add(" - Arrow Keys: move");
		helpList.add(" - A: create an Anchor");
		helpList.add(" - S: create a static Chrone");
		helpList.add(" - D: create a dynamic Chrone");
		helpList.add(" - R: restart level");
		helpList.add(" - O: open a new level");
		helpList.add(" - H: toggle this help message");
		helpList.add(" - Escape: return to menu");
		HELP = String.join("\n", helpList);
	}

	@Override
	public void handle(Entity entity) {
		if (isActive)
			ToastManager.getInstance().hide(ToggleHelpIntent.HELP);
		else
			ToastManager.getInstance().show(ToggleHelpIntent.HELP);
		toggleActive();
	}

	private void toggleActive() {
		isActive = !isActive;
	}
}
