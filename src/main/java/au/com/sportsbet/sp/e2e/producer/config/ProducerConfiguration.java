package au.com.sportsbet.sp.e2e.producer.config;

import au.com.sportsbet.sp.e2e.producer.KafkaProducingService;
import au.com.sportsbet.sp.e2e.producer.sender.KafkaSender;
import au.com.sportsbet.sp.e2e.producer.sender.KafkaSenderImpl;
import au.com.sportsbet.sp.e2e.repository.MessageRepository;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;

import java.net.InetAddress;
import java.util.HashMap;

import static au.com.sportsbet.sp.e2e.Application.resolveClasspathResourceAbsolutePath;
import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.common.config.SslConfigs.*;
import static org.springframework.util.StringUtils.isEmpty;

@Configuration
public class ProducerConfiguration {

    @Value("${spring.kafka.producer.bootstrap-servers:}")
    private String producerBootstrapServer;

    @Value("${spring.kafka.producer.topic:}")
    private String testsTopic;

    @Value("${spring.kafka.producer.ssl.truststore-location:}")
    private String trustStoreLocation;

    @Value("${spring.kafka.producer.ssl.truststore-password:}")
    private String trustStorePassword;

    @Value("${spring.kafka.producer.ssl.keystore-location:}")
    private String keyStoreLocation;

    @Value("${spring.kafka.producer.ssl.keystore-password:}")
    private String keyStorePassword;

    @Value("${spring.kafka.producer.ssl.key-password:}")
    private String keyPassword;

    @Bean
    @SneakyThrows
    public ProducerFactory<String, Object> producerFactory() {
        HashMap<String, Object> configs = new HashMap<String, Object>() {{
            put(BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServer);
            put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
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

        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    @DependsOn("kafkaTopicListener")
    public KafkaProducingService<String, Object> messageProducingService() {
        return new KafkaProducingService<>(kafkaMessageSender(), producedMessageRepository());
    }

    @Bean
    public KafkaSender<String, Object> kafkaMessageSender() {
        return new KafkaSenderImpl<>(testsTopic);
    }

    @Bean
    public MessageRepository<String, SendResult<String, Object>> producedMessageRepository() {
        return new MessageRepository<>();
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
