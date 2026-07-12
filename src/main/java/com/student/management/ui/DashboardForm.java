package com.student.management.ui;

import com.student.management.dao.StudentDAO;
import com.student.management.util.Theme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.URL;

public class DashboardForm extends JFrame {

    private String username;
    private JLabel countLabel;
    private StudentDAO studentDAO;
    private JLabel timeLabel;

    public DashboardForm(String username) {
        this.username = username;
        this.studentDAO = new StudentDAO();
        setTitle("Student Management System - Dashboard");
        setSize(1000, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);

        // Set App Icon
        try {
            URL iconUrl = getClass().getResource("/logo.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception ignored) {}

        initUI();
        
        Timer timer = new Timer(1000, e -> updateDateTime());
        timer.start();

        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                updateStats();
            }
        });
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.PRIMARY_COLOR),
            new EmptyBorder(20, 40, 20, 40)
        ));

        // Left side of header (Title & Welcome)
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Theme.CARD_COLOR);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleRow.setBackground(Theme.CARD_COLOR);
        JLabel iconLabel = new JLabel(Theme.getIcon(FontAwesomeSolid.GRADUATION_CAP, 36, Theme.PRIMARY_COLOR));
        JLabel titleLabel = new JLabel("Student Management System");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleRow.add(iconLabel);
        titleRow.add(titleLabel);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(Theme.FONT_REGULAR);
        welcomeLabel.setForeground(Color.GRAY);
        welcomeLabel.setBorder(new EmptyBorder(5, 55, 0, 0));

        titlePanel.add(titleRow);
        titlePanel.add(welcomeLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Right side of header (Stats & Time)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        statsPanel.setBackground(Theme.CARD_COLOR);
        
        JPanel countBox = createInfoBox(FontAwesomeSolid.USER_FRIENDS, "Total Students", String.valueOf(studentDAO.getTotalStudentCount()));
        countLabel = (JLabel) countBox.getClientProperty("valueLabel");
        
        JPanel timeBox = createInfoBox(FontAwesomeSolid.CALENDAR_ALT, "Date & Time", getCurrentDateTime());
        timeLabel = (JLabel) timeBox.getClientProperty("valueLabel");
        
        statsPanel.add(countBox);
        statsPanel.add(timeBox);
        headerPanel.add(statsPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel for Cards
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        gridPanel.setBackground(Theme.BACKGROUND_COLOR);
        gridPanel.setBorder(new EmptyBorder(50, 80, 50, 80));

        gridPanel.add(createActionCard("Add Student", "Register new student", FontAwesomeSolid.USER_PLUS, Theme.PRIMARY_COLOR, () -> new AddStudentForm().setVisible(true)));
        gridPanel.add(createActionCard("View Students", "View all students", FontAwesomeSolid.USERS, Theme.SECONDARY_COLOR, () -> new ViewStudentsForm().setVisible(true)));
        gridPanel.add(createActionCard("Search Student", "Search student records", FontAwesomeSolid.SEARCH, Theme.ACCENT_COLOR, () -> new SearchStudentForm().setVisible(true)));
        gridPanel.add(createActionCard("Update Student", "Update student details", FontAwesomeSolid.EDIT, new Color(139, 92, 246), () -> new UpdateStudentForm().setVisible(true)));
        gridPanel.add(createActionCard("Delete Student", "Delete student record", FontAwesomeSolid.TRASH_ALT, Theme.ERROR_COLOR, () -> new DeleteStudentForm().setVisible(true)));
        
        gridPanel.add(createActionCard("Logout", "Logout from system", FontAwesomeSolid.SIGN_OUT_ALT, Theme.TEXT_COLOR, () -> {
            int confirm = JOptionPane.showConfirmDialog(DashboardForm.this, 
                    "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginForm().setVisible(true);
            }
        }));

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(Theme.createFooter(), BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private JPanel createInfoBox(FontAwesomeSolid icon, String labelText, String valueText) {
        JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        box.setBackground(Theme.CARD_COLOR);
        box.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(229, 231, 235), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));

        JLabel iconLabel = new JLabel(Theme.getIcon(icon, 24, new Color(156, 163, 175)));
        box.add(iconLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Theme.CARD_COLOR);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Color.GRAY);
        
        JLabel val = new JLabel(valueText);
        val.setFont(Theme.FONT_BOLD);
        
        textPanel.add(lbl);
        textPanel.add(val);
        box.add(textPanel);
        
        box.putClientProperty("valueLabel", val); // Store reference for updating
        return box;
    }

    private JPanel createActionCard(String title, String subtitle, FontAwesomeSolid icon, Color iconColor, Runnable action) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD_COLOR);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Custom border with rounded corners logic is handled by FlatLaf panel settings or standard line border
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(229, 231, 235), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Icon
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.insets = new Insets(0, 0, 0, 20);
        card.add(new JLabel(Theme.getIcon(icon, 48, iconColor)), gbc);

        // Title
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_COLOR);
        card.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Color.GRAY);
        card.add(subtitleLabel, gbc);

        // Hover effect & Click
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(iconColor, 1, true),
                    new EmptyBorder(20, 20, 20, 20)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Theme.CARD_COLOR);
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(229, 231, 235), 1, true),
                    new EmptyBorder(20, 20, 20, 20)
                ));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });

        return card;
    }
    
    private void updateStats() {
        countLabel.setText(String.valueOf(studentDAO.getTotalStudentCount()));
    }
    
    private String getCurrentDateTime() {
        return new SimpleDateFormat("MMM dd, yyyy | hh:mm a").format(new Date());
    }
    
    private void updateDateTime() {
        if(timeLabel != null) {
            timeLabel.setText(getCurrentDateTime());
        }
    }
}
