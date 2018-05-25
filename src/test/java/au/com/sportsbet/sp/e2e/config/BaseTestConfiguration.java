package au.com.sportsbet.sp.e2e.config;

import au.com.sportsbet.sp.e2e.consumer.config.ConsumerConfiguration;
import au.com.sportsbet.sp.e2e.producer.config.ProducerConfiguration;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ConsumerConfiguration.class, ProducerConfiguration.class})
public class BaseTestConfiguration {

    @SneakyThrows
    public static void sleep(long millis) {
        Thread.sleep(millis);
    }

}
