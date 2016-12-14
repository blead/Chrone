/*
 * @author Thad Benjaponpitak
 */
package entities;

import components.RenderComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import renderables.RenderableRectangle;

public class StaticChrone extends Chrone {
	public static final Paint COLOR = Color.RED;

	public StaticChrone() {
		this(0, 0);
	}

	public StaticChrone(Point2D position) {
		this(position.getX(), position.getY());

	}

	public StaticChrone(double positionX, double positionY) {
		super(positionX, positionY, 0, 0);
		add(new RenderComponent(new RenderableRectangle(Player.WIDTH, Player.HEIGHT), StaticChrone.COLOR,
				Chrone.ALPHA));
	}
}
