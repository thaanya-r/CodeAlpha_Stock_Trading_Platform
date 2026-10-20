import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final String type;
    private final String symbol;
    private final int quantity;
    private final double price;
    private final LocalDateTime dateTime;

    public Transaction(String type, String symbol, int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return type + " | " + symbol + " | Qty: " + quantity +
               " | Price: " + String.format("%.2f", price) +
               " | " + dateTime.format(format);
    }

    public String toFileString() {
        return type + "," + symbol + "," + quantity + "," + price + "," + dateTime;
    }
}
