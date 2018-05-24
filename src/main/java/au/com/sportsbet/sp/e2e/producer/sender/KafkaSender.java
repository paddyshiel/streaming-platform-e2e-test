package au.com.sportsbet.sp.e2e.producer.sender;

import org.springframework.kafka.support.SendResult;

public interface KafkaSender<K, V> extends Sender<K, V, SendResult<K, V>> {
}
