package stock.tracker.stock_statistics.models;

import java.math.BigDecimal;
import java.util.List;

public interface Stats<T extends Number> {
    void updateData(List<T> data);
    String getVersion();
    T getMin();
    T getMax();
    T getLast();
    BigDecimal getAvg();
    BigDecimal getVar();
}
