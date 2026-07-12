package com.student.management.ui;

import com.student.management.dao.AdminDAO;

import com.student.management.util.Theme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AdminDAO adminDAO;

    public LoginForm() {
        adminDAO = new AdminDAO();
        
        setTitle("Student Management System - Login");
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
        // Main panel with GridBagLayout to perfectly center the login card
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        // The login card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Theme.CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1), // Light border
                new EmptyBorder(40, 60, 50, 60)
        ));

        // Graduation Cap Icon
        JLabel iconLabel = new JLabel(Theme.getIcon(FontAwesomeSolid.USER_GRADUATE, 64, Theme.PRIMARY_COLOR));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(iconLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Header
        JLabel headerLabel = new JLabel("Student Management System");
        headerLabel.setFont(Theme.FONT_TITLE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(headerLabel);
        
        JLabel subHeaderLabel = new JLabel("Admin Login");
        subHeaderLabel.setFont(Theme.FONT_REGULAR);
        subHeaderLabel.setForeground(Color.GRAY);
        subHeaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(subHeaderLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.CARD_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Username Field
        gbc.gridy = 0;
        usernameField = new JTextField(20);
        usernameField.putClientProperty("JTextField.placeholderText", "Username");
        usernameField.putClientProperty("JTextField.leadingIcon", Theme.getIcon(FontAwesomeSolid.USER, 16, Color.GRAY));
        usernameField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(usernameField, gbc);

        // Password Field
        gbc.gridy = 1;
        passwordField = new JPasswordField(20);
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        passwordField.putClientProperty("JTextField.leadingIcon", Theme.getIcon(FontAwesomeSolid.LOCK, 16, Color.GRAY));
        passwordField.setPreferredSize(new Dimension(300, 40));
        formPanel.add(passwordField, gbc);
        
        // Show Password & Forgot Password row
        gbc.gridy = 2;
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(Theme.CARD_COLOR);
        
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(Theme.CARD_COLOR);
        showPassword.setFont(Theme.FONT_SMALL);
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        optionsPanel.add(showPassword, BorderLayout.WEST);

        JLabel forgotPassword = new JLabel("Forgot Password?");
        forgotPassword.setFont(Theme.FONT_SMALL);
        forgotPassword.setForeground(Theme.PRIMARY_COLOR);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginForm.this, "Please contact system administrator.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        optionsPanel.add(forgotPassword, BorderLayout.EAST);
        
        formPanel.add(optionsPanel, gbc);

        // Login Button
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 10, 0);
        loginButton = new JButton("Login");
        loginButton.setFont(Theme.FONT_BOLD);
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        loginButton.setIcon(Theme.getIcon(FontAwesomeSolid.SIGN_IN_ALT, 16, Theme.CARD_COLOR));
        formPanel.add(loginButton, gbc);
        
        // Register link
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBackground(Theme.CARD_COLOR);
        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(Theme.FONT_SMALL);
        JLabel registerLabel = new JLabel("Register");
        registerLabel.setFont(Theme.FONT_SMALL);
        registerLabel.setForeground(Theme.PRIMARY_COLOR);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new AdminRegistrationForm().setVisible(true);
            }
        });
        registerPanel.add(noAccountLabel);
        registerPanel.add(registerLabel);
        formPanel.add(registerPanel, gbc);

        cardPanel.add(formPanel);
        mainPanel.add(cardPanel);

        // Action Listener for Login Button
        loginButton.addActionListener(e -> handleLogin());

        // Setup main layout with footer
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(Theme.createFooter(), BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isAuthenticated = adminDAO.authenticate(username, password);

        if (isAuthenticated) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Close login form and open dashboard
            this.dispose();
            DashboardForm dashboard = new DashboardForm(username);
            dashboard.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
