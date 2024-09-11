package services.user;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import entities.*;
import repositories.ConsumptionRepository;
import repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import services.consumption.ConsumptionService;



public class UserService {

    private UserRepository userRepository = new UserRepository();
    private final ConsumptionRepository consumptionRepository = new ConsumptionRepository();
    private final ConsumptionService consumptionService= new ConsumptionService();


    /**
     * Add a new user to the database
     * @param id
     * @param name
     * @param age
     * @param cin
     * @return
     */

    public UserResult addUser(int id, String name, int age, String cin) {
        User user = new User(id, name, age, cin);

        try {
            if (userRepository.getUserByCin(cin) == null) {
                userRepository.addUser(user);
                return new UserResult(true, "| Account created successfully |");
            } else {
                return new UserResult(false, "| Account creation failed. User with this CIN already exists |");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new UserResult(false, "| Account creation failed. Database error: " + e.getMessage() + " |");
        }
    }

    /**
     * Update user information
     * @param userId
     * @param name
     * @param age
     * @param cin
     * @return
     */

    public UserResult updateUser(int userId, String name, int age, String cin) {
        try{
            User user = new User(userId, name, age, cin);
            userRepository.updateUser(user);
            return new UserResult(true, "| Account updated successfully |");

        }catch (SQLException e){
            System.out.println("\n\t\t| Database error: " + e.getMessage() + " |");
            return new UserResult(false, "| Account update failed |");
        }

    }

    /**
     * Delete user account
     * @param cin
     * @return
     */
    public UserResult deleteUser(String cin){
        try {

            User user = userRepository.getUserByCin(cin);

            if (user != null) {

                userRepository.deleteUser(user.getId());
                return  new UserResult(true, "| Account deleted successfully |");

            } else {
               return new UserResult(false, "| Account deletion failed. User not found |");
            }
        } catch (SQLException e) {
            System.out.println("\n\t\t| Database error: " + e.getMessage() + " |");
            return new UserResult(false, "| Account deletion failed |");
        }
    }

    /**
     * Display user information
     * @param cin
     * @return
     */
    public Optional displayUserInfo(String cin) {
        try {
            User user = userRepository.getUserByCin(cin);

            if (user != null) {
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.out.println("\n\t\t| Database error: " + e.getMessage() + " |");
            return Optional.empty();
        }
    }


    /**
     * Find users who have not made any consumption in the specified period
     * @param startDate
     * @param endDate
     * @return
     * @throws SQLException
     */
    public List<User> findInactiveUsers(LocalDate startDate,LocalDate endDate) throws SQLException {
        
        List<User> users = userRepository.getAllUsers();
        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();

        return filterInactiveUsers(users, consumptions, startDate, endDate);
    }

    /**
     * Filter users who have not made any consumption in the specified period
     * @param users
     * @param consumptions
     * @param startDate
     * @param endDate
     * @return
     */
    public List<User> filterInactiveUsers(List<User> users, List<Consumption> consumptions, LocalDate startDate, LocalDate endDate) {
        // Filter consumptions within the specified period
        List<Consumption> filteredConsumptions = consumptions.stream()
                .filter(c -> (c.getStartDate().isBefore(endDate) || c.getStartDate().isEqual(endDate)) &&
                        (c.getEndDate().isAfter(startDate) || c.getEndDate().isEqual(startDate)))
                .collect(Collectors.toList());

        // Group consumptions by userId
        Map<Integer, List<Consumption>> consumptionByUser = filteredConsumptions.stream()
                .collect(Collectors.groupingBy(Consumption::getUserId));

        // Filter users who have no consumption records in the specified period
        return users.stream()
                .filter(user -> !consumptionByUser.containsKey(user.getId()))
                .collect(Collectors.toList());
    }


    /**
*                                eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee                                        s
     * sssssssssssssssssssssssssssssssssssssssssssssssss
     * """"""""""""""""""""""""""S---------------------E"""""""""""""""""""""""""""
     * Display users sorted by total carbon consumption impact
     */
    public void displaySortedUsersByConsumption() {
        try {
            // Fetch all users and consumptions
            List<User> users = userRepository.getAllUsers();
            List<Consumption> consumptions = consumptionRepository.getAllConsumptions();

            // Sort users
            List<User> sortedUsers = sortUsersByConsumption(users, consumptions);

            // Display sorted users
            System.out.println("\t\tUsers sorted by total carbon consumption:");
            sortedUsers.forEach(user -> System.out.println("\t\t" + user + ": Total Consumption impact  = " +
                    consumptions.stream()
                            .filter(c -> c.getUserId() == user.getId())
                            .mapToDouble(Consumption::calculateImpact)
                            .sum()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display users who have total carbon consumption impact greater than 3000 kgCO2eq
     * @throws SQLException
     */
    public void printFilteredUsers() throws SQLException {

        List<User> users = userRepository.getAllUsers();

        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();

        List<User> filteredUsers = filterUsersByConsumption(users, consumptions);

        filteredUsers.forEach(user -> System.out.println("\t\t"+user ));
    }

    /**
     * Filter users who have total carbon consumption impact greater than 3000 kgCO2eq
     * @param users
     * @param consumptions
     * @return
     */
    public List<User> filterUsersByConsumption(List<User> users, List<Consumption> consumptions) {
        // Group consumptions by userId
        Map<Integer, List<Consumption>> consumptionByUser = consumptions.stream()
                .collect(Collectors.groupingBy(consumption -> consumption.getUserId()));


        return users.stream()
                .filter(user -> {

                    List<Consumption> userConsumptions = consumptionByUser.get(user.getId());


                    double totalImpact = userConsumptions != null
                            ? userConsumptions.stream().mapToDouble(Consumption::calculateImpact).sum()
                            : 0;

                    return totalImpact > 3000;
                })
                .collect(Collectors.toList());
    }


    /**
     * Sort users by total carbon consumption impact
     * @param users
     * @param consumptions
     * @return
     */
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

    public Double displayAverageConsumptionForPeriod(LocalDate startDate, LocalDate endDate, String userCin) throws SQLException {
        User user = userRepository.getUserByCin(userCin);
        if (user == null) {
            System.out.println("User with CIN " + userCin + " not found.");
            return null;
        }
        // Get all consumptions
        List<Consumption> consumptions = consumptionRepository.getAllConsumptions();
        Map<Integer, List<Consumption>> consumptionByUser = consumptions.stream()
                .collect(Collectors.groupingBy(Consumption::getUserId));

        // get all consumptions for the user
        List<Consumption> userConsumptions = consumptionByUser.get(user.getId());


        double averageConsumption = consumptionService.getAverageConsumptionInPeriod(startDate, endDate, userConsumptions);

        return averageConsumption;
    }




}
