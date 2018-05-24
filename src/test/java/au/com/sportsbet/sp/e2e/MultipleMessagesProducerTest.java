package au.com.sportsbet.sp.e2e;

import au.com.sportsbet.sp.e2e.config.BaseTestConfiguration;
import au.com.sportsbet.sp.e2e.producer.KafkaProducingService;
import au.com.sportsbet.sp.e2e.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.UUID.randomUUID;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseTestConfiguration.class)
public class MultipleMessagesProducerTest {

    @Autowired
    private KafkaProducingService<String, Object> kafkaProducingService;

    @Autowired
    @Qualifier("producedMessageRepository")
    private MessageRepository<String, SendResult<String, Object>> producedMessageRepository;

    @Test
    public void testSending() {
        int expectedMessageSize = 10;

        range(0, expectedMessageSize).forEach(i -> {
            String messageKey = randomUUID().toString();
            String messageValue = getClass().getCanonicalName() + " | " + messageKey;
            kafkaProducingService.produceMessage(messageKey, messageValue);
        });

        assertThat(producedMessageRepository.size()).isEqualTo(expectedMessageSize);
    }

}
