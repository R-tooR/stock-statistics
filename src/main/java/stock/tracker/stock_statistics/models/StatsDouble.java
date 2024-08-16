package stock.tracker.stock_statistics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import stock.tracker.stock_statistics.utils.StatsCalculatorDouble;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StatsDouble implements Stats<Double> {
    private Double min;
    private Double max;
    private Double last;
    private BigDecimal avg;
    private BigDecimal var;
    @JsonIgnore //to check how fast buffers are increasing
    private Integer size;
    @JsonIgnore
    private final long pointsConsidered;
    @JsonIgnore
    private String version;
    @JsonIgnore
    private final StatsCalculatorDouble recentPoints;


    public StatsDouble(long pointsConsidered) {
        this(Double.MAX_VALUE,0., 0., BigDecimal.ZERO, BigDecimal.ZERO, pointsConsidered, "EMPTY");
    }

    private StatsDouble(Double min, Double max, Double last, BigDecimal avg, BigDecimal var, long pointsConsidered, String version) {
        this.min = min;
        this.max = max;
        this.last = last;
        this.avg = avg;
        this.var = var;
        this.pointsConsidered = pointsConsidered;
        this.version = version;
        recentPoints = new StatsCalculatorDouble((int) this.pointsConsidered);
    }

    @Override
    public void updateData(final List<Double> values) {
        values.forEach(recentPoints::addElement);

        this.min = recentPoints.getMin().orElse(0.);
        this.max = recentPoints.getMax().orElse(0.);

        this.last = values.getLast();
        this.avg = recentPoints.getMean();

        this.var = recentPoints.getVariance();
        this.version = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getLast() {
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

    public static StatsDouble empty() {
        return new StatsDouble(Double.MAX_VALUE, 0., 0., BigDecimal.ZERO, BigDecimal.ZERO, 0, "EMPTY");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsDouble stats = (StatsDouble) o;
        return pointsConsidered == stats.pointsConsidered && Objects.equals(min, stats.min) && Objects.equals(max, stats.max) && Objects.equals(last, stats.last) && Objects.equals(avg, stats.avg) && Objects.equals(var, stats.var) && Objects.equals(version, stats.version) && Objects.equals(recentPoints, stats.recentPoints);
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
