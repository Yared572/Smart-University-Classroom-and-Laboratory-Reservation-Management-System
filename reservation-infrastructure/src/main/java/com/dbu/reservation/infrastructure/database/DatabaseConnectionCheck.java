package com.dbu.reservation.infrastructure.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/** Small command-line check for the configured MySQL connection. */
public final class DatabaseConnectionCheck {
    private DatabaseConnectionCheck() {
    }

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.open();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT 1")) {
            if (result.next() && result.getInt(1) == 1) {
                System.out.println("Database connection successful: " + connection.getCatalog());
            } else {
                System.err.println("Connected, but the database check returned an unexpected result.");
                System.exit(1);
            }
        } catch (Exception exception) {
            System.err.println("Database connection failed: " + exception.getMessage());
            System.err.println("Check MySQL is running and verify DBU_RESERVATION_DB_URL, DBU_RESERVATION_DB_USER, and DBU_RESERVATION_DB_PASSWORD.");
            System.exit(1);
        }
    }
}
