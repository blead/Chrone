/*
 * @author Thad Benjaponpitak
 */
package entities;

import java.util.Queue;
import java.util.Set;

import components.ContactComponent;
import components.DelayedInputComponent;
import components.ExpirationComponent;
import components.GravityComponent;
import components.InputRecordComponent;
import components.JumpableSurfaceComponent;
import components.RenderComponent;
import core.ChroneManager;
import intents.JumpIntent;
import intents.MoveIntent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import renderables.RenderableRectangle;

public class DynamicChrone extends Chrone {
	public static final Paint COLOR = Color.GREEN;

	public DynamicChrone(Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord) {
		this(0, 0, pressedRecord, triggeredRecord);
	}

	public DynamicChrone(Point2D position, Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord) {
		this(position.getX(), position.getY(), pressedRecord, triggeredRecord);
	}

	public DynamicChrone(double positionX, double positionY, Queue<Set<KeyCode>> pressedRecord,
			Queue<Set<KeyCode>> triggeredRecord) {
		super(positionX, positionY, 0, 0);
		DelayedInputComponent delayedInputComponent = new DelayedInputComponent();
		delayedInputComponent.setPressedIntent(KeyCode.RIGHT,
				new MoveIntent(new Point2D(Player.ACCELERATION_X, 0), Player.MAX_VELOCITY));
		delayedInputComponent.setPressedIntent(KeyCode.LEFT,
				new MoveIntent(new Point2D(-Player.ACCELERATION_X, 0), Player.MAX_VELOCITY));
		delayedInputComponent.setPressedIntent(KeyCode.UP,
				new JumpIntent(new Point2D(0, -Player.ACCELERATION_Y), Player.MAX_VELOCITY));
		add(new RenderComponent(new RenderableRectangle(Player.WIDTH, Player.HEIGHT), DynamicChrone.COLOR,
				Chrone.ALPHA), new GravityComponent(), new ContactComponent(JumpableSurfaceComponent.class),
				new ExpirationComponent(ChroneManager.DYNAMIC_CHRONE_DURATION),
				new InputRecordComponent(pressedRecord, triggeredRecord, ChroneManager.DYNAMIC_CHRONE_DURATION),
				delayedInputComponent);
	}
}