package utils;

public class Collision {
	public static Collision NONE = new Collision(Direction.NONE, 1);
	private Direction direction;
	private double time;

	public Collision(Direction direction, double time) {
		this.direction = direction;
		this.time = time;
	}

	public static Collision min(Collision a, Collision b) {
		return a.getTime() < b.getTime() ? a : b;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
}
