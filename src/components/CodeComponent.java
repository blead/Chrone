/*
 * @author Thad Benjaponpitak
 */
package components;

public abstract class CodeComponent extends Component {
	private char code;

	public CodeComponent(char code) {
		this.code = code;
	}

	public char getCode() {
		return code;
	}

	public void setCode(char code) {
		this.code = code;
	}
}
