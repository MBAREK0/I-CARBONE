package database;



import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQueryExample {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Get the singleton connection instance
            conn = DatabaseConnection.getConnection();

            // Create a statement
            stmt = conn.createStatement();

            // Execute a query
            String query = "SELECT * FROM people"; // Replace with your actual table name
            rs = stmt.executeQuery(query);

            // Process the result set
            while (rs.next()) {
                // Replace "first_name" with your actual column name
                System.out.println("First Name: " + rs.getString("first_name"));
                // Process other columns as needed
            }
        } catch (SQLException e) {
            System.err.println("SQL error occurred.");
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Connection is managed by the Singleton and should not be closed here
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
