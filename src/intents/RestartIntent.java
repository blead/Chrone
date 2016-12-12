package intents;

import core.LevelManager;
import entities.Entity;

public class RestartIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		LevelManager.getInstance().load();
	}
}
