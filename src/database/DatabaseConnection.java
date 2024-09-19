package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        try {
            String url = "jdbc:postgresql://localhost:5432/Bati";
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            if (username == null || password == null) {
                throw new IllegalArgumentException("Database credentials not found in environment variables.");
            }

            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            throw new SQLException("Failed to create the database connection.", ex);
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void executeQuery(String query) {
        // Utilisation du try-with-resources pour garantir que Statement et ResultSet
        // seront fermés
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            // Traitement des résultats de la requête
            while (rs.next()) {
                System.out.println("Résultat : " + rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
