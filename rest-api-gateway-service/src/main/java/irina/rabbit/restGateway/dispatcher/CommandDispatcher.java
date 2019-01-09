package irina.rabbit.restGateway.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import irina.rabbit.restGateway.exception.EventProcessingException;
import irina.rabbit.restGateway.model.Booking;
import irina.rabbit.restGateway.model.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandDispatcher {

    private static final String EXCHANGE_NAME = "exchangeBooking";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void processBooking(Booking booking) {

        publishCommand(booking, "COMMAND_CreateBooking");
    }

    public void createUser(User user) {

        publishCommand(user, "COMMAND_CreateUser");
    }

    private <T> void publishCommand(T entity, String commandName) {
        try {
            String commandPayload = objectMapper.writeValueAsString(entity);
            ExponentialBackOff.execute(() -> rabbitTemplate.convertAndSend(EXCHANGE_NAME, commandName, commandPayload));
            //rabbitTemplate.convertAndSend(EXCHANGE_NAME, commandName, commandPayload);
        } catch (JsonProcessingException e) {
            throw new EventProcessingException(e);
        }

    }
}
