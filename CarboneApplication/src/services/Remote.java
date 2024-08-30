package services;

import services.UiElements.Ui;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Remote {
    private static Map<String, User> users = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        boolean running = true;
        boolean isMenu = true;

        while (running) {
            if(isMenu){
                Ui ui = new Ui();
                ui.displayMenu2();
                isMenu = false;
            }


         
            System.out.print("\n\n\n\t\ti-carbone@guest:~$ ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

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

    private static void createAccount() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter ID: ");
        String id = scanner.nextLine();
        System.out.print("\t\tEnter Name: ");
        String name = scanner.nextLine();
        System.out.print("\t\tEnter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        User user = new User(id, name, age);
        users.put(id, user);
        System.out.println("\t\t\t\t\t\t\tAccount created successfully.");
    }

    private static void updateAccount() {
        System.out.print("Enter ID of the user to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter new Name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new Age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        User user = users.get(id);
        if (user != null) {
            // Directly updating the user's fields (assuming mutable fields or using setters if defined)
            user.name = newName;
            user.age = newAge;
            System.out.println("Account updated successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void deleteAccount() {
        System.out.print("Enter ID of the user to delete: ");
        String id = scanner.nextLine();

        if (users.remove(id) != null) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void displayUserInfo() {
        System.out.print("Enter ID of the user to display: ");
        String id = scanner.nextLine();

        User user = users.get(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void addCarbonConsumption() {
        System.out.print("Enter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("Enter amount of carbon consumption to add: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateStr = scanner.next();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateStr = scanner.next();
        LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);
        scanner.nextLine(); // consume the newline

        User user = users.get(id);
        if (user != null) {
            user.addConsumption(startDate, endDate, amount);
            System.out.println("Carbon consumption updated successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void displayTotalConsumptionForPeriod() {
        System.out.print("Enter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();

        LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);

        User user = users.get(id);
        if (user != null) {
            double totalConsumption = user.getTotalConsumption(startDate, endDate);
            System.out.println("Total Carbon Consumption from " + startDate + " to " + endDate + ": " + totalConsumption);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void displayDailyCarbonConsumption() {
        System.out.print("Enter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);

        User user = users.get(id);
        if (user != null) {
            double dailyConsumption = user.getDailyConsumption(date);
            System.out.println("Daily Carbon Consumption for " + date + ": " + dailyConsumption);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void displayWeeklyCarbonConsumption() {
        System.out.print("Enter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        System.out.print("Enter week number of the year: ");
        int weekOfYear = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        User user = users.get(id);
        if (user != null) {
            double weeklyConsumption = user.getWeeklyConsumption(year, weekOfYear);
            System.out.println("Weekly Carbon Consumption for Year " + year + ", Week " + weekOfYear + ": " + weeklyConsumption);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void displayMonthlyCarbonConsumption() {
        System.out.print("Enter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        User user = users.get(id);
        if (user != null) {
            double monthlyConsumption = user.getMonthlyConsumption(year, month);
            System.out.println("Monthly Carbon Consumption for Year " + year + ", Month " + month + ": " + monthlyConsumption);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void displayYearlyCarbonConsumption() {
        System.out.print("Enter ID of the user: ");
        String id = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        User user = users.get(id);
        if (user != null) {
            double yearlyConsumption = user.getYearlyConsumption(year);
            System.out.println("Yearly Carbon Consumption for Year " + year + ": " + yearlyConsumption);
        } else {
            System.out.println("User not found.");
        }
    }
}
