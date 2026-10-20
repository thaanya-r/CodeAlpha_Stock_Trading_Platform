import java.util.ArrayList;
import java.util.List;

public class User {
    private final int userId;
    private final String name;
    private double balance;
    private final Portfolio portfolio = new Portfolio();
    private final List<Transaction> transactions = new ArrayList<>();

    public User(int userId, String name, double balance) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public boolean buyStock(Stock stock, int quantity) {
        double total = stock.getPrice() * quantity;

        if (quantity <= 0 || total > balance) {
            return false;
        }

        portfolio.buy(stock, quantity);
        balance -= total;
        transactions.add(new Transaction("BUY", stock.getSymbol(), quantity, stock.getPrice()));
        return true;
    }

    public boolean sellStock(Stock stock, int quantity) {
        Holding holding = portfolio.getHolding(stock.getSymbol());

        if (holding == null || quantity <= 0 || quantity > holding.getQuantity()) {
            return false;
        }

        portfolio.sell(stock.getSymbol(), quantity);
        balance += stock.getPrice() * quantity;
        transactions.add(new Transaction("SELL", stock.getSymbol(), quantity, stock.getPrice()));
        return true;
    }

    public double getBalance() { return balance; }
    public Portfolio getPortfolio() { return portfolio; }
    public List<Transaction> getTransactions() { return transactions; }
    public String getName() { return name; }
    public int getUserId() { return userId; }
}
