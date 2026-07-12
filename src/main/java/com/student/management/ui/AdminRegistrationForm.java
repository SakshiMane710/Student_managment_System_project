package com.student.management.ui;

import com.student.management.dao.AdminDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class AdminRegistrationForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private AdminDAO adminDAO;

    public AdminRegistrationForm() {
        adminDAO = new AdminDAO();

        setTitle("Student Management System - Admin Registration");
        setSize(900, 700); // Desktop mode size
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full laptop desktop mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        // Set App Icon
        try {
            URL iconUrl = getClass().getResource("/logo.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception ignored) {}

        initUI();
    }

    private void initUI() {
        // Main panel with GridBagLayout to perfectly center the card
        JPanel mainPanel = new JPanel(new GridBagLayout());

        // The registration card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 10));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1),
                new EmptyBorder(30, 50, 40, 50)
        ));

        boolean isFirstTime = !adminDAO.hasAdmins();

        // Header inside the card
        JLabel headerLabel = new JLabel(isFirstTime ? "First Time Setup" : "Register Admin", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        cardPanel.add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Info Label
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        if (isFirstTime) {
            JLabel infoLabel = new JLabel("<html><center>No admin found in the database.<br>Please register the first administrator.</center></html>", SwingConstants.CENTER);
            infoLabel.setForeground(UIManager.getColor("Actions.Blue"));
            formPanel.add(infoLabel, gbc);
        }

        gbc.gridwidth = 1; // reset

        // Username Label and Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password Label and Field
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        // Confirm Password Label and Field
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        confirmPasswordField = new JPasswordField(15);
        formPanel.add(confirmPasswordField, gbc);
        
        // Show Password Checkbox
        gbc.gridx = 1; gbc.gridy = 4;
        JCheckBox showPassword = new JCheckBox("Show Passwords");
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
                confirmPasswordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
                confirmPasswordField.setEchoChar('•');
            }
        });
        formPanel.add(showPassword, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        
        JButton loginButton = new JButton("Go to Login");
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });
        
        if (!isFirstTime) {
            buttonPanel.add(loginButton);
        }

        registerButton = new JButton("Register");
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(registerButton);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 10, 0, 10);
        formPanel.add(buttonPanel, gbc);

        cardPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(cardPanel);

        // Action Listener for Register Button
        registerButton.addActionListener(e -> handleRegistration());

        add(mainPanel);
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isRegistered = adminDAO.registerAdmin(username, password);

        if (isRegistered) {
            JOptionPane.showMessageDialog(this, "Admin registered successfully! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Close registration form and open login form
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username might already exist.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
