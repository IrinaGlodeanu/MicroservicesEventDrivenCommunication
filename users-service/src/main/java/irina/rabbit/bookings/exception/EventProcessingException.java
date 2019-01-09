package irina.rabbit.bookings.exception;

public class EventProcessingException extends RuntimeException {

    public EventProcessingException(Exception cause) {
        super(cause);
    }
}
