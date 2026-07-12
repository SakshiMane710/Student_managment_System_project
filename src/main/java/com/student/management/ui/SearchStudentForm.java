package com.student.management.ui;

import com.student.management.dao.StudentDAO;
import com.student.management.model.Student;
import com.student.management.util.Theme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchStudentForm extends JFrame {

    private JComboBox<String> searchCriteriaBox;
    private JTextField searchInputField;
    private JButton searchButton;
    private JButton clearButton;

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private JLabel resultsLabel;

    public SearchStudentForm() {
        studentDAO = new StudentDAO();

        setTitle("Student Management System - Search Student");
        setSize(950, 650);
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
        
        JLabel titleLabel = new JLabel("Search Student");
        titleLabel.setFont(Theme.FONT_TITLE);
        
        JLabel subtitleLabel = new JLabel("Search records by ID, Name or Email");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setBackground(Theme.CARD_COLOR);
        titleGroup.add(titleLabel);
        titleGroup.add(subtitleLabel);
        
        headerPanel.add(titleGroup, BorderLayout.WEST);
        
        JLabel iconLabel = new JLabel(Theme.getIcon(FontAwesomeSolid.SEARCH, 32, Theme.ACCENT_COLOR));
        headerPanel.add(iconLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Area
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Theme.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Top Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Theme.CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));

        String[] criteria = {"Student ID", "Email", "Name"};
        searchCriteriaBox = new JComboBox<>(criteria);
        searchCriteriaBox.setPreferredSize(new Dimension(150, 35));
        
        searchInputField = new JTextField(20);
        searchInputField.putClientProperty("JTextField.placeholderText", "Enter search value...");
        searchInputField.setPreferredSize(new Dimension(300, 35));
        
        searchButton = new JButton("Search");
        searchButton.setIcon(Theme.getIcon(FontAwesomeSolid.SEARCH, 14, Theme.CARD_COLOR));
        searchButton.setBackground(Theme.ACCENT_COLOR); // Amber
        searchButton.setForeground(Theme.CARD_COLOR);
        searchButton.setFont(Theme.FONT_BOLD);
        searchButton.putClientProperty("JButton.buttonType", "roundRect");
        searchButton.setPreferredSize(new Dimension(100, 35));

        searchPanel.add(new JLabel("Search By:"));
        searchPanel.add(searchCriteriaBox);
        searchPanel.add(searchInputField);
        searchPanel.add(searchButton);

        contentPanel.add(searchPanel, BorderLayout.NORTH);

        // Center Results Table
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Phone", "Enrollment Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Theme.BACKGROUND_COLOR);
        
        clearButton = new JButton("Clear Results");
        clearButton.setIcon(Theme.getIcon(FontAwesomeSolid.TIMES, 14, Theme.PRIMARY_COLOR));
        clearButton.setFont(Theme.FONT_BOLD);
        clearButton.putClientProperty("JButton.buttonType", "outlined");
        
        resultsLabel = new JLabel("Results found: 0");
        resultsLabel.setFont(Theme.FONT_BOLD);
        
        bottomPanel.add(clearButton, BorderLayout.WEST);
        bottomPanel.add(resultsLabel, BorderLayout.EAST);
        
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(Theme.createFooter(), BorderLayout.SOUTH);

        // Action Listeners
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> clearFields());
        // Also trigger search on enter key in input field
        searchInputField.addActionListener(e -> performSearch());

        add(mainPanel);
    }

    private void performSearch() {
        String input = searchInputField.getText().trim();
        String criteria = (String) searchCriteriaBox.getSelectedItem();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search value.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Student> results = new ArrayList<>();

        if ("Student ID".equals(criteria)) {
            try {
                int id = Integer.parseInt(input);
                Student student = studentDAO.getStudentById(id);
                if (student != null) results.add(student);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Student ID must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if ("Email".equals(criteria)) {
            Student student = studentDAO.getStudentByEmail(input);
            if (student != null) results.add(student);
        } else if ("Name".equals(criteria)) {
            results = studentDAO.searchStudentsByName(input);
        }

        if (!results.isEmpty()) {
            populateTable(results);
        } else {
            tableModel.setRowCount(0);
            resultsLabel.setText("Results found: 0");
            JOptionPane.showMessageDialog(this, "No student found with the given " + criteria + ".", "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void populateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] rowData = {
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getEnrollmentDate()
            };
            tableModel.addRow(rowData);
        }
        resultsLabel.setText("Results found: " + students.size());
    }

    private void clearFields() {
        searchInputField.setText("");
        searchCriteriaBox.setSelectedIndex(0);
        tableModel.setRowCount(0);
        resultsLabel.setText("Results found: 0");
        searchInputField.requestFocus();
    }
}
