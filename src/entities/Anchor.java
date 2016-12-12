package entities;

import components.CameraComponent;
import components.InputComponent;
import components.PositionComponent;
import components.RenderComponent;
import core.Level;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import renderables.RenderableDiamond;

public class Anchor extends Entity {
	public static final double WIDTH = Level.TILE_SIZE / 3;
	public static final double HEIGHT = Level.TILE_SIZE / 3;
	public static final Paint COLOR = Color.LIGHTGRAY;
	public static final double ALPHA = 0.8;

	public Anchor() {
		this(0, 0);
	}

	public Anchor(Point2D position) {
		this(position.getX(), position.getY());

	}

	public Anchor(double positionX, double positionY) {
		InputComponent inputComponent = new InputComponent();
		add(new RenderComponent(new RenderableDiamond(Anchor.WIDTH, Anchor.HEIGHT), Anchor.COLOR,
				(Player.WIDTH - Anchor.WIDTH) / 2, (Player.HEIGHT - Anchor.HEIGHT) / 2, Anchor.ALPHA),
				new PositionComponent(positionX, positionY), new CameraComponent(1), inputComponent);
	}

}
