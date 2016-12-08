package utils;

import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
	void draw(GraphicsContext gc, double x, double y);
}
