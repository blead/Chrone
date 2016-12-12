package components;

public class ExpirationComponent extends Component {
	private double expirationTime;

	public ExpirationComponent(double expirationTime) {
		this.expirationTime = expirationTime;
	}

	public double getExpirationTime() {
		return expirationTime;
	}

	public void decreaseExpirationTime(double amount) {
		expirationTime -= amount;
	}
}
