package stock.tracker.stock_statistics.models;

import java.util.Objects;

public final class SymbolAndValue {
    private String symbol;
    private Integer k;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }

    public String getStringKey() {
        return symbol.concat("-").concat(String.valueOf(k));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolAndValue that = (SymbolAndValue) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(k, that.k);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, k);
    }

    @Override
    public String toString() {
        return "SymbolAndValue{" +
                "symbol='" + symbol + '\'' +
                ", k=" + k +
                '}';
    }
}
