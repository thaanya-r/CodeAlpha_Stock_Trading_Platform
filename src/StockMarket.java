import java.util.LinkedHashMap;
import java.util.Map;

public class StockMarket {
    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    public StockMarket() {
        addDefaultStocks();
    }

    private void addDefaultStocks() {
        stocks.put("TCS", new Stock("TCS", "Tata Consultancy Services", 3450));
        stocks.put("INFY", new Stock("INFY", "Infosys", 1650));
        stocks.put("RELIANCE", new Stock("RELIANCE", "Reliance Industries", 2980));
        stocks.put("HDFCBANK", new Stock("HDFCBANK", "HDFC Bank", 1720));
        stocks.put("ITC", new Stock("ITC", "ITC Limited", 520));
    }

    public void displayMarket() {
        System.out.println("\n========== MARKET DATA ==========");
        System.out.printf("%-12s %-28s %12s%n", "Symbol", "Company", "Price");
        System.out.println("-----------------------------------------------");

        for (Stock stock : stocks.values()) {
            System.out.printf("%-12s %-28s ₹%10.2f%n",
                    stock.getSymbol(), stock.getCompanyName(), stock.getPrice());
        }
    }

    public Stock findStock(String symbol) {
        if (symbol == null) return null;
        return stocks.get(symbol.toUpperCase());
    }

    public Map<String, Stock> getStocks() {
        return stocks;
    }
}
