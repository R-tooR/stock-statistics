package stock.tracker.stock_statistics.repositories;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stock.tracker.stock_statistics.models.Stats;
import stock.tracker.stock_statistics.models.SymbolAndValue;
import stock.tracker.stock_statistics.models.SymbolBatch;

@Repository
@Primary
public class InMemoryRepositoryImplDoubleAsync extends InMemoryRepositoryImplDouble {

    public Mono<Void> upsert(Mono<SymbolBatch> symbolBatchMono) {
        return symbolBatchMono.doOnNext(super::upsertAsync).then();
    }

    public Mono<Stats<Double>> select(Mono<SymbolAndValue> symbolAndValueMono) {
        return symbolAndValueMono.map(super::select);
    }

}
