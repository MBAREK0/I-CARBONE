package services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import entities.Consumption;
import entities.User;
import repositories.ConsumptionRepository;
import repositories.UserRepository;
import java.sql.SQLException;




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

    public void filterUsers() throws SQLException {

        List<User> users = userRepository.getAllUsers();

        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();

        List<User> filteredUsers = filterUsersByConsumption(users, consumptions);

        filteredUsers.forEach(System.out::println);
    }
}
