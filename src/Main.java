import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StockMarket market = new StockMarket();
    private static final User user = new User(1001, "Demo User", 100000);
    private static final FileManager fileManager = new FileManager();

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("       JAVA STOCK TRADING PLATFORM");
        System.out.println("======================================");
        System.out.println("Welcome, " + user.getName());

        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> market.displayMarket();
                case 2 -> buyStock();
                case 3 -> sellStock();
                case 4 -> showPortfolio();
                case 5 -> showTransactions();
                case 6 -> System.out.printf("Available Cash: ₹%.2f%n", user.getBalance());
                case 7 -> {
                    fileManager.saveTransactions(user);
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n1. View Market");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. Transaction History");
        System.out.println("6. Account Balance");
        System.out.println("7. Exit");
    }

    private static void buyStock() {
        String symbol = readText("Enter stock symbol: ").toUpperCase();
        Stock stock = market.findStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        int quantity = readInt("Enter quantity: ");
        double total = stock.getPrice() * quantity;

        if (user.buyStock(stock, quantity)) {
            System.out.printf("Bought %d %s for ₹%.2f%n", quantity, symbol, total);
        } else {
            System.out.println("Purchase failed. Check quantity or available balance.");
        }
    }

    private static void sellStock() {
        String symbol = readText("Enter stock symbol: ").toUpperCase();
        Stock stock = market.findStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        int quantity = readInt("Enter quantity: ");

        if (user.sellStock(stock, quantity)) {
            System.out.printf("Sold %d %s for ₹%.2f%n",
                    quantity, symbol, stock.getPrice() * quantity);
        } else {
            System.out.println("Sale failed. Check your portfolio quantity.");
        }
    }

    private static void showPortfolio() {
        System.out.println("\n========== MY PORTFOLIO ==========");

        if (user.getPortfolio().getHoldings().isEmpty()) {
            System.out.println("No stocks in portfolio.");
            return;
        }

        System.out.printf("%-12s %8s %15s %15s%n",
                "Stock", "Qty", "Avg Price", "Current Value");

        for (Holding holding : user.getPortfolio().getHoldings()) {
            Stock stock = market.findStock(holding.getSymbol());
            double currentValue = holding.getQuantity() * stock.getPrice();

            System.out.printf("%-12s %8d ₹%13.2f ₹%13.2f%n",
                    holding.getSymbol(),
                    holding.getQuantity(),
                    holding.getAveragePrice(),
                    currentValue);
        }

        double investment = user.getPortfolio().calculateInvestment();
        double currentValue = user.getPortfolio().calculateCurrentValue(market.getStocks());
        double profitLoss = currentValue - investment;

        System.out.println("---------------------------------------------");
        System.out.printf("Investment : ₹%.2f%n", investment);
        System.out.printf("Value      : ₹%.2f%n", currentValue);
        System.out.printf("Profit/Loss: ₹%.2f%n", profitLoss);
    }

    private static void showTransactions() {
        System.out.println("\n========== TRANSACTIONS ==========");

        if (user.getTransactions().isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (Transaction transaction : user.getTransactions()) {
            System.out.println(transaction);
        }
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readText(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}
