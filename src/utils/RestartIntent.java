package utils;

import core.LevelManager;
import entities.Entity;
import intents.Intent;

public class RestartIntent implements Intent {
	@Override
	public void handle(Entity entity) {
		LevelManager.getInstance().load();
	}
}
