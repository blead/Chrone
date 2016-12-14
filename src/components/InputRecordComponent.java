/*
 * @author Thad Benjaponpitak
 */
package components;

import java.util.Queue;
import java.util.Set;

import javafx.scene.input.KeyCode;

public class InputRecordComponent extends Component {
	private Queue<Set<KeyCode>> pressedRecord;
	private Queue<Set<KeyCode>> triggeredRecord;
	private int duration;

	public InputRecordComponent(Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord, int duration) {
		this.pressedRecord = pressedRecord;
		this.triggeredRecord = triggeredRecord;
		this.duration = duration;
	}

	public boolean isAvailable() {
		return duration > 0 && !pressedRecord.isEmpty() && !triggeredRecord.isEmpty();
	}

	public Set<KeyCode> getPressed() {
		return pressedRecord.remove();
	}

	public Set<KeyCode> getTriggered() {
		return triggeredRecord.remove();
	}

	public void decreaseDuration() {
		duration--;
	}
}
