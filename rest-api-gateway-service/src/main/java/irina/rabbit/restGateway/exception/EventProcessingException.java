package irina.rabbit.restGateway.exception;

public class EventProcessingException extends RuntimeException {

    public EventProcessingException(Exception cause) {
        super(cause);
    }
}
