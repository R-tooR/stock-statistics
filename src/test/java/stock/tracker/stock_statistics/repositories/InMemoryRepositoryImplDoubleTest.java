package stock.tracker.stock_statistics.repositories;

import org.junit.jupiter.api.Test;
import stock.tracker.stock_statistics.models.StatsDouble;
import stock.tracker.stock_statistics.models.SymbolAndValue;
import stock.tracker.stock_statistics.models.SymbolBatch;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryRepositoryImplDoubleTest {

    @Test
    public void testAddBatch() {
        var repository = new InMemoryRepositoryImplDouble();

        var symbAndVal = new SymbolAndValue();
        symbAndVal.setK(1);
        symbAndVal.setSymbol("AAPL");

        var symbBatch = new SymbolBatch();
        symbBatch.setSymbol("AAPL");
        symbBatch.setValues(List.of(1.1, 1.3, 1.2));

        repository.upsert(symbBatch);
        var stats = repository.select(symbAndVal);

        assertNotNull(stats);
        assertNotEquals(StatsDouble.empty(), stats);
    }
}
