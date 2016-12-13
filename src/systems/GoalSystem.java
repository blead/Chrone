package systems;

import java.util.List;

import components.ContactComponent;
import components.GoalComponent;
import components.RenderComponent;
import core.EntityManager;
import entities.Entity;
import entities.GoalBlock;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import renderables.RenderableGoalBlock;
import utils.ComponentNotFoundException;

public class GoalSystem extends EntitySystem {
	public GoalSystem() {
		super(28);
	}

	@Override
	public void update(double deltaTime) {
		List<Entity> entities = EntityManager.getInstance().getEntities();
		for (Entity entity : entities) {
			try {
				ContactComponent contactComponent = (ContactComponent) entity.getComponent(ContactComponent.class);
				GoalComponent goalComponent = (GoalComponent) entity.getComponent(GoalComponent.class);
				RenderComponent renderComponent = (RenderComponent) entity.getComponent(RenderComponent.class);
				RenderableGoalBlock goalBlock = (RenderableGoalBlock) renderComponent.getShape();
				if (contactComponent.isContact()) {
					goalBlock.setSecondaryColor(GoalBlock.ACTIVE_COLOR);
					entity.remove(GoalComponent.class);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setContentText("CONGRATULATIONS!!!!!!!1ONE1ONE11ELEVEN\nYOU WONlaksjdflkajsdflkajlf");
							alert.showAndWait();
						}
					});
					// TODO: congratulations!
				} else {
					goalBlock.setSecondaryColor(GoalBlock.COLOR);
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
