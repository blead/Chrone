package core;

import com.google.gson.Gson;

import javafx.geometry.Point2D;

public class Level {
	public static final double TILE_SIZE = 60;
	public static final Point2D DEFAULT_GRAVITY = new Point2D(0, 2);
	public static final String DEFAULT_BACKGROUND = ClassLoader.getSystemResource("bg1.jpg").toString();
	private final String[] data;
	private final double width, height;
	private double gravityX, gravityY;
	private String background;

	public Level(String[] data) {
		this(data, Level.DEFAULT_GRAVITY, Level.DEFAULT_BACKGROUND);
	}

	public Level(String[] data, Point2D gravity, String background) {
		this.data = data;
		width = data[0].length() * Level.TILE_SIZE;
		height = data.length * Level.TILE_SIZE;
		gravityX = gravity.getX();
		gravityY = gravity.getY();
		this.background = background;
	}

	public static Level fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Level.class);
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

	public double getHeight() {
		return height;
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