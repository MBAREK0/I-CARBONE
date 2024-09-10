package repositories;

import entities.User;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {



    public UserRepository() {

    }

    // Create user
    public void addUser(User user) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "INSERT INTO users (name, age, cin) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.setString(3, user.getCin());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {

            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
    }

    // Read user by ID
    public User getUserById(int id) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("cin")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
        return null;
    }

    // Read user by CIN
    public User getUserByCin(String cin) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM users WHERE cin = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("cin")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
        return null;
    }

    // Update user
    public void updateUser(User user) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "UPDATE users SET name = ?, age = ?, cin = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.setString(3, user.getCin());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
    }

    // Delete user
    public void deleteUser(int id) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Delete records in the consumption table first
            String deleteConsumptionSql = "DELETE FROM consumption WHERE userid = ?";
            try (PreparedStatement deleteConsumptionStatement = connection.prepareStatement(deleteConsumptionSql)) {
                deleteConsumptionStatement.setInt(1, id);
                deleteConsumptionStatement.executeUpdate();
            }

            // Now delete the user
            String deleteUserSql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserSql)) {
                deleteUserStatement.setInt(1, id);
                deleteUserStatement.executeUpdate();
            }

            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(); // Rollback in case of error
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true); // Restore default auto-commit behavior
            DatabaseConnection.closeConnection();
        }
    }


    // List all users
    public List<User> getAllUsers() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, age, cin FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("cin")
                );
                users.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection();
        }
        return users;
    }

}
