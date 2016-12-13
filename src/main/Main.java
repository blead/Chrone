package main;

import core.EntitySystemManager;
import core.InputManager;
import core.LevelManager;
import intents.OpenLevelIntent;
import intents.RestartIntent;
import intents.ReturnToMenuIntent;
import intents.ToggleHelpIntent;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import systems.CameraSystem;
import systems.CollisionSystem;
import systems.ContactSystem;
import systems.DelayedInputSystem;
import systems.DoorSwitchSystem;
import systems.ExpirationSystem;
import systems.GravitySystem;
import systems.InputRecorderSystem;
import systems.InputSystem;
import systems.MessageSystem;
import systems.MovementSystem;
import systems.RenderSystem;
import utils.Level;

public class Main extends Application {
	public static final String TITLE = "Chrone";
	public static final boolean IS_RESIZABLE = false;
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;
	public static Main instance = null;
	private Stage primaryStage;
	private Pane applicationRoot;
	private Canvas background;
	private Canvas game;
	private Canvas toast;
	private Thread gameLoop;
	private boolean isRunning;

	public Main() {
		synchronized (Main.class) {
			if (instance != null)
				throw new UnsupportedOperationException();
			instance = this;
		}
	}

	public static Main getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public Pane getApplicationRoot() {
		return applicationRoot;
	}

	public Canvas getBackground() {
		return background;
	}

	public Canvas getGame() {
		return game;
	}

	public Canvas getToast() {
		return toast;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void start(Stage primaryStage) {
		// initialize states
		this.primaryStage = primaryStage;
		background = new Canvas();
		game = new Canvas();
		toast = new Canvas(Main.WIDTH, Main.HEIGHT);
		applicationRoot = new Pane();
		applicationRoot.setPrefSize(Main.WIDTH, Main.HEIGHT);
		applicationRoot.getChildren().addAll(background, game, toast);
		primaryStage.setTitle(Main.TITLE);
		primaryStage.setResizable(Main.IS_RESIZABLE);
		primaryStage.setScene(new Scene(applicationRoot));
		primaryStage.show();
		LevelManager.getInstance().setLevel(Level.MENU);
		LevelManager.getInstance().load();
		InputManager.getInstance().setTriggeredIntent(KeyCode.R, new RestartIntent());
		InputManager.getInstance().setTriggeredIntent(KeyCode.O, new OpenLevelIntent());
		InputManager.getInstance().setTriggeredIntent(KeyCode.H, new ToggleHelpIntent());
		InputManager.getInstance().setTriggeredIntent(KeyCode.ESCAPE, new ReturnToMenuIntent());
		EntitySystemManager.getInstance().add(new InputRecorderSystem(), new InputSystem(), new DelayedInputSystem(),
				new GravitySystem(), new CollisionSystem(), new MovementSystem(), new ContactSystem(),
				new DoorSwitchSystem(), new MessageSystem(), new ExpirationSystem(), new CameraSystem(),
				new RenderSystem());
		isRunning = true;
		// set input handlers
		primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				InputManager.getInstance().setPressed(keyEvent.getCode(), true);
			}
		});
		primaryStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				InputManager.getInstance().setPressed(keyEvent.getCode(), false);
			}
		});
		// game loop
		gameLoop = new Thread(new Runnable() {
			@Override
			public void run() {
				final int FPS = 60;
				final long TIME_PER_FRAME = 1000000000 / FPS;
				final double MAXIMUM_DELTA_TIME = 1;
				long lastUpdateTime = System.nanoTime();
				while (Main.getInstance().isRunning()) {
					long now = System.nanoTime();
					double deltaTime = (now - lastUpdateTime) / TIME_PER_FRAME;
					// deltaTime in frame unit
					if (deltaTime >= 1) {
						if (deltaTime > MAXIMUM_DELTA_TIME)
							deltaTime = MAXIMUM_DELTA_TIME;
						EntitySystemManager.getInstance().update(deltaTime);
						lastUpdateTime = now;
					}
					long timeUntilNextUpdate = (lastUpdateTime + TIME_PER_FRAME - System.nanoTime()) / 1000000;
					if (timeUntilNextUpdate > 0) {
						try {
							Thread.sleep(timeUntilNextUpdate);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
			}
		});
		gameLoop.start();
	}

	@Override
	public void stop() throws Exception {
		setRunning(false);
		gameLoop.interrupt();
		super.stop();
	}
}
