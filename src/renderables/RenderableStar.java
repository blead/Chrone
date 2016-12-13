package renderables;

import javafx.scene.canvas.GraphicsContext;

public class RenderableStar implements Renderable {
	public static final double GOLDEN_RATIO = (Math.sqrt(5) + 1) / 2;
	private double width, height;
	private double outerEdge, innerEdge;

	public RenderableStar(double size) {
		width = size;
		height = size * Math.cos(Math.PI / 10);
		outerEdge = size / (RenderableStar.GOLDEN_RATIO * RenderableStar.GOLDEN_RATIO);
		innerEdge = outerEdge / RenderableStar.GOLDEN_RATIO;
	}

	@Override
	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public void render(GraphicsContext gc, double x, double y) {
		double x2 = outerEdge * Math.cos(Math.PI / 5), x3 = width / 2 - width * Math.sin(Math.PI / 10);
		double y1 = outerEdge * Math.cos(Math.PI / 10), y2 = (outerEdge + innerEdge) * Math.cos(Math.PI / 10),
				y3 = height - outerEdge * Math.sin(Math.PI / 5);
		gc.fillPolygon(
				new double[] { x, x + outerEdge, x + width / 2, x + width - outerEdge, x + width, x + width - x2,
						x + width - x3, x + width / 2, x + x3, x + x2 },
				new double[] { y + y1, y + y1, y, y + y1, y + y1, y + y2, y + height, y + y3, y + height, y + y2 }, 10);
	}
}
