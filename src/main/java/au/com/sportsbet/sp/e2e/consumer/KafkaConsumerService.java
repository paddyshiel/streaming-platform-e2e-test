package au.com.sportsbet.sp.e2e.consumer;

import au.com.sportsbet.sp.e2e.repository.Repository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageHeaders;

import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.isEmpty;

public class KafkaConsumerService implements ConsumerService<MessageHeaders, ConsumerRecord> {

    @Autowired
    @Qualifier("consumedMessageRepository")
    private Repository<String, Object> repository;

    @Override
    public void consume(MessageHeaders headers, ConsumerRecord message) {
        final boolean messageHasKey = nonNull(message.key()) && !isEmpty(message.key());
        final String messageKey = messageHasKey ? message.key().toString() : UUID.randomUUID().toString();

        repository.put(messageKey, message);
    }

}
