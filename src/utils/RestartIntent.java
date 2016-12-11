package utils;

import core.Entity;
import core.LevelManager;

public class RestartIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		LevelManager.getInstance().load();
	}
}
