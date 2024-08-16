package stock.tracker.stock_statistics.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StatsDoubleTest {

    @Test
    public void testUpdatesStates() {
        var stats = new StatsDouble(10);

        stats.updateData(List.of(1.2, 1.3, 1.1));

        assertEquals(1.1, stats.getMin());
        assertEquals(1.3, stats.getMax());
        assertEquals(1.1, stats.getLast());
        assertEquals(BigDecimal.valueOf(1.2), stats.getAvg());
        assertEquals(BigDecimal.valueOf(0.006667), stats.getVar());
        assertNotNull(stats.getVersion());
    }

    @Test
    public void testUpdatesStatusesWhenPointsConsideredExceeds() {
        var stats = new StatsDouble(5);

        stats.updateData(List.of(1.1, 1.3, 1.2));
        stats.updateData(List.of(1.5, 1.4, 1.6));

        assertEquals(1.2, stats.getMin());
        assertEquals(1.6, stats.getMax());
        assertEquals(1.6, stats.getLast());
        assertEquals(BigDecimal.valueOf(1.4), stats.getAvg());
        assertEquals(BigDecimal.valueOf(0.02), stats.getVar());
        assertNotNull(stats.getVersion());

    }
}
