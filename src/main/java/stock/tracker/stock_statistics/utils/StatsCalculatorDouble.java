package stock.tracker.stock_statistics.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Stream;

public class StatsCalculatorDouble {
    private final ArrayDeque<Double> deque;
    private final PriorityQueue<Double> minHeap;
    private final PriorityQueue<Double> maxHeap;
    private BigDecimal sum;
    private BigDecimal sumOfSquares;
    private final int MAX_SIZE;
    private BigDecimal sizeBigDecimal;

//    private static final int MAX_INIT_SIZE = 1_000_000;

    public StatsCalculatorDouble(int size) {
        MAX_SIZE = size;
//        deque = new ArrayDeque<>(Math.min(MAX_INIT_SIZE, MAX_SIZE));
        deque = new ArrayDeque<>();
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        sum = BigDecimal.ZERO;
        sumOfSquares = BigDecimal.ZERO;
        sizeBigDecimal = BigDecimal.ZERO;
    }

    public void addElement(Double element) {
        if (deque.size() == MAX_SIZE) {
            var el = deque.removeFirst();
            var bigDecimal = BigDecimal.valueOf(el);
            minHeap.remove(el);
            maxHeap.remove(el);
            sum = sum.subtract(bigDecimal);
            sumOfSquares = sumOfSquares.subtract(bigDecimal.multiply(bigDecimal));

        }
        var bigDecimal = BigDecimal.valueOf(element);
        deque.addLast(element);
        minHeap.add(element);
        maxHeap.add(element);
        sum = sum.add(bigDecimal);
        sumOfSquares = sumOfSquares.add(bigDecimal.multiply(bigDecimal));
        sizeBigDecimal = BigDecimal.valueOf(deque.size());
    }

    public Optional<Double> getMin() {
        return Optional.ofNullable(minHeap.peek());
    }

    public Optional<Double> getMax() {
        return Optional.ofNullable(maxHeap.peek());
    }

    public BigDecimal getMean() {
        return sum.divide(sizeBigDecimal, MathContext.DECIMAL32);
    }

    public BigDecimal getVariance() {
        var mean = getMean();
        return sumOfSquares.divide(sizeBigDecimal, MathContext.DECIMAL32).subtract(mean.multiply(mean));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsCalculatorDouble that = (StatsCalculatorDouble) o;
        return MAX_SIZE == that.MAX_SIZE && Objects.equals(deque, that.deque) && Objects.equals(minHeap, that.minHeap) && Objects.equals(maxHeap, that.maxHeap) && Objects.equals(sum, that.sum) && Objects.equals(sumOfSquares, that.sumOfSquares) && Objects.equals(sizeBigDecimal, that.sizeBigDecimal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deque, minHeap, maxHeap, sum, sumOfSquares, MAX_SIZE, sizeBigDecimal);
    }

    public Integer size() {
        return deque.size();
    }
}
