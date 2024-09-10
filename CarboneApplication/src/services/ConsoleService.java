package services;

import entities.*;
import repositories.UserRepository;
import services.consumption.ConsumptionService;
import services.user.UserResult;
import services.user.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import repositories.ConsumptionRepository;



import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import repositories.ConsumptionRepository;
import entities.Consumption;
import entities.ConsumptionType;
import entities.Food;
import entities.Housing;
import entities.Transport;
import repositories.UserRepository;
import entities.User;
import utils.DateChecker;

import java.util.List;
import java.util.OptionalDouble;
import java.time.format.DateTimeParseException;

public class ConsoleService {

    private UserService userService = new UserService();
    private Scanner scanner = new Scanner(System.in);
    private UserRepository userRepository = new UserRepository();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ConsumptionService consumptionService = new ConsumptionService();


    // -------------------------------------------------------------
    // ✨ User Methods
    // -------------------------------------------------------------
    public void addUser() {
        System.out.print("\n\t\t-------------------\n");

        int id = 0;
        System.out.print("\t\tEnter Name: ");
        String name = scanner.nextLine();
        System.out.print("\t\tEnter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("\t\tEnter CIN: ");
        String cin = scanner.nextLine();

        UserResult r =  userService.addUser(id, name, age, cin);
        if (r.isSuccess()){
            System.out.println("\n\t\t"+r.getMessage());
        } else {
            System.out.println("\n\t\t"+r.getMessage());
        }


    }

    public void displayUserInfo(){
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter Cin of the user to display: ");
        String cin = scanner.nextLine();

        Optional user = userService.displayUserInfo(cin);
        if (user.isPresent()){
            System.out.println("\n\t\t"+user.get());
        } else {
            System.out.println("\n\t\t| User not found |");
        }
    }
    public void updateUser() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter Cin of the user to update: ");
        String cin = scanner.nextLine();


        try {

            User user = userRepository.getUserByCin(cin);

            if (user != null) {
                System.out.print("\t\tEnter new Name: ");
                String newName = scanner.nextLine();
                System.out.print("\t\tEnter new Age: ");
                int newAge = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                System.out.print("\t\tEnter New CIN: ");
                String newCin = scanner.nextLine();


                if (newAge < 0 || newAge > 120) {
                    System.out.println("\n\t\t| Account update failed. Please enter a valid age |");
                } else {
                   UserResult r = userService.updateUser(user.getId(), newName, newAge, newCin);
                     if (r.isSuccess()){
                          System.out.println("\n\t\t"+r.getMessage());
                        } else {
                            System.out.println("\n\t\t"+r.getMessage());
                            }
                     }
            } else {
                System.out.println("\n\t\t| User not found |");
            }
        } catch (SQLException e) {
            System.out.println("\n\t\t| Database error: " + e.getMessage() + " |");
        }
    }

    public void deleteUser() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter Cin of the user to delete: ");
        String cin = scanner.nextLine();

        UserResult r = userService.deleteUser(cin);
        if (r.isSuccess()) {
            System.out.println("\n\t\t" + r.getMessage());
        } else {
            System.out.println("\n\t\t" + r.getMessage());
        }
    }

    public void findInactiveUsers() throws SQLException {

        // Get start date from user
        System.out.print("\t\tEnter start date (yyyy-MM-dd): ");
        String startDateInput = scanner.nextLine();
        LocalDate startDate = LocalDate.parse(startDateInput, dateFormatter);

        // Get end date from user
        System.out.print("\t\tEnter end date (yyyy-MM-dd): ");
        String endDateInput = scanner.nextLine();
        LocalDate endDate = LocalDate.parse(endDateInput, dateFormatter);
        List<User> users = userService.findInactiveUsers(startDate, endDate);

        if(users.size() > 0){
            System.out.println("\n\t\tInactive users:");
            userService.findInactiveUsers(startDate, endDate).forEach(user ->
                    System.out.println("\t\t" + user)
            );
        } else {
            System.out.println("\n\t\tNo inactive users found.");
        }
    }

    // -------------------------------------------------------------
    // ✨ Consumption Methods
    // -------------------------------------------------------------

    public void addConsumption() {
        try {

            System.out.print("\t\tEnter User cin: ");
            String cin = scanner.nextLine();
            UserRepository userRepository = new UserRepository();
            User user = userRepository.getUserByCin(cin);
            if (user == null) {
                System.out.println("\t\tUser not found.");
                return;
            }
            int userId = user.getId();

            // Get amount
            System.out.print("\t\tEnter Amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            // Get consumption type
            System.out.print("\t\tEnter Type (FOOD, HOUSING, TRANSPORT): ");
            String typeStr = scanner.nextLine();
            ConsumptionType type = ConsumptionType.valueOf(typeStr.toUpperCase());

            // Get period
            System.out.print("\t\tEnter Start Date (yyyy-MM-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

            System.out.print("\t\tEnter End Date (yyyy-MM-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

            // Create the appropriate Consumption object based on the type
            Consumption consumption = null;
            switch (type) {
                case FOOD:
                    System.out.print("\t\tEnter Type of Food (MEAT, VEGETABLE): ");
                    String foodType = scanner.nextLine();
                    foodType = foodType.toUpperCase();
                    System.out.print("\t\tEnter Weight: ");
                    double weight = scanner.nextDouble();
                    scanner.nextLine();

                    consumption = new Food(startDate, endDate, amount, type, userId, foodType, weight);
                    break;
                case HOUSING:
                    System.out.print("\t\tEnter Energy Consumption: ");
                    double energyConsumption = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("\t\tEnter Energy Type (ELECTRICITY, GAS): ");
                    String energyType = scanner.nextLine();
                    energyType = energyType.toUpperCase();
                    consumption = new Housing(startDate, endDate, amount, type, userId, energyConsumption, energyType);
                    break;
                case TRANSPORT:
                    System.out.print("\t\tEnter Distance Traveled: ");
                    double distanceTraveled = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("\t\tEnter Vehicle Type (CAR, TRAIN): ");
                    String vehicleType = scanner.nextLine();
                    vehicleType = vehicleType.toUpperCase();
                    consumption = new Transport(startDate, endDate, amount, type, userId, distanceTraveled, vehicleType);
                    break;
                default:
                    System.out.println("\t\tInvalid type.");
                    return;
            }

            // Add the consumption record to the database
            if (consumption != null) {
                ConsumptionRepository consumptionRepository = new ConsumptionRepository();
                consumptionRepository.addConsumption(consumption);
                System.out.println("\t\tConsumption record added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("\t\tError adding consumption record: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("\t\tInvalid input. Please check your entries.");
        }
    }


    public void getAndDisplayAverageConsumption() {
        try {
            // Get input from the user
            System.out.print("\t\tEnter Start Date (yyyy-MM-dd): ");
            LocalDate startDate = DateChecker.parseDate(scanner.nextLine());

            System.out.print("\t\tEnter End Date (yyyy-MM-dd): ");
            LocalDate endDate = DateChecker.parseDate(scanner.nextLine());

            System.out.print("\t\tEnter User CIN: ");
            String userCin = scanner.nextLine();

            // Call the method to display the average consumption
            consumptionService.displayMoyenneConsumptionForPeriod(startDate, endDate, userCin);

        } catch (SQLException e) {
            System.out.println("\t\tError accessing the database: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("\t\tInvalid date format. Please use yyyy-MM-dd.");
        }
    }









}
