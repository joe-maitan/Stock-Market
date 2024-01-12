public class Stock {

    private String company_name;
    private String stock_symbol;
    private double recent_price;
    private double year_low;
    private double year_high;

    static int numStocks = 0;

    /** Stock Constructor
     * @param name
     * @param symbol
     * @param currentPrice
     * @param low
     * @param high
     * 
     * @return A new Stock object with a unique name, symbol and information on its current price, year low, and year high
     */
    public Stock(String name, String symbol, double currentPrice, double low, double high) {
        this.company_name = name;
        this.stock_symbol = symbol;
        this.recent_price = currentPrice;
        this.year_low = low;
        this.year_high = high;
        numStocks++;
    } // End Stock constructor

    /** get_name()
     * @param none
     * @return The name of the company we are tracking
     */
    public String get_name() {
        return this.company_name;
    } // End get_name() method

    /** get_symbol
     * @param none
     * @return The stock symbol of the company we are tracking
     */
    public String get_symbol() {
        return this.stock_symbol;
    } // End get_symbol() method

    /** get_current_price()
     * @param none
     * @return The recent trading price of the company we are tracking
     */
    public double get_current_price() {
        return this.recent_price;
    } // End get_current_price() method

    /** set_current_price(current)
     * @param current The current trading price of the stock 
     * @return none
     */
    public void set_current_price(Double current) {
        this.recent_price = current;
    } // End set_current_price() method

    /** get_year_low()
     * @param none
     * @return The year low of the company we are tracking.
     */
    public double get_year_low() {
        return this.year_low;
    } // End get_year_low() method

    /** get_year_high()
     * @param none
     * @return The year high of the company we are tracking.
     */
    public double get_year_high() {
        return this.year_high;
    } // End get_year_high() method

    /** get_num_stocks()
     * @param none
     * @return The number of stocks we are currently tracking.
     */
    public static int get_num_stocks() {
        return numStocks;
    } // End get_num_stocks() method

    /** delete_stock()
     * @param none
     * @return none
     * 
     * Decrements the number of stocks we are keeping track of.
     */
    public static void delete_stock() {
        numStocks--;
    } // End delete_stock() method
} // End Stock class