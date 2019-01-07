package zkejid.grid.manager.server.iteration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IterationBean<T> {

    @Value("${grid.iteration.time.limit:50}")
    private Long keyExistenceDelta;

    private Map<String, Iterator<T>> iterators = new ConcurrentHashMap<>();
    private SortedMap<Long, List<String>> activeKeys = new ConcurrentSkipListMap<>();
    private AtomicLong counter = new AtomicLong(0);

    public String addIterator(Iterator<T> iterator) {
        final String key = UUID.randomUUID().toString();
        final Iterator oldValue = iterators.put(key, iterator);

        if (oldValue != null) {
            iterators.remove(key);
            throw new RuntimeException("key " + key + " already used.");
        }

        List<String> value = new CopyOnWriteArrayList<>();
        List<String> oldVKeysValue = activeKeys.putIfAbsent(counter.get(), value);
        if (oldVKeysValue != null) {
            value = oldVKeysValue;
        }
        value.add(key);

        return key;
    }

    public Iterator<T> getIterator(String key) {
        return iterators.get(key);
    }


    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    private void cleanInactiveKeys() {
        final long currentValue = incrementCounter();
        final long minCountAllowed = getMinCountAllowed(currentValue, keyExistenceDelta);
        // if we have long positive - do job
        if (currentValue >= 0) {
            removeEntriesOlderThan(minCountAllowed);
        } else {
            // if we overflowed (0_o) long - delete all iterators and move to 0
            iterators.clear();
            activeKeys.clear();
            counter.set(0);
        }

    }

    private long incrementCounter() {
        final long l = counter.incrementAndGet();
        if (l == Long.MAX_VALUE) {
            counter.set(0);
        }
        return l;
    }

    private long getMinCountAllowed(long currentValue, long delta) {
        final long minCount = currentValue - delta;
        if (minCount > 0) {
            return minCount;
        } else {
            // should not be negative
            return 0;
        }
    }

    private void removeEntriesOlderThan(long minValue) {
        final Iterator<Map.Entry<Long, List<String>>> iterator = activeKeys.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Long, List<String>> entry = iterator.next();
            final Long key = entry.getKey();

            if (key > minValue) {
                break;
            }

            iterator.remove();
            final List<String> value = entry.getValue();
            for (String iteratorKey : value) {
                iterators.remove(iteratorKey);
            }
        }
    }
}
