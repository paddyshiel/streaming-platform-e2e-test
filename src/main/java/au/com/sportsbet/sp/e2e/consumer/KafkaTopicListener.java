package au.com.sportsbet.sp.e2e.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;

@Slf4j
public class KafkaTopicListener<T> {

    @Autowired
    private ConsumerService<MessageHeaders, T> messageConsumerService;

    @KafkaListener(topics = "${spring.kafka.consumer.topic}")
    public void onReceiving(T message, @Headers MessageHeaders headers) {
        log.info("Received [headers = {}, message = {}]", headers, message);
        messageConsumerService.consume(headers, message);
    }

}
