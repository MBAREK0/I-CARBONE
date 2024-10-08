package repositories;

import entities.Consumption;
import dataBase.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entities.Food;
import entities.Housing;
import entities.Transport;
import entities.ConsumptionType;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class ConsumptionRepository {



    public ConsumptionRepository() {

    }
    public Consumption addConsumption(Consumption consumption) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String insertConsumptionSQL = "INSERT INTO Consumption (startDate, endDate, amount, consumptionImpact, consumptionType, userId) VALUES (?, ?, ?, ?, ?, ?)";
        String insertSpecificSQL = null;

        try {
            // Disable auto commit for manual transaction management and rollback on error
            connection.setAutoCommit(false);

            try (PreparedStatement consumptionStatement = connection.prepareStatement(insertConsumptionSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                consumptionStatement.setDate(1, java.sql.Date.valueOf(consumption.getStartDate()));
                consumptionStatement.setDate(2, java.sql.Date.valueOf(consumption.getEndDate()));
                consumptionStatement.setDouble(3, consumption.getAmount());
                consumptionStatement.setDouble(4, consumption.calculateImpact());
                consumptionStatement.setString(5, consumption.getType().name());
                consumptionStatement.setInt(6, consumption.getUserId());

                int affectedRows = consumptionStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating consumption failed, no rows affected.");
                }

                int consumptionId = 0;
                try (var generatedKeys = consumptionStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        consumptionId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating consumption failed, no ID obtained.");
                    }
                }

                if (consumption instanceof Food) {
                    insertSpecificSQL = "INSERT INTO Food (typeOfFood, weight, consumptionId) VALUES (?, ?, ?)";
                    try (PreparedStatement specificStatement = connection.prepareStatement(insertSpecificSQL)) {
                        Food food = (Food) consumption;
                        specificStatement.setString(1, food.getTypeOfFood());
                        specificStatement.setDouble(2, food.getWeight());
                        specificStatement.setInt(3, consumptionId);
                        specificStatement.executeUpdate();
                    }
                } else if (consumption instanceof Housing) {
                    insertSpecificSQL = "INSERT INTO Housing (energyConsumption, energyType, consumptionId) VALUES (?, ?, ?)";
                    try (PreparedStatement specificStatement = connection.prepareStatement(insertSpecificSQL)) {
                        Housing housing = (Housing) consumption;
                        specificStatement.setDouble(1, housing.getEnergyConsumption());
                        specificStatement.setString(2, housing.getEnergyType());
                        specificStatement.setInt(3, consumptionId);
                        specificStatement.executeUpdate();
                    }
                } else if (consumption instanceof Transport) {
                    insertSpecificSQL = "INSERT INTO Transport (distanceTraveled, vehicleType, consumptionId) VALUES (?, ?, ?)";
                    try (PreparedStatement specificStatement = connection.prepareStatement(insertSpecificSQL)) {
                        Transport transport = (Transport) consumption;
                        specificStatement.setDouble(1, transport.getDistanceTraveled());
                        specificStatement.setString(2, transport.getVehicleType());
                        specificStatement.setInt(3, consumptionId);
                        specificStatement.executeUpdate();
                    }
                }


                connection.commit();
            } catch (SQLException e) {

                connection.rollback();
                throw e;
            }
        } finally {

            try {
                connection.setAutoCommit(true);
                DatabaseConnection.closeConnection();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return consumption;
    }


    public List<Consumption> getAllConsumptions() throws SQLException {

        Connection connection = DatabaseConnection.getConnection();
        List<Consumption> consumptions = new ArrayList<>();
        String sql = "SELECT id, startDate, endDate, amount, consumptionImpact, consumptionType, userId FROM Consumption";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                LocalDate endDate = resultSet.getDate("endDate").toLocalDate();
                double amount = resultSet.getDouble("amount");
                double impact = resultSet.getDouble("consumptionImpact");
                ConsumptionType type = ConsumptionType.valueOf(resultSet.getString("consumptionType"));
                int userId = resultSet.getInt("userId");

                // Based on the type, create the specific consumption object
                Consumption consumption;
                switch (type) {
                    case FOOD:
                        // getFoodConsumption is a private method that returns a Food object
                        consumption = getFoodConsumption(id, startDate, endDate, amount, impact, userId);
                        break;
                    case HOUSING:
                        // getHousingConsumption is a private method that returns a Housing object
                        consumption = getHousingConsumption(id, startDate, endDate, amount, impact, userId);
                        break;
                    case TRANSPORT:
                        // getTransportConsumption is a private method that returns a Transport object
                        consumption = getTransportConsumption(id, startDate, endDate, amount, impact, userId);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown consumption type: " + type);
                }
                consumptions.add(consumption);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DatabaseConnection.closeConnection();
        }
        return consumptions;
    }

    // Private methods to get FOOD consumption objects
    private Food getFoodConsumption(int id, LocalDate startDate, LocalDate endDate, double amount, double impact, int userId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT typeOfFood, weight FROM Food WHERE consumptionId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String typeOfFood = resultSet.getString("typeOfFood");
                double weight = resultSet.getDouble("weight");
                return new Food(startDate, endDate, amount, ConsumptionType.FOOD, userId, typeOfFood, weight);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DatabaseConnection.closeConnection();
        }
        return null;
    }

    private Housing getHousingConsumption(int id, LocalDate startDate, LocalDate endDate, double amount, double impact, int userId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT energyConsumption, energyType FROM Housing WHERE consumptionId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double energyConsumption = resultSet.getDouble("energyConsumption");
                String energyType = resultSet.getString("energyType");
                return new Housing(startDate, endDate, amount, ConsumptionType.HOUSING, userId, energyConsumption, energyType);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DatabaseConnection.closeConnection();
        }
        return null;
    }

    private Transport getTransportConsumption(int id, LocalDate startDate, LocalDate endDate, double amount, double impact, int userId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT distanceTraveled, vehicleType FROM Transport WHERE consumptionId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double distanceTraveled = resultSet.getDouble("distanceTraveled");
                String vehicleType = resultSet.getString("vehicleType");
                return new Transport(startDate, endDate, amount, ConsumptionType.TRANSPORT, userId, distanceTraveled, vehicleType);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DatabaseConnection.closeConnection();
        }
        return null;
    }

}
