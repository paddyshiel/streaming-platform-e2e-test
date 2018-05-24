package au.com.sportsbet.sp.e2e.producer.sender;

public interface Sender<K, V, R> {

    R send(K key, V value);

}
