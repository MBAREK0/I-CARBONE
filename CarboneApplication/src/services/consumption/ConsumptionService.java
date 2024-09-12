package services.consumption;

import java.sql.SQLException;
import java.time.LocalDate;
import repositories.ConsumptionRepository;
import entities.Consumption;
import repositories.UserRepository;
import entities.User;
import utils.DateChecker;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;



public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository = new ConsumptionRepository();
    private final UserRepository userRepository = new UserRepository();

    /**
        * Add a new consumption to the database
        */
    public void displayAverageConsumptionForPeriod(LocalDate startDate, LocalDate endDate, String userCin) throws SQLException {
        User user = userRepository.getUserByCin(userCin);
        if (user == null) {
            System.out.println("User with CIN " + userCin + " not found.");
            return;
        }

        double averageConsumption = getSumConsumptionsImpactInPeriod(startDate, endDate, user.getId())/ getTotalConsumptionDays(startDate, endDate, user.getId());
        System.out.println("\t\tAverage consumption for user with CIN " + userCin + " from " + startDate + " to " + endDate + " is: " + averageConsumption);

    }
    /**
     * Add a new consumption to the database
     */
    public List<Consumption> getConsumptionsInPeriod(LocalDate startDate, LocalDate endDate, int userId) throws SQLException {
        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();
        return consumptions.stream()
                .filter(consumption ->
                        (consumption.getStartDate().isBefore(endDate) || consumption.getStartDate().isEqual(endDate)) &&
                                (consumption.getEndDate().isAfter(startDate) || consumption.getEndDate().isEqual(startDate))
                )
                .filter(consumption -> consumption.getUserId() == userId)
                .collect(Collectors.toList());
    }


    /**
     * Get the sum of consumptions impact in the specified period
     */
    public Double getSumConsumptionsImpactInPeriod(LocalDate startDate, LocalDate endDate,int userId) throws SQLException{
        return  getConsumptionsInPeriod(startDate,endDate,userId).stream()

                .mapToDouble(Consumption::calculateImpact)
                .sum();
    }

    /**
     * Get the total number of consumption days in the specified period
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

    /**
     * this function is return a total consumption in period
     */
    public double getAverageConsumptionInPeriod(LocalDate startDate, LocalDate endDate, List<Consumption> consumptions) {
        double totalConsumptionImpact = 0.0;
        double totalConsumptionDays = 0.0;
        for (Consumption c : consumptions) {
            if ((c.getStartDate().isBefore(endDate) || c.getStartDate().isEqual(endDate)) &&
                    (c.getEndDate().isAfter(startDate) || c.getEndDate().isEqual(startDate))) {
                LocalDate StartOfPeriod = startDate.isAfter(c.getStartDate()) ? startDate : c.getStartDate();
                LocalDate EndOfPeriod = endDate.isBefore(c.getEndDate()) ? endDate : c.getEndDate();
                long daysBetween = EndOfPeriod.toEpochDay() - StartOfPeriod.toEpochDay() + 1;
                totalConsumptionDays += daysBetween;
                long totalDays = c.getEndDate().toEpochDay() - c.getStartDate().toEpochDay() + 1;
                double dailyImpact = c.calculateImpact() / totalDays;
                totalConsumptionImpact += dailyImpact * daysBetween;
            }
        }
        return totalConsumptionImpact / totalConsumptionDays;
    }

    /**
     * this function is return a list of consumptions for a user
     */
    public List<Consumption> getConsumptionsForUser(int userId) throws SQLException {
        return consumptionRepository.getAllConsumptions().stream()
                .filter(consumption -> consumption.getUserId() == userId)
                .collect(Collectors.toList());
    }
    public List<Consumption> getConsumptionsForUserByType(int userId,String type) throws SQLException {
        return consumptionRepository.getAllConsumptions().stream()
                .filter(consumption -> consumption.getUserId() == userId)
                .filter(consumption -> consumption.getType().toString().equals(type))
                .collect(Collectors.toList());
    }

}
