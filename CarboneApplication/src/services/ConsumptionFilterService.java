package services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import entities.Consumption;
import entities.User;
import repositories.ConsumptionRepository;
import repositories.UserRepository;
import java.sql.SQLException;
import java.time.LocalDate;




public class ConsumptionFilterService {

    private ConsumptionRepository consumptionRepository = new ConsumptionRepository();
    private UserRepository userRepository = new UserRepository();



    public List<User> filterUsersByConsumption(List<User> users, List<Consumption> consumptions) {
        // Group consumptions by userId
        Map<Integer, List<Consumption>> consumptionByUser = consumptions.stream()
                .collect(Collectors.groupingBy(consumption -> consumption.getUserId()));

        // Filter users whose total consumption exceeds 3000 KgCO2eq
        return users.stream()
                .filter(user -> {
                    // Get the list of consumptions for this user
                    List<Consumption> userConsumptions = consumptionByUser.get(user.getId());

                    // Sum the impact of the user's consumptions
                    double totalImpact = userConsumptions != null
                            ? userConsumptions.stream().mapToDouble(Consumption::calculateImpact).sum()
                            : 0;

                    // Filter users where total impact exceeds 3000 KgCO2eq
                    return totalImpact > 3000;
                })
                .collect(Collectors.toList());
    }

    // USERS HAVE > 3000 KGCO2EQ
    public void filterUsers() throws SQLException {

        List<User> users = userRepository.getAllUsers();

        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();

        List<User> filteredUsers = filterUsersByConsumption(users, consumptions);

        filteredUsers.forEach(System.out::println);
    }

    public List<User> filterInactiveUsers(List<User> users, List<Consumption> consumptions, LocalDate startDate, LocalDate endDate) {
        // Filter consumptions within the specified period
        List<Consumption> filteredConsumptions = consumptions.stream()
                .filter(c -> !c.getStartDate().isBefore(startDate) && !c.getEndDate().isAfter(endDate))
                .collect(Collectors.toList());

        // Group consumptions by userId
        Map<Integer, List<Consumption>> consumptionByUser = filteredConsumptions.stream()
                .collect(Collectors.groupingBy(Consumption::getUserId));

        // Filter users who have no consumption records in the specified period
        return users.stream()
                .filter(user -> !consumptionByUser.containsKey(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> sortUsersByConsumption(List<User> users, List<Consumption> consumptions) {
        Map<Integer, List<Consumption>> consumptionByUser = consumptions.stream()
                .collect(Collectors.groupingBy(Consumption::getUserId));

        return users.stream()
                .sorted((u1, u2) -> {
                    double totalImpact1 = consumptionByUser.getOrDefault(u1.getId(), List.of())
                            .stream().mapToDouble(Consumption::calculateImpact).sum();
                    double totalImpact2 = consumptionByUser.getOrDefault(u2.getId(), List.of())
                            .stream().mapToDouble(Consumption::calculateImpact).sum();
                    return Double.compare(totalImpact2, totalImpact1);
                })
                .collect(Collectors.toList());
    }
}
