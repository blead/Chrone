package entities;

import components.CameraComponent;
import components.CollisionComponent;
import components.ContactComponent;
import components.GravityComponent;
import components.InputComponent;
import components.JumpableSurfaceComponent;
import components.PositionComponent;
import components.RenderComponent;
import components.VelocityComponent;
import intents.JumpIntent;
import intents.MoveIntent;
import intents.CreateAnchorIntent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import renderables.RenderableRectangle;
import utils.Level;

public class Player extends Entity {
	public static final double WIDTH = Level.TILE_SIZE / 2;
	public static final double HEIGHT = Level.TILE_SIZE / 2;
	public static final Paint COLOR = Color.WHITE;
	public static final double ACCELERATION_X = Level.TILE_SIZE / 10;
	public static final double ACCELERATION_Y = Level.TILE_SIZE / 2.5;
	public static final Point2D MAX_VELOCITY = new Point2D(Level.TILE_SIZE, Double.POSITIVE_INFINITY);

	public Player() {
		this(0, 0, 0, 0);
	}

	public Player(Point2D position, Point2D velocity) {
		this(position.getX(), position.getY(), velocity.getX(), velocity.getY());

	}

	public Player(double positionX, double positionY, double velocityX, double velocityY) {
		InputComponent inputComponent = new InputComponent();
		inputComponent.setPressedIntent(KeyCode.RIGHT,
				new MoveIntent(new Point2D(Player.ACCELERATION_X, 0), Player.MAX_VELOCITY));
		inputComponent.setPressedIntent(KeyCode.LEFT,
				new MoveIntent(new Point2D(-Player.ACCELERATION_X, 0), Player.MAX_VELOCITY));
		inputComponent.setPressedIntent(KeyCode.UP,
				new JumpIntent(new Point2D(0, -Player.ACCELERATION_Y), Player.MAX_VELOCITY));
		inputComponent.setTriggeredIntent(KeyCode.A, new CreateAnchorIntent());
		add(new RenderComponent(new RenderableRectangle(Player.WIDTH, Player.HEIGHT), Player.COLOR),
				new PositionComponent(positionX, positionY), new VelocityComponent(velocityX, velocityY),
				new GravityComponent(), new CollisionComponent(new Rectangle(Player.WIDTH, Player.HEIGHT)),
				new ContactComponent(JumpableSurfaceComponent.class), new CameraComponent(), inputComponent);
	}
}
