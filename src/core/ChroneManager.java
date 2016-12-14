/*
 * @author Thad Benjaponpitak
 */
package core;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

import entities.Anchor;
import entities.DynamicChrone;
import entities.StaticChrone;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class ChroneManager {
	public static final int MAX_ANCHOR = 1;
	public static final int MAX_STATIC_CHRONE = 1;
	public static final int MAX_DYNAMIC_CHRONE = 1;
	public static final int STATIC_CHRONE_DURATION = 1;
	public static final int DYNAMIC_CHRONE_DURATION = 180;
	private static ChroneManager instance = new ChroneManager();
	private Queue<Anchor> anchors;
	private Queue<StaticChrone> staticChrones;
	private Queue<DynamicChrone> dynamicChrones;

	private ChroneManager() {
		anchors = new ArrayDeque<>();
		staticChrones = new ArrayDeque<>();
		dynamicChrones = new ArrayDeque<>();
	}

	public static ChroneManager getInstance() {
		return instance;
	}

	public static int getMaxDuration() {
		return Math.max(ChroneManager.STATIC_CHRONE_DURATION, ChroneManager.DYNAMIC_CHRONE_DURATION);
	}

	public void createAnchor(Point2D position) {
		if (anchors.size() >= ChroneManager.MAX_ANCHOR)
			removeAnchor();
		addAnchor(new Anchor(position));
		AudioManager.getInstance().uniquePlay(AudioManager.ANCHOR);
		AudioManager.getInstance().remove(AudioManager.ANCHOR);
	}

	public void createStaticChrone(Point2D position) {
		removeAnchor();
		if (staticChrones.size() >= ChroneManager.MAX_STATIC_CHRONE)
			removeStaticChrone();
		addStaticChrone(new StaticChrone(position));
		AudioManager.getInstance().uniquePlay(AudioManager.CHRONE_CREATE);
		AudioManager.getInstance().remove(AudioManager.CHRONE_CREATE);
	}

	public void createDynamicChrone(Point2D position, Queue<Set<KeyCode>> pressedRecord,
			Queue<Set<KeyCode>> triggeredRecord) {
		removeAnchor();
		if (dynamicChrones.size() >= ChroneManager.MAX_DYNAMIC_CHRONE)
			removeDynamicChrone();
		addDynamicChrone(new DynamicChrone(position, pressedRecord, triggeredRecord));
		AudioManager.getInstance().uniquePlay(AudioManager.CHRONE_CREATE);
		AudioManager.getInstance().remove(AudioManager.CHRONE_CREATE);
	}

	private void addAnchor(Anchor anchor) {
		synchronized (anchors) {
			anchors.add(anchor);
		}
		EntityManager.getInstance().add(anchor);
	}

	private void removeAnchor() {
		Anchor anchor;
		synchronized (anchors) {
			anchor = anchors.remove();
		}
		EntityManager.getInstance().remove(anchor);
	}

	private void addStaticChrone(StaticChrone staticChrone) {
		synchronized (staticChrones) {
			staticChrones.add(staticChrone);
		}
		EntityManager.getInstance().add(staticChrone);
	}

	private void removeStaticChrone() {
		StaticChrone staticChrone;
		synchronized (staticChrones) {
			staticChrone = staticChrones.remove();
		}
		EntityManager.getInstance().remove(staticChrone);
	}

	private void addDynamicChrone(DynamicChrone dynamicChrone) {
		synchronized (dynamicChrones) {
			dynamicChrones.add(dynamicChrone);
		}
		EntityManager.getInstance().add(dynamicChrone);
	}

	private void removeDynamicChrone() {
		DynamicChrone dynamicChrone;
		synchronized (dynamicChrones) {
			dynamicChrone = dynamicChrones.remove();
		}
		EntityManager.getInstance().remove(dynamicChrone);
	}
}
