package com.student.management.ui;

import com.student.management.dao.StudentDAO;
import com.student.management.model.Student;
import com.student.management.util.Theme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ViewStudentsForm extends JFrame {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private StudentDAO studentDAO;
    private JTextField searchField;
    private JLabel totalLabel;

    public ViewStudentsForm() {
        studentDAO = new StudentDAO();

        setTitle("Student Management System - View Students");
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
        
        JLabel titleLabel = new JLabel("All Students");
        titleLabel.setFont(Theme.FONT_TITLE);
        
        JLabel subtitleLabel = new JLabel("View and manage all registered students");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setBackground(Theme.CARD_COLOR);
        titleGroup.add(titleLabel);
        titleGroup.add(subtitleLabel);
        headerPanel.add(titleGroup, BorderLayout.WEST);

        // Search panel in header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(Theme.CARD_COLOR);
        searchField = new JTextField(20);
        searchField.putClientProperty("JTextField.placeholderText", "Search by name, email or phone...");
        searchField.putClientProperty("JTextField.leadingIcon", Theme.getIcon(FontAwesomeSolid.SEARCH, 14, Color.GRAY));
        searchField.setPreferredSize(new Dimension(250, 35));
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setIcon(Theme.getIcon(FontAwesomeSolid.SYNC_ALT, 14, Theme.CARD_COLOR));
        refreshBtn.setBackground(Theme.PRIMARY_COLOR);
        refreshBtn.setForeground(Theme.CARD_COLOR);
        refreshBtn.putClientProperty("JButton.buttonType", "roundRect");
        refreshBtn.addActionListener(e -> loadStudentData());
        
        searchPanel.add(searchField);
        searchPanel.add(refreshBtn);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Area
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBackground(Theme.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Table Setup
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
        
        rowSorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Theme.BACKGROUND_COLOR);
        
        JButton exportBtn = new JButton("Export to CSV");
        exportBtn.setIcon(Theme.getIcon(FontAwesomeSolid.FILE_CSV, 16, Theme.SECONDARY_COLOR));
        exportBtn.setFont(Theme.FONT_BOLD);
        exportBtn.putClientProperty("JButton.buttonType", "outlined");
        exportBtn.addActionListener(e -> exportToCSV());
        
        totalLabel = new JLabel("Total Students: 0");
        totalLabel.setFont(Theme.FONT_BOLD);
        
        bottomPanel.add(exportBtn, BorderLayout.WEST);
        bottomPanel.add(totalLabel, BorderLayout.EAST);
        
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(Theme.createFooter(), BorderLayout.SOUTH);

        // Search Filter Logic
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        // Initial Load
        loadStudentData();

        add(mainPanel);
    }

    private void loadStudentData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();

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
        
        totalLabel.setText("Total Students: " + students.size());
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as CSV");
        fileChooser.setSelectedFile(new File("students.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (FileWriter fw = new FileWriter(fileToSave)) {
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    fw.write(tableModel.getColumnName(i) + (i == tableModel.getColumnCount() - 1 ? "" : ","));
                }
                fw.write("\n");
                
                for (int i = 0; i < studentTable.getRowCount(); i++) {
                    for (int j = 0; j < studentTable.getColumnCount(); j++) {
                        Object val = studentTable.getValueAt(i, j);
                        fw.write((val != null ? val.toString() : "") + (j == studentTable.getColumnCount() - 1 ? "" : ","));
                    }
                    fw.write("\n");
                }
                
                JOptionPane.showMessageDialog(this, "Data exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error writing to file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
