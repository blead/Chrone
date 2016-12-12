package intents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.JsonSyntaxException;

import core.Level;
import core.LevelManager;
import entities.Entity;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import main.Main;

public class OpenLevelIntent implements Intent {
	private static final FileChooser fileChooser;
	static {
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open Level File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
	}

	@Override
	public void handle(Entity entity) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				File file = fileChooser.showOpenDialog(Main.getInstance().getPrimaryStage());
				try {
					Level level = Level.fromJson(read(file));
					LevelManager.getInstance().setLevel(level);
					LevelManager.getInstance().load();
				} catch (NullPointerException e) {
					return;
				} catch (IOException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("File not found");
					alert.showAndWait();
				} catch (JsonSyntaxException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Unable to parse level due to badly formed data:\n" + e.getMessage());
					alert.showAndWait();
				}
			}
		});
	}

	private String read(File file) throws NullPointerException, IOException {
		return new String(Files.readAllBytes(file.toPath()));
	}
}
