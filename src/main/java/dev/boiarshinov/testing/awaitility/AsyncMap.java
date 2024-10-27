package dev.boiarshinov.testing.awaitility;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncMap<K, V> {

    private final Duration awaitTimeout;
    private final Map<K,TimedValueWrapper<V>> internalMap = new ConcurrentHashMap<>();

    public AsyncMap(Duration awaitTimeout) {
        this.awaitTimeout = awaitTimeout;
    }

    public void put(K key, V value) {
        internalMap.put(key, TimedValueWrapper.of(value));
    }

    public V get(K key) {
        TimedValueWrapper<V> timedValueWrapper = internalMap.get(key);
        if (timedValueWrapper == null) {
            return null;
        }
        if (Duration.between(Instant.now(), timedValueWrapper.updatedAt()).abs().compareTo(awaitTimeout) > 0) {
            return timedValueWrapper.value();
        } else {
            return null;
        }
    }

    public V getOrThrow(K key) {
        V v = get(key);
        if (v == null) throw new IllegalArgumentException(key + " key is not exist");
        return v;
    }

    private record TimedValueWrapper<V>(V value, Instant updatedAt) {
        public static <V> TimedValueWrapper<V> of(V value) {
            return new TimedValueWrapper<>(value, Instant.now());
        }
    }
}
