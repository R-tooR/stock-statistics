package stock.tracker.stock_statistics.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import stock.tracker.stock_statistics.models.Stats;
import stock.tracker.stock_statistics.models.StatsDouble;
import stock.tracker.stock_statistics.models.SymbolAndValue;
import stock.tracker.stock_statistics.models.SymbolBatch;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
public class InMemoryRepositoryImplDouble implements InMemoryRepository<Double> {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryRepositoryImplDouble.class);

    // assuming that 1 symbol won't be updated simultaneously we can use faster HashMap instead of ConcurrentHashMap
    protected final Map<String, Stats<Double>> currentState = new HashMap<>();
    protected final Map<String, Integer> windows = Map.of(
            "1", 10,
            "2", 100,
            "3", 1000,
            "4", 10000,
            "5", 100000,
            "6", 1000000,
            "7", 10000000,
            "8", 100000000
    );

    public void upsert(SymbolBatch symbolBatch) {
        if (symbolBatch == null || symbolBatch.getValues() == null || symbolBatch.getSymbol() == null) throw new IllegalArgumentException();
        logger.info("Upload {}", symbolBatch.getSymbol());
        for (Map.Entry<String, Integer> e : windows.entrySet()) {
            String key = symbolBatch.getSymbol().concat("-").concat(e.getKey());
            if (!currentState.containsKey(key)) {
                currentState.put(key, new StatsDouble(e.getValue()));
            }
            currentState.get(key).updateData(symbolBatch.getValues());
        }
    }

    public CompletableFuture<Void> upsertAsync(SymbolBatch symbolBatch) {
        return CompletableFuture.runAsync(() -> this.upsert(symbolBatch));
    }

    public Stats<Double> select(SymbolAndValue symbolAndValue) {
        logger.info("{}->{}", symbolAndValue.getStringKey(), currentState.get(symbolAndValue.getStringKey()));
        return currentState.getOrDefault(symbolAndValue.getStringKey(), StatsDouble.empty());
    }

    // only to manual tests to see asynchronicity
    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
