package core;

import entities.Block;
import entities.DoorBlock;
import entities.Player;
import entities.SwitchBlock;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import main.Main;
import utils.Code;

public class LevelManager {
	public static final String[] LEVEL_ONE = new String[] { "000000000000000000000000000000",
			"000000000000000000000000000000", "000000000000000000000000000000", "000020000000000000000000000000",
			"000000000000000000000000000000", "000000000000000000000000000000", "000000000000000000000000000000",
			"0001B1000000000000000000000000", "00000000a110000000000000000000", "00000000000001A100000000000000",
			"0000011b0000000000011100011000", "111a11110011110001111100111111" };
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
				char code = data[i].charAt(j);
				if (code == '1')
					EntityManager.getInstance().add(new Block(Level.TILE_SIZE * j, Level.TILE_SIZE * i));
				else if (code == '2')
					EntityManager.getInstance().add(new Player(Level.TILE_SIZE * j, Level.TILE_SIZE * i, 0, 0));
				else if ('a' <= code && code <= 'z')
					EntityManager.getInstance().add(new SwitchBlock(Level.TILE_SIZE * j, Level.TILE_SIZE * i,
							Code.getCodeColor(Character.toUpperCase(code)), Character.toUpperCase(code)));
				else if ('A' <= code && code <= 'Z')
					EntityManager.getInstance().add(
							new DoorBlock(Level.TILE_SIZE * j, Level.TILE_SIZE * i, Code.getCodeColor(code), code));
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
