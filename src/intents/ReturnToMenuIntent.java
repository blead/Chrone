package intents;

import core.LevelManager;
import entities.Entity;
import utils.Level;

public class ReturnToMenuIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		if (!LevelManager.getInstance().getLevel().equals(Level.MENU)) {
			LevelManager.getInstance().setLevel(Level.MENU);
			LevelManager.getInstance().load();
		}
	}
}
