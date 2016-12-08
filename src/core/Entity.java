package core;

import java.util.HashMap;
import java.util.Map;

public class Entity implements Comparable<Entity> {
	private Map<Class<? extends Component>, Component> components;
	private int z;

	public Entity() {
		components = new HashMap<>();
		z = 0;
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

	public Component getComponent(Class<? extends Component> componentClass) {
		return components.get(componentClass);
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
