package com.student.management;

import com.student.management.dao.AdminDAO;
import com.student.management.ui.AdminRegistrationForm;
import com.student.management.ui.LoginForm;

import javax.swing.*;

import com.student.management.util.Theme;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Student Management System - Phase 2...");

        try {
            Theme.setup();
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Launch the application GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminDAO adminDAO = new AdminDAO();
                
                // If there are no admins, prompt to register the first admin
                if (!adminDAO.hasAdmins()) {
                    AdminRegistrationForm registrationForm = new AdminRegistrationForm();
                    registrationForm.setVisible(true);
                } else {
                    // Admins exist, proceed to login
                    LoginForm loginForm = new LoginForm();
                    loginForm.setVisible(true);
                }
            }
        });
    }
}
