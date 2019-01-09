package irina.rabbit.statistics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import irina.rabbit.statistics.exception.EventProcessingException;
import irina.rabbit.statistics.model.Booking;
import irina.rabbit.statistics.repository.BookingRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingCreatedEventListener implements MessageListener {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "EVENT_BookingCreated", durable = "true"),
            exchange = @Exchange(value = "exchangeBooking", type = "topic"),
            key = "EVENT_BookingCreated"),
            concurrency = "3-5"
    )
    public void onMessage(Message message) {

        String messagePayload = new String(message.getBody());

        try {
            Booking booking = objectMapper.readValue(messagePayload, Booking.class);

            bookingRepository.save(booking);
        } catch (IOException e) {
            throw new EventProcessingException(e);
        }
    }
}
