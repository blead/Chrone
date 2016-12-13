package intents;

import core.LevelManager;
import core.ToastManager;
import entities.Entity;
import utils.Level;

public class RestartIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		if (!LevelManager.getInstance().getLevel().equals(Level.MENU)) {
			ToastManager.getInstance().hide();
			LevelManager.getInstance().load();
		}
	}
}
