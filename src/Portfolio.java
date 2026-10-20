import java.util.*;

public class Portfolio {
    private final Map<String, Holding> holdings = new LinkedHashMap<>();

    public boolean buy(Stock stock, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        Holding holding = holdings.get(stock.getSymbol());

        if (holding == null) {
            holdings.put(stock.getSymbol(),
                    new Holding(stock.getSymbol(), quantity, stock.getPrice()));
        } else {
            holding.buyMore(quantity, stock.getPrice());
        }

        return true;
    }

    public boolean sell(String symbol, int quantity) {
        Holding holding = holdings.get(symbol);

        if (holding == null || !holding.sell(quantity)) {
            return false;
        }

        if (holding.getQuantity() == 0) {
            holdings.remove(symbol);
        }

        return true;
    }

    public Holding getHolding(String symbol) {
        return holdings.get(symbol);
    }

    public Collection<Holding> getHoldings() {
        return holdings.values();
    }

    public double calculateInvestment() {
        double total = 0;
        for (Holding holding : holdings.values()) {
            total += holding.getQuantity() * holding.getAveragePrice();
        }
        return total;
    }

    public double calculateCurrentValue(Map<String, Stock> market) {
        double total = 0;
        for (Holding holding : holdings.values()) {
            Stock stock = market.get(holding.getSymbol());
            if (stock != null) {
                total += holding.getQuantity() * stock.getPrice();
            }
        }
        return total;
    }
}
