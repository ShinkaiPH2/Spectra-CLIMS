package ui.views;

import dao.DeviceDAO;
import model.User;
import ui.components.Sidebar;
import util.UIHelpers;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardUI represents the main dashboard view of the application.
 * It displays key statistics about devices in a grid layout.
 */

public class DashboardUI extends JFrame {
    private User currentUser;
    private JLabel timeLabel;
    private JLabel allCountLbl, newCountLbl, damagedCountLbl, repairedCountLbl;

    /**
     * Constructs the DashboardUI with the specified user.
     *
     * @param user The current logged-in user.
     */

    public DashboardUI(User user) {
        this.currentUser = user;
        setTitle("CLIMS - Dashboard");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        UIHelpers.startClock(timeLabel);
        refreshCounts();
    }

    /*
    *   Initializes the UI components and layout.
    */

    private void init() {
        setLayout(new BorderLayout());

        // Left Sidebar - using the reusable component
        Sidebar sidebar = new Sidebar(currentUser, this, "DASHBOARD");
        add(sidebar, BorderLayout.WEST);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Top header
        timeLabel = new JLabel();
        JPanel header = UIHelpers.createHeader("DASHBOARD", timeLabel, new Color(238, 232, 170));
        mainContent.add(header, BorderLayout.NORTH);

        // Dashboard content
        JPanel dashContent = createDashboardContent();
        mainContent.add(dashContent, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    /*
    *   Creates the dashboard content with statistics cards.
    */

    private JPanel createDashboardContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // All Stocked card
        gbc.gridx = 0;
        gbc.gridy = 0;
        JPanel allCard = UIHelpers.createCard("All Stocked", new Color(100, 181, 246));
        allCountLbl = (JLabel) allCard.getComponent(0);
        content.add(allCard, gbc);

        // New Devices card
        gbc.gridx = 1;
        JPanel newCard = UIHelpers.createCard("New Devices", new Color(255, 167, 138));
        newCountLbl = (JLabel) newCard.getComponent(0);
        content.add(newCard, gbc);

        // Damaged Devices card
        gbc.gridx = 0;
        gbc.gridy = 1;
        JPanel damagedCard = UIHelpers.createCard("Damage Devices", new Color(239, 154, 154));
        damagedCountLbl = (JLabel) damagedCard.getComponent(0);
        content.add(damagedCard, gbc);

        // Repaired Devices card
        gbc.gridx = 1;
        JPanel repairedCard = UIHelpers.createCard("Repaired Devices", new Color(165, 214, 167));
        repairedCountLbl = (JLabel) repairedCard.getComponent(0);
        content.add(repairedCard, gbc);

        // Cards are informational only on the dashboard; clicking is disabled.
        // Ensure default cursor so they don't appear clickable.
        allCard.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        newCard.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        damagedCard.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        repairedCard.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        return content;
    }

    /*
    *   Refreshes the counts displayed on the dashboard.
    */

    private void refreshCounts() {
        // All stocked
        int totalDevices = DeviceDAO.countAll();
        allCountLbl.setText(String.valueOf(totalDevices));

        int newDevicesCount = DeviceDAO.countByStatus("new");
        newCountLbl.setText(String.valueOf(newDevicesCount));

        int damagedDevicesCount = DeviceDAO.countByStatuses(new String[] { "broken", "missing" });
        damagedCountLbl.setText(String.valueOf(damagedDevicesCount));

        int repairedDevicesCount = DeviceDAO.countByStatus("repaired");
        repairedCountLbl.setText(String.valueOf(repairedDevicesCount));
    }
}
