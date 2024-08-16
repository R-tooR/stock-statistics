package stock.tracker.stock_statistics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import stock.tracker.stock_statistics.utils.StatsCalculatorBigDecimal;

import java.math.BigDecimal;
import java.util.*;

public class StatsBigDecimal implements Stats<BigDecimal> {
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal last;
    private BigDecimal avg;
    private BigDecimal var;
    @JsonIgnore //to check how fast buffers are increasing
    private Integer size;
    @JsonIgnore
    private final long pointsConsidered;
    @JsonIgnore
    private String version;
    @JsonIgnore
    private final StatsCalculatorBigDecimal<BigDecimal> recentPoints;


    public StatsBigDecimal(long pointsConsidered) {
        this(BigDecimal.valueOf(Double.MAX_VALUE), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, pointsConsidered, "EMPTY");
    }

    private StatsBigDecimal(BigDecimal min, BigDecimal max, BigDecimal last, BigDecimal avg, BigDecimal var, long pointsConsidered, String version) {
        this.min = min;
        this.max = max;
        this.last = last;
        this.avg = avg;
        this.var = var;
        this.pointsConsidered = pointsConsidered;
        this.version = version;
        recentPoints = new StatsCalculatorBigDecimal<>((int) this.pointsConsidered);
    }

    @Override
    public void updateData(final List<BigDecimal> values) {
        values.forEach(recentPoints::addElement);

        this.min = recentPoints.getMin().orElse(BigDecimal.ZERO);
        this.max = recentPoints.getMax().orElse(BigDecimal.ZERO);

        this.last = values.getLast();
        this.avg = recentPoints.getMean();

        this.var = recentPoints.getVariance();
        this.version = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getLast() {
        return last;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public BigDecimal getVar() {
        return var;
    }

    public String getVersion() {
        return version;
    }

    public Integer getSize() {
        size = recentPoints.size();
        return size;
    }

    public static StatsBigDecimal empty() {
        return new StatsBigDecimal(BigDecimal.valueOf(Double.MAX_VALUE), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, "EMPTY");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsBigDecimal statsBigDecimal = (StatsBigDecimal) o;
        return pointsConsidered == statsBigDecimal.pointsConsidered && Objects.equals(min, statsBigDecimal.min) && Objects.equals(max, statsBigDecimal.max) && Objects.equals(last, statsBigDecimal.last) && Objects.equals(avg, statsBigDecimal.avg) && Objects.equals(var, statsBigDecimal.var) && Objects.equals(version, statsBigDecimal.version) && Objects.equals(recentPoints, statsBigDecimal.recentPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, last, avg, var, pointsConsidered, version, recentPoints);
    }

    @Override
    public String toString() {
        return "Stats{" +
                "min=" + min +
                ", max=" + max +
                ", last=" + last +
                ", avg=" + avg +
                ", var=" + var +
                ", size=" + size +
                '}';
    }
}
