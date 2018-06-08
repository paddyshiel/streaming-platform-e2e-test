package au.com.sportsbet.sp.e2e.consumer.config;

import au.com.sportsbet.sp.e2e.consumer.ConsumerService;
import au.com.sportsbet.sp.e2e.consumer.KafkaConsumerService;
import au.com.sportsbet.sp.e2e.consumer.KafkaTopicListener;
import au.com.sportsbet.sp.e2e.repository.MessageRepository;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.net.InetAddress;
import java.util.HashMap;

import static au.com.sportsbet.sp.e2e.Application.resolveClasspathResourceAbsolutePath;
import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.common.config.SslConfigs.*;
import static org.springframework.util.StringUtils.isEmpty;

@Configuration
@EnableKafka
public class ConsumerConfiguration {

    @Value("${spring.kafka.consumer.bootstrap-servers:}")
    private String consumerBootstrapServer;

    @Value("${spring.kafka.consumer.group-id:}")
    private String consumerGroupId;

    @Value("${spring.kafka.consumer.topic:}")
    private String testsTopic;

    @Value("${spring.kafka.consumer.concurrency:}")
    private Integer concurrency;

    @Value("${spring.kafka.consumer.ssl.truststore-location:}")
    private String trustStoreLocation;

    @Value("${spring.kafka.consumer.ssl.truststore-password:}")
    private String trustStorePassword;

    @Value("${spring.kafka.consumer.ssl.keystore-location:}")
    private String keyStoreLocation;

    @Value("${spring.kafka.consumer.ssl.keystore-password:}")
    private String keyStorePassword;

    @Value("${spring.kafka.consumer.ssl.key-password:}")
    private String keyPassword;

    @Bean
    @SneakyThrows
    public ConsumerFactory<String, Object> consumerFactory() {

        HashMap<String, Object> configs = new HashMap<String, Object>() {{
            put(BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServer);
            put(GROUP_ID_CONFIG, consumerGroupId);
            put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            put(CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        }};

        if (!(isEmpty(trustStoreLocation) || isEmpty(trustStorePassword))) {
            configs.put(SECURITY_PROTOCOL_CONFIG, "SSL");
            configs.put(SSL_TRUSTSTORE_LOCATION_CONFIG, resolveClasspathResourceAbsolutePath(trustStoreLocation));
            configs.put(SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
        }

        if (!(isEmpty(keyStoreLocation) || isEmpty(keyStorePassword))) {
            configs.put(SECURITY_PROTOCOL_CONFIG, "SSL");
            configs.put(SSL_KEYSTORE_LOCATION_CONFIG, resolveClasspathResourceAbsolutePath(keyStoreLocation));
            configs.put(SSL_KEYSTORE_PASSWORD_CONFIG, keyStorePassword);
            configs.put(SSL_KEY_PASSWORD_CONFIG, keyStorePassword);
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
