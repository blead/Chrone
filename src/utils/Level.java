package utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Level {
	public static final double TILE_SIZE = 60;
	public static final Point2D DEFAULT_GRAVITY = new Point2D(0, 2);
	public static final Image MENU_BACKGROUND = new Image(ClassLoader.getSystemResource("menu.png").toString());
	public static final Image DEFAULT_BACKGROUND = new Image(ClassLoader.getSystemResource("bg1.jpg").toString());
	private final String[] data;
	private double width, height;
	private double gravityX, gravityY;
	private String background;

	public Level(String[] data) {
		this(data, Level.DEFAULT_GRAVITY, null);
	}

	public Level(String[] data, Point2D gravity, String background) {
		this.data = data;
		width = data[0].length() * Level.TILE_SIZE;
		height = data.length * Level.TILE_SIZE;
		gravityX = gravity.getX();
		gravityY = gravity.getY();
		this.background = background;
	}

	public static Level fromJson(String json) throws JsonSyntaxException {
		Gson gson = new Gson();
		Level level = gson.fromJson(json, Level.class);
		if (level.getData() == null)
			throw new JsonSyntaxException("data field missing");
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
}