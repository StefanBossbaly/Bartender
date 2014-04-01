package uofs.robotics.bartender.protocol;

public class MalformedMessageException extends RuntimeException {

	private static final long serialVersionUID = -9093459212610617639L;

	public MalformedMessageException() {
		super();
	}

	public MalformedMessageException(String message) {
		super(message);
	}
}
