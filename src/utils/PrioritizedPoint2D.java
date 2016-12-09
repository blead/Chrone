package utils;

import javafx.geometry.Point2D;

public class PrioritizedPoint2D implements Comparable<PrioritizedPoint2D> {
	private Point2D point2D;
	private Integer priority;

	public PrioritizedPoint2D(Point2D point2D, Integer priority) {
		this.point2D = point2D;
		this.priority = priority;
	}

	public PrioritizedPoint2D(double x, double y, Integer priority) {
		point2D = new Point2D(x, y);
		this.priority = priority;
	}

	public Point2D getPoint2D() {
		return point2D;
	}

	public void setPoint2D(Point2D point2d) {
		point2D = point2d;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public int compareTo(PrioritizedPoint2D other) {
		return priority.compareTo(other.priority);
	}
}
