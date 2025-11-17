package ui.views;

import dao.LogDAO;
import model.User;
import ui.components.Sidebar;
import util.UIHelpers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

    /*
    * ReportsUI class - main frame for viewing reports (login and action logs)
    */

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
        JPanel panel = buildLogPanel("Actions", new String[] { "id", "Actions", "User", "Time" });
        JScrollPane actionScroll = (JScrollPane) ((JPanel) panel.getComponent(1)).getComponent(0);
        JTable actionTable = (JTable) actionScroll.getViewport().getView();
        actionModel = (DefaultTableModel) actionTable.getModel();
        // wire date field action (top panel's right textfield is index 1 in the right
        // panel)
        JTextField dateField = findDateField(panel);
        dateField.addActionListener(e -> {
            String datePrefix = dateField.getText().trim();
            if (datePrefix.isEmpty()) {
                refreshActionLogs();
            } else {
                refreshActionLogsByDate(datePrefix);
            }
        });
        return panel;
    }

    private JPanel buildLoginPanel() {
        JPanel panel = buildLogPanel("Logs", new String[] { "id", "Logs", "User", "Time" });
        JScrollPane loginScroll = (JScrollPane) ((JPanel) panel.getComponent(1)).getComponent(0);
        JTable loginTable = (JTable) loginScroll.getViewport().getView();
        loginModel = (DefaultTableModel) loginTable.getModel();
        JTextField loginDateField = findDateField(panel);
        loginDateField.addActionListener(e -> {
            String datePrefix = loginDateField.getText().trim();
            if (datePrefix.isEmpty()) {
                refreshLoginLogs();
            } else {
                refreshLoginLogsByDate(datePrefix);
            }
        });
        return panel;
    }

    // Helper: build a log view with a back button and a date field. Returns a panel
    // where index 0 is top panel and index 1 is the center wrapper containing the
    // scroll pane.
    private JPanel buildLogPanel(String centerLabel, String[] columns) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(Color.WHITE);
        JButton back = new JButton("Back");
        back.addActionListener(e -> centerCard.show(centerPanel, "MENU"));
        left.add(back);
        top.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(new JLabel("Choose Date: mm/dd/yyyy"));
        JTextField dateField = new JTextField(15);
        right.add(dateField);
        top.add(right, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);

        DefaultTableModel tm = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(tm);
        JScrollPane sp = new JScrollPane(table);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        wrap.add(sp, BorderLayout.CENTER);
        panel.add(wrap, BorderLayout.CENTER);
        return panel;
    }

    // Find the date field placed on the top right of a log panel
    private JTextField findDateField(JPanel logPanel) {
        Component top = logPanel.getComponent(0);
        if (top instanceof JPanel) {
            JPanel topPanel = (JPanel) top;
            Component right = ((BorderLayout) topPanel.getLayout()).getLayoutComponent(topPanel, BorderLayout.EAST);
            if (right instanceof JPanel) {
                for (Component c : ((JPanel) right).getComponents()) {
                    if (c instanceof JTextField)
                        return (JTextField) c;
                }
            }
        }
        return new JTextField(15); // fallback (shouldn't happen)
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
