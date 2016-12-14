/*
 * @author Thad Benjaponpitak
 */
package utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Level {
	public static final double TILE_SIZE;
	public static final Point2D DEFAULT_GRAVITY;
	public static final Image MENU_BACKGROUND;
	public static final Image DEFAULT_BACKGROUND;
	public static final Level MENU;
	private final String[] data;
	private final String[] messages;
	private double width, height;
	private double gravityX, gravityY;
	private String background;
	private String music;

	static {
		TILE_SIZE = 60;
		DEFAULT_GRAVITY = new Point2D(0, 2);
		MENU_BACKGROUND = new Image(ClassLoader.getSystemResource("menu.png").toString());
		DEFAULT_BACKGROUND = new Image(ClassLoader.getSystemResource("bg1.jpg").toString());
		MENU = new Level(new String[] { "000000000000000000000000000", "000000000000000000000000000",
				"000000000000000000000000000", "000000000000000000000000000", "000000000000000000000000000",
				"000000000000000000000000000", "000000000000000000000000000", "@00000000000000000000000000",
				"000000000000000000000000000", "#00000000000000000000000000", "000000000000000000000000000",
				"000000000000000000000000000", "000000000000000000000000000", "000000000000000000000000000",
				"000000000000000000000000000" }, new String[] {}, Point2D.ZERO, "_menu", "_lost_signal");
	}

	public Level(String[] data) {
		this(data, new String[] {}, Level.DEFAULT_GRAVITY, null, null);
	}

	public Level(String[] data, String[] messages, Point2D gravity, String background, String music) {
		this.data = data;
		this.messages = messages;
		width = data[0].length() * Level.TILE_SIZE;
		height = data.length * Level.TILE_SIZE;
		gravityX = gravity.getX();
		gravityY = gravity.getY();
		this.background = background;
		this.music = music;
	}

	public static Level fromJson(String json) throws JsonSyntaxException {
		Gson gson = new Gson();
		Level level = gson.fromJson(json, Level.class);
		if (level.getData() == null)
			throw new JsonSyntaxException("data field is missing");
		if (level.getMessages() == null)
			throw new JsonSyntaxException("messages field is missing");
		if (level.getWidth() == 0)
			level.setWidth(level.getData()[0].length() * Level.TILE_SIZE);
		if (level.getHeight() == 0)
			level.setHeight(level.getData().length * Level.TILE_SIZE);
		return level;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String[] getData() {
		return data;
	}

	public String[] getMessages() {
		return messages;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Point2D getGravity() {
		return new Point2D(gravityX, gravityY);
	}

	public void setGravity(Point2D gravity) {
		gravityX = gravity.getX();
		gravityY = gravity.getY();
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music;
	}
}