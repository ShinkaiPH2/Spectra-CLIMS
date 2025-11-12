package ui.views;

import dao.LogDAO;
import model.User;
import ui.components.Sidebar;
import util.UIHelpers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsUI extends JFrame {
    private DefaultTableModel loginModel;
    private DefaultTableModel actionModel;
    private JLabel timeLabel;
    private User currentUser;
    // single-frame center area (CardLayout) so we don't open duplicate frames
    private JPanel centerPanel;
    private CardLayout centerCard;
    private JPanel menuPanel;
    private JPanel actionPanel;
    private JPanel loginPanel;

    public ReportsUI(User currentUser) {
        this.currentUser = currentUser;
        setTitle("CLIMS - Reports");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        UIHelpers.startClock(timeLabel);
    }

    private void init() {
        setLayout(new BorderLayout());

        // Left Sidebar - using the reusable component
        Sidebar sidebar = new Sidebar(currentUser, this, "REPORTS");
        add(sidebar, BorderLayout.WEST);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Top header
        timeLabel = new JLabel();
        JPanel header = UIHelpers.createHeader("REPORTS", timeLabel, new Color(238, 232, 170));
        mainContent.add(header, BorderLayout.NORTH);

        // Content: build a menu card and two log cards inside a CardLayout so we reuse
        // one frame
        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Action Logs button on menu
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton actionLogsBtn = createReportButton("Action Logs");
        actionLogsBtn.addActionListener(e -> showActionLogsScreen());
        menuPanel.add(actionLogsBtn, gbc);

        // Login Logs button on menu
        gbc.gridx = 1;
        JButton loginLogsBtn = createReportButton("Login Logs");
        loginLogsBtn.addActionListener(e -> showLoginLogsScreen());
        menuPanel.add(loginLogsBtn, gbc);

        // Card layout center panel
        centerCard = new CardLayout();
        centerPanel = new JPanel(centerCard);
        centerPanel.add(menuPanel, "MENU");

        // build log panels and add as cards
        actionPanel = buildActionPanel();
        loginPanel = buildLoginPanel();
        centerPanel.add(actionPanel, "ACTION");
        centerPanel.add(loginPanel, "LOGIN");

        // show menu by default
        centerCard.show(centerPanel, "MENU");

        mainContent.add(centerPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
    }

    private JButton createReportButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        return btn;
    }

    private void showActionLogsScreen() {
        // refresh data and switch to action card
        refreshActionLogs();
        centerCard.show(centerPanel, "ACTION");
    }

    private void showLoginLogsScreen() {
        // refresh data and switch to login card
        refreshLoginLogs();
        centerCard.show(centerPanel, "LOGIN");
    }

    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(Color.WHITE);
        JButton back = new JButton("Back");
        back.addActionListener(e -> centerCard.show(centerPanel, "MENU"));
        left.add(back);
        topPanel.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(new JLabel("Choose Date: mm/dd/yyyy"));
        JTextField dateField = new JTextField(15);
        right.add(dateField);
        topPanel.add(right, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        actionModel = new DefaultTableModel(new Object[] { "id", "Actions", "User", "Time" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(actionModel);
        // wrap scrollpane to add padding around the table
        JScrollPane actionScroll = new JScrollPane(table);
        JPanel actionCenterWrap = new JPanel(new BorderLayout());
        actionCenterWrap.setBackground(Color.WHITE);
        actionCenterWrap.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        actionCenterWrap.add(actionScroll, BorderLayout.CENTER);
        panel.add(actionCenterWrap, BorderLayout.CENTER);

        dateField.addActionListener(e -> {
            String prefix = dateField.getText().trim();
            if (prefix.isEmpty())
                refreshActionLogs();
            else
                refreshActionLogsByDate(prefix);
        });

        return panel;
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(Color.WHITE);
        JButton back = new JButton("Back");
        back.addActionListener(e -> centerCard.show(centerPanel, "MENU"));
        left.add(back);
        topPanel.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(new JLabel("Choose Date: mm/dd/yyyy"));
        JTextField dateField = new JTextField(15);
        right.add(dateField);
        topPanel.add(right, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        loginModel = new DefaultTableModel(new Object[] { "id", "Logs", "User", "Time" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(loginModel);
        // wrap scrollpane to add padding around the table
        JScrollPane loginScroll = new JScrollPane(table);
        JPanel loginCenterWrap = new JPanel(new BorderLayout());
        loginCenterWrap.setBackground(Color.WHITE);
        loginCenterWrap.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        loginCenterWrap.add(loginScroll, BorderLayout.CENTER);
        panel.add(loginCenterWrap, BorderLayout.CENTER);

        dateField.addActionListener(e -> {
            String prefix = dateField.getText().trim();
            if (prefix.isEmpty())
                refreshLoginLogs();
            else
                refreshLoginLogsByDate(prefix);
        });

        return panel;
    }

    // Removed separate dialogs; both logs are shown inside the main frame tabs.

    private void refreshLoginLogs() {
        loginModel.setRowCount(0);
        List<String[]> logs = LogDAO.getLoginLogs();
        for (String[] log : logs) {
            loginModel.addRow(log);
        }
    }

    private void refreshLoginLogsByDate(String datePrefix) {
        loginModel.setRowCount(0);
        List<String[]> logs = LogDAO.getLoginLogsByDatePrefix(datePrefix);
        for (String[] log : logs) {
            loginModel.addRow(log);
        }
    }

    private void refreshActionLogs() {
        actionModel.setRowCount(0);
        List<String[]> logs = LogDAO.getActionLogs();
        for (String[] log : logs) {
            actionModel.addRow(log);
        }
    }

    private void refreshActionLogsByDate(String datePrefix) {
        actionModel.setRowCount(0);
        List<String[]> logs = LogDAO.getActionLogsByDatePrefix(datePrefix);
        for (String[] log : logs) {
            actionModel.addRow(log);
        }
    }
}
