/*
 * @author Thad Benjaponpitak
 */
package renderables;

import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
	public abstract void render(GraphicsContext gc, double x, double y);

	public abstract double getWidth();

	public abstract double getHeight();
}
