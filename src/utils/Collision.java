/*
 * @author Thad Benjaponpitak
 */
package utils;

public class Collision {
	public static final Collision NONE = new Collision(Direction.NONE, 1), ALL = new Collision(Direction.ALL, 0),
			CORNER = new Collision(Direction.DIAGONAL, 0);
	private Direction direction;
	private double time;

	public Collision(Direction direction, double time) {
		this.direction = direction;
		this.time = time;
	}

	public static Collision min(Collision a, Collision b) {
		if (a.equals(Collision.ALL) || b.equals(Collision.ALL)
				|| (a.getTime() == b.getTime() && a.getDirection().isPerpendicularTo(b.getDirection())))
			return Collision.ALL;
		if (a.equals(Collision.CORNER) && !b.equals(Collision.NONE))
			return b;
		if (b.equals(Collision.CORNER) && !a.equals(Collision.NONE))
			return a;
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
