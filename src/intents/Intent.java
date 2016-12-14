/*
 * @author Thad Benjaponpitak
 */
package intents;

import entities.Entity;

public interface Intent {
	public abstract void handle(Entity entity);
}
