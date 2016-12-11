package core;

import com.google.gson.Gson;

import javafx.geometry.Point2D;

public class Level {
	public static final double TILE_SIZE = 60;
	public static final Point2D DEFAULT_GRAVITY = new Point2D(0, 6);
	private final String[] data;
	private final double width, height;
	private Point2D gravity;

	public Level(String[] data) {
		this(data, Level.DEFAULT_GRAVITY);
	}

	public Level(String[] data, Point2D gravity) {
		this.data = data;
		width = data[0].length() * Level.TILE_SIZE;
		height = data.length * Level.TILE_SIZE;
		this.gravity = gravity;
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
		return gravity;
	}

	public void setGravity(Point2D gravity) {
		this.gravity = gravity;
	}
}