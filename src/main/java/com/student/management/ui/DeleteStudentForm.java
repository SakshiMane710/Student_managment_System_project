package com.student.management.ui;

import com.student.management.dao.StudentDAO;
import com.student.management.model.Student;
import com.student.management.util.Theme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class DeleteStudentForm extends JFrame {

    private JTextField searchIdField;
    private JButton searchButton;

    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField enrollmentDateField;
    
    private JButton deleteButton;
    private JButton clearButton;

    private StudentDAO studentDAO;
    private Student currentStudent;

    public DeleteStudentForm() {
        studentDAO = new StudentDAO();

        setTitle("Student Management System - Delete Student");
        setSize(800, 500); // Shorter than update since no address/dob
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
        
        JLabel titleLabel = new JLabel("Delete Student");
        titleLabel.setFont(Theme.FONT_TITLE);
        
        JLabel subtitleLabel = new JLabel("Search and delete student records");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setBackground(Theme.CARD_COLOR);
        titleGroup.add(titleLabel);
        titleGroup.add(subtitleLabel);
        
        headerPanel.add(titleGroup, BorderLayout.WEST);
        
        JLabel iconLabel = new JLabel(Theme.getIcon(FontAwesomeSolid.TRASH_ALT, 32, Theme.ERROR_COLOR));
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
        searchButton.setBackground(Theme.PRIMARY_COLOR);
        searchButton.setForeground(Theme.CARD_COLOR);
        searchButton.setFont(Theme.FONT_BOLD);
        searchButton.putClientProperty("JButton.buttonType", "roundRect");
        searchButton.setPreferredSize(new Dimension(100, 35));
        
        searchPanel.add(searchLbl);
        searchPanel.add(searchIdField);
        searchPanel.add(searchButton);
        contentContainer.add(searchPanel, BorderLayout.NORTH);

        // 2. Form Card (Read Only)
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
        idField = createReadOnlyField("Student ID", FontAwesomeSolid.ID_CARD);
        formCard.add(idField, gbc);

        gbc.gridx = 1;
        firstNameField = createReadOnlyField("First Name", FontAwesomeSolid.USER);
        formCard.add(firstNameField, gbc);

        // Row 2
        gbc.gridy = 1; gbc.gridx = 0;
        lastNameField = createReadOnlyField("Last Name", FontAwesomeSolid.USER);
        formCard.add(lastNameField, gbc);

        gbc.gridx = 1;
        emailField = createReadOnlyField("Email", FontAwesomeSolid.ENVELOPE);
        formCard.add(emailField, gbc);

        // Row 3
        gbc.gridy = 2; gbc.gridx = 0;
        phoneField = createReadOnlyField("Phone Number", FontAwesomeSolid.PHONE);
        formCard.add(phoneField, gbc);

        gbc.gridx = 1;
        enrollmentDateField = createReadOnlyField("Enrollment Date", FontAwesomeSolid.CALENDAR_ALT);
        formCard.add(enrollmentDateField, gbc);

        // Buttons
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 0, 15);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(Theme.CARD_COLOR);
        
        clearButton = new JButton("Clear");
        clearButton.setIcon(Theme.getIcon(FontAwesomeSolid.TIMES, 14, Theme.PRIMARY_COLOR));
        clearButton.setFont(Theme.FONT_BOLD);
        clearButton.setPreferredSize(new Dimension(120, 40));
        clearButton.putClientProperty("JButton.buttonType", "outlined");
        
        deleteButton = new JButton("Delete Student");
        deleteButton.setIcon(Theme.getIcon(FontAwesomeSolid.TRASH, 14, Theme.CARD_COLOR));
        deleteButton.setBackground(Theme.ERROR_COLOR); 
        deleteButton.setForeground(Theme.CARD_COLOR);
        deleteButton.setFont(Theme.FONT_BOLD);
        deleteButton.setPreferredSize(new Dimension(160, 40));
        deleteButton.putClientProperty("JButton.buttonType", "roundRect");
        deleteButton.setEnabled(false); // Disabled initially

        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);
        formCard.add(buttonPanel, gbc);

        contentContainer.add(formCard, BorderLayout.CENTER);
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(Theme.createFooter(), BorderLayout.SOUTH);

        // Listeners
        searchButton.addActionListener(e -> performSearch());
        deleteButton.addActionListener(e -> performDelete());
        clearButton.addActionListener(e -> clearAllFields());

        add(mainPanel);
    }
    
    private JTextField createReadOnlyField(String placeholder, FontAwesomeSolid icon) {
        JTextField field = new JTextField(20);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JTextField.leadingIcon", Theme.getIcon(icon, 14, Color.GRAY));
        field.setPreferredSize(new Dimension(300, 40));
        field.setEditable(false);
        field.setBackground(new Color(249, 250, 251)); // slightly gray to show read only
        return field;
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
                idField.setText(String.valueOf(currentStudent.getId()));
                firstNameField.setText(currentStudent.getFirstName());
                lastNameField.setText(currentStudent.getLastName());
                emailField.setText(currentStudent.getEmail());
                phoneField.setText(currentStudent.getPhone() != null ? currentStudent.getPhone() : "");
                enrollmentDateField.setText(currentStudent.getEnrollmentDate().toString());
                
                deleteButton.setEnabled(true);
            } else {
                clearDetailsFields();
                deleteButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "No student found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Student ID must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performDelete() {
        if (currentStudent == null) return;

        // Custom styled confirmation dialog
        int confirm = JOptionPane.showOptionDialog(
            this,
            "Are you sure you want to permanently delete student: " + currentStudent.getFirstName() + " " + currentStudent.getLastName() + "?\nThis action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            Theme.getIcon(FontAwesomeSolid.EXCLAMATION_TRIANGLE, 32, Theme.ERROR_COLOR),
            new String[]{"Delete", "Cancel"},
            "Cancel"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = studentDAO.deleteStudent(currentStudent.getId());

            if (success) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Student record has been successfully deleted.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE,
                    Theme.getIcon(FontAwesomeSolid.CHECK_CIRCLE, 32, Theme.SECONDARY_COLOR)
                );
                clearAllFields();
            } else {
                JOptionPane.showMessageDialog(
                    this, 
                    "Failed to delete student. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE,
                    Theme.getIcon(FontAwesomeSolid.TIMES_CIRCLE, 32, Theme.ERROR_COLOR)
                );
            }
        }
    }

    private void clearDetailsFields() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        enrollmentDateField.setText("");
        currentStudent = null;
    }

    private void clearAllFields() {
        searchIdField.setText("");
        clearDetailsFields();
        deleteButton.setEnabled(false);
        searchIdField.requestFocus();
    }
}
