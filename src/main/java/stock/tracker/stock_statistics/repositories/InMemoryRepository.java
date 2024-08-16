package stock.tracker.stock_statistics.repositories;

import stock.tracker.stock_statistics.models.Stats;
import stock.tracker.stock_statistics.models.SymbolAndValue;
import stock.tracker.stock_statistics.models.SymbolBatch;

import java.util.concurrent.CompletableFuture;

public interface InMemoryRepository<T extends Number> {
    void upsert(SymbolBatch symbolBatch);
    CompletableFuture<Void> upsertAsync(SymbolBatch symbolBatch);
    Stats<T> select(SymbolAndValue symbolAndValue);
}
