import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// 1. Stock Class: Represents individual stock data
class Stock {
    String symbol;
    String name;
    double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    // Simulates market fluctuation (-5% to +5%)
    public void updatePrice() {
        double change = 1 + (Math.random() * 0.1 - 0.05);
        this.price = this.price * change;
    }
}

// 2. Portfolio Class: Manages user's money and owned stocks
class Portfolio {
    double balance;
    Map<String, Integer> holdings;

    public Portfolio(double initialBalance) {
        this.balance = initialBalance;
        this.holdings = new HashMap<>();
    }

    public void buyStock(Stock stock, int quantity) {
        double totalCost = stock.price * quantity;
        if (balance >= totalCost) {
            balance -= totalCost;
            holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
            System.out.println("✅ Successfully bought " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("❌ Insufficient funds! You need $" + String.format("%.2f", totalCost));
        }
    }

    public void sellStock(Stock stock, int quantity) {
        int ownedQty = holdings.getOrDefault(stock.symbol, 0);
        if (ownedQty >= quantity) {
            double totalEarnings = stock.price * quantity;
            balance += totalEarnings;
            
            if (ownedQty == quantity) {
                holdings.remove(stock.symbol); // Sold all shares
            } else {
                holdings.put(stock.symbol, ownedQty - quantity);
            }
            System.out.println("✅ Successfully sold " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("❌ You don't own enough shares to sell! You have " + ownedQty);
        }
    }

    public void displayPortfolio(Map<String, Stock> marketStocks) {
        System.out.println("\n--- YOUR PORTFOLIO ---");
        System.out.printf("Cash Balance: $%.2f\n", balance);
        System.out.println("Holdings:");
        
        double totalStockValue = 0.0;
        if (holdings.isEmpty()) {
            System.out.println("  No stocks owned yet.");
        } else {
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                String symbol = entry.getKey();
                int qty = entry.getValue();
                double currentPrice = marketStocks.get(symbol).price;
                double value = qty * currentPrice;
                totalStockValue += value;
                System.out.printf("  %s : %d shares | Current Value: $%.2f\n", symbol, qty, value);
            }
        }
        System.out.printf("Total Portfolio Value (Cash + Stocks): $%.2f\n", (balance + totalStockValue));
        System.out.println("----------------------");
    }
}

// 3. Market Class: Manages all available stocks in the exchange
class Market {
    Map<String, Stock> availableStocks;

    public Market() {
        availableStocks = new HashMap<>();
        // Adding initial dummy stocks
        availableStocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.00));
        availableStocks.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2800.00));
        availableStocks.put("TSLA", new Stock("TSLA", "Tesla Inc.", 700.00));
        availableStocks.put("AMZN", new Stock("AMZN", "Amazon.com", 3300.00));
    }

    public void displayMarket() {
        System.out.println("\n--- LIVE MARKET DATA ---");
        System.out.println("SYMBOL\tPRICE\t\tCOMPANY");
        for (Stock stock : availableStocks.values()) {
            System.out.printf("%s\t$%.2f\t\t%s\n", stock.symbol, stock.price, stock.name);
        }
        System.out.println("------------------------");
    }

    public void fluctuateMarket() {
        for (Stock stock : availableStocks.values()) {
            stock.updatePrice();
        }
    }

    public Stock getStock(String symbol) {
        return availableStocks.get(symbol.toUpperCase());
    }
}

// 4. Main Class: Runs the system and user interface
public class Task2stocktrading {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Market market = new Market();
        
        // Give the user a starting balance of $10,000
        Portfolio userPortfolio = new Portfolio(10000.00); 

        System.out.println("==========================================");
        System.out.println("   WELCOME TO CODEALPHA STOCK EXCHANGE    ");
        System.out.println("==========================================");

        while (true) {
            System.out.println("\n1. View Live Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View My Portfolio");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");
            
            int choice = scanner.nextInt();

            if (choice == 1) {
                market.displayMarket();
            } 
            else if (choice == 2) {
                market.displayMarket();
                System.out.print("Enter Stock Symbol to Buy: ");
                String symbol = scanner.next();
                Stock stock = market.getStock(symbol);
                
                if (stock != null) {
                    System.out.print("Enter quantity to buy: ");
                    int qty = scanner.nextInt();
                    userPortfolio.buyStock(stock, qty);
                } else {
                    System.out.println("❌ Invalid Stock Symbol.");
                }
            } 
            else if (choice == 3) {
                userPortfolio.displayPortfolio(market.availableStocks);
                System.out.print("Enter Stock Symbol to Sell: ");
                String symbol = scanner.next();
                Stock stock = market.getStock(symbol);
                
                if (stock != null) {
                    System.out.print("Enter quantity to sell: ");
                    int qty = scanner.nextInt();
                    userPortfolio.sellStock(stock, qty);
                } else {
                    System.out.println("❌ Invalid Stock Symbol.");
                }
            } 
            else if (choice == 4) {
                userPortfolio.displayPortfolio(market.availableStocks);
            } 
            else if (choice == 5) {
                System.out.println("Exiting the trading platform. Have a great day!");
                break;
            } 
            else {
                System.out.println("❌ Invalid choice. Please try again.");
            }

            // Simulate time passing (market prices fluctuate after every action)
            market.fluctuateMarket(); 
        }
        
        scanner.close();
    }
}
