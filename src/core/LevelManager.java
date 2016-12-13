package core;

import com.google.gson.JsonSyntaxException;

import entities.Block;
import entities.DoorBlock;
import entities.GoalBlock;
import entities.InfoBlock;
import entities.Player;
import entities.SwitchBlock;
import entities.TextUserInterface;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import main.Main;
import utils.Code;
import utils.Level;

public class LevelManager {
	private static LevelManager instance = new LevelManager();
	private Level level;
	private Image background;
	private Media music;

	private LevelManager() {
		level = null;
	}

	public static LevelManager getInstance() {
		return instance;
	}

	public void load() {
		checkLevelData(level);
		EntityManager.getInstance().clear();
		AudioManager.getInstance().stopBgm();
		try {
			if (level.getBackground().equals("_menu"))
				this.background = Level.MENU_BACKGROUND;
			else
				this.background = new Image(level.getBackground());
		} catch (NullPointerException e) {
			this.background = Level.DEFAULT_BACKGROUND;
		}
		try {
			if (level.getMusic().equals("_lost_signal"))
				this.music = AudioManager.LOST_SIGNAL;
			else
				this.music = new Media(level.getMusic());
		} catch (NullPointerException e) {
			this.background = Level.DEFAULT_BACKGROUND;
			this.music = AudioManager.DEAR_DIARY;
		}
		Canvas background = Main.getInstance().getBackground(), game = Main.getInstance().getGame();
		double backgroundWidth = Math.max(this.background.getWidth(), Main.WIDTH),
				backgroundHeight = Math.max(this.background.getHeight(), Main.HEIGHT);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				background.setWidth(backgroundWidth);
				background.setHeight(backgroundHeight);
				background.setTranslateX(0);
				background.setTranslateY(0);
				game.setWidth(level.getWidth());
				game.setHeight(level.getHeight());
				game.setTranslateX(0);
				game.setTranslateY(0);
			}
		});
		AudioManager.getInstance().playBgm(this.music);
		parseLevelData(level.getData(), level.getMessages());
	}

	private void checkLevelData(Level level) {
		String[] data = level.getData();
		int messageCount = 0;
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length(); j++)
				if (data[i].charAt(j) == '3')
					messageCount++;
		if (messageCount > level.getMessages().length)
			throw new JsonSyntaxException("insufficient amount of messages");
	}

	private void parseLevelData(String[] data, String[] messages) {
		int messageCount = 0;
		try {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length(); j++) {
					char code = data[i].charAt(j);
					// menu
					if (code == '@')
						EntityManager.getInstance().add(new TextUserInterface("Press O to open a new level",
								Font.font(Level.TILE_SIZE / 2), Level.TILE_SIZE * j, Level.TILE_SIZE * i));
					else if (code == '#')
						EntityManager.getInstance().add(new TextUserInterface("Press H to toggle help message",
								Font.font(Level.TILE_SIZE / 2), Level.TILE_SIZE * j, Level.TILE_SIZE * i));
					// level
					else if (code == '1')
						EntityManager.getInstance().add(new Block(Level.TILE_SIZE * j, Level.TILE_SIZE * i));
					else if (code == '2')
						EntityManager.getInstance().add(new Player(Level.TILE_SIZE * j, Level.TILE_SIZE * i, 0, 0));
					else if (code == '3') {
						EntityManager.getInstance().add(new InfoBlock(Level.TILE_SIZE * j, Level.TILE_SIZE * i,
								messages[messageCount], GoalBlock.COLOR));
						messageCount++;
					} else if (code == '4')
						EntityManager.getInstance()
								.add(new GoalBlock(Level.TILE_SIZE * j, Level.TILE_SIZE * i, GoalBlock.COLOR));
					else if ('a' <= code && code <= 'z')
						EntityManager.getInstance().add(new SwitchBlock(Level.TILE_SIZE * j, Level.TILE_SIZE * i,
								Code.getCodeColor(Character.toUpperCase(code)), Character.toUpperCase(code)));
					else if ('A' <= code && code <= 'Z')
						EntityManager.getInstance().add(
								new DoorBlock(Level.TILE_SIZE * j, Level.TILE_SIZE * i, Code.getCodeColor(code), code));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new JsonSyntaxException("insufficient amount of messages");
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
