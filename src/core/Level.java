package core;

public class Level {
	public static final double TILE_SIZE = 60;
	private final String[] data;
	private final double width, height;

	public Level(String[] data) {
		this.data = data;
		width = data[0].length() * Level.TILE_SIZE;
		height = data.length * Level.TILE_SIZE;
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
}