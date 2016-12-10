package systems;

import components.GravityComponent;
import components.VelocityComponent;
import core.Entity;
import core.EntityManager;
import core.EntitySystem;
import core.Level;
import javafx.geometry.Point2D;
import utils.ComponentNotFoundException;

public class GravitySystem extends EntitySystem {
	public static final Point2D GLOBAL_GRAVITY = new Point2D(0, Level.TILE_SIZE / 10);
	// TODO: assign gravity for each level?

	public GravitySystem() {
		super(10);
	}

	@Override
	public void update(double deltaTime) {
		for (Entity entity : EntityManager.getInstance().getEntities()) {
			try {
				VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.class);
				GravityComponent gravityComponent = (GravityComponent) entity.getComponent(GravityComponent.class);
				velocityComponent.setVelocity(velocityComponent.getVelocity().add(GravitySystem.GLOBAL_GRAVITY));
			} catch (ComponentNotFoundException e) {
				continue;
			}
		}
	}

}
