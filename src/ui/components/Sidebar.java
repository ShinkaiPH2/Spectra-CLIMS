package ui.components;

import dao.LogDAO;
import model.User;
import ui.UiUtils;

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
        JButton dashboardBtn = UiUtils.makeNavButton("DASHBOARD", new Color(255, 182, 193));
        if ("DASHBOARD".equals(currentScreen)) {
            dashboardBtn.setEnabled(false);
        } else {
            dashboardBtn.addActionListener(e -> navigateToDashboard());
        }
        navPanel.add(dashboardBtn);
        navPanel.add(Box.createVerticalStrut(5));

        // Manage Devices button
        JButton manageBtn = UiUtils.makeNavButton("MANAGE DEVICES", new Color(188, 143, 107));
        if ("MANAGE_DEVICES".equals(currentScreen)) {
            manageBtn.setEnabled(false);
        } else {
            manageBtn.addActionListener(e -> navigateToManageDevices());
        }
        navPanel.add(manageBtn);
        navPanel.add(Box.createVerticalStrut(5));

        // Reports button
        JButton reportsBtn = UiUtils.makeNavButton("REPORTS", new Color(238, 232, 170));
        if ("REPORTS".equals(currentScreen)) {
            reportsBtn.setEnabled(false);
        } else {
            reportsBtn.addActionListener(e -> navigateToReports());
        }
        navPanel.add(reportsBtn);

        return navPanel;
    }

    private JPanel createBottomSection() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(new Color(105, 105, 105));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JLabel userLabel = new JLabel("Current User: " + currentUser.getUsername());
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerPanel.add(userLabel);

        footerPanel.add(Box.createVerticalStrut(10));

        JButton logoutBtn = UiUtils.makeNavButton("LOGOUT", new Color(80, 80, 80));
        logoutBtn.addActionListener(e -> logout());
        footerPanel.add(logoutBtn);

        return footerPanel;
    }

    // Navigation methods
    private void navigateToDashboard() {
        openFrameAndCloseParent(() -> new ui.views.DashboardUI(currentUser));
    }

    private void navigateToManageDevices() {
        openFrameAndCloseParent(() -> new ui.views.ManageDevicesUI(currentUser));
    }

    private void navigateToReports() {
        openFrameAndCloseParent(() -> new ui.views.ReportsUI(currentUser));
    }

    // Helper to open a new frame and close the parent in the EDT
    private void openFrameAndCloseParent(java.util.function.Supplier<JFrame> frameSupplier) {
        SwingUtilities.invokeLater(() -> {
            Rectangle bounds = parentFrame.getBounds();
            int state = parentFrame.getExtendedState();
            JFrame frame = frameSupplier.get();
            frame.setBounds(bounds);
            frame.setExtendedState(state);
            frame.setVisible(true);
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
