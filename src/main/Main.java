package main;

import core.EntitySystemManager;
import core.InputManager;
import core.Level;
import core.LevelManager;
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
import systems.GravitySystem;
import systems.InputSystem;
import systems.MovementSystem;
import systems.RenderSystem;
import utils.RestartIntent;

public class Main extends Application {
	public static final String TITLE = "Chrone";
	public static final boolean IS_RESIZABLE = false;
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;
	public static Main instance = null;
	private Pane applicationRoot;
	private Canvas gameRoot;
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

	public Pane getApplicationRoot() {
		return applicationRoot;
	}

	public Canvas getGameRoot() {
		return gameRoot;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void start(Stage primaryStage) {
		LevelManager.getInstance().setLevel(new Level(LevelManager.LEVEL_ONE));
		gameRoot = new Canvas();
		applicationRoot = new Pane();
		applicationRoot.setPrefSize(Main.WIDTH, Main.HEIGHT);
		applicationRoot.getChildren().add(gameRoot);
		primaryStage.setTitle(Main.TITLE);
		primaryStage.setResizable(Main.IS_RESIZABLE);
		primaryStage.setScene(new Scene(applicationRoot));
		primaryStage.show();
		LevelManager.getInstance().load();
		EntitySystemManager.getInstance().add(new InputSystem(), new GravitySystem(), new CollisionSystem(),
				new MovementSystem(), new CameraSystem(), new RenderSystem());
		isRunning = true;

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

		InputManager.getInstance().setTriggeredIntent(KeyCode.R, new RestartIntent());

		gameLoop = new Thread(new Runnable() {
			@Override
			public void run() {
				final int FPS = 60;
				final long timePerFrame = 1000000000 / FPS;
				long lastUpdateTime = System.nanoTime();
				while (Main.getInstance().isRunning()) {
					long now = System.nanoTime();
					double deltaTime = (now - lastUpdateTime) / timePerFrame;
					// deltaTime in frame unit
					if (deltaTime >= 1) {
						EntitySystemManager.getInstance().update(deltaTime);
						InputManager.getInstance().clearTriggered();
						lastUpdateTime = now;
					}
					long timeUntilNextUpdate = (lastUpdateTime + timePerFrame - System.nanoTime()) / 1000000;
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
