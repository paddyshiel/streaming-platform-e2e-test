package au.com.sportsbet.sp.e2e.producer;

public interface ProducerService<K, V, R> {

    R produceMessage(K key, V value);

}
