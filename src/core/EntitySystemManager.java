/*
 * @author Thad Benjaponpitak
 */
package core;

import java.util.SortedSet;
import java.util.TreeSet;

import systems.EntitySystem;

public class EntitySystemManager {
	private static EntitySystemManager instance = new EntitySystemManager();
	private SortedSet<EntitySystem> systems;

	private EntitySystemManager() {
		systems = new TreeSet<>();
	}

	public static EntitySystemManager getInstance() {
		return instance;
	}

	public void add(EntitySystem... systems) {
		for (EntitySystem system : systems)
			synchronized (this.systems) {
				this.systems.add(system);
			}
	}

	public void remove(EntitySystem... systems) {
		for (EntitySystem system : systems)
			synchronized (this.systems) {
				this.systems.remove(system);
			}
	}

	public void clear() {
		synchronized (systems) {
			systems.clear();
		}
	}

	public void update(double deltaTime) {
		for (EntitySystem system : systems) {
			system.update(deltaTime);
		}
	}
}
