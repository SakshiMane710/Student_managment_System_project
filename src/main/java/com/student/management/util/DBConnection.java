package com.student.management.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/student_management_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Sakshi@710"; // Change this if your DB password is different

    private static Connection connection = null;

    /**
     * Establishes and returns a database connection using the singleton pattern.
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    // Register the MySQL JDBC driver (Optional for JDBC 4.0+)
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    
                    // Establish connection
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                } catch (ClassNotFoundException e) {
                    System.err.println("JDBC Driver not found. Make sure the dependency is included in pom.xml.");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.err.println("Failed to connect to the database. Check credentials and ensure MySQL is running.");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the existing database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error while closing the database connection.");
                e.printStackTrace();
            }
        }
    }
}
