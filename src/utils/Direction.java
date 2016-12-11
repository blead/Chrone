package utils;

import javafx.geometry.Point2D;

public enum Direction {
	UP(0, 0, -1), RIGHT(1, 1, 0), DOWN(2, 0, 1), LEFT(3, -1, 0), NONE(-1, 0, 0), ALL(-2, 1, 1), DIAGONAL(-3, 0, 0);

	private final int rotate90, rotate180, rotate270;
	private final boolean isHorizontal, isVertical;
	private final int dx, dy;

	private Direction(int value, int dx, int dy) {
		rotate90 = value < 0 ? value : (value + 1) % 4;
		rotate180 = value < 0 ? value : (value + 2) % 4;
		rotate270 = value < 0 ? value : (value + 3) % 4;
		isHorizontal = dx != 0;
		isVertical = dy != 0;
		this.dx = dx;
		this.dy = dy;
	}

	public static Direction fromPoint2D(Point2D direction) {
		if (direction.getX() == 0 && direction.getY() != 0)
			return direction.getY() < 0 ? Direction.UP : Direction.DOWN;
		if (direction.getY() == 0 && direction.getX() != 0)
			return direction.getX() < 0 ? Direction.LEFT : Direction.RIGHT;
		return Direction.NONE;
	}

	public Direction rotate90() {
		return values()[rotate90];
	}

	public Direction rotate180() {
		return values()[rotate180];
	}

	public Direction rotate270() {
		return values()[rotate270];
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public boolean isVertical() {
		return isVertical;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public boolean isPerpendicularTo(Direction direction) {
		if (dx == dy)
			return false;
		return (isHorizontal && direction.isVertical()) || (isVertical && direction.isHorizontal());
	}
}
