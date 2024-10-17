package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static final ArrayList<Transactions> transactions = new ArrayList<Transactions>();
    private static final String FILE_NAME = "transactions.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

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
            /*One return form initial method switch defaults to "Invalid Option"
            * Unsure why this behavior persists.*/
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
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));){

            System.out.println("Enter the date (yyyy-MM-dd):");
            String unformattedDate = scan.nextLine();
            LocalDate date = LocalDate.parse(unformattedDate, DATE_FORMATTER);

            System.out.println("Enter the time (HH:mm:ss): ");
            String unformattedTime = scan.nextLine();
            LocalTime time = LocalTime.parse(unformattedTime, TIME_FORMATTER);

            System.out.println("Enter a description of the transaction:");
            String description = scan.nextLine();

            System.out.println("Enter the vendor: ");
            String vendor = scan.nextLine();

            System.out.println("Enter the amount");
            double amount = scan.nextDouble();

            //While loop to valid positive amount
            while (amount < 0) {
                System.out.println("Please Enter an amount greater than 0");
                amount = scan.nextDouble();
            }

            Transactions transaction = new Transactions(date, time, description, vendor, amount);
            transactions.add(transaction);

            writer.write(String.format("%s|%s|%s|%s|%.2f%n", date, time, description, vendor, amount));
            System.out.println("Deposit added: " + transactions);


        }catch(Exception e)
        {e.printStackTrace();}
    }

    private static void addPayment(Scanner scan) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));){

            System.out.println("Enter the date:");
            String unformattedDate = scan.nextLine();

            LocalDate date = LocalDate.parse(unformattedDate, DATE_FORMATTER);

            System.out.println("Enter the time: ");
            String unformattedTime = scan.nextLine();

            LocalTime time = LocalTime.parse(unformattedTime, TIME_FORMATTER);

            System.out.println("Enter a description of the transaction:");
            String description = scan.nextLine();

            System.out.println("Enter the vendor: ");
            String vendor = scan.nextLine();

            System.out.println("Enter the amount");
            double amount = scan.nextDouble();

            //while loop to validate positive amount
            while(amount < 0){
                System.out.println("Please Enter an amount greater than 0");
                amount = scan.nextDouble();
            }
            //amount multiplied by -1 to make negative
            amount = amount * -1;

            Transactions transaction = new Transactions(date, time, description, vendor, amount);
            transactions.add(transaction);
                //Formatting to align items with proper columns and add zeros to amount
            writer.write(String.format("%s|%s|%s|%s|%.2f%n", date, time, description, vendor, amount));
            System.out.println("Payment recorded successfully.");

        }catch(IOException e)
        {e.printStackTrace();}
    }

    private static void ledgerMenu(Scanner scan) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scan.nextLine().trim();

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
                    reportsMenu(scan);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {

        //formatting to align items with correct columns
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

        System.out.println("Deposits:");
        System.out.printf("%-12s %-8s %-30s %-20s %s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------------");

        boolean hasDeposits = false;
        for (Transactions transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("%-12s %-8s %-30s %-20s %.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                hasDeposits = true;
            }
        }
        if (!hasDeposits) {
            System.out.println("No deposits found.");
        }
    }

    private static void displayPayments() {
        System.out.println("Deposits:");
        System.out.printf("%-12s %-8s %-30s %-20s %s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------------");

        boolean hasPayments = false;
        for (Transactions transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("%-12s %-8s %-30s %-20s %.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                hasPayments = true;
            }
        }
        if (!hasPayments) {
            System.out.println("No deposits found.");
        }

    }

    private static void reportsMenu(Scanner scan) {
        LocalDate currentDate = LocalDate.now();

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

            String input = scan.nextLine().trim();

            switch (input) {
                case "1":
                    YearMonth currentMonth = YearMonth.from(currentDate);
                    LocalDate currentStartDate = currentMonth.atDay(1);
                    LocalDate currentEndDate = currentMonth.atEndOfMonth();

                    filterTransactionsByDate(currentStartDate, currentEndDate);

                    break;
                case "2":
                    YearMonth previousMonth = YearMonth.from(currentDate.minusMonths(1));
                    LocalDate previousStartDate = previousMonth.atDay(1);
                    LocalDate previousEndDate = previousMonth.atEndOfMonth();

                   filterTransactionsByDate(previousStartDate, previousEndDate);

                   break;
                case "3":
                    LocalDate currentYearStartDate = LocalDate.of(currentDate.getYear(), 1, 1); // January 1st
                    LocalDate currentYearEndDate = LocalDate.of(currentDate.getYear(), 12, 31); // December 31st

                    filterTransactionsByDate(currentYearStartDate, currentYearEndDate);

                    break;
                case "4":
                    LocalDate previousYearStartDate = LocalDate.of(currentDate.getYear(), 1, 1).minusYears(1); // January 1st
                    LocalDate previousYearEndDate = LocalDate.of(currentDate.getYear(), 12, 31).minusYears(1); // December 31st

                    filterTransactionsByDate(previousYearStartDate, previousYearEndDate);
                    break;
                case "5":
                    System.out.println("What vendor would you like to search for?");
                    String search = scan.nextLine();

                    filterTransactionsByVendor(search);

                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        boolean hasDate = false;


        System.out.println("Transactions from " + startDate + " to " + endDate + ":");
        System.out.printf("%-12s %-8s %-30s %-20s %s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------------");


    for (Transactions transaction : transactions) {
        if (!transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate)) {
            System.out.printf("%-12s %-8s %-30s %-20s %.2f%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
            hasDate= true;
        }
    }

    if (!hasDate) {
        System.out.println("No transactions found within the specified date range.");
    }
    }

    private static void filterTransactionsByVendor(String vendor) {
        boolean hasVendor = false;

        System.out.println("Transactions involving Vendor " + vendor);
        System.out.printf("%-12s %-8s %-30s %-20s %s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------------");
        Scanner scan = new Scanner(System.in);
        String search = scan.nextLine();
        
            for (Transactions transaction : transactions) {

                if (search.equalsIgnoreCase(transaction.getVendor())) {
                    System.out.printf("%-12s %-8s %-30s %-20s %.2f%n",
                            transaction.getDate(),
                            transaction.getTime(),
                            transaction.getDescription(),
                            transaction.getVendor(),
                            transaction.getAmount());
                    hasVendor = true;
                }
            }

            if (!hasVendor) {
                System.out.println("No transactions found within the specified date range.");
            }

        }
}

