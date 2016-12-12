package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import entities.Entity;

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
		return new ArrayList<>(entities);
	}

	public void add(Entity... entities) {
		for (Entity entity : entities)
			synchronized (this.entities) {
				this.entities.add(entity);
			}
	}

	public void addAll(List<Entity> entities) {
		synchronized (this.entities) {
			this.entities.addAll(entities);
		}
	}

	public void remove(Entity... entities) {
		synchronized (this.entities) {
			this.entities.removeAll(Arrays.asList(entities));
		}
	}

	public void clear() {
		synchronized (entities) {
			entities.clear();
		}
	}

	public void sort() {
		synchronized (entities) {
			Collections.sort(entities);
		}
	}
}
