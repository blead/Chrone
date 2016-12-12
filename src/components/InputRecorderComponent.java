package components;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

import javafx.scene.input.KeyCode;

public class InputRecorderComponent extends Component {
	private Queue<Set<KeyCode>> pressedRecord;
	private Queue<Set<KeyCode>> triggeredRecord;
	private int duration;

	public InputRecorderComponent(int duration) {
		pressedRecord = new ArrayDeque<>();
		triggeredRecord = new ArrayDeque<>();
		this.duration = duration;
	}

	public Queue<Set<KeyCode>> getPressedRecord() {
		return pressedRecord;
	}

	public void addPressedRecord(Set<KeyCode> pressed) {
		pressedRecord.add(pressed);
	}

	public Queue<Set<KeyCode>> getTriggeredRecord() {
		return triggeredRecord;
	}

	public void addTriggeredRecord(Set<KeyCode> triggered) {
		triggeredRecord.add(triggered);
	}

	public double getDuration() {
		return duration;
	}

	public void decreaseDuration() {
		duration--;
	}
}
