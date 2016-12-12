package components;

import java.util.HashSet;
import java.util.Set;

import utils.Direction;

public class ContactComponent extends Component {
	private Set<Direction> contacts;
	private Class<? extends Component> targetComponent;

	public ContactComponent(Class<? extends Component> targetComponent) {
		contacts = new HashSet<>();
		this.targetComponent = targetComponent;
	}

	public boolean isContact(Direction direction) {
		return contacts.contains(direction);
	}

	public void setContact(Direction direction) {
		contacts.add(direction);
	}

	public void clearContacts() {
		contacts.clear();
	}

	public Class<? extends Component> getTargetComponent() {
		return targetComponent;
	}
}
