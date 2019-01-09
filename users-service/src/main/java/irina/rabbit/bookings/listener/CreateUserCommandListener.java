package irina.rabbit.bookings.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import irina.rabbit.bookings.exception.EventProcessingException;
import irina.rabbit.bookings.model.User;
import irina.rabbit.bookings.repository.UserRepository;
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
public class CreateUserCommandListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(CreateUserCommandListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue_CreateUser", durable = "true"),
            exchange = @Exchange(value = "exchangeBooking", type = "topic"),
            key = "COMMAND_CreateUser"),
            concurrency = "3-5"
    )
    public void onMessage(Message message) {

        String payload = new String(message.getBody());

        try {
            User user = objectMapper.readValue(payload, User.class);

            user.setId(UUID.randomUUID().toString());

            LOG.info("Received new user: {}", user.getId());

            userRepository.save(user);

            sendUserCreatedEvent(user);
        } catch (IOException e) {
            throw new EventProcessingException(e);
        }
    }

    private void sendUserCreatedEvent(User user) throws JsonProcessingException {

        String payload = objectMapper.writeValueAsString(user);

        rabbitTemplate.convertAndSend("exchangeBooking", "EVENT_UserCreated", payload);
    }
}
