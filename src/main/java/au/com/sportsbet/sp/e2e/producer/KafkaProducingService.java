package au.com.sportsbet.sp.e2e.producer;

import au.com.sportsbet.sp.e2e.producer.sender.Sender;
import au.com.sportsbet.sp.e2e.repository.Repository;
import org.springframework.kafka.support.SendResult;

public class KafkaProducingService<K, V> extends AbstractProducingService<K, V, SendResult<K, V>> {

    public KafkaProducingService(Sender<K, V, SendResult<K, V>> sender, Repository<K, SendResult<K, V>> repository) {
        super(sender, repository);
    }

}
