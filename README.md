# CodeAlpha Stock Trading Platform

## Project
Console-based stock trading simulation developed in Java.

## Objective
Simulate a basic stock market where a user can view market data, buy and sell stocks using virtual money, maintain a portfolio, calculate portfolio value and profit/loss, and save transaction history.

## Features
- Market data display
- Virtual cash balance
- Buy stocks
- Sell stocks
- Portfolio tracking
- Average purchase price
- Current portfolio value
- Profit/loss calculation
- Transaction history
- File-based transaction storage
- Input validation

## Java Concepts
- OOP
- Encapsulation
- Classes and objects
- `Map` and `List` collections
- Methods
- Exception handling
- File I/O
- Java Date/Time API
- Console input

## Project Structure
```text
CodeAlpha_StockTradingPlatform/
├── src/
│   ├── Main.java
│   ├── Stock.java
│   ├── Holding.java
│   ├── User.java
│   ├── Portfolio.java
│   ├── Transaction.java
│   ├── StockMarket.java
│   └── FileManager.java
├── data/
│   └── transactions.txt  (created after transactions are saved)
├── screenshots/
└── README.md
```

## Requirements
- JDK 17 or newer
- No external libraries

## Run
```bash
javac -d out src/*.java
java -cp out Main
```

## Main flow
```text
View market
    ↓
Choose stock
    ↓
Enter quantity
    ↓
Check balance
    ↓
Buy
    ↓
Update portfolio
    ↓
Record transaction
    ↓
Calculate portfolio value
```

## Notes
The application uses fixed sample market prices. It is an educational simulation and does not connect to a real stock exchange or real money.
