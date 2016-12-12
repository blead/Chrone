package core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import entities.Anchor;
import javafx.geometry.Point2D;
import utils.Intent;

public class ChroneManager {
	public static final int MAX_ANCHOR = 1;
	public static final int MAX_STATIC_CHRONE = 1;
	private static ChroneManager instance = null;
	private List<List<Intent>> intents;
	private Queue<Anchor> anchors;
	private Queue<StaticChrone> staticChrones;

	private ChroneManager() {
		intents = new ArrayList<>();
		anchors = new ArrayDeque<>();
		staticChrones = new ArrayDeque<>();
	}

	public static ChroneManager getInstance() {
		if (instance == null)
			instance = new ChroneManager();
		return instance;
	}

	public void createAnchor(Point2D position) {
		Anchor anchor;
		if (anchors.size() >= ChroneManager.MAX_ANCHOR) {
			synchronized (anchors) {
				anchor = anchors.poll();
			}
			EntityManager.getInstance().remove(anchor);
		}
		anchor = new Anchor(position);
		synchronized (anchors) {
			anchors.add(anchor);
		}
		EntityManager.getInstance().add(anchor);
	}

	public void createStaticChrone(Point2D position) {
		StaticChrone staticChrone;
		if (staticChrones.size() >= ChroneManager.MAX_STATIC_CHRONE) {
			synchronized (staticChrones) {
				staticChrone = staticChrones.poll();
			}
			EntityManager.getInstance().remove(staticChrone);
		}
		staticChrone = new StaticChrone(position);
		synchronized (staticChrones) {
			staticChrones.add(staticChrone);
		}
		EntityManager.getInstance().add(staticChrone);
	}
}
