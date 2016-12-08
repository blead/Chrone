package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntityManager {
	private static EntityManager instance = null;
	private List<Entity> entities;

	private EntityManager() {
		entities = new ArrayList<>();
	}

	public static EntityManager getInstance() {
		if (instance == null)
			instance = new EntityManager();
		return instance;
	}

	public List<Entity> getEntities() {
		return Collections.unmodifiableList(entities);
	}

	public void add(Entity... entities) {
		for (Entity entity : entities)
			this.entities.add(entity);
		sort();
	}

	public void remove(Entity... entities) {
		this.entities.removeAll(Arrays.asList(entities));
	}

	public void sort() {
		Collections.sort(entities);
	}
}
