import java.sql.Connection;

import database.DatabaseConnection;

public class App {
    public static void main(String[] args) {
        try {
            // Initialize the database connection (Singleton)
            DatabaseConnection connectionInstance = DatabaseConnection.getInstance();
            Connection connection = connectionInstance.getConnection();
            System.out.println("Database connection initialized!");

            // You can now use the connection in your repository classes or services
            // For example:
            // YourRepository repository = new YourRepository();
            // repository.someMethod();

        } catch (Exception e) {
            System.err.println("Error initializing the database connection: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Application is running...");
    }
}
