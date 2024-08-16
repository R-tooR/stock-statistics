package stock.tracker.stock_statistics.models;

import java.util.List;

public class SymbolBatch {

    private String symbol;
    private List<Double> values;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "SymbolBatch{" +
                "symbol='" + symbol + '\'' +
                '}';
    }
}
