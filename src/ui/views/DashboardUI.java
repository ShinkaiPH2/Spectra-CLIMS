package ui.views;

import dao.DeviceDAO;
import model.User;
import ui.components.Sidebar;
import util.UIHelpers;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {
    private User currentUser;
    private JLabel timeLabel;
    private JLabel allCountLbl, newCountLbl, damagedCountLbl, repairedCountLbl;

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

        // Add click listeners to cards
        allCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        allCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new ManageDevicesUI(currentUser).setVisible(true);
                dispose();
            }
        });

        newCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new ManageDevicesUI(currentUser, new String[] { "New" }).setVisible(true);
            }
        });

        damagedCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        damagedCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new ManageDevicesUI(currentUser, new String[] { "Broken", "Missing" }).setVisible(true);
            }
        });

        repairedCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        repairedCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new ManageDevicesUI(currentUser, new String[] { "Repaired" }).setVisible(true);
            }
        });

        return content;
    }

    private void refreshCounts() {
        // All stocked
        int all = DeviceDAO.countAll();
        allCountLbl.setText(String.valueOf(all));

        // New Devices
        int nw = DeviceDAO.countByStatus("new");
        newCountLbl.setText(String.valueOf(nw));

        // Damaged Devices (Broken or Missing)
        int damaged = DeviceDAO.countByStatuses(new String[] { "broken", "missing" });
        damagedCountLbl.setText(String.valueOf(damaged));

        // Repaired Devices
        int repaired = DeviceDAO.countByStatus("repaired");
        repairedCountLbl.setText(String.valueOf(repaired));
    }
}
