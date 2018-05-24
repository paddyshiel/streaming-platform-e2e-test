package au.com.sportsbet.sp.e2e;

import au.com.sportsbet.sp.e2e.config.BaseTestConfiguration;
import au.com.sportsbet.sp.e2e.producer.KafkaProducingService;
import au.com.sportsbet.sp.e2e.repository.MessageRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.UUID.randomUUID;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseTestConfiguration.class)
public class ProducerConsumerTest {

    @Autowired
    private KafkaProducingService<String, Object> kafkaProducingService;

    @Autowired
    @Qualifier("producedMessageRepository")
    private MessageRepository producedMessageRepository;

    @Autowired
    @Qualifier("consumedMessageRepository")
    private MessageRepository consumedMessageRepository;

    @Test
    @SneakyThrows
    public void testSending() {
        int expectedMessageSize = 10;

        produceMessages(expectedMessageSize);


        Thread.sleep(10000);

        assertThat(producedMessageRepository.size()).isGreaterThanOrEqualTo(expectedMessageSize);
        assertThat(consumedMessageRepository.size()).isGreaterThanOrEqualTo(expectedMessageSize);

        producedMessageRepository.keySet()
                .forEach(producedMessageKey -> assertThat(consumedMessageRepository.containsKey(producedMessageKey)));

    }

    private void produceMessages(int numMessages) {
        range(0, numMessages).forEach(i -> {
            String messageKey = randomUUID().toString();
            String messageValue = getClass().getCanonicalName() + " | " + messageKey;
            kafkaProducingService.produceMessage(messageKey, messageValue);
        });
    }

}
