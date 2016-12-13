package systems;

import java.util.List;

import components.ContactComponent;
import components.GoalComponent;
import components.MessageComponent;
import components.RenderComponent;
import core.EntityManager;
import core.ToastManager;
import entities.Entity;
import entities.GoalBlock;
import entities.InfoBlock;
import renderables.RenderableBlock;
import utils.ComponentNotFoundException;

public class MessageSystem extends EntitySystem {
	public MessageSystem() {
		super(28);
	}

	@Override
	public void update(double deltaTime) {
		List<Entity> entities = EntityManager.getInstance().getEntities();
		for (Entity entity : entities) {
			try {
				ContactComponent contactComponent = (ContactComponent) entity.getComponent(ContactComponent.class);
				MessageComponent messageComponent = (MessageComponent) entity.getComponent(MessageComponent.class);
				RenderComponent renderComponent = (RenderComponent) entity.getComponent(RenderComponent.class);
				RenderableBlock renderableBlock = (RenderableBlock) renderComponent.getShape();
				if (contactComponent.isContact()) {
					if (!messageComponent.isActive()) {
						messageComponent.setActive(true);
						renderableBlock.setSecondaryColor(entity.contains(GoalComponent.class) ? GoalBlock.ACTIVE_COLOR
								: InfoBlock.COLOR_DISABLED);
						ToastManager.getInstance().show(messageComponent.getMessage());
						// Platform.runLater(new Runnable() {
						// @Override
						// public void run() {
						// Alert alert = new Alert(AlertType.INFORMATION);
						// alert.setContentText(messageComponent.getMessage());
						// alert.showAndWait();
						// }
						// });
					}
				} else {
					if (messageComponent.isActive()) {
						if (!messageComponent.isSingle()) {
							messageComponent.setActive(false);
							renderableBlock.setSecondaryColor(
									entity.contains(GoalComponent.class) ? GoalBlock.COLOR : InfoBlock.COLOR);
						}
						ToastManager.getInstance().hide(messageComponent.getMessage());
					}
				}
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}
}
