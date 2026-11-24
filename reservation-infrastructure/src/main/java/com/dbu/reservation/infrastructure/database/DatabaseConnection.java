package com.dbu.reservation.infrastructure.database;
import com.dbu.reservation.infrastructure.config.DatabaseConfig;
import java.sql.*;
public final class DatabaseConnection {private DatabaseConnection(){}public static Connection open() throws SQLException{return DriverManager.getConnection(DatabaseConfig.url(),DatabaseConfig.user(),DatabaseConfig.password());}}
