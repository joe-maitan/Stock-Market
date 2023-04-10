package StockMarket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static int getIntegerInput(Scanner in, boolean lowerLimitFlag, int lowerLimit, boolean upperLimitFlag, int upperLimit, String prompt, String errorMsg) {
        boolean validInput = false;
        int num = 0;

        while (validInput != true) {
            validInput = true;
            System.out.println(prompt);
            num = in.nextInt();
            
            try {
                if (num < 0) {}
            } catch (NumberFormatException nfe) {
                System.out.println(errorMsg);
            } // End try-catch block

            if (lowerLimitFlag && num < lowerLimit) {
                System.out.println(errorMsg);
                validInput = false;
            }

            if (upperLimitFlag && num > upperLimit) {
                System.out.println(errorMsg);
                validInput = false;
            }
        } // End while loop
        in.close();
        return num;
    } // End getDoubleInput

    public static double getDoubleInput(Scanner in, boolean lowerLimitFlag, int lowerLimit, boolean upperLimitFlag, int upperLimit, String prompt, String errorMsg) {
        boolean validInput = false;
        double num = 0;

        while (validInput != true) {
            validInput = true;
            System.out.println(prompt);
            num = in.nextDouble();
            
            try {
                if (num < 0) {}
            } catch (NumberFormatException nfe) {
                System.out.println(errorMsg);
            } // End try-catch block

            if (lowerLimitFlag && num < lowerLimit) {
                System.out.println(errorMsg);
                validInput = false;
            }

            if (upperLimitFlag && num > upperLimit) {
                System.out.println(errorMsg);
                validInput = false;
            }
        } // End while loop
        in.close();
        return num;
    } // End getDoubleInput

    public static void importFile(Stock[] stockArray, String filename) {
        File newFile = new File(filename);
        Scanner read = null;

        if (newFile.exists()) {
            System.out.println("Absoulute path: " + newFile.getAbsolutePath());
            System.out.println("Writeable: " + newFile.canWrite());
            System.out.println("Readable: " + newFile.canRead());
            System.out.println();
        } else {
            System.out.println("File does not exist");
            System.out.println();
            System.exit(0);
        }

        String symbol = "";
        String name = "";
        String tempLow = "";
        String tempHigh = "";
        String tempCurrent = "";

        double low = 0;
        double high = 0;
        double current = 0;

        int currIndex = 0;

        try {
            read = new Scanner(newFile);
        } catch (FileNotFoundException ex) {
            ex.getMessage();
            System.exit(0);
        }

        try {
            read.useDelimiter(",");
            while (read.hasNext()) {
                name = read.next();
                name = name.trim();

                symbol = read.next();
                symbol = symbol.trim();
                symbol = symbol.toUpperCase();

                tempCurrent = read.next();
                tempCurrent = tempCurrent.trim();
                current = Double.parseDouble(tempCurrent);

                tempLow = read.next();
                tempLow = tempLow.trim();
                low = Double.parseDouble(tempLow);

                tempHigh = read.next();
                tempHigh = tempHigh.trim();
                high = Double.parseDouble(tempHigh);
                // Then it should go to the next line, but it was catching the name Lululemon on the next line as what is next for the high price
                stockArray[currIndex++] = new Stock(name, symbol, current, low, high);
                
            } // End while loop
            read.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } // End try-catch
    } // End importFile
    
    public static void exportFile(Stock[] stockArray, String filename) {
        String data = "";
        Scanner user = new Scanner(System.in);
        int input;
        PrintWriter output = null;
        File outputFile = new File(filename);

        if (outputFile.exists()) {
            System.out.println(filename + " already exists.");
            input = getIntegerInput(user, true, 1, true, 2, "What would you like to do?\n1 - Overwrite\n2 - Nothing\n", "Error please try again.");
            
            if (input == 1) {
                // Delete the previous file and overwrite it with the new
            } else if (input == 2) {
                System.out.println("Exiting program");
                System.exit(0);
            }
        } // End if statement

        try {
            output = new PrintWriter(filename);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        } // End try-catch

        for (int i = 0; i < Stock.getNumStocks(); i++) {
            data = stockArray[i].getName() + ", ";
            data += stockArray[i].getSymbol() + ", ";
            data += String.format("%.2f", stockArray[i].getLastPrice() + ", ");
            data += String.format("%.2f", stockArray[i].getLow() + ", ");
            data += String.format("%.2f", stockArray[i].getHigh() + ", ");

            output.write(data);
        } // End for loop

        System.out.println("Stock data was successfuly exported to " + outputFile.getName() + "\n");
        output.close();
        user.close();
    } // End exportFile
    
    public static int isStockInList(Stock[] stockArray, String symbol) {
        for (int i = 0; i < Stock.getNumStocks(); i++) {
            Stock currStock = stockArray[i];
            String currStockSym = currStock.getSymbol();

            if (symbol.equals(currStockSym)) {
                return i;
            }
        }

        return -1;
    } // End isStockInList
    
    public static void deleteStocks(Stock[] stockArray) {
        Scanner in = new Scanner(System.in);
        String stockSymbol;

        System.out.println("Please enter the stock symbol you would like to delete");
		stockSymbol = in.next();
		stockSymbol = stockSymbol.toUpperCase();

		int index = isStockInList(stockArray, stockSymbol); // The index of the stock we wish to delete

		if (index != -1) {
			// Find the line where the stock symbol is and delete that line from the file
			for (int i = index; i < Stock.getNumStocks() - 1; i++) {
				stockArray[i] = stockArray[i + 1];
			}
			stockArray[Stock.getNumStocks() - 1] = null;
			Stock.deleteStock();
			System.out.println("Successfully deleted " + stockSymbol);
		} else {
			System.out.println("The stock " + stockSymbol + ", you tried to delete is one we are not currently tracking");
		}

        in.close();
	} // End deleteStocks
    public static void main(String[] args) {
       Scanner input = new Scanner(System.in);
       int userInput = 0;

       Stock[] stockArray = new Stock[10]; // Could use an arrayList but the original assignmnet has us working with arrays
       String companyName;
       String stockSymbol;
       double current;
       double low;
       double high;
    
       String filename = "";
       
       // Import the data of stocks from the file (spreedsheet or txt)
       System.out.println("Please select an input file with the initial stock data. If you do not have a file you would like to select please just hit enter.");
       filename = input.nextLine();

       if (filename.equals(null)) {
        // No import file was specified so it hits this statement and moves on
       } else {
        importFile(stockArray, filename);
       }

       while (userInput != 4) {
            userInput = getIntegerInput(input, true, 0, true, 4, "Enter 0 if you want to add a stock\nEnter 1 if you want stock statistics\nEnter 2 if you want to output stock data to file stockdata.txt\nEnter 3 if you want to delete a stock\nEnter 4 if you want to exit", "Invalid input. Please try again.");

            // Add or get stock information 
            if (userInput == 0) {
                if (Stock.getNumStocks() == stockArray.length) {
                    System.out.println("Cannot add another stock - our list is full.");
                    continue;
                }

                companyName = input.nextLine();
                stockSymbol = input.nextLine();
                stockSymbol = stockSymbol.toUpperCase();
                low = getDoubleInput(input, true, 0, false, 1, "Please enter the 52 week low for " + companyName, "Invalid input, try again.");
                high = getDoubleInput(input, true, 0, false, 1, "Please enter the 52 week high for " + companyName, "Invalid input, try again.");
                current = getDoubleInput(input, true, 0, false, 1, "Please enter the current price for " + companyName, "Invalid input, try again.");

                Stock newStock = new Stock(companyName, stockSymbol, current, low, high);
                stockArray[Stock.getNumStocks()] = newStock;
            } // End if userInput == 0

            // View stock statistics
            if (userInput == 1) {
                String outputStr; 

                System.out.println("Please enter the stock symbol you're interested in");
                stockSymbol = input.next();
                stockSymbol = stockSymbol.toUpperCase();

                int specifiedIndex = isStockInList(stockArray, stockSymbol);

                if (specifiedIndex == -1) {
                    System.out.println("Sorry, for the stock you're interested ins is one that we are not currently tracking.");
                    continue;
                }

                System.out.println("Please enter the most recent price for " + stockSymbol);
                current = input.nextDouble();

                Stock currStock = stockArray[specifiedIndex];

                outputStr = "The current price of " + currStock.getName() + " (" + currStock.getSymbol() + ") stock is " + String.format("%.2f", current);

                double changeInCurrent = current - currStock.getLastPrice();
                double changeInLow = current - currStock.getLow();
                double changeInHigh = current - currStock.getHigh();

                outputStr += "That represents a change of $" + String.format("%/2f", changeInCurrent)
                + " from their most recent recorded price of $" + String.format("%.2f", currStock.getLastPrice() + "\n");

                currStock.setCurrentPrice(current);

                if (current < currStock.getLow()) {

                }

                if (current > currStock.getHigh()) {

                }

                if (current < currStock.getHigh() && current > currStock.getLow()) {

                }

                System.out.println(outputStr);

            } // End if userInput == 1

            // Export stock data
            if (userInput == 2) {
                String exportFilename = "";
                System.out.println("What is the name of the file you would like to export the data to?");
                exportFilename = input.next();

                exportFile(stockArray, exportFilename);
            } // End if userInput == 2

            // Delete a stock
            if (userInput == 3) {
                deleteStocks(stockArray);
            } // End if userInput == 3
       } // End while loop
    } // End main method
    
} // End Main class
