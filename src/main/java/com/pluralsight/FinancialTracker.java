package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transactions> transactions = new ArrayList<Transactions>();
    private static final String FILE_NAME = "transactions.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final String DATE_FORMAT ="yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scan = new Scanner(System.in);
        boolean running = true;



        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scan.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scan);
                    break;
                case "P":
                    addPayment(scan);
                    break;
                case "L":
                    ledgerMenu(scan);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scan.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    LocalTime time = LocalTime.parse(parts[1]);
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);
                    Transactions transaction = new Transactions(date, time, description, vendor, amount);

                   transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void addDeposit(Scanner scan) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.

        try{
                
                BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));

            System.out.println("Enter the date (yyyy-MM-dd):");
            String dateInput = scan.nextLine();
            LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern(DATE_FORMAT));

            System.out.println("Enter the time (HH:mm:ss): ");
            String timeInput = scan.nextLine();
            LocalTime time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern(TIME_FORMAT));


                System.out.println("Enter a description of the transaction:");
                String description = scan.nextLine();

                System.out.println("Enter the vendor: ");
                String vendor = scan.nextLine();

                System.out.println("Enter the amount");
                double amount = scan.nextDouble();
                while (amount < 0) {
                    System.out.println("Please Enter an amount greater than 0");
                    amount = scan.nextDouble();
                }

            Transactions transaction = new Transactions(date, time, description, vendor, amount);
            transactions.add(transaction);

                writer.write(String.format("%s|%s|%s|%s|%.2f%n", dateInput, timeInput, description, vendor, amount));
                System.out.println("Deposit added: " + transactions);


        }catch(Exception e)
        {e.printStackTrace();}
    }

    private static void addPayment(Scanner scan) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        try{
            StringBuilder builder = new StringBuilder();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            System.out.println("Enter the date:");
            LocalDate date = LocalDate.parse(scan.nextLine());
            //String.format(DATE_FORMAT);

            System.out.println("Enter the time: ");
            LocalTime time = LocalTime.parse(scan.nextLine());
            //time.format(TIME_FORMAT);

            System.out.println("Enter a description of the transaction:");
            String description = scan.nextLine();

            System.out.println("Enter the vendor: ");
            String vendor = scan.nextLine();

            System.out.println("Enter the amount");
            double amount = scan.nextDouble();
            while(amount < 0){
                System.out.println("Please Enter an amount greater than 0");
                amount = scan.nextDouble();
            }
            amount = amount * -1;

            Transactions transaction = new Transactions(date, time, description, vendor, amount);
            transactions.add(transaction);

            // Write to file
            writer.write(String.format("%s|%s|%s|%s|%.2f%n", date, time, description, vendor, amount));
            System.out.println("Payment recorded successfully.");

        }catch(IOException e)
        {e.printStackTrace();}
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
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

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        //The table should have columns for date, time, description, vendor, and amount.


        System.out.printf("%-10s %-8s %-20s %-15s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transactions transaction : transactions) {
            System.out.printf("%-10s %-8s %-20s %-15s %-10.2f%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
        }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
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


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}