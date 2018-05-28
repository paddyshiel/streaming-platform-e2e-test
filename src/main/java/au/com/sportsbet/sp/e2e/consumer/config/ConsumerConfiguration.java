package au.com.sportsbet.sp.e2e.consumer.config;

import au.com.sportsbet.sp.e2e.Application;
import au.com.sportsbet.sp.e2e.consumer.ConsumerService;
import au.com.sportsbet.sp.e2e.consumer.KafkaConsumerService;
import au.com.sportsbet.sp.e2e.consumer.KafkaTopicListener;
import au.com.sportsbet.sp.e2e.repository.MessageRepository;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.HashMap;

import static au.com.sportsbet.sp.e2e.Application.resolveClasspathResourceAbsolutePath;
import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
import static org.springframework.util.StringUtils.isEmpty;

@Configuration
@EnableKafka
public class ConsumerConfiguration {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String consumerBootstrapServer;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.consumer.topic}")
    private String testsTopic;

    @Value("${spring.kafka.consumer.concurrency}")
    private Integer concurrency;

    @Value("${spring.kafka.consumer.ssl.truststore-location}")
    private String trustStoreLocation;

    @Value("${spring.kafka.consumer.ssl.truststore-password}")
    private String trustStorePassword;

    @Bean
    @SneakyThrows
    public ConsumerFactory<String, Object> consumerFactory() {

        HashMap<String, Object> configs = new HashMap<String, Object>() {{
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServer);
            put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            put(ConsumerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        }};

        if (!(isEmpty(trustStoreLocation) || isEmpty(trustStorePassword))) {
            configs.put(SECURITY_PROTOCOL_CONFIG, "SSL");
            configs.put(SSL_TRUSTSTORE_LOCATION_CONFIG, resolveClasspathResourceAbsolutePath(trustStoreLocation));
            configs.put(SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
        }

        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(concurrency);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaTopicListener<String> kafkaTopicListener() {
        return new KafkaTopicListener<>();
    }

    @Bean
    public ConsumerService kafkaConsumerService() {
        return new KafkaConsumerService();
    }

    @Bean
    public MessageRepository<String, Object> consumedMessageRepository() {
        return new MessageRepository<>();
    }

}
