package com.student.management.dao;

import com.student.management.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    /**
     * Authenticates an admin using the provided username and password.
     *
     * @param username The admin username.
     * @param password The admin password.
     * @return true if credentials are valid, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If a record is found, the credentials are correct
                return resultSet.next();
            }
            
        } catch (SQLException e) {
            System.err.println("Database error during authentication.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if there are any admin users registered in the system.
     *
     * @return true if at least one admin exists, false otherwise.
     */
    public boolean hasAdmins() {
        String query = "SELECT COUNT(*) FROM admin";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
             
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Database error checking for existing admins.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Registers a new admin in the system.
     *
     * @param username The admin username.
     * @param password The admin password.
     * @return true if registration was successful, false otherwise.
     */
    public boolean registerAdmin(String username, String password) {
        String query = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password); // Note: In production, hash this password!
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Database error during admin registration.");
            e.printStackTrace();
            return false;
        }
    }
}
