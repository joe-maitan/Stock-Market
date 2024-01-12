import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class StockMarket {
	
	public static int get_integer_input(boolean lower_limit_flag, int lower_limit, boolean upper_limit_flag, int upper_limit, String prompt, String error_msg) {
		String user_input = "";
		int input = 0;
		boolean valid_input = false;
			
		while (valid_input != true) {
			user_input = JOptionPane.showInputDialog(prompt);
			
			try {
				input = Integer.parseInt(user_input);
			} catch (NumberFormatException e) {
				valid_input = false;
			} // End try-catch block
			
			if (lower_limit_flag && input < lower_limit || upper_limit_flag && input > upper_limit) {
				valid_input = false;
				JOptionPane.showMessageDialog(null, error_msg);
			} else {
				valid_input = true;
			} // End if-else statement
		} // End while loop
					
		return input;
	} // End getIntegerInput

	public static double get_double_input(boolean lower_limit_flag, int lower_limit, boolean upper_limit_flag, int upper_limit, String prompt, String error_msg) {
		String user_input = "";
		double input = 0;
		boolean valid_input = false;
		
		while (valid_input != true) {
			user_input = JOptionPane.showInputDialog(prompt);
		
			try {
				input = Double.parseDouble(user_input);
			} catch (NumberFormatException e) {
				valid_input = false;
			} // End try-catch block
		
			if (lower_limit_flag && input < lower_limit || upper_limit_flag && input > upper_limit) {
				valid_input = false;
				JOptionPane.showMessageDialog(null, error_msg);
			} else {
				valid_input = true;
			} // End if-else statement
		} // End while loop
				
		return input;
	} // End getDoubleInput

	public static int is_stock_in_list(Stock[] stock_data, String target_symbol) {
		for (int i = 0; i < Stock.get_num_stocks(); i++) {
			Stock s = stock_data[i];
			String s_symbol = s.get_symbol();

			if (s_symbol.equals(target_symbol)) {
				return i;
			} // End if statement
		} // End for loop
		
		return -1;
	} // End isStockInList

	public static void import_data(Stock[] stock_data) {
		String comp_name;
		String stock_symbol;
		String temp_curr_price;
		String temp_low;
		String temp_high;

		int stock_number = 0;

		double low = 0;
		double high = 0;
		double curr_price = 0;

		// Welcome the user to the stock tracker and input the file chooser
		JOptionPane.showMessageDialog(null, "Please select an input file with the initial stock data", "Welcome to the stock tracker!", JOptionPane.INFORMATION_MESSAGE);
		
		JFileChooser inputFile = new JFileChooser(); // Create the JFileChooser Object that is going to be used for reading the file
		Scanner input = null;
		if (inputFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File newFile = inputFile.getSelectedFile(); // Gathering the selected file

			try {
				input = new Scanner(newFile);
			} catch (FileNotFoundException ex) {
				JOptionPane.showMessageDialog(null, "File was not found.\nExiting Program");
				System.exit(0);
			} // End try-catch block 

			/* Read text from the file and match the correct variables with where they are in
			the input file, have to use trim to remove unnecessary spaces and such */
			input.useDelimiter(",");
			while (input.hasNext()) {
				comp_name = input.next();
				comp_name = comp_name.trim();

				stock_symbol = input.next();
				stock_symbol = stock_symbol.trim();
				stock_symbol = stock_symbol.toUpperCase();

				temp_curr_price = input.next();
				temp_curr_price = temp_curr_price.trim();
				curr_price = Double.parseDouble(temp_curr_price);

				temp_low = input.next();
				temp_low = temp_low.trim();
				low = Double.parseDouble(temp_low);

				temp_high = input.next();
				temp_high = temp_high.trim();
				high = Double.parseDouble(temp_high);

				stock_data[stock_number++] = new Stock(comp_name, stock_symbol, curr_price, low, high); /* Add the parsed data to our stock array */
			} // End while loop (read the file)
			
			input.close(); // Close the file
		} else {
			JOptionPane.showMessageDialog(null, "No file selected");
		} // End if-else statement
	} // End importFile

	public static void export_data(Stock[] stock_data) {
		String outputStr = "";
		PrintWriter output = null;
		File outputFile = new File("stockdata.txt");

		if (outputFile.exists()) {
			int delete = JOptionPane.showConfirmDialog(null, "The file " + outputFile.getName() + " already exists. Would you like to overwrite it?");
			
			if (delete == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(null, "Exiting program");
				System.exit(0);
			} // End if-statement
		} // End if-statement
		
		try {
			output = new PrintWriter(outputFile);
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "File does not exist, exiting");
			System.exit(0);
		} // End try-catch block
		
		for (int i = 0; i < Stock.get_num_stocks(); ++i) { // Create the for loop to print out the data of the stocks to the document
			Stock curr_s = stock_data[i];
			outputStr = curr_s.get_name() + ", ";
			outputStr += curr_s.get_symbol() + ", ";
			outputStr += String.format("%.2f", curr_s.get_current_price()) + ", ";
			outputStr += String.format("%.2f", curr_s.get_year_low()) + ", ";
			outputStr += String.format("%.2f", curr_s.get_year_high()) + "\n";
			output.write(outputStr);
		} // End for loop
		
		JOptionPane.showMessageDialog(null, "Stock data was successfully exported to " + outputFile.getName());
		output.close();
	} // End exportFile() method

	public static void delete_a_stock(Stock[] stock_data) {
		String stockSymbol;
		stockSymbol = JOptionPane.showInputDialog("Please enter the stock symbol you would like to delete.");
		stockSymbol = stockSymbol.toUpperCase();
		int deleteStockSymbol = is_stock_in_list(stock_data, stockSymbol);

		if (deleteStockSymbol != -1) {
			for (int i = deleteStockSymbol; i < Stock.get_num_stocks() - 1; i++) { // Find the line where the stock symbol is and delete that line from the file
				stock_data[i] = stock_data[i + 1];
			} // End for loop

			stock_data[Stock.get_num_stocks() - 1] = null;
			Stock.delete_stock();
			JOptionPane.showMessageDialog(null, "Successfully deleted " + stockSymbol);
		} else {
			JOptionPane.showMessageDialog(null, "The stock " + stockSymbol + ", you tried to delete is one we are not currently tracking");
		} // End if-else statement
	} // End deleteStocks

	public static void add_stock(Stock[] stock_data) {
		String symbol;
		String name;
		double low = 0;
		double high = 0;
		double currentPrice = 0;

		if (Stock.get_num_stocks() == stock_data.length) {
			JOptionPane.showMessageDialog(null, "Can't add another stock - our list is full!");
			return;
		} // End if-statement

		name = JOptionPane.showInputDialog("Please enter the stock name:");
		symbol = JOptionPane.showInputDialog("Please enter the stock symbol:");
		symbol = symbol.toUpperCase();
		
		low = get_double_input(true, 0, false, 1, "Please enter the 52 week low for " + name, "Invalid Input try again");
		high = get_double_input(true, 0, false, 1, "Please enter the 52 week high for " + name, "Invalid input try again");
		currentPrice = get_double_input(true, 0, false, 1, "Please enter the last trading price for " + name, "Invalid input try again");

		int t = Stock.get_num_stocks();
		Stock new_stock = new Stock(name, symbol, currentPrice, low, high);
		
		stock_data[t] = new_stock; // Adding the value of the stock to the array
	} // End add_stock() method

	public static void view_stock_statistics(Stock[] stock_data) {
		String input_str, output_str = null;
		String symbol;
		// String name;
		// double low = 0;
		// double high = 0;
		double currentPrice = 0;

		symbol = JOptionPane.showInputDialog("Please enter the stock symbol you're interested in");
		symbol = symbol.toUpperCase();
				
		int stockSymbolIndex = is_stock_in_list(stock_data, symbol);

		if (stockSymbolIndex == -1) {
			JOptionPane.showMessageDialog(null, "I'm sorry but that symbol is not one we are currently tracking.");
			return;
		}

		input_str = JOptionPane.showInputDialog("Please enter the most recent price for " + symbol);
		currentPrice = Double.parseDouble(input_str);

		Stock s = stock_data[stockSymbolIndex];

		output_str = "The current price of " + s.get_name() + " (" + s.get_symbol() + ") stock is $" + String.format("%.2f", currentPrice) + ".\n";

		// Calculate the changes in price between the the low and high of the years
		double changeOfCurrent = currentPrice - s.get_current_price();
		double changeOfLow = currentPrice - s.get_year_low();
		double changeOfHigh = currentPrice - s.get_year_high();

		output_str += "That represents a change of $" + String.format("%.2f", changeOfCurrent)
		+ " from their most recent recorded price of $" + String.format("%.2f", s.get_current_price()) + "\n";

		s.set_current_price(currentPrice);

		// If current price is below the 52 week low
		if (currentPrice < s.get_year_low()) {
			output_str += "The current price is $" + String.format("%.2f", changeOfLow)
			+ " below the stock's 52 week low of $" + String.format("%.2f", s.get_year_low())
			+ "!";
		}
		
		// If current price is above the 52 week high
		if (currentPrice > s.get_year_high()) {
			output_str += "The current price is $" + String.format("%.2f", changeOfHigh)
			+ " higher than the stock's 52 week high of $"
			+ String.format("%.2f", s.get_year_high()) + "!";
		}
		
		// And finally if the current price is in between the 52 week low and high
		if (currentPrice < s.get_year_low() && currentPrice > s.get_year_low()) {
			output_str += "The current price is $" + String.format("%.2f", changeOfLow)
			+ " above it's 52 week low of $" + String.format("%.2f", s.get_year_low()) + " and $"
			+ String.format("%.2f", changeOfHigh) + " below it's 52 week high of $"
			+ String.format("%.2f", s.get_year_high()) + "!";
		}
		
		// Display the output to the user
		JOptionPane.showMessageDialog(null, output_str);
	} // End view_stock_statistics
	public static void main(String[] args) {
		int user_in = 0;
	
		Stock[] our_stock_market = new Stock[10];
		
		import_data(our_stock_market);
		
		while (user_in != 4) {
			user_in = get_integer_input(true, 0, true, 4,
			"Enter 0 if you want to add a stock\nEnter 1 if you want stock statistics\nEnter 2 if you want to output stock data to file stockdata.txt\nEnter 3 if you want to delete a stock\nEnter 4 if you want to exit",
			"Invalid input try again");
			
			switch(user_in) {
				case 0:
					add_stock(our_stock_market);
					break;
				case 1:
					view_stock_statistics(our_stock_market);
					break;
				case 2:
					export_data(our_stock_market);
					break;
				case 3:
					delete_a_stock(our_stock_market);
					break;
				default:
					break;
			} // End switch statement
		} // End while loop

	} // End main method

} // End StockMarket class