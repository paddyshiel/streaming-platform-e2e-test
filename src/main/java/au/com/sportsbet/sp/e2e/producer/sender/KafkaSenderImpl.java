package au.com.sportsbet.sp.e2e.producer.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@RequiredArgsConstructor
public class KafkaSenderImpl<K, V> implements KafkaSender<K, V> {

    private final String topic;
    @Autowired
    private KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public SendResult<K, V> send(K key, V value) {
        try {
            log.info("sending payload='{}' to topic='{}'", value.toString(), topic);

            return kafkaTemplate
                    .send(topic, key, value)
                    .get(5, SECONDS);

        } catch (Exception e) {
            throw new MessageSendingException("Error sending message to kafka.", e);
        }
    }
}
