package repositories;

import entities.Consumption;
import entities.ConsumptionType;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entities.Food;
import entities.Housing;
import entities.Transport;


public class ConsumptionRepository {

    private Connection connection;

    public ConsumptionRepository() {
        try {
            // Use the connection from the DatabaseConnection utility
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addConsumption(Consumption consumption) throws SQLException {
        String insertConsumptionSQL = "INSERT INTO Consumption (startDate, endDate, amount, consumptionImpact, consumptionType, userId) VALUES (?, ?, ?, ?, ?, ?)";
        String insertSpecificSQL = null;

        try {
            // Disable auto-commit
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

                // Retrieve the generated ID of the inserted consumption
                int consumptionId = 0;
                try (var generatedKeys = consumptionStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        consumptionId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating consumption failed, no ID obtained.");
                    }
                }

                // Determine the specific SQL statement and parameters based on the consumption type
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

                // Commit the transaction
                connection.commit();
            } catch (SQLException e) {
                // Rollback the transaction if something goes wrong
                connection.rollback();
                throw e; // Re-throw the exception to be handled by the caller
            }
        } finally {
            // Restore auto-commit mode
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Additional methods for updating, deleting, and retrieving consumption records

    public void updateConsumption(Consumption consumption) throws SQLException {
        String sql = "UPDATE Consumption SET startDate = ?, endDate = ?, amount = ?, consumptionImpact = ?, consumptionType = ?, userId = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            statement.setDate(1, java.sql.Date.valueOf(consumption.getStartDate()));
            statement.setDate(2, java.sql.Date.valueOf(consumption.getEndDate()));
            statement.setDouble(3, consumption.getAmount());
            statement.setDouble(4, consumption.calculateImpact());
            statement.setString(5, consumption.getType().name());
            statement.setInt(6, consumption.getUserId());
            statement.setInt(7, consumption.getId()); // Assuming Consumption has a getId() method

            // Execute the update operation
            statement.executeUpdate();
        }
    }

    public void deleteConsumption(int id) throws SQLException {
        String sql = "DELETE FROM Consumption WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the ID parameter
            statement.setInt(1, id);

            // Execute the delete operation
            statement.executeUpdate();
        }
    }

    // Add more methods for retrieving records as needed
}
