package ui.components;

import dao.LogDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Reusable sidebar navigation component for CLIMS application.
 * Displays logo, navigation buttons, current user, and logout button.
 */
public class Sidebar extends JPanel {
    private User currentUser;
    private JFrame parentFrame;
    private String currentScreen; // "DASHBOARD", "MANAGE_DEVICES", or "REPORTS"
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    /**
     * Creates a new Sidebar component.
     *
     * @param currentUser   The logged-in user
     * @param parentFrame   The parent frame (used for navigation disposal)
     * @param currentScreen Which screen is currently active (to disable that
     *                      button)
     */
    public Sidebar(User currentUser, JFrame parentFrame, String currentScreen) {
        this.currentUser = currentUser;
        this.parentFrame = parentFrame;
        this.currentScreen = currentScreen;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 650));
        setBackground(new Color(105, 105, 105));

        // Top section with logo
        add(createTopSection(), BorderLayout.NORTH);

        // Navigation buttons
        add(createNavigationPanel(), BorderLayout.CENTER);

        // Bottom section with user info and logout
        add(createBottomSection(), BorderLayout.SOUTH);
    }

    private JPanel createTopSection() {
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(new Color(105, 105, 105));
        topSection.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo label (you can replace with actual image)
        JLabel logoLabel = new JLabel("STI College", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topSection.add(logoLabel);

        topSection.add(Box.createVerticalStrut(10));

        // CLIMS title
        JLabel climsLabel = new JLabel("CLIMS", SwingConstants.CENTER);
        climsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        climsLabel.setForeground(Color.WHITE);
        climsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topSection.add(climsLabel);

        return topSection;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(105, 105, 105));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Dashboard button
        JButton dashboardBtn = createNavButton("DASHBOARD", new Color(255, 182, 193));
        if ("DASHBOARD".equals(currentScreen)) {
            dashboardBtn.setEnabled(false);
        } else {
            dashboardBtn.addActionListener(e -> navigateToDashboard());
        }
        navPanel.add(dashboardBtn);
        navPanel.add(Box.createVerticalStrut(5));

        // Manage Devices button
        JButton manageBtn = createNavButton("MANAGE DEVICES", new Color(188, 143, 107));
        if ("MANAGE_DEVICES".equals(currentScreen)) {
            manageBtn.setEnabled(false);
        } else {
            manageBtn.addActionListener(e -> navigateToManageDevices());
        }
        navPanel.add(manageBtn);
        navPanel.add(Box.createVerticalStrut(5));

        // Reports button
        JButton reportsBtn = createNavButton("REPORTS", new Color(238, 232, 170));
        if ("REPORTS".equals(currentScreen)) {
            reportsBtn.setEnabled(false);
        } else {
            reportsBtn.addActionListener(e -> navigateToReports());
        }
        navPanel.add(reportsBtn);

        return navPanel;
    }

    private JPanel createBottomSection() {
        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.setBackground(new Color(105, 105, 105));
        bottomSection.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // Current user label
        JLabel userLabel = new JLabel("Current User: " + currentUser.getUsername());
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomSection.add(userLabel);

        bottomSection.add(Box.createVerticalStrut(10));

        // Logout button
        JButton logoutBtn = createNavButton("LOGOUT", new Color(80, 80, 80));
        logoutBtn.addActionListener(e -> logout());
        bottomSection.add(logoutBtn);

        return bottomSection;
    }

    private JButton createNavButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setPreferredSize(new Dimension(260, 50));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    // Navigation methods
    private void navigateToDashboard() {
        SwingUtilities.invokeLater(() -> {
            // preserve parent bounds/state so new frame keeps fullscreen if the parent was
            // fullscreen
            Rectangle bounds = parentFrame.getBounds();
            int state = parentFrame.getExtendedState();
            JFrame f = new ui.views.DashboardUI(currentUser);
            // apply same size/state as parent
            f.setBounds(bounds);
            f.setExtendedState(state);
            f.setVisible(true);
            parentFrame.dispose();
        });
    }

    private void navigateToManageDevices() {
        SwingUtilities.invokeLater(() -> {
            Rectangle bounds = parentFrame.getBounds();
            int state = parentFrame.getExtendedState();
            JFrame f = new ui.views.ManageDevicesUI(currentUser);
            f.setBounds(bounds);
            f.setExtendedState(state);
            f.setVisible(true);
            parentFrame.dispose();
        });
    }

    private void navigateToReports() {
        SwingUtilities.invokeLater(() -> {
            Rectangle bounds = parentFrame.getBounds();
            int state = parentFrame.getExtendedState();
            JFrame f = new ui.views.ReportsUI(currentUser);
            f.setBounds(bounds);
            f.setExtendedState(state);
            f.setVisible(true);
            parentFrame.dispose();
        });
    }

    private void logout() {
        // Log logout action
        String timestamp = LocalDateTime.now().format(dtf);
        LogDAO.insertLoginLog(currentUser.getId(), "<User> logout", timestamp);

        // Navigate to login screen
        SwingUtilities.invokeLater(() -> {
            new ui.views.LoginUI().setVisible(true);
            parentFrame.dispose();
        });
    }
}
