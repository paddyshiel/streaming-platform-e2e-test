package au.com.sportsbet.sp.e2e.config;

import au.com.sportsbet.sp.e2e.consumer.config.ConsumerConfiguration;
import au.com.sportsbet.sp.e2e.producer.config.ProducerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ConsumerConfiguration.class, ProducerConfiguration.class})
public class BaseTestConfiguration {
}
