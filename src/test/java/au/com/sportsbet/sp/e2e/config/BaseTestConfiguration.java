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

    private static long TEST_CONSUMER_BOOTSTRAPPING_TIMEOUT = 5000;

    @SneakyThrows
    public static void sleep(long millis) {
        Thread.sleep(millis);
    }

    public static void waitForConsumerSubscription() {
        sleep(TEST_CONSUMER_BOOTSTRAPPING_TIMEOUT);
    }
}
