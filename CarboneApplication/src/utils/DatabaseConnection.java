package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;
    private static final String URL = "jdbc:postgresql://localhost:5432/icarbone";
    private static final String USER = "user";
    private static final String PASSWORD = "password";


    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {

                Class.forName("org.postgresql.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL driver not found.", e);
            }
        }
        return connection;
    }
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }

}
