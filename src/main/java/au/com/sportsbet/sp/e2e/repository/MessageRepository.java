package au.com.sportsbet.sp.e2e.repository;

import java.util.concurrent.ConcurrentHashMap;

public class MessageRepository<K, V> extends ConcurrentHashMap<K, V> implements Repository<K, V> {
}
