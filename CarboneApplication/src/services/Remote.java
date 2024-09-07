package services;

import services.UiElements.Ui;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import entities.User;

public class Remote {
     private Map<String, User> users = new HashMap<>();
     private Scanner scanner = new Scanner(System.in);
     private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void main() {
        boolean running = true;
        boolean isMenu = true;

        while (running) {

            if (isMenu) {
                Ui ui = new Ui();
                ui.displayMenu2();
                isMenu = false;
            }

            System.out.print("\n\n\t\ti@carbone:~$ ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    updateAccount();
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 4:
                    displayUserInfo();
                    break;
                case 5:
                    addCarbonConsumption();
                    break;
                case 6:
                    displayTotalConsumptionForPeriod();
                    break;
                case 7:
                    displayDailyCarbonConsumption();
                    break;
                case 8:
                    displayWeeklyCarbonConsumption();
                    break;
                case 9:
                    displayMonthlyCarbonConsumption();
                    break;
                case 10:
                    displayYearlyCarbonConsumption();
                    break;
                case 11:
                    generateReport();
                    break;
                case 12:
                    isMenu = true;
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("\n\t\tInvalid choice, please try again.");
            }

        }
    }

    // -------------------------------------------------------------
    // ✨ Account Methods
    // -------------------------------------------------------------

     private void createAccount() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter Name: ");
        String name = scanner.nextLine();
        System.out.print("\t\tEnter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        if (users.get(id) == null) {

            if (age < 0 || age > 120)
                System.out.println("\n\t\t| Account creation failed. Please enter a valid age |" + users.get(id));
            else {
                User user = new User(id, name, age);
                users.put(id, user);
                System.out.println("\n\t\t| Account created successfully |");
            }

        } else {

            System.out.println("\n\t\t| Account creation failed. User already exists |");
        }

    }

     private void updateAccount() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user to update: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter new Name: ");
        String newName = scanner.nextLine();
        System.out.print("\t\tEnter new Age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine();

        User user = users.get(id);

        if (user != null) {

            if (newAge < 0 || newAge > 120)
                System.out.println("\n\t\t| Account creation failed. Please enter a valid age |");
            else {
                user.name = newName;
                user.age = newAge;
                System.out.println("\n\t\t| Account updated successfully |");
            }

        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

     private void deleteAccount() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user to delete: ");
        String id = scanner.nextLine();

        if (users.remove(id) != null) {
            System.out.println("\n\t\t| Account deleted successfully |");
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

     private void displayUserInfo() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user to display: ");
        String id = scanner.nextLine();

        User user = users.get(id);
        if (user != null) {
            System.out.println("\n\t\t" + user);
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

    // -------------------------------------------------------------
    // ✨ Carbon Consumption Methods
    // -------------------------------------------------------------

     private void addCarbonConsumption() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter amount of carbon consumption to add: ");
        double amount = scanner.nextDouble();
        System.out.print("\t\tEnter start date (yyyy-MM-dd): ");
        String startDateStr = scanner.next();
        System.out.print("\t\tEnter end date (yyyy-MM-dd): ");
        String endDateStr = scanner.next();
        LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);
        scanner.nextLine();

        User user = users.get(id);
        if (user != null) {
            
            //user.addConsumption(startDate, endDate, amount);
            System.out.println("\n\t\t| Carbon consumption added successfully |");
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

     private void displayTotalConsumptionForPeriod() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter start date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        System.out.print("\t\tEnter end date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();

        LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);

        User user = users.get(id);
        if (user != null) {
            double totalConsumption = user.getTotalConsumption(startDate, endDate);
            System.out.println("\n\t\t| Total Carbon Consumption from " + startDate + " to " + endDate + ": "
                    + totalConsumption + " |");
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

     private void displayDailyCarbonConsumption() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);

        User user = users.get(id);
        if (user != null) {
            double dailyConsumption = user.getDailyConsumption(date);
            System.out.println("\n\t\t| Daily Carbon Consumption for " + date + ": " + dailyConsumption + " |");
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

     private void displayWeeklyCarbonConsumption() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter year: ");
        int year = scanner.nextInt();
        System.out.print("\t\tEnter week of year: ");
        int weekOfYear = scanner.nextInt();
        scanner.nextLine();

        User user = users.get(id);
        if (user != null) {
            double weeklyConsumption = user.getWeeklyConsumption(year, weekOfYear);
            System.out.println("\n\t\t| Weekly Carbon Consumption for Year " + year + ", Week " + weekOfYear + ": "
                    + weeklyConsumption + " |");
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

     private void displayMonthlyCarbonConsumption() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter year: ");
        int year = scanner.nextInt();
        System.out.print("\t\tEnter month (1-12): ");
        int month = scanner.nextInt();
        scanner.nextLine();

        User user = users.get(id);
        if (user != null) {
            double monthlyConsumption = user.getMonthlyConsumption(year, month);
            System.out.println("\n\t\t| Monthly Carbon Consumption for Year " + year + ", Month " + month + ": "
                    + monthlyConsumption + " |");
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

     private void displayYearlyCarbonConsumption() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        User user = users.get(id);
        if (user != null) {
            double yearlyConsumption = user.getYearlyConsumption(year);
            System.out.println("\n\t\t| Yearly Carbon Consumption for Year " + year + ": " + yearlyConsumption + " |");
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

    public void generateReport() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID of the user: ");
        String id = scanner.nextLine();
        User user = users.get(id);
        if (user != null) {
            System.out.print("\n\t\t");

            System.err.println("\n\t\t| Report for user " + user.name + " |");
            System.out.print("\t\t");

            for (int i = 0; i < 50; i++) {
                System.out.print("-");
            }

            System.out.println();
            System.out.println("\t\t" + user.generateReport());
            System.out.print("\t\t");

            for (int i = 0; i < 50; i++) {
                System.out.print("-");
            }

            // total consumption
            double totalConsumption = user.getTotalConsumption(LocalDate.MIN, LocalDate.MAX);
            System.out.println("\n\t\t| Total Carbon Consumption: " + totalConsumption + " |");

        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }

}
