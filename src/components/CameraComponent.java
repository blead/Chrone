/*
 * @author Thad Benjaponpitak
 */
package components;

public class CameraComponent extends Component {
	private int priority;

	public CameraComponent() {
		this(0);
	}

	public CameraComponent(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
