/*
 * @author Thad Benjaponpitak
 */
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

	public boolean isContact() {
		return !contacts.isEmpty();
	}

	public boolean isContact(Direction direction) {
		return contacts.contains(direction);
	}

	public void setContact(Direction direction) {
		setContact(direction, true);
	}

	public void setContact(Direction direction, boolean isContact) {
		if (isContact)
			contacts.add(direction);
		else
			contacts.remove(direction);
	}

	public void clearContacts() {
		contacts.clear();
	}

	public Class<? extends Component> getTargetComponent() {
		return targetComponent;
	}
}
