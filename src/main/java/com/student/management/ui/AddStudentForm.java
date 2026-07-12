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

public class AddStudentForm extends JFrame {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField; // Adding dummy address field as per design
    private DatePicker dateOfBirthPicker; // Adding DOB as per design
    private DatePicker enrollmentDatePicker;
    
    private JButton saveButton;
    private JButton clearButton;
    private StudentDAO studentDAO;

    public AddStudentForm() {
        studentDAO = new StudentDAO();

        setTitle("Student Management System - Add Student");
        setSize(800, 550);
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
        
        JLabel titleLabel = new JLabel("Add New Student");
        titleLabel.setFont(Theme.FONT_TITLE);
        
        JLabel subtitleLabel = new JLabel("Enter student details to register");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setBackground(Theme.CARD_COLOR);
        titleGroup.add(titleLabel);
        titleGroup.add(subtitleLabel);
        
        headerPanel.add(titleGroup, BorderLayout.WEST);
        
        JLabel iconLabel = new JLabel(Theme.getIcon(FontAwesomeSolid.USER_PLUS, 32, Theme.PRIMARY_COLOR));
        headerPanel.add(iconLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Container
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Theme.BACKGROUND_COLOR);
        formContainer.setBorder(new EmptyBorder(30, 50, 30, 50));
        
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

        // Row 1
        gbc.gridy = 0; gbc.gridx = 0;
        firstNameField = createStyledTextField("First Name", FontAwesomeSolid.USER);
        formCard.add(firstNameField, gbc);

        gbc.gridx = 1;
        enrollmentDatePicker = createStyledDatePicker();
        formCard.add(createLabeledComponent("Enrollment Date", enrollmentDatePicker), gbc);

        // Row 2
        gbc.gridy = 1; gbc.gridx = 0;
        lastNameField = createStyledTextField("Last Name", FontAwesomeSolid.USER);
        formCard.add(lastNameField, gbc);

        gbc.gridx = 1; gbc.gridheight = 2;
        addressField = new JTextField();
        addressField.putClientProperty("JTextField.placeholderText", "Address");
        addressField.setPreferredSize(new Dimension(300, 90));
        formCard.add(addressField, gbc);
        gbc.gridheight = 1;

        // Row 3
        gbc.gridy = 2; gbc.gridx = 0;
        emailField = createStyledTextField("Email", FontAwesomeSolid.ENVELOPE);
        formCard.add(emailField, gbc);

        // Row 4
        gbc.gridy = 3; gbc.gridx = 0;
        phoneField = createStyledTextField("Phone Number", FontAwesomeSolid.PHONE);
        formCard.add(phoneField, gbc);

        gbc.gridx = 1;
        dateOfBirthPicker = createStyledDatePicker();
        formCard.add(createLabeledComponent("Date of Birth", dateOfBirthPicker), gbc);

        // Buttons
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 0, 15);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(Theme.CARD_COLOR);
        
        clearButton = new JButton("Reset");
        clearButton.setIcon(Theme.getIcon(FontAwesomeSolid.REDO, 14, Theme.PRIMARY_COLOR));
        clearButton.setFont(Theme.FONT_BOLD);
        clearButton.setPreferredSize(new Dimension(120, 40));
        clearButton.putClientProperty("JButton.buttonType", "outlined");
        
        saveButton = new JButton("Save Student");
        saveButton.setIcon(Theme.getIcon(FontAwesomeSolid.SAVE, 14, Theme.CARD_COLOR));
        saveButton.setFont(Theme.FONT_BOLD);
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.putClientProperty("JButton.buttonType", "roundRect");

        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        formCard.add(buttonPanel, gbc);

        formContainer.add(formCard, BorderLayout.CENTER);
        mainPanel.add(formContainer, BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(Theme.createFooter(), BorderLayout.SOUTH);

        // Listeners
        saveButton.addActionListener(e -> saveStudent());
        clearButton.addActionListener(e -> clearFields());

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
        datePicker.setDateToToday();
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
        
        JTextField dummy = new JTextField();
        dummy.putClientProperty("JTextField.placeholderText", placeholder);
        dummy.setEnabled(false);
        dummy.setPreferredSize(new Dimension(100, 40));
        dummy.setBorder(BorderFactory.createEmptyBorder());
        dummy.setBackground(Theme.CARD_COLOR);
        
        // This is a small hack to simulate the floating label or embedded label look from the design
        // Since LGoodDatePicker is its own component, we wrap it
        
        return (JPanel) comp; // For now just return the component, let's keep it simple
    }

    private void saveStudent() {
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

        Student newStudent = new Student(firstName, lastName, email, phone, enrollmentDate);
        boolean success = studentDAO.addStudent(newStudent);

        if (success) {
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Email might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        enrollmentDatePicker.setDateToToday();
        dateOfBirthPicker.setDate(null);
        firstNameField.requestFocus();
    }
}
