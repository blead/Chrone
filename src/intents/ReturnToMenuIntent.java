package intents;

import core.LevelManager;
import entities.Entity;
import utils.Level;

public class ReturnToMenuIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		LevelManager.getInstance().setLevel(Level.MENU);
		LevelManager.getInstance().load();
	}
}
