import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class StockDashboardGUI extends JFrame {

    // ============================================================
    // BACKEND
    // ============================================================

    private final StockMarket market = new StockMarket();
    private final User user = new User(1001, "Demo User", 100000);
    private final FileManager fileManager = new FileManager();

    // ============================================================
    // COLORS
    // ============================================================

    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color WHITE = Color.WHITE;
    private final Color DARK = new Color(30, 35, 42);
    private final Color SECONDARY = new Color(105, 112, 122);
    private final Color GREEN = new Color(24, 155, 95);
    private final Color RED = new Color(215, 70, 70);
    private final Color BLUE = new Color(55, 110, 220);
    private final Color SIDEBAR = new Color(28, 34, 42);
    private final Color BORDER = new Color(225, 229, 235);

    // ============================================================
    // COMPONENTS
    // ============================================================

    private JLabel portfolioValueLabel;
    private JLabel cashLabel;
    private JLabel investmentLabel;
    private JLabel profitLossLabel;

    private JTable holdingsTable;
    private JTable marketTable;
    private JTable transactionTable;

    private JPanel contentPanel;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public StockDashboardGUI() {

        setTitle("CodeTrade - Stock Trading Platform");
        setSize(1250, 780);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        buildUI();
        showDashboard();
    }

    // ============================================================
    // MAIN UI
    // ============================================================

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);

        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);

        setContentPane(root);
    }

    // ============================================================
    // SIDEBAR
    // ============================================================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BorderLayout());

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(28, 25, 25, 20));

        JLabel logo = new JLabel("CODETRADE");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Stock Trading Platform");
        subtitle.setForeground(new Color(170, 177, 188));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        logoPanel.add(logo);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(subtitle);

        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Navigation
        JPanel navigation = new JPanel();
        navigation.setBackground(SIDEBAR);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));
        navigation.setBorder(new EmptyBorder(10, 12, 10, 12));

        JButton dashboardButton = createNavButton("Dashboard");
        JButton marketButton = createNavButton("Market");
        JButton portfolioButton = createNavButton("Portfolio");
        JButton transactionButton = createNavButton("Transactions");

        dashboardButton.addActionListener(e -> showDashboard());
        marketButton.addActionListener(e -> showMarket());
        portfolioButton.addActionListener(e -> showPortfolio());
        transactionButton.addActionListener(e -> showTransactions());

        navigation.add(dashboardButton);
        navigation.add(marketButton);
        navigation.add(portfolioButton);
        navigation.add(transactionButton);

        navigation.add(Box.createVerticalStrut(25));

        JLabel tradeTitle = new JLabel("TRADING");
        tradeTitle.setForeground(new Color(140, 148, 160));
        tradeTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tradeTitle.setBorder(new EmptyBorder(10, 15, 10, 10));

        navigation.add(tradeTitle);

        JButton buyButton = createTradeButton("BUY STOCK", GREEN);
        JButton sellButton = createTradeButton("SELL STOCK", RED);

        buyButton.addActionListener(e -> showBuyDialog());
        sellButton.addActionListener(e -> showSellDialog());

        navigation.add(buyButton);
        navigation.add(Box.createVerticalStrut(10));
        navigation.add(sellButton);

        sidebar.add(navigation, BorderLayout.CENTER);

        // Exit
        JButton exitButton = createNavButton("Exit");
        exitButton.addActionListener(e -> exitApplication());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(SIDEBAR);
        bottom.setBorder(new EmptyBorder(10, 12, 20, 12));
        bottom.add(exitButton, BorderLayout.CENTER);

        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    // ============================================================
    // MAIN AREA
    // ============================================================

    private JPanel createMainArea() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Stock Trading Dashboard");
        title.setForeground(DARK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));

        JLabel userLabel = new JLabel(
                "Demo User   •   ● Market Open"
        );
        userLabel.setForeground(SECONDARY);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        header.add(title, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        main.add(contentPanel, BorderLayout.CENTER);

        return main;
    }

    // ============================================================
    // DASHBOARD
    // ============================================================

    private void showDashboard() {

        contentPanel.removeAll();

        JPanel page = new JPanel(new BorderLayout(0, 20));
        page.setBackground(BACKGROUND);

        // Summary cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
        cards.setBackground(BACKGROUND);

        double investment =
                user.getPortfolio().calculateInvestment();

        double currentValue =
                user.getPortfolio()
                        .calculateCurrentValue(market.getStocks());

        double profitLoss = currentValue - investment;

        portfolioValueLabel =
                createValueLabel("Portfolio Value",
                        "₹" + money(currentValue));

        cashLabel =
                createValueLabel("Available Cash",
                        "₹" + money(user.getBalance()));

        investmentLabel =
                createValueLabel("Investment",
                        "₹" + money(investment));

        profitLossLabel =
                createValueLabel("Profit / Loss",
                        "₹" + money(profitLoss));

        if (profitLoss >= 0) {
            profitLossLabel.setForeground(GREEN);
        } else {
            profitLossLabel.setForeground(RED);
        }

        cards.add(createCard(
                "PORTFOLIO VALUE",
                "₹" + money(currentValue),
                BLUE));

        cards.add(createCard(
                "AVAILABLE CASH",
                "₹" + money(user.getBalance()),
                GREEN));

        cards.add(createCard(
                "INVESTMENT",
                "₹" + money(investment),
                new Color(140, 90, 200)));

        cards.add(createCard(
                "PROFIT / LOSS",
                "₹" + money(profitLoss),
                profitLoss >= 0 ? GREEN : RED));

        page.add(cards, BorderLayout.NORTH);

        // Middle
        JPanel middle = new JPanel(new GridLayout(1, 2, 20, 0));
        middle.setBackground(BACKGROUND);

        middle.add(createPortfolioPanel());
        middle.add(createMarketPanel());

        page.add(middle, BorderLayout.CENTER);

        // Bottom
        page.add(createTransactionPanel(), BorderLayout.SOUTH);

        contentPanel.add(page);

        refreshTables();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ============================================================
    // PORTFOLIO PANEL
    // ============================================================

    private JPanel createPortfolioPanel() {

        JPanel panel = createWhitePanel();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(WHITE);

        JLabel title = new JLabel("My Holdings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(DARK);

        JButton view = smallButton("View Portfolio");
        view.addActionListener(e -> showPortfolio());

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(view, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        holdingsTable = createTable(
                new String[]{
                        "Stock",
                        "Qty",
                        "Avg Price",
                        "Current",
                        "Value"
                });

        JScrollPane scroll = new JScrollPane(holdingsTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // MARKET PANEL
    // ============================================================

    private JPanel createMarketPanel() {

        JPanel panel = createWhitePanel();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(WHITE);

        JLabel title = new JLabel("Market Watch");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(DARK);

        JButton view = smallButton("View Market");
        view.addActionListener(e -> showMarket());

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(view, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        marketTable = createTable(
                new String[]{
                        "Symbol",
                        "Company",
                        "Price"
                });

        JScrollPane scroll = new JScrollPane(marketTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // TRANSACTION PANEL
    // ============================================================

    private JPanel createTransactionPanel() {

        JPanel panel = createWhitePanel();
        panel.setPreferredSize(new Dimension(0, 190));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(WHITE);

        JLabel title = new JLabel("Recent Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(DARK);

        JButton view = smallButton("View All");
        view.addActionListener(e -> showTransactions());

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(view, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        transactionTable = createTable(
                new String[]{
                        "Type",
                        "Stock",
                        "Quantity",
                        "Price",
                        "Date / Time"
                });

        JScrollPane scroll = new JScrollPane(transactionTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // MARKET PAGE
    // ============================================================

    private void showMarket() {

        contentPanel.removeAll();

        JPanel page = new JPanel(new BorderLayout(0, 20));
        page.setBackground(BACKGROUND);

        JLabel title = new JLabel("Market");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(DARK);

        JLabel description = new JLabel(
                "Available stocks and current market prices"
        );
        description.setForeground(SECONDARY);

        JPanel heading = new JPanel();
        heading.setBackground(BACKGROUND);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(description);

        page.add(heading, BorderLayout.NORTH);

        JPanel card = createWhitePanel();

        JTable table = createTable(
                new String[]{
                        "Symbol",
                        "Company",
                        "Current Price",
                        "Action"
                });

        DefaultTableModel model =
                (DefaultTableModel) table.getModel();

        for (Stock stock : market.getStocks().values()) {

            model.addRow(new Object[]{
                    stock.getSymbol(),
                    stock.getCompanyName(),
                    "₹" + money(stock.getPrice()),
                    "BUY / SELL"
            });
        }

        card.add(new JScrollPane(table), BorderLayout.CENTER);

        page.add(card, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(BACKGROUND);

        JButton buy = createTradeButton(
                "BUY STOCK", GREEN);

        JButton sell = createTradeButton(
                "SELL STOCK", RED);

        buy.addActionListener(e -> showBuyDialog());
        sell.addActionListener(e -> showSellDialog());

        buttons.add(buy);
        buttons.add(sell);

        page.add(buttons, BorderLayout.SOUTH);

        contentPanel.add(page);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ============================================================
    // PORTFOLIO PAGE
    // ============================================================

    private void showPortfolio() {

        contentPanel.removeAll();

        JPanel page = new JPanel(new BorderLayout(0, 20));
        page.setBackground(BACKGROUND);

        JLabel title = new JLabel("My Portfolio");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(DARK);

        page.add(title, BorderLayout.NORTH);

        JPanel card = createWhitePanel();

        JTable table = createTable(
                new String[]{
                        "Stock",
                        "Quantity",
                        "Average Price",
                        "Current Price",
                        "Current Value"
                });

        DefaultTableModel model =
                (DefaultTableModel) table.getModel();

        for (Holding holding :
                user.getPortfolio().getHoldings()) {

            Stock stock =
                    market.findStock(holding.getSymbol());

            if (stock == null) {
                continue;
            }

            double currentValue =
                    holding.getQuantity()
                            * stock.getPrice();

            model.addRow(new Object[]{
                    holding.getSymbol(),
                    holding.getQuantity(),
                    "₹" + money(
                            holding.getAveragePrice()),
                    "₹" + money(stock.getPrice()),
                    "₹" + money(currentValue)
            });
        }

        card.add(new JScrollPane(table),
                BorderLayout.CENTER);

        page.add(card, BorderLayout.CENTER);

        contentPanel.add(page);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ============================================================
    // TRANSACTIONS PAGE
    // ============================================================

    private void showTransactions() {

        contentPanel.removeAll();

        JPanel page = new JPanel(new BorderLayout(0, 20));
        page.setBackground(BACKGROUND);

        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(DARK);

        page.add(title, BorderLayout.NORTH);

        JPanel card = createWhitePanel();

        JTable table = createTable(
                new String[]{
                        "Transaction"
                });

        DefaultTableModel model =
                (DefaultTableModel) table.getModel();

        for (Transaction transaction :
                user.getTransactions()) {

            model.addRow(
                    new Object[]{transaction.toString()});
        }

        card.add(new JScrollPane(table),
                BorderLayout.CENTER);

        page.add(card, BorderLayout.CENTER);

        contentPanel.add(page);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ============================================================
    // BUY DIALOG
    // ============================================================

    private void showBuyDialog() {

        JComboBox<String> stockBox =
                new JComboBox<>();

        for (Stock stock : market.getStocks().values()) {
            stockBox.addItem(stock.getSymbol());
        }

        JTextField quantityField =
                new JTextField();

        JLabel priceLabel =
                new JLabel("₹0.00");

        JLabel totalLabel =
                new JLabel("₹0.00");

        stockBox.addActionListener(e -> {

            Stock stock =
                    market.findStock(
                            (String) stockBox.getSelectedItem());

            if (stock != null) {
                priceLabel.setText(
                        "₹" + money(stock.getPrice()));

                updateTotal(
                        stock,
                        quantityField,
                        totalLabel);
            }
        });

        quantityField.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                updateTotalFromDialog(
                                        stockBox,
                                        quantityField,
                                        totalLabel);
                            }

                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                updateTotalFromDialog(
                                        stockBox,
                                        quantityField,
                                        totalLabel);
                            }

                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                updateTotalFromDialog(
                                        stockBox,
                                        quantityField,
                                        totalLabel);
                            }
                        });

        JPanel form = createTradeForm(
                "BUY STOCK",
                stockBox,
                quantityField,
                priceLabel,
                totalLabel);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Buy Stock",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            try {

                int quantity =
                        Integer.parseInt(
                                quantityField.getText().trim());

                if (quantity <= 0) {
                    showError("Quantity must be greater than 0.");
                    return;
                }

                String symbol =
                        (String) stockBox.getSelectedItem();

                Stock stock =
                        market.findStock(symbol);

                if (stock == null) {
                    showError("Stock not found.");
                    return;
                }

                double total =
                        stock.getPrice() * quantity;

                if (total > user.getBalance()) {
                    showError(
                            "Insufficient balance.\n\n" +
                            "Required: ₹" + money(total) +
                            "\nAvailable: ₹" +
                            money(user.getBalance()));
                    return;
                }

                boolean success =
                        user.buyStock(stock, quantity);

                if (success) {

                    fileManager.saveTransactions(user);

                    JOptionPane.showMessageDialog(
                            this,
                            "Purchase successful!\n\n" +
                            "Stock: " + stock.getSymbol() +
                            "\nQuantity: " + quantity +
                            "\nPrice: ₹" +
                            money(stock.getPrice()) +
                            "\nTotal: ₹" +
                            money(total),
                            "Trade Successful",
                            JOptionPane.INFORMATION_MESSAGE);

                    showDashboard();

                } else {

                    showError(
                            "Purchase failed.\n" +
                            "Please check quantity or balance.");
                }

            } catch (NumberFormatException e) {

                showError(
                        "Please enter a valid quantity.");
            }
        }
    }

    // ============================================================
    // SELL DIALOG
    // ============================================================

    private void showSellDialog() {

        if (user.getPortfolio()
                .getHoldings()
                .isEmpty()) {

            showError(
                    "You don't have any stocks to sell.");

            return;
        }

        JComboBox<String> stockBox =
                new JComboBox<>();

        for (Holding holding :
                user.getPortfolio().getHoldings()) {

            stockBox.addItem(
                    holding.getSymbol());
        }

        JTextField quantityField =
                new JTextField();

        JLabel priceLabel =
                new JLabel("₹0.00");

        JLabel availableLabel =
                new JLabel("Available: 0");

        JLabel totalLabel =
                new JLabel("₹0.00");

        Runnable update =
                () -> {

                    String symbol =
                            (String) stockBox.getSelectedItem();

                    Stock stock =
                            market.findStock(symbol);

                    Holding holding =
                            user.getPortfolio()
                                    .getHolding(symbol);

                    if (stock != null) {

                        priceLabel.setText(
                                "₹" +
                                money(stock.getPrice()));
                    }

                    if (holding != null) {

                        availableLabel.setText(
                                "Available: " +
                                holding.getQuantity());

                        try {

                            int quantity =
                                    Integer.parseInt(
                                            quantityField
                                                    .getText()
                                                    .trim());

                            totalLabel.setText(
                                    "₹" +
                                    money(
                                            stock.getPrice()
                                                    * quantity));

                        } catch (Exception ignored) {

                            totalLabel.setText("₹0.00");
                        }
                    }
                };

        stockBox.addActionListener(e -> update.run());

        quantityField.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                update.run();
                            }

                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                update.run();
                            }

                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                update.run();
                            }
                        });

        update.run();

        JPanel form = createTradeForm(
                "SELL STOCK",
                stockBox,
                quantityField,
                priceLabel,
                totalLabel);

        form.add(
                availableLabel,
                BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Sell Stock",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            try {

                int quantity =
                        Integer.parseInt(
                                quantityField.getText().trim());

                if (quantity <= 0) {

                    showError(
                            "Quantity must be greater than 0.");

                    return;
                }

                String symbol =
                        (String) stockBox.getSelectedItem();

                Stock stock =
                        market.findStock(symbol);

                Holding holding =
                        user.getPortfolio()
                                .getHolding(symbol);

                if (stock == null || holding == null) {

                    showError("Stock not found.");

                    return;
                }

                if (quantity > holding.getQuantity()) {

                    showError(
                            "You only own " +
                            holding.getQuantity() +
                            " shares of " + symbol + ".");

                    return;
                }

                double total =
                        stock.getPrice() * quantity;

                boolean success =
                        user.sellStock(
                                stock,
                                quantity);

                if (success) {

                    fileManager.saveTransactions(user);

                    JOptionPane.showMessageDialog(
                            this,
                            "Sale successful!\n\n" +
                            "Stock: " +
                            stock.getSymbol() +
                            "\nQuantity: " +
                            quantity +
                            "\nPrice: ₹" +
                            money(stock.getPrice()) +
                            "\nReceived: ₹" +
                            money(total),
                            "Trade Successful",
                            JOptionPane.INFORMATION_MESSAGE);

                    showDashboard();

                } else {

                    showError(
                            "Sale failed.\n" +
                            "Check your portfolio quantity.");
                }

            } catch (NumberFormatException e) {

                showError(
                        "Please enter a valid quantity.");
            }
        }
    }

    // ============================================================
    // TRADE FORM
    // ============================================================

    private JPanel createTradeForm(
            String title,
            JComboBox<String> stockBox,
            JTextField quantityField,
            JLabel priceLabel,
            JLabel totalLabel) {

        JPanel panel = new JPanel(
                new BorderLayout(15, 15));

        panel.setPreferredSize(
                new Dimension(420, 250));

        panel.setBackground(WHITE);

        JLabel heading =
                new JLabel(title);

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20));

        heading.setForeground(DARK);

        panel.add(
                heading,
                BorderLayout.NORTH);

        JPanel fields =
                new JPanel(
                        new GridLayout(4, 2, 12, 15));

        fields.setBackground(WHITE);

        fields.add(createLabel("Stock"));
        fields.add(stockBox);

        fields.add(createLabel("Quantity"));
        fields.add(quantityField);

        fields.add(createLabel("Price"));
        fields.add(priceLabel);

        fields.add(createLabel("Total"));
        totalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16));

        fields.add(totalLabel);

        panel.add(fields,
                BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // TOTAL CALCULATION
    // ============================================================

    private void updateTotalFromDialog(
            JComboBox<String> stockBox,
            JTextField quantityField,
            JLabel totalLabel) {

        String symbol =
                (String) stockBox.getSelectedItem();

        Stock stock =
                market.findStock(symbol);

        updateTotal(
                stock,
                quantityField,
                totalLabel);
    }

    private void updateTotal(
            Stock stock,
            JTextField quantityField,
            JLabel totalLabel) {

        if (stock == null) {
            totalLabel.setText("₹0.00");
            return;
        }

        try {

            int quantity =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim());

            if (quantity > 0) {

                totalLabel.setText(
                        "₹" +
                        money(
                                stock.getPrice()
                                        * quantity));

            } else {

                totalLabel.setText("₹0.00");
            }

        } catch (Exception e) {

            totalLabel.setText("₹0.00");
        }
    }

    // ============================================================
    // REFRESH TABLES
    // ============================================================

    private void refreshTables() {

        if (holdingsTable != null) {

            DefaultTableModel model =
                    (DefaultTableModel)
                            holdingsTable.getModel();

            model.setRowCount(0);

            for (Holding holding :
                    user.getPortfolio().getHoldings()) {

                Stock stock =
                        market.findStock(
                                holding.getSymbol());

                if (stock == null) {
                    continue;
                }

                double currentValue =
                        holding.getQuantity()
                                * stock.getPrice();

                model.addRow(
                        new Object[]{
                                holding.getSymbol(),
                                holding.getQuantity(),
                                "₹" +
                                money(
                                        holding
                                                .getAveragePrice()),
                                "₹" +
                                money(
                                        stock.getPrice()),
                                "₹" +
                                money(currentValue)
                        });
            }
        }

        if (marketTable != null) {

            DefaultTableModel model =
                    (DefaultTableModel)
                            marketTable.getModel();

            model.setRowCount(0);

            for (Stock stock :
                    market.getStocks().values()) {

                model.addRow(
                        new Object[]{
                                stock.getSymbol(),
                                stock.getCompanyName(),
                                "₹" +
                                money(stock.getPrice())
                        });
            }
        }

        if (transactionTable != null) {

            DefaultTableModel model =
                    (DefaultTableModel)
                            transactionTable.getModel();

            model.setRowCount(0);

            int start =
                    Math.max(
                            0,
                            user.getTransactions()
                                    .size() - 5);

            for (int i = start;
                 i < user.getTransactions().size();
                 i++) {

                Transaction transaction =
                        user.getTransactions().get(i);

                model.addRow(
                        new Object[]{
                                transaction.toString()
                        });
            }
        }
    }

    // ============================================================
    // UI HELPERS
    // ============================================================

    private JPanel createCard(
            String title,
            String value,
            Color accent) {

        JPanel card =
                new JPanel(
                        new BorderLayout(0, 8));

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER),
                        new EmptyBorder(
                                18, 18, 18, 18)));

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11));

        titleLabel.setForeground(
                SECONDARY);

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20));

        valueLabel.setForeground(accent);

        card.add(
                titleLabel,
                BorderLayout.NORTH);

        card.add(
                valueLabel,
                BorderLayout.CENTER);

        return card;
    }

    private JLabel createValueLabel(
            String title,
            String value) {

        JLabel label =
                new JLabel(value);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20));

        label.setForeground(DARK);

        return label;
    }

    private JPanel createWhitePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(0, 15));

        panel.setBackground(WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER),
                        new EmptyBorder(
                                18, 18, 18, 18)));

        return panel;
    }

    private JTable createTable(
            String[] columns) {

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        JTable table =
                new JTable(model);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13));

        table.setRowHeight(34);

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12));

        table.getTableHeader()
                .setBackground(
                        new Color(245, 247, 250));

        table.setSelectionBackground(
                new Color(225, 235, 250));

        return table;
    }

    private JButton createNavButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45));

        button.setHorizontalAlignment(
                SwingConstants.LEFT);

        button.setForeground(
                new Color(220, 225, 232));

        button.setBackground(SIDEBAR);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14));

        button.setBorder(
                new EmptyBorder(
                        10, 15, 10, 10));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));

        return button;
    }

    private JButton createTradeButton(
            String text,
            Color color) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13));

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));

        button.setBorder(
                new EmptyBorder(
                        11, 18, 11, 18));

        return button;
    }

    private JButton smallButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12));

        button.setForeground(BLUE);
        button.setBackground(WHITE);

        button.setFocusPainted(false);

        return button;
    }

    private JLabel createLabel(
            String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13));

        label.setForeground(SECONDARY);

        return label;
    }

    // ============================================================
    // ERROR
    // ============================================================

    private void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "CodeTrade",
                JOptionPane.ERROR_MESSAGE);
    }

    // ============================================================
    // EXIT
    // ============================================================

    private void exitApplication() {

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Save transactions and exit CodeTrade?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION);

        if (result ==
                JOptionPane.YES_OPTION) {

            fileManager.saveTransactions(user);

            dispose();
            System.exit(0);
        }
    }

    // ============================================================
    // MONEY FORMAT
    // ============================================================

    private String money(
            double value) {

        return String.format(
                "%,.2f",
                value);
    }

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(
            String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager
                            .getSystemLookAndFeelClassName());

        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {

            StockDashboardGUI gui =
                    new StockDashboardGUI();

            gui.setVisible(true);
        });
    }
}