/*
 * @author Thad Benjaponpitak
 */
package core;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import main.Main;
import renderables.RenderableText;
import utils.Level;

public class ToastManager {
	public static final Paint COLOR = Color.BLACK;
	public static final Paint TEXT_COLOR = Color.WHITE;
	public static final double ALPHA = 0.75;
	public static final double MARGIN = 40;
	private static ToastManager instance = new ToastManager();
	private String message;

	private ToastManager() {
		message = "";
	}

	public static ToastManager getInstance() {
		return instance;
	}

	public void show(String message) {
		hide(this.message);
		this.message = message;
		RenderableText renderableText = new RenderableText(message, Font.font(Level.TILE_SIZE / 3));
		GraphicsContext gc = Main.getInstance().getToast().getGraphicsContext2D();
		double x = (Main.WIDTH - (renderableText.getWidth() + MARGIN)) / 2,
				y = (Main.HEIGHT - (renderableText.getHeight() + MARGIN)) / 2;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gc.setGlobalAlpha(ToastManager.ALPHA);
				gc.setFill(ToastManager.COLOR);
				gc.fillRect(x, y, renderableText.getWidth() + MARGIN, renderableText.getHeight() + MARGIN);
				gc.setGlobalAlpha(1);
				gc.setFill(ToastManager.TEXT_COLOR);
				renderableText.render(gc, x + MARGIN / 2, y + MARGIN * 0.8);
			}
		});
	}

	public void hide() {
		GraphicsContext gc = Main.getInstance().getToast().getGraphicsContext2D();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gc.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			}
		});
	}

	public void hide(String message) {
		if (message.equals(this.message))
			hide();
	}
}
