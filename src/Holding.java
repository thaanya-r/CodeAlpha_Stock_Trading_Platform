public class Holding {
    private final String symbol;
    private int quantity;
    private double averagePrice;

    public Holding(String symbol, int quantity, double averagePrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getAveragePrice() { return averagePrice; }

    public void buyMore(int newQuantity, double price) {
        double totalCost = (quantity * averagePrice) + (newQuantity * price);
        quantity += newQuantity;
        averagePrice = totalCost / quantity;
    }

    public boolean sell(int sellQuantity) {
        if (sellQuantity <= 0 || sellQuantity > quantity) {
            return false;
        }
        quantity -= sellQuantity;
        return true;
    }
}
