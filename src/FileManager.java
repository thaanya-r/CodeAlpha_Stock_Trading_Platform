import java.io.*;

public class FileManager {
    private final File transactionFile = new File("data/transactions.txt");

    public void saveTransactions(User user) {
        try {
            File parent = transactionFile.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFile))) {
                for (Transaction transaction : user.getTransactions()) {
                    writer.write(transaction.toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Could not save transactions: " + e.getMessage());
        }
    }
}
