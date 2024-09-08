package services;

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
import java.util.List;
import java.util.OptionalDouble;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class ConsumptionService {

    private Scanner scanner = new Scanner(System.in);
    private ConsumptionRepository consumptionRepository = new ConsumptionRepository(); // Assume you have this class for DB operations
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private UserRepository userRepository = new UserRepository();



    public void addConsumption() {
        try {
            // Get user ID
            System.out.print("Enter User cin: ");
            String cin = scanner.nextLine();
            UserRepository userRepository = new UserRepository();
            User user = userRepository.getUserByCin(cin);
            if (user == null) {
                System.out.println("User not found.");
                return;
            }
            int userId = user.getId();

            // Get amount
            System.out.print("Enter Amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            // Get consumption type
            System.out.print("Enter Type (FOOD, HOUSING, TRANSPORT): ");
            String typeStr = scanner.nextLine();
            ConsumptionType type = ConsumptionType.valueOf(typeStr.toUpperCase());

            // Get period
            System.out.print("Enter Start Date (yyyy-MM-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

            System.out.print("Enter End Date (yyyy-MM-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

            // Create the appropriate Consumption object based on the type
            Consumption consumption = null;
            switch (type) {
                case FOOD:
                    System.out.print("Enter Type of Food (MEAT, VEGETABLE): ");
                    String foodType = scanner.nextLine();
                    foodType = foodType.toUpperCase();
                    System.out.print("Enter Weight: ");
                    double weight = scanner.nextDouble();
                    scanner.nextLine();

                    consumption = new Food(startDate, endDate, amount, type, userId, foodType, weight);
                    break;
                case HOUSING:
                    System.out.print("Enter Energy Consumption: ");
                    double energyConsumption = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Energy Type (ELECTRICITY, GAS): ");
                    String energyType = scanner.nextLine();
                    energyType = energyType.toUpperCase();
                    consumption = new Housing(startDate, endDate, amount, type, userId, energyConsumption, energyType);
                    break;
                case TRANSPORT:
                    System.out.print("Enter Distance Traveled: ");
                    double distanceTraveled = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Vehicle Type (CAR, TRAIN): ");
                    String vehicleType = scanner.nextLine();
                    vehicleType = vehicleType.toUpperCase();
                    consumption = new Transport(startDate, endDate, amount, type, userId, distanceTraveled, vehicleType);
                    break;
                default:
                    System.out.println("Invalid type.");
                    return;
            }

            // Add the consumption record to the database
            if (consumption != null) {
                consumptionRepository.addConsumption(consumption);
                System.out.println("Consumption record added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding consumption record: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Please check your entries.");
        }
    }

    public void getAndDisplayAverageConsumption() {
        try {
            // Get input from the user
            System.out.print("Enter Start Date (yyyy-MM-dd): ");
            LocalDate startDate = parseDate(scanner.nextLine());

            System.out.print("Enter End Date (yyyy-MM-dd): ");
            LocalDate endDate = parseDate(scanner.nextLine());

            System.out.print("Enter User CIN: ");
            String userCin = scanner.nextLine();

            // Call the method to display the average consumption
            displayMoyenneConsumptionForPeriod(startDate, endDate, userCin);

        } catch (SQLException e) {
            System.out.println("Error accessing the database: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString);  // You might want to handle parsing exceptions here
    }

    public void displayMoyenneConsumptionForPeriod(LocalDate startDate, LocalDate endDate, String userCin) throws SQLException {
        User user = userRepository.getUserByCin(userCin);
        if (user == null) {
            System.out.println("User with CIN " + userCin + " not found.");
            return;
        }

        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();

        OptionalDouble averageConsumption = consumptions.stream()
                .filter(consumption -> consumption.getUserId() == user.getId())
                .filter(consumption -> !consumption.getStartDate().isBefore(startDate) && !consumption.getEndDate().isAfter(endDate))
                .mapToDouble(Consumption::calculateImpact)
                .average();

        if (averageConsumption.isPresent()) {
            System.out.println("Average consumption for user with CIN " + userCin + " from " + startDate + " to " + endDate + " is: " + averageConsumption.getAsDouble());
        } else {
            System.out.println("No consumption records found for the given criteria.");
        }
    }
}
