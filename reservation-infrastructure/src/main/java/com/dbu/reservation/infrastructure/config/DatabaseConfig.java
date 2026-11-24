package com.dbu.reservation.infrastructure.config;

public final class DatabaseConfig {
    private DatabaseConfig() {
    }

    public static String url() {
        return System.getenv().getOrDefault("DBU_RESERVATION_DB_URL",
                "jdbc:mysql://localhost:3306/dbu_reservations?serverTimezone=Africa/Addis_Ababa");
    }

    public static String user() {
        return System.getenv().getOrDefault("DBU_RESERVATION_DB_USER", "root");
    }

    public static String password() {
        // Local development default documented for this project. Production deployments
        // should always provide DBU_RESERVATION_DB_PASSWORD through the environment.
        String configured = System.getenv("DBU_RESERVATION_DB_PASSWORD");
        return configured == null || configured.isBlank() ? "1615" : configured;
    }
}
