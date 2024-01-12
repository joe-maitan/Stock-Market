import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class StockMarket {
	
	public static int getIntegerInput(boolean lower_limit_flag, int lower_limit, boolean upper_limit_flag, int upper_limit, String prompt, String error_msg) {
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

	public static double getDoubleInput(boolean lower_limit_flag, int lower_limit, boolean upper_limit_flag, int upper_limit, String prompt, String error_msg) {
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

	public static int isStockInList(Stock[] stock_data, String target_symbol) {
		for (int i = 0; i < Stock.get_num_stocks(); i++) {
			Stock s = stock_data[i];
			String s_symbol = s.get_symbol();

			if (s_symbol.equals(target_symbol)) {
				return i;
			} // End if statement
		} // End for loop
		
		return -1;
	} // End isStockInList

	public static void importStocks(Stock[] stock_data) {
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

	public static void exportStocks(Stock[] stockArray) {
		String outputStr = "";
		PrintWriter output = null;
		File outputFile = new File("stockdata.txt");

		if (outputFile.exists()) {
			int delete = JOptionPane.showConfirmDialog(null,
					"The file " + outputFile.getName() + " already exists. Would you like to overwrite it?");
			if (delete == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(null, "Exiting program");
				System.exit(0);
			}
		}
		try {
			output = new PrintWriter(outputFile);
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "File does not exist, exiting");
			System.exit(0);
		}
		// Create the for loop to print out the data of the stocks to the document
		for (int ii = 0; ii < Stock.get_num_stocks(); ii++) {
			outputStr = stockArray[ii].getName() + ", ";
			outputStr += stockArray[ii].getSymbol() + ", ";
			outputStr += String.format("%.2f", stockArray[ii].getLastPrice()) + ", ";
			outputStr += String.format("%.2f", stockArray[ii].getLow()) + ", ";
			outputStr += String.format("%.2f", stockArray[ii].getHigh()) + "\n";

			output.write(outputStr);

		}
		// Close the file
		JOptionPane.showMessageDialog(null, "Stock data was successfully exported to " + outputFile.getName());
		output.close();

//		return outputFile;
	} // End exportFile

	public static void deleteStocks(Stock[] stockArray) {
		String stockSymbol;
		stockSymbol = JOptionPane.showInputDialog("Please enter the stock symbol you would like to delete.");
		stockSymbol = stockSymbol.toUpperCase();
		int deleteStockSymbol = isStockInList(stockArray, stockSymbol);

		if (deleteStockSymbol != -1) {
			// Find the line where the stock symbol is and delete that line from the file
			for (int i = deleteStockSymbol; i < Stock.getNumStocks() - 1; i++) {
				stockArray[i] = stockArray[i + 1];
			}
			stockArray[Stock.getNumStocks() - 1] = null;
			Stock.deleteStock();
			JOptionPane.showMessageDialog(null, "Successfully deleted " + stockSymbol);
		} else {
			JOptionPane.showMessageDialog(null,
					"The stock " + stockSymbol + ", you tried to delete is one we are not currently tracking");
		}
	} // End deleteStocks

	public static void main(String[] args) {
		int userInput = 0;
		Stock[] stockArray = new Stock[10];

		
		String stockSymbol;
		String stockName;
		String inputStr, outputStr = null;

		double low = 0;
		double high = 0;
		double currentPrice = 0;

		importStocks(stockArray);
		
		// Prompt the user on what they are allowed to do in the stock app
		while (userInput != 4) {
			userInput = getIntegerInput(true, 0, true, 4,
					"Enter 0 if you want to add a stock\nEnter 1 if you want stock statistics\nEnter 2 if you want to output stock data to file stockdata.txt\nEnter 3 if you want to delete a stock\nEnter 4 if you want to exit",
					"Invalid input try again");
			
			// Add a stock
			if (userInput == 0) {
				if (Stock.getNumStocks() == stockArray.length) {
					JOptionPane.showMessageDialog(null, "Can't add another stock - our list is full!");
					continue;
				}

				stockName = JOptionPane.showInputDialog("Please enter the stock name:");
				stockSymbol = JOptionPane.showInputDialog("Please enter the stock symbol:");
				stockSymbol = stockSymbol.toUpperCase();
				// Gather the 52WeekLow from the user and parse their input
				low = getDoubleInput(true, 0, false, 1, "Please enter the 52 week low for " + stockName,
						"Invalid Input try again");
				// Gather the 52WeekHigh from the user and parse their input
				high = getDoubleInput(true, 0, false, 1, "Please enter the 52 week high for " + stockName,
						"Invalid input try again");
				// Gather the current trading price for the company
				currentPrice = getDoubleInput(true, 0, false, 1, "Please enter the last trading price for " + stockName,
						"Invalid input try again");

				int tempNum = Stock.getNumStocks();
				Stock userStock = new Stock(stockName, stockSymbol, currentPrice, low, high);
				// Adding the value of the stock to the array
				stockArray[tempNum] = userStock;

			} // End if Statement
			
			// View stock statistics
			if (userInput == 1) {
				stockSymbol = JOptionPane.showInputDialog("Please enter the stock symbol you're interested in");
				stockSymbol = stockSymbol.toUpperCase();
				int stockSymbolIndex = isStockInList(stockArray, stockSymbol);
				if (stockSymbolIndex == -1) {
					JOptionPane.showMessageDialog(null,
							"I'm sorry but that symbol is not one we are currently tracking.");
					continue;
				}

				inputStr = JOptionPane.showInputDialog("Please enter the most recent price for " + stockSymbol);
				currentPrice = Double.parseDouble(inputStr);

				Stock stockObj = stockArray[stockSymbolIndex];

				outputStr = "The current price of " + stockObj.getName() + " (" + stockObj.getSymbol() + ") stock is $"
						+ String.format("%.2f", currentPrice) + ".\n";

				// Calculate the changes in price between the the low and high of the years
				double changeOfCurrent = currentPrice - stockObj.getLastPrice();
				double changeOfLow = currentPrice - stockObj.getLow();
				double changeOfHigh = currentPrice - stockObj.getHigh();

				outputStr += "That represents a change of $" + String.format("%.2f", changeOfCurrent)
						+ " from their most recent recorded price of $" + String.format("%.2f", stockObj.getLastPrice())
						+ "\n";

				stockObj.setCurrentPrice(currentPrice);

				// If current price is below the 52 week low
				if (currentPrice < stockObj.getLow()) {
					outputStr += "The current price is $" + String.format("%.2f", changeOfLow)
							+ " below the stock's 52 week low of $" + String.format("%.2f", stockObj.getLow())
							+ "!";
				}
				// If current price is above the 52 week high
				if (currentPrice > stockObj.getHigh()) {
					outputStr += "The current price is $" + String.format("%.2f", changeOfHigh)
							+ " higher than the stock's 52 week high of $"
							+ String.format("%.2f", stockObj.getHigh()) + "!";
				}
				// And finally if the current price is in between the 52 week low and high
				if (currentPrice < stockObj.getHigh() && currentPrice > stockObj.getLow()) {
					outputStr += "The current price is $" + String.format("%.2f", changeOfLow)
							+ " above it's 52 week low of $" + String.format("%.2f", stockObj.getLow()) + " and $"
							+ String.format("%.2f", changeOfHigh) + " below it's 52 week high of $"
							+ String.format("%.2f", stockObj.getHigh()) + "!";
				}
				// Display the output to the user
				JOptionPane.showMessageDialog(null, outputStr);
			} // End else if userInput == 1

			// output the stock data
			if (userInput == 2) {
				exportStocks(stockArray);
			} // End if userInput == 2

			// delete a stock
			if (userInput == 3) {
				deleteStocks(stockArray);
			} // End if userInput == 3

		} // End while loop

	} // End main

} // End JoeMaitanPA08
