package au.com.sportsbet.sp.e2e.producer;

import au.com.sportsbet.sp.e2e.producer.sender.Sender;
import au.com.sportsbet.sp.e2e.repository.Repository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractProducingService<K, V, R> implements ProducerService<K, V, R> {

    private final Sender<K, V, R> sender;

    private final Repository<K, R> repository;

    public R produceMessage(K key, V value) {
        final R result = sender.send(key, value);
        repository.put(key, result);

        return result;
    }

}
