package com.student.management.ui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.student.management.dao.StudentDAO;
import com.student.management.model.Student;
import com.student.management.util.Theme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UpdateStudentForm extends JFrame {

    // Search Components
    private JTextField searchIdField;
    private JButton searchButton;

    // Editable Details Components
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField; // Add dummy address
    private DatePicker dateOfBirthPicker; // Add dummy DOB
    private DatePicker enrollmentDatePicker;
    
    private JButton updateButton;
    private JButton clearButton;

    private StudentDAO studentDAO;
    private Student currentStudent;

    public UpdateStudentForm() {
        studentDAO = new StudentDAO();

        setTitle("Student Management System - Update Student");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);

        try {
            URL iconUrl = getClass().getResource("/logo.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception ignored) {}

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
            new EmptyBorder(15, 30, 15, 30)
        ));
        
        JLabel titleLabel = new JLabel("Update Student");
        titleLabel.setFont(Theme.FONT_TITLE);
        
        JLabel subtitleLabel = new JLabel("Search and update student details");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setBackground(Theme.CARD_COLOR);
        titleGroup.add(titleLabel);
        titleGroup.add(subtitleLabel);
        
        headerPanel.add(titleGroup, BorderLayout.WEST);
        
        JLabel iconLabel = new JLabel(Theme.getIcon(FontAwesomeSolid.EDIT, 32, new Color(139, 92, 246)));
        headerPanel.add(iconLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Container
        JPanel contentContainer = new JPanel(new BorderLayout(0, 20));
        contentContainer.setBackground(Theme.BACKGROUND_COLOR);
        contentContainer.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        // 1. Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Theme.CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        
        JLabel searchLbl = new JLabel("Student ID:");
        searchLbl.setFont(Theme.FONT_BOLD);
        
        searchIdField = new JTextField(15);
        searchIdField.putClientProperty("JTextField.placeholderText", "Enter ID to search...");
        searchIdField.setPreferredSize(new Dimension(200, 35));
        
        searchButton = new JButton("Search");
        searchButton.setIcon(Theme.getIcon(FontAwesomeSolid.SEARCH, 14, Theme.CARD_COLOR));
        searchButton.setBackground(Theme.ACCENT_COLOR); // Amber
        searchButton.setForeground(Theme.CARD_COLOR);
        searchButton.setFont(Theme.FONT_BOLD);
        searchButton.putClientProperty("JButton.buttonType", "roundRect");
        searchButton.setPreferredSize(new Dimension(100, 35));
        
        searchPanel.add(searchLbl);
        searchPanel.add(searchIdField);
        searchPanel.add(searchButton);
        contentContainer.add(searchPanel, BorderLayout.NORTH);

        // 2. Form Card
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Theme.CARD_COLOR);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 0: ID (Read-only)
        gbc.gridy = 0; gbc.gridx = 0;
        idField = createStyledTextField("Student ID (Read-only)", FontAwesomeSolid.ID_CARD);
        idField.setEditable(false);
        formCard.add(idField, gbc);

        // Row 1
        gbc.gridy = 1; gbc.gridx = 0;
        firstNameField = createStyledTextField("First Name", FontAwesomeSolid.USER);
        formCard.add(firstNameField, gbc);

        gbc.gridx = 1;
        enrollmentDatePicker = createStyledDatePicker();
        formCard.add(createLabeledComponent("Enrollment Date", enrollmentDatePicker), gbc);

        // Row 2
        gbc.gridy = 2; gbc.gridx = 0;
        lastNameField = createStyledTextField("Last Name", FontAwesomeSolid.USER);
        formCard.add(lastNameField, gbc);

        gbc.gridx = 1; gbc.gridheight = 2;
        addressField = new JTextField();
        addressField.putClientProperty("JTextField.placeholderText", "Address");
        addressField.setPreferredSize(new Dimension(300, 90));
        formCard.add(addressField, gbc);
        gbc.gridheight = 1;

        // Row 3
        gbc.gridy = 3; gbc.gridx = 0;
        emailField = createStyledTextField("Email", FontAwesomeSolid.ENVELOPE);
        formCard.add(emailField, gbc);

        // Row 4
        gbc.gridy = 4; gbc.gridx = 0;
        phoneField = createStyledTextField("Phone Number", FontAwesomeSolid.PHONE);
        formCard.add(phoneField, gbc);

        gbc.gridx = 1;
        dateOfBirthPicker = createStyledDatePicker();
        formCard.add(createLabeledComponent("Date of Birth", dateOfBirthPicker), gbc);

        // Buttons
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 0, 15);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(Theme.CARD_COLOR);
        
        clearButton = new JButton("Clear");
        clearButton.setIcon(Theme.getIcon(FontAwesomeSolid.TIMES, 14, Theme.PRIMARY_COLOR));
        clearButton.setFont(Theme.FONT_BOLD);
        clearButton.setPreferredSize(new Dimension(120, 40));
        clearButton.putClientProperty("JButton.buttonType", "outlined");
        
        updateButton = new JButton("Update Details");
        updateButton.setIcon(Theme.getIcon(FontAwesomeSolid.SAVE, 14, Theme.CARD_COLOR));
        updateButton.setBackground(new Color(139, 92, 246)); // Purple
        updateButton.setForeground(Theme.CARD_COLOR);
        updateButton.setFont(Theme.FONT_BOLD);
        updateButton.setPreferredSize(new Dimension(160, 40));
        updateButton.putClientProperty("JButton.buttonType", "roundRect");
        updateButton.setEnabled(false); // Disabled initially

        buttonPanel.add(clearButton);
        buttonPanel.add(updateButton);
        formCard.add(buttonPanel, gbc);

        contentContainer.add(formCard, BorderLayout.CENTER);
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(Theme.createFooter(), BorderLayout.SOUTH);

        // Listeners
        searchButton.addActionListener(e -> performSearch());
        updateButton.addActionListener(e -> performUpdate());
        clearButton.addActionListener(e -> clearAllFields());

        add(mainPanel);
    }
    
    private JTextField createStyledTextField(String placeholder, FontAwesomeSolid icon) {
        JTextField field = new JTextField(20);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JTextField.leadingIcon", Theme.getIcon(icon, 14, Color.GRAY));
        field.setPreferredSize(new Dimension(300, 40));
        return field;
    }
    
    private DatePicker createStyledDatePicker() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFontValidDate(Theme.FONT_REGULAR);
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForDatesBeforeCommonEra("uuuu-MM-dd");
        
        DatePicker datePicker = new DatePicker(settings);
        datePicker.getComponentDateTextField().setPreferredSize(new Dimension(200, 40));
        
        JButton btn = datePicker.getComponentToggleCalendarButton();
        btn.setText("");
        btn.setIcon(Theme.getIcon(FontAwesomeSolid.CALENDAR_ALT, 16, Color.GRAY));
        btn.setPreferredSize(new Dimension(40, 40));
        
        return datePicker;
    }
    
    private JPanel createLabeledComponent(String placeholder, Component comp) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.CARD_COLOR);
        return (JPanel) comp; 
    }

    private void performSearch() {
        String input = searchIdField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(input);
            currentStudent = studentDAO.getStudentById(id);

            if (currentStudent != null) {
                // Populate fields
                idField.setText(String.valueOf(currentStudent.getId()));
                firstNameField.setText(currentStudent.getFirstName());
                lastNameField.setText(currentStudent.getLastName());
                emailField.setText(currentStudent.getEmail());
                phoneField.setText(currentStudent.getPhone() != null ? currentStudent.getPhone() : "");
                enrollmentDatePicker.setDate(currentStudent.getEnrollmentDate());
                
                // Address and DOB are dummy for now since model doesn't have them
                addressField.setText("");
                dateOfBirthPicker.setDate(null);
                
                // Enable update button
                updateButton.setEnabled(true);
            } else {
                clearDetailsFields();
                updateButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "No student found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Student ID must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performUpdate() {
        if (currentStudent == null) return;

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        LocalDate enrollmentDate = enrollmentDatePicker.getDate();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name, Last Name, and Email are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (enrollmentDate == null) {
            JOptionPane.showMessageDialog(this, "Please select an enrollment date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentStudent.setFirstName(firstName);
        currentStudent.setLastName(lastName);
        currentStudent.setEmail(email);
        currentStudent.setPhone(phone);
        currentStudent.setEnrollmentDate(enrollmentDate);

        boolean success = studentDAO.updateStudent(currentStudent);

        if (success) {
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearAllFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student. Please check details or duplicate email.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearDetailsFields() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        enrollmentDatePicker.setDate(null);
        dateOfBirthPicker.setDate(null);
        currentStudent = null;
    }

    private void clearAllFields() {
        searchIdField.setText("");
        clearDetailsFields();
        updateButton.setEnabled(false);
        searchIdField.requestFocus();
    }
}
