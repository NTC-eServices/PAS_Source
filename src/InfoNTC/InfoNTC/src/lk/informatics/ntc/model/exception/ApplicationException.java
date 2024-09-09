package lk.informatics.ntc.model.exception;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -2351392372304857934L;

	public ApplicationException() {
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
