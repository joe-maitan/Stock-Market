package StockMarket.Stock;

public class Stock {

    String companyName;
    String stockSymbol;
    double lastPrice;
    double yearLow;
    double yearHigh;

    static int numStocks = 0;

    public Stock (String name, String symbol, double currentPrice, double low, double high) {
        this.companyName = name;
        this.stockSymbol = symbol;
        this.lastPrice = currentPrice;
        this.yearLow = low;
        this.yearHigh = high;
        numStocks++;
    }

    public String getName() {
        return this.companyName;
    }

    public String getSymbol() {
        return this.stockSymbol;
    }

    public double getLastPrice() {
        return this.lastPrice;
    }

    public void setCurrentPrice(Double current) {
        this.lastPrice = current;
    }

    public double getLow() {
        return this.yearLow;
    }

    public double getHigh() {
        return this.yearHigh;
    }

    public static int getNumStocks() {
        return numStocks;
    }

    public static void deleteStock() {
        numStocks--;
    }
} // End stock class