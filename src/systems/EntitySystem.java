/*
 * @author Thad Benjaponpitak
 */
package systems;

public abstract class EntitySystem implements Comparable<EntitySystem> {
	private int priority;
	private boolean isActive;

	public EntitySystem(int priority) {
		this.priority = priority;
		this.isActive = true;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int compareTo(EntitySystem other) {
		if (this.equals(other))
			return 0;
		if (getPriority() < other.getPriority())
			return -1;
		return 1;
	}

	public abstract void update(double deltaTime);
}
