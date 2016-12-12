package entities;

import java.util.HashMap;
import java.util.Map;

import components.Component;
import core.EntityManager;
import utils.ComponentNotFoundException;

public class Entity implements Comparable<Entity> {
	private Map<Class<? extends Component>, Component> components;
	private int z;

	public Entity() {
		this(0);
	}

	public Entity(int z) {
		components = new HashMap<>();
		this.z = z;
	}

	public void add(Component... components) {
		for (Component component : components)
			this.components.put(component.getClass(), component);
	}

	public void remove(Component... components) {
		for (Component component : components)
			this.components.remove(component.getClass());
	}

	public void clear() {
		components.clear();
	}

	public boolean contains(Class<? extends Component> componentClass) {
		return components.containsKey(componentClass);
	}

	public Component getComponent(Class<? extends Component> componentClass) throws ComponentNotFoundException {
		Component component = components.get(componentClass);
		if (component == null)
			throw new ComponentNotFoundException();
		return component;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
		EntityManager.getInstance().sort();
	}

	@Override
	public int compareTo(Entity other) {
		if (this.getZ() < other.getZ())
			return -1;
		if (this.getZ() == other.getZ())
			return 0;
		return 1;
	}
}
