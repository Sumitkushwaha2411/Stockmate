package ui;

import database.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;

    public LoginFrame() {
        setTitle("StockMate Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel title = new JLabel("     Welcome to StockMate");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(80, 20, 250, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 180, 25);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 120, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 180, 25);
        panel.add(passwordField);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(150, 150, 150, 25);
        panel.add(showPassword);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 190, 100, 30);
        panel.add(loginBtn);

        add(panel);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });

        loginBtn.addActionListener(e -> loginUser());
    }

    private void loginUser() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username and password");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(this,
                        "Login Successful");

                try {
                    DashboardFrame dashboard = new DashboardFrame();
                    dashboard.setVisible(true);
                    dispose();
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Dashboard Error: " + ex2.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database Connection Error: " + ex.getMessage());
        }
    }
}