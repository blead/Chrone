package core;

import entities.Block;
import entities.Player;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import main.Main;

public class LevelManager {
	public static final String[] LEVEL_ONE = new String[] { "000000000000000000000000000000",
			"000000000000000000000000000000", "000000000000000000000000000000", "0000P0000000000000000000000000",
			"000000000000000000000000000000", "000000000000000000000000000000", "000000000000000000000000000000",
			"000111000000000000000000000000", "000000001110000000000000000000", "000002000000011100002000000000",
			"000001110000000000011100011000", "111111110011110001111100111111" };
	private static LevelManager instance = null;
	private Level level;
	private Image background;

	private LevelManager() {
		level = null;
	}

	public static LevelManager getInstance() {
		if (instance == null)
			instance = new LevelManager();
		return instance;
	}

	public void load() {
		if (level == null)
			throw new NullPointerException();
		EntityManager.getInstance().clear();
		String[] data = level.getData();
		Canvas background = Main.getInstance().getBackground(), game = Main.getInstance().getGame();
		try {
			this.background = new Image(level.getBackground());
		} catch (NullPointerException e) {
			System.out.println(e + "\nusing default background image instead");
			this.background = new Image(Level.DEFAULT_BACKGROUND);
		}
		double backgroundWidth = Math.max(this.background.getWidth(), Main.WIDTH),
				backgroundHeight = Math.max(this.background.getHeight(), Main.HEIGHT);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				background.setWidth(backgroundWidth);
				background.setHeight(backgroundHeight);
				game.setWidth(level.getWidth());
				game.setHeight(level.getHeight());
			}
		});
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length(); j++) {
				switch (data[i].charAt(j)) {
				case '0':
					break;
				case '1':
					EntityManager.getInstance().add(new Block(Level.TILE_SIZE * j, Level.TILE_SIZE * i));
					break;
				case 'P':
					EntityManager.getInstance().add(new Player(Level.TILE_SIZE * j, Level.TILE_SIZE * i, 0, 0));
				}
			}
		}
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Image getBackground() {
		return background;
	}

	public void setBackground(Image background) {
		this.background = background;
	}
}
