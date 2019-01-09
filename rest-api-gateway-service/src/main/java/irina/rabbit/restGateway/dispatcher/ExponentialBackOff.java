package irina.rabbit.restGateway.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import static java.util.Arrays.asList;

public final class ExponentialBackOff {

    private static final Logger LOG = LoggerFactory.getLogger(ExponentialBackOff.class);

    private static final int[] BACKOFF_VALUES = new int[] { 1, 1, 2, 3, 5, 8, 13, 30, 100 };
    private static final List<Class<? extends Exception>> EXPECTED_COMMUNICATION_ERRORS =
            asList(ConnectException.class, AmqpConnectException.class, AmqpIOException.class, IOException.class);

    private ExponentialBackOff() {

    }

    public static void execute(Runnable fn) {
        for (int attempt = 0; attempt < BACKOFF_VALUES.length; attempt++) {
            try {
                fn.run();
                return;
            } catch (Exception e) {
                handleFailure(attempt, e);
            }
        }
        throw new RuntimeException("Failed to communicate.");
    }

    private static void handleFailure(int attempt, Exception e) {
        if (e.getCause() != null && !EXPECTED_COMMUNICATION_ERRORS.contains(e.getCause().getClass()))
            throw new RuntimeException(e);
        doWait(attempt);
    }

    private static void doWait(int attempt) {
        try {
            LOG.warn("Call failed. Sleeping for {} seconds.", BACKOFF_VALUES[attempt]);
            Thread.sleep(BACKOFF_VALUES[attempt] * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
