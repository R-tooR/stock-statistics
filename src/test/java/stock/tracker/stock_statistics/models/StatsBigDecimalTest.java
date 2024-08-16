package stock.tracker.stock_statistics.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StatsBigDecimalTest {

    @Test
    public void testUpdatesStates() {
        var stats = new StatsBigDecimal(10);

        stats.updateData(Stream.of(1.2, 1.3, 1.1).map(BigDecimal::valueOf).toList());

        assertEquals(BigDecimal.valueOf(1.1), stats.getMin());
        assertEquals(BigDecimal.valueOf(1.3), stats.getMax());
        assertEquals(BigDecimal.valueOf(1.1), stats.getLast());
        assertEquals(BigDecimal.valueOf(1.2), stats.getAvg());
        assertEquals(BigDecimal.valueOf(0.006667), stats.getVar());
        assertNotNull(stats.getVersion());
    }

    @Test
    public void testUpdatesStatusesWhenPointsConsideredExceeds() {
        var stats = new StatsBigDecimal(5);

        stats.updateData(List.of(1.1, 1.3, 1.2).stream().map(BigDecimal::valueOf).toList());
        stats.updateData(List.of(1.5, 1.4, 1.6).stream().map(BigDecimal::valueOf).toList());

        assertEquals(BigDecimal.valueOf(1.2), stats.getMin());
        assertEquals(BigDecimal.valueOf(1.6), stats.getMax());
        assertEquals(BigDecimal.valueOf(1.6), stats.getLast());
        assertEquals(BigDecimal.valueOf(1.4), stats.getAvg());
        assertEquals(BigDecimal.valueOf(0.02), stats.getVar());
        assertNotNull(stats.getVersion());

    }
}
