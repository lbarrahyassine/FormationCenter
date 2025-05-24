import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login screen for the Formation Center Management System
 */
public class LoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    private UserDAO userDAO;

    public LoginScreen() {
        userDAO = new UserDAO();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Formation Center - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title at the top
        JLabel titleLabel = new JLabel("Formation Center Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Login form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        buttonPanel.add(loginButton);

        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Set default button
        getRootPane().setDefaultButton(loginButton);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }

        User user = userDAO.authenticate(username, password);
/*
        if (user != null) {
            statusLabel.setText("");

            // Close login screen and open main application
            dispose();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainScreen mainScreen = new MainScreen(user);
                    mainScreen.setVisible(true);
                }
            });
        } else {
            statusLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
        */

        if (user != null) {
            if ("administrateur".equalsIgnoreCase(user.getRole())) {
                dispose();
                new AdminMainScreen(user).setVisible(true);
            } else if ("receptionniste".equalsIgnoreCase(user.getRole())) {
                dispose();
                new ReceptionisteMainScreen(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Unrecognized role.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
        }

    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }
}