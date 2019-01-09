package irina.rabbit.bookings.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import irina.rabbit.bookings.exception.EventProcessingException;
import irina.rabbit.bookings.model.Booking;
import irina.rabbit.bookings.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CreateBookingCommandListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(CreateBookingCommandListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue_CreateBooking4", durable = "true"),
            exchange = @Exchange(value = "exchangeBooking", type = "topic"),
            key = "COMMAND_CreateBooking"),
            concurrency = "3-5"
    )
    public void onMessage(Message message) {

        String payload = new String(message.getBody());

        try {
            Booking booking = objectMapper.readValue(payload, Booking.class);

            booking.setId(UUID.randomUUID().toString());

            LOG.info("Received new booking with id {} for client {} at hotel {}",
                    booking.getId(), booking.getUserId(), booking.getHotelName());

            bookingRepository.save(booking);

            rabbitTemplate.convertAndSend("exchangeBooking", "EVENT_BookingCreated", objectMapper.writeValueAsString(booking));
        } catch (IOException e) {
            throw new EventProcessingException(e);
        }
    }
}
