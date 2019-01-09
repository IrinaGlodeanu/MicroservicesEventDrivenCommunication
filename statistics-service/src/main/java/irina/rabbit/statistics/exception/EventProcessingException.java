package irina.rabbit.statistics.exception;

public class EventProcessingException extends RuntimeException {

    public EventProcessingException(Exception cause) {
        super(cause);
    }
}
