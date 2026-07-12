package com.student.management.util;

import com.formdev.flatlaf.FlatLightLaf;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Theme {
    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(37, 99, 235);     // #2563EB (Blue)
    public static final Color SECONDARY_COLOR = new Color(16, 185, 129);  // #10B981 (Green)
    public static final Color ACCENT_COLOR = new Color(245, 158, 11);     // #F59E0B (Amber)
    public static final Color BACKGROUND_COLOR = new Color(243, 244, 246); // #F3F4F6 (Light Gray)
    public static final Color TEXT_COLOR = new Color(31, 41, 55);         // #1F2937 (Dark Gray)
    public static final Color CARD_COLOR = new Color(255, 255, 255);      // #FFFFFF (White)
    public static final Color ERROR_COLOR = new Color(239, 68, 68);       // #EF4444 (Red)

    // Fonts
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    public static void setup() {
        try {
            // Customize FlatLaf properties before installing
            UIManager.put("defaultFont", FONT_REGULAR);
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("TextField.foreground", TEXT_COLOR);
            UIManager.put("PasswordField.foreground", TEXT_COLOR);
            UIManager.put("TextArea.foreground", TEXT_COLOR);
            
            // Rounded corners and borders
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("TextComponent.arc", 8);

            // Primary Button styling
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", CARD_COLOR);
            UIManager.put("Button.hoverBackground", PRIMARY_COLOR.darker());
            UIManager.put("Button.focusedBackground", PRIMARY_COLOR.darker());
            UIManager.put("Button.disabledBackground", Color.LIGHT_GRAY);

            // Tables
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.alternateRowColor", new Color(248, 250, 252));
            UIManager.put("Table.rowHeight", 40);
            UIManager.put("Table.selectionBackground", new Color(219, 234, 254)); // Light blue
            UIManager.put("Table.selectionForeground", TEXT_COLOR);
            UIManager.put("TableHeader.background", PRIMARY_COLOR);
            UIManager.put("TableHeader.foreground", CARD_COLOR);
            UIManager.put("TableHeader.font", FONT_BOLD);
            
            // Focus
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.focusColor", new Color(147, 197, 253)); // Soft blue

            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Failed to initialize modern LaF");
        }
    }

    public static FontIcon getIcon(FontAwesomeSolid icon, int size, Color color) {
        FontIcon fontIcon = FontIcon.of(icon);
        fontIcon.setIconSize(size);
        fontIcon.setIconColor(color);
        return fontIcon;
    }

    public static JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(PRIMARY_COLOR);
        footer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel footerLabel = new JLabel("Developed using Java, JDBC, MySQL, Maven, and Swing");
        footerLabel.setForeground(CARD_COLOR);
        footerLabel.setFont(FONT_SMALL);
        footer.add(footerLabel);
        
        return footer;
    }
}
