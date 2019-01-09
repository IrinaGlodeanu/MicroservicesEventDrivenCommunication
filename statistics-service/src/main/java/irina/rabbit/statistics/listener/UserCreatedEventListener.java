package irina.rabbit.statistics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import irina.rabbit.statistics.exception.EventProcessingException;
import irina.rabbit.statistics.model.User;
import irina.rabbit.statistics.repository.UserRepository;
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
public class UserCreatedEventListener implements MessageListener {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "EVENT_UserCreated", durable = "true"),
            exchange = @Exchange(value = "exchangeBooking", type = "topic"),
            key = "EVENT_UserCreated"),
            concurrency = "3-5"
    )
    public void onMessage(Message message) {

        String messagePayload = new String(message.getBody());

        try {
            User user = objectMapper.readValue(messagePayload, User.class);

            userRepository.save(user);
        } catch (IOException e) {
            throw new EventProcessingException(e);
        }
    }
}
