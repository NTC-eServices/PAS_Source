package lk.informatics.ntc.model.exception;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -2351392372304857934L;

	public UserNotFoundException() {
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
