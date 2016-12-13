package components;

public class MessageComponent extends Component {
	private String message;
	private boolean isSingle;
	private boolean isActive;

	public MessageComponent(String message, boolean isSingle) {
		this.message = message;
		this.isSingle = isSingle;
		isActive = false;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
