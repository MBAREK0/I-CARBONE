package services.consumption;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import repositories.ConsumptionRepository;
import entities.Consumption;;
import repositories.UserRepository;
import entities.User;
import utils.DateChecker;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;



public class ConsumptionService {

    private Scanner scanner = new Scanner(System.in);
    private ConsumptionRepository consumptionRepository = new ConsumptionRepository(); // Assume you have this class for DB operations
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private UserRepository userRepository = new UserRepository();

    /**
     * Add a new consumption to the database
     * @param startDate
     * @param endDate
     * @param impact
     * @param userId
     * @return
     */
    public void displayMoyenneConsumptionForPeriod(LocalDate startDate, LocalDate endDate, String userCin) throws SQLException {
        User user = userRepository.getUserByCin(userCin);
        if (user == null) {
            System.out.println("User with CIN " + userCin + " not found.");
            return;
        }

        Double averageConsumption = getSumConsumptionsImpactInPeriod(startDate, endDate, user.getId())/ getTotalConsumptionDays(startDate, endDate, user.getId());
        System.out.println("\t\tAverage consumption for user with CIN " + userCin + " from " + startDate + " to " + endDate + " is: " + averageConsumption);

    }
    /**
     * Add a new consumption to the database
     * @param startDate
     * @param endDate
     * @param impact
     * @param userId
     * @return
     */

    public List<Consumption> getConsumptionsInPeriod(LocalDate startDate, LocalDate endDate,int userId) throws SQLException{
        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();
        return consumptions.stream()
                .filter(consumption -> !consumption.getStartDate().isBefore(startDate) && !consumption.getEndDate().isAfter(endDate))
                .filter(consumption -> consumption.getUserId() == userId)
                .collect(Collectors.toList());
    }

    /**
     * Get the sum of consumptions impact in the specified period
     * @param startDate
     * @param endDate
     * @return
     */
    public Double getSumConsumptionsImpactInPeriod(LocalDate startDate, LocalDate endDate,int userId) throws SQLException{
        return  getConsumptionsInPeriod(startDate,endDate,userId).stream()

                .mapToDouble(Consumption::calculateImpact)
                .sum();
    }

    /**
     * Get the total number of consumption days in the specified period
     * @param startDate
     * @param endDate
     * @return
     */
    public long getTotalConsumptionDays(LocalDate startDate, LocalDate endDate, int userId) throws SQLException {
        // Get the list of consumptions for the user
        List<Consumption> consumptions = getConsumptionsInPeriod(startDate, endDate, userId);

        // Use a set to collect unique consumption days
        Set<LocalDate> uniqueDays = consumptions.stream()
                .flatMap(consumption -> DateChecker.getDatesBetween(consumption.getStartDate(), consumption.getEndDate()).stream())
                .filter(date -> DateChecker.getDatesBetween(startDate, endDate).contains(date))
                .collect(Collectors.toSet());

        return uniqueDays.size();
    }

}
