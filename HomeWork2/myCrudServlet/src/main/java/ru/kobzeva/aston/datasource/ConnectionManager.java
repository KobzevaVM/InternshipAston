package ru.kobzeva.aston.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static ConnectionManager connectionManager;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private ConnectionManager() {
    }

    public static synchronized ConnectionManager getConnectionManager() {
        if (connectionManager == null) {
            connectionManager = new ConnectionManager();
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                System.out.println("Driver not loaded");
            }
        }

        return connectionManager;
    }
}
