package Yearup.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.io.FileWriter;

public class FinancialTracker
{
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args)
    {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running)
        {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase())
            {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName)
    {
        try
        {
            File file = new File(fileName);
            String line;

            if(!file.exists())
            {
                System.out.println("The file doesn't exist. Creating new file: " + fileName);
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] tokens = line.split("\\|");
                //Check transactions.csv for 5 data inputs
                if (tokens.length == 5)
                {
                    LocalDate date = LocalDate.parse(tokens[0], DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(tokens[1], TIME_FORMATTER);
                    String description = tokens[2];
                    String vendor = tokens[3];
                    double amount = Double.parseDouble(tokens[4]);

                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            }
            //close bufferReader to prevent data leak
            bufferedReader.close();
        } catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
    }

    private static void addDeposit(Scanner scanner)
    {
        try
        {
            //Ask user for inputs
            System.out.println("Please enter the date: (yyyy-MM-dd)");
            String dateInput = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

            System.out.println("Please enter the time: (HH:mm:ss)");
            String timeInput = scanner.nextLine().trim();
            LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);

            System.out.println("Please enter the description: ");
            String descriptionInput = scanner.nextLine().trim();

            System.out.println("Please enter the vendor: ");
            String vendorInput = scanner.nextLine().trim();

            System.out.println("Please enter the amount: ");
            double amountInput = scanner.nextDouble();
            scanner.nextLine();
            //Check amount for positive #
            if(amountInput <= 0)
            {
                System.out.println("The amount must be positive!");
                return;
            }
            //Creating new transaction object
            Transaction newDeposit = new Transaction(date, time, descriptionInput, vendorInput, amountInput);
            // Add the newDeposit to the list of transactions
            transactions.add(newDeposit);
            //Save to csv file via saveTransaction method
            saveTransaction(newDeposit);

        }catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
    }

    private static void addPayment(Scanner scanner)
    {
        try
        {
            //Ask user for input
            System.out.println("Please enter the date: (yyyy-MM-dd)");
            String dateInput = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

            System.out.println("Please enter the time: (HH:mm:ss)");
            String timeInput = scanner.nextLine().trim();
            LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);

            System.out.println("Please enter the description: ");
            String descriptionInput = scanner.nextLine().trim();

            System.out.println("Please enter the vendor: ");
            String vendorInput = scanner.nextLine().trim();

            System.out.println("Please enter the amount: ");
            double amountInput = scanner.nextDouble();
            scanner.nextLine();

            //Changing amount to negative
            amountInput = amountInput * -1;
            //Creating new transaction object
            Transaction newPayment = new Transaction(date, time, descriptionInput, vendorInput, amountInput);
            // Add the newPayment to the list of transactions
            transactions.add(newPayment);
            //Saving newPayment via saveTransaction method
            saveTransaction(newPayment);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
    }

    private static void ledgerMenu(Scanner scanner)
    {
        boolean running = true;
        while (running)
        {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase())
            {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger()
    {
        //Check if transactions list is empty
        if (transactions.isEmpty())
        {
            System.out.println("No transactions available.");
            return;
        }

        //Column Header
        System.out.println("Date       | Time     | Description                    | Vendor                    | Amount    ");
        System.out.println("--------------------------------------------------------------------------------------------------");

        //Enhanced Loop to iterate through each transaction
        for (Transaction transaction : transactions)
        {
            //Formatting specifiers
            System.out.printf("%-10s | %-8s | %-30s | %-25s | %-10.2f%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayDeposits()
    {
        boolean hasDeposits = false;

        //Column Header
        System.out.println("Date       | Time     | Description                    | Vendor                    | Amount    ");
        System.out.println("--------------------------------------------------------------------------------------------------");

        //Enhanced Loop to iterate through each transaction
        for (Transaction transaction : transactions)
        {
            if (transaction.getAmount() > 0)
            {
                //Formatting specifiers
                System.out.printf("%-10s | %-8s | %-30s | %-25s | %-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                hasDeposits = true;
            }
        }

        //Check if deposits is empty
        if (!hasDeposits)
        {
            System.out.println("No Deposits available.");
        }
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayPayments()
    {
        boolean hasPayments = false;

        //Column Header
        System.out.println("Date       | Time     | Description                    | Vendor                    | Amount    ");
        System.out.println("--------------------------------------------------------------------------------------------------");

        //Enhanced Loop to iterate through each transaction
        for (Transaction transaction : transactions)
        {
            if (transaction.getAmount() < 0)
            {
                //Formatting specifiers
                System.out.printf("%-10s | %-8s | %-30s | %-25s | %-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                hasPayments = true;
            }

            //Check if payments is empty
            if (!hasPayments)
            {
                System.out.println("No Payments available.");
            }
        }
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void reportsMenu(Scanner scanner)
    {
        boolean running = true;
        while (running)
        {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();
            //Getting today's date
            LocalDate today = LocalDate.now();

            switch (input)
            {
                case "1":
                    //getting 1st of today's month
                    LocalDate firstOfMonth = today.withDayOfMonth(1);
                    filterTransactionsByDate(firstOfMonth, today);
                    break;
                // Generate a report for all transactions within the current month,
                // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    //getting 1st of previous month
                    LocalDate firstOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    //getting last day of previous month
                    LocalDate endOfPreviousMonth = firstOfPreviousMonth.withDayOfMonth(firstOfPreviousMonth.lengthOfMonth());
                    filterTransactionsByDate(firstOfPreviousMonth, endOfPreviousMonth);
                    break;
                // Generate a report for all transactions within the previous month,
                // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    //getting 1st of current year
                    LocalDate firstOfYear = today.withDayOfYear(1);
                    filterTransactionsByDate(firstOfYear, today);
                    break;
                // Generate a report for all transactions within the current year,
                // including the date, time, description, vendor, and amount for each transaction.
                case "4":
                    //getting 1st of previous year
                    LocalDate firstOfPreviousYear = today.minusYears(1).withDayOfYear(1);
                    //getting last of previous years
                    LocalDate endOfPreviousYear = firstOfPreviousYear.withDayOfYear(firstOfPreviousYear.lengthOfYear());
                    filterTransactionsByDate(firstOfPreviousYear, endOfPreviousYear);
                    break;
                // Generate a report for all transactions within the previous year,
                // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    System.out.println("Enter the vendor name:");
                    String vendor = scanner.nextLine().trim();
                    //calling method and passing method
                    filterTransactionsByVendor(vendor);
                    break;
                // Prompt the user to enter a vendor name, then generate a report for all transactions
                // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate)
    {
        boolean foundTransactions = false;

        //Column Header
        System.out.println("Date       | Time     | Description                    | Vendor                    | Amount    ");
        System.out.println("--------------------------------------------------------------------------------------------------");

        //Enhanced Loop to iterate through each transaction
        for (Transaction transaction : transactions)
        {
            //Get transaction date and set = to transactionDate
            LocalDate transactionDate = transaction.getDate();

            //Checking for transactionDate within specified range
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate))
            {
                //Formatting Specifiers
                System.out.printf("%-10s | %-8s | %-30s | %-25s | %-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                foundTransactions = true;
            }
        }

        if (!foundTransactions)
        {
            System.out.println("No transactions found within the date range!");
        }
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor)
    {
        boolean foundTransactions = false;

        //Column Header
        System.out.println("Date       | Time     | Description                    | Vendor                    | Amount    ");
        System.out.println("--------------------------------------------------------------------------------------------------");

        //Enhanced Loop to iterate through each transaction
        for (Transaction transaction : transactions)
        {
            //Checking if vendors match
            if (transaction.getVendor().equalsIgnoreCase(vendor))
            {
                // Formating Specifiers
                System.out.printf("%-10s | %-8s | %-30s | %-25s | %-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                foundTransactions = true;
            }
        }

        if (!foundTransactions)
        {
            System.out.println("No transactions found within the date range!");
        }
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }

    private static void saveTransaction(Transaction transaction)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            //Formatting transaction data
            String line = String.format("%s|%s|%s|%s|%.2f",
                    transaction.getDate().format(DATE_FORMATTER),
                    transaction.getTime().format(TIME_FORMATTER),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
            //writing data to transactions.csv
            bufferedWriter.write(line);
            //add new line after writing to csv
            bufferedWriter.newLine();
            //close bufferedReader to prevent data leak
            bufferedWriter.close();

            System.out.println("New transaction has been added: " + transaction);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}