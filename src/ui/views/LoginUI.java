package ui.views;

import dao.LogDAO;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

/*
 * LoginUI class represents the login interface for the CLIMS application.
 */

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("CLIMS - Login");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    /*
    * DateTimeFormatter for logging login timestamps.
    */
    private static final java.time.format.DateTimeFormatter LOGIN_DTF = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    /*
    * Initializes UI components and layout.
    */
    private void initComponents() {
        // Create main panel with background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set background color (similar to Windows XP green hill)
                g.setColor(new Color(91, 155, 213));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        // Create center login box
        JPanel loginBox = new JPanel();
        loginBox.setBackground(Color.WHITE);
        loginBox.setLayout(null);
        loginBox.setBounds(300, 150, 300, 280);
        loginBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Logo placeholder (you can add actual logo image)
        JLabel logoLabel = new JLabel("STI College", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 102, 204));
        logoLabel.setBounds(50, 20, 200, 40);
        loginBox.add(logoLabel);

        // CLIMS title
        JLabel titleLabel = new JLabel("CLIMS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(50, 65, 200, 30);
        loginBox.add(titleLabel);

        // Username label and field
        JLabel userLabel = new JLabel("USER");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        userLabel.setBounds(30, 110, 240, 20);
        loginBox.add(userLabel);

    usernameField = new JTextField();
        usernameField.setBounds(30, 130, 240, 30);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginBox.add(usernameField);

        // Password label and field
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        passLabel.setBounds(30, 165, 240, 20);
        loginBox.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(30, 185, 240, 30);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginBox.add(passwordField);

        // Login button
        JButton loginBtn = ui.UiUtils.makeButton("LOGIN", new Color(108, 117, 125));
        loginBtn.setBounds(30, 225, 105, 30);
        loginBtn.setForeground(Color.WHITE);
    loginBtn.addActionListener(this::onLogin);
        loginBox.add(loginBtn);

        // Exit button
        JButton exitBtn = ui.UiUtils.makeButton("EXIT", new Color(220, 53, 69));
        exitBtn.setBounds(165, 225, 105, 30);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.addActionListener(e -> System.exit(0));
        loginBox.add(exitBtn);

        mainPanel.add(loginBox);
        setContentPane(mainPanel);
    }

    /*
    * Handles login button action.
    */
    private void onLogin(ActionEvent ev) {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        User authenticatedUser = UserDAO.authenticate(user, pass);
        if (authenticatedUser != null) {
            // Log login timestamp
            String timestamp = LocalDateTime.now().format(LOGIN_DTF);
            LogDAO.insertLoginLog(authenticatedUser.getId(), "<User> login", timestamp);

            // Open dashboard
            SwingUtilities.invokeLater(() -> {
                DashboardUI dashboard = new DashboardUI(authenticatedUser);
                dashboard.setVisible(true);
                this.dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
