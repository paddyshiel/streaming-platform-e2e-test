package au.com.sportsbet.sp.e2e.config;

import au.com.sportsbet.sp.e2e.consumer.config.ConsumerConfiguration;
import au.com.sportsbet.sp.e2e.producer.config.ProducerConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({ConsumerConfiguration.class, ProducerConfiguration.class})
public class BaseTestConfiguration {

    private static long TEST_CONSUMER_BOOTSTRAPPING_TIMEOUT = 2000;

    public static void waitForConsumerSubscription() {
        log.info("waiting For Consumer Subscription: {} millis", TEST_CONSUMER_BOOTSTRAPPING_TIMEOUT);
        sleep(TEST_CONSUMER_BOOTSTRAPPING_TIMEOUT);
    }

    public static void waitForConsumerMessages(long timeoutInMillis) {
        log.info("waiting For Consumer Messages: {} millis", timeoutInMillis);
        sleep(timeoutInMillis);
    }

    @SneakyThrows
    public static void sleep(long millis) {
        Thread.sleep(millis);
    }
}
