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
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseTestConfiguration.class)
public class SingleMessageProducerTest {

    @Autowired
    private KafkaProducingService<String, Object> kafkaProducingService;

    @Autowired
    @Qualifier("producedMessageRepository")
    private MessageRepository<String, SendResult<String, Object>> producedMessageRepository;

    @Test
    public void testSimpleSending() {
        String messageKey = randomUUID().toString();
        String messageValue = getClass().getCanonicalName() + " | " + messageKey;

        SendResult<String, Object> result = kafkaProducingService.produceMessage(messageKey, messageValue);
        SendResult<String, Object> storedMessageResult = producedMessageRepository.get(messageKey);

        assertThat(storedMessageResult).isEqualTo(result);
    }

}
