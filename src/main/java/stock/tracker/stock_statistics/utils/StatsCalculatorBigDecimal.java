package stock.tracker.stock_statistics.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Stream;

public class StatsCalculatorBigDecimal<T extends BigDecimal> {
    private final ArrayDeque<T> deque;
    private final PriorityQueue<T> minHeap;
    private final PriorityQueue<T> maxHeap;
    private BigDecimal sum;
    private BigDecimal sumOfSquares;
    private final int MAX_SIZE;
    private BigDecimal sizeBigDecimal;

    public StatsCalculatorBigDecimal(int size) {
        MAX_SIZE = size;
        deque = new ArrayDeque<>();
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        sum = BigDecimal.ZERO;
        sumOfSquares = BigDecimal.ZERO;
        sizeBigDecimal = BigDecimal.ZERO;
    }

    public void addElement(T element) {
        if (deque.size() == MAX_SIZE) {
            var el = deque.removeFirst();
            minHeap.remove(el);
            maxHeap.remove(el);
            sum = sum.subtract(el);
            sumOfSquares = sumOfSquares.subtract(el.multiply(el));

        }
        deque.addLast(element);
        minHeap.add(element);
        maxHeap.add(element);
        sum = sum.add(element);
        sumOfSquares = sumOfSquares.add(element.multiply(element));
        sizeBigDecimal = BigDecimal.valueOf(deque.size());
    }

    public Optional<T> getMin() {
        return Optional.ofNullable(minHeap.peek());
    }

    public Optional<T> getMax() {
        return Optional.ofNullable(maxHeap.peek());
    }

    public BigDecimal getMean() {
        return sum.divide(sizeBigDecimal, MathContext.DECIMAL32);
    }

    public BigDecimal getVariance() {
        var mean = getMean();
        return sumOfSquares.divide(sizeBigDecimal, MathContext.DECIMAL32).subtract(mean.multiply(mean));
    }

    public Stream<T> getStream() {
        return deque.stream();
    }

    public int size() {
        return deque.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsCalculatorBigDecimal<?> that = (StatsCalculatorBigDecimal<?>) o;
        return MAX_SIZE == that.MAX_SIZE && Objects.equals(deque, that.deque);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deque, MAX_SIZE);
    }
}
